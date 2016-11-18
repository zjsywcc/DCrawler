package com.moecheng.distributedcrawler;

import com.moecheng.distributedcrawler.processor.Processor;
import com.moecheng.distributedcrawler.downloader.Downloader;
import com.moecheng.distributedcrawler.downloader.OkHttpDownloader;
import com.moecheng.distributedcrawler.model.Page;
import com.moecheng.distributedcrawler.model.Site;
import com.moecheng.distributedcrawler.model.URLRequest;
import com.moecheng.distributedcrawler.pipeline.ConsolePipeline;
import com.moecheng.distributedcrawler.pipeline.Pipeline;
import com.moecheng.distributedcrawler.scheduler.QueueScheduler;
import com.moecheng.distributedcrawler.scheduler.Scheduler;
import com.moecheng.distributedcrawler.thread.CountableThreadPool;
import com.moecheng.distributedcrawler.utils.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public class Crawler implements Runnable, Task {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private Downloader downloader;

    private Processor processor;

    private Scheduler scheduler = new QueueScheduler();

    private List<Pipeline> pipelines = new ArrayList<>();

    private List<URLRequest> startRequests;

    private Site site;

    private int threadNum = 1;

    private CountableThreadPool threadPool;

    private ExecutorService executorService;

    private ReentrantLock newUrlLock = new ReentrantLock();

    private Condition newUrlCondition = newUrlLock.newCondition();

    private int emptySleepTime = 30000;

    protected String uuid;

    protected final static int STAT_INIT = 0;

    protected AtomicInteger stat = new AtomicInteger(STAT_INIT);

    protected final static int STAT_RUNNING = 1;

    protected final static int STAT_STOPPED = 2;

    private Crawler(Processor processor) {
        this.processor = processor;
        this.site = processor.getSite();
        this.startRequests = processor.getSite().getStartURLRequests();
    }

    public static Crawler create(Processor processor) {
        return new Crawler(processor);
    }

    public Crawler addUrl(String... urls) {
        for(String url : urls) {
            URLRequest request = new URLRequest(url);
            if (site.getDomain() == null && request.getUrl() != null) {
                site.setDomain(UrlUtils.getDomain(request.getUrl()));
            }
            startRequests.add(new URLRequest(url));
        }
        return this;
    }

    public Crawler addPipeline(Pipeline pipeline) {
        checkIfRunning();
        pipelines.add(pipeline);
        return this;
    }

    public Crawler setThread(int threadNum) {
        checkIfRunning();
        this.threadNum = threadNum;
        if(threadNum <= 0) {
            throw new IllegalArgumentException("threadNum should be more than one!");
        }
        return this;
    }

    public Crawler setScheduler(Scheduler scheduler) {
        checkIfRunning();
        Scheduler oldScheduler = this.scheduler;
        this.scheduler = scheduler;
        if (oldScheduler != null) {
            URLRequest request;
            while ((request = oldScheduler.poll(this)) != null) {
                this.scheduler.push(request, this);
            }
        }
        return this;
    }

    private void initComponent() {
        if(downloader == null) {
            downloader = new OkHttpDownloader();
        }
        downloader.setThread(threadNum);
        if(pipelines.isEmpty()) {
            pipelines.add(new ConsolePipeline());
        }
        if(startRequests != null) {
            for(URLRequest request : startRequests) {
                scheduler.push(request, this);
            }
            startRequests.clear();
        }
        if(threadPool == null || threadPool.isShutdown()) {
            if(executorService != null && !executorService.isShutdown()) {
                threadPool = new CountableThreadPool(threadNum, executorService);
            } else {
                threadPool = new CountableThreadPool(threadNum);
            }
        }
    }


    @Override
    public void run() {
        checkRunningStat();
        initComponent();
        logger.info("crawler " + site.getDomain() + " started!");
        while(!Thread.currentThread().isInterrupted() && stat.get() == STAT_RUNNING) {
            URLRequest request = scheduler.poll(this);
            if(request == null) {
                if(threadPool.getThreadAlive() == 0) {
                    break;
                }
                waitNewUrl();
            } else {
                final URLRequest requestFinal = request;
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            processRequest(requestFinal);
//                            onSuccess(requestFinal);
                        } catch(Exception e) {
//                            onError(requestFinal);
                            logger.error("process request " + requestFinal + " error", e);
                        } finally {
                            signalNewUrl();
                        }
                    }
                });
            }
        }
        stat.set(STAT_STOPPED);
        close();
    }

    private void waitNewUrl() {
        newUrlLock.lock();
        try {
            //double check
            if(threadPool.getThreadAlive() == 0) {
                return;
            }
            newUrlCondition.await(emptySleepTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.warn("waitNewUrl - interrupted, error {}", e);
        } finally {
            newUrlLock.unlock();
        }
    }

    private void signalNewUrl() {
        try {
            newUrlLock.lock();
            newUrlCondition.signalAll();
        } finally {
            newUrlLock.unlock();
        }
    }

    public void close() {
        destroyEach(downloader);
        destroyEach(processor);
        destroyEach(scheduler);
        for (Pipeline pipeline : pipelines) {
            destroyEach(pipeline);
        }
        threadPool.shutdown();
    }

    private void destroyEach(Object object) {
        if (object instanceof Closeable) {
            try {
                ((Closeable) object).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processRequest(URLRequest request) {
        Page page = downloader.download(request, this);
        if(page == null) {
            throw new RuntimeException("unacceptable response status");
        }
        processor.process(page);
        extractAndAddRequests(page);
        for(Pipeline pipeline : pipelines) {
            pipeline.process(page.getResultItems(), this);
        }
        sleep(site.getSleepTime());
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void extractAndAddRequests(Page page) {
        if(!page.getTargetURLRequests().isEmpty()) {
            for(URLRequest request : page.getTargetURLRequests()) {
                addRequest(request);
            }
        }
    }

    private void addRequest(URLRequest request) {
        scheduler.push(request, this);
    }

    @Override
    public String getUUID() {
        if (uuid != null) {
            return uuid;
        }
        if (site != null) {
            return site.getDomain();
        }
        uuid = UUID.randomUUID().toString();
        return uuid;
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void stop() {
        if (stat.compareAndSet(STAT_RUNNING, STAT_STOPPED)) {
            logger.info("Spider " + getUUID() + " stop success!");
        } else {
            logger.info("Spider " + getUUID() + " stop fail!");
        }
    }

    private void checkRunningStat() {
        while (true) {
            int statNow = stat.get();
            if (statNow == STAT_RUNNING) {
                throw new IllegalStateException("Spider is already running!");
            }
            if (stat.compareAndSet(statNow, STAT_RUNNING)) {
                break;
            }
        }
    }

    protected void checkIfRunning() {
        if (stat.get() == STAT_RUNNING) {
            throw new IllegalStateException("Spider is already running!");
        }
    }

    public void runAsync() {
        Thread thread = new Thread(this);
        thread.setDaemon(false);
        thread.start();
    }

    public void start() {
        runAsync();
    }

}
