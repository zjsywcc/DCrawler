package com.moecheng.distributedcrawler;

import com.moecheng.distributedcrawler.model.Site;
import com.moecheng.distributedcrawler.processor.Processor;
import com.moecheng.distributedcrawler.model.Page;
import com.moecheng.distributedcrawler.network.model.Config;
import com.moecheng.distributedcrawler.scheduler.RedisScheduler;
import com.moecheng.distributedcrawler.utils.JsonFilePipeline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mengchenyun on 2016/11/11.
 */
public class StartCrawler implements Processor {

    private static StartCrawler mContext = null;

    private Crawler crawler;

    public static StartCrawler getInstance(Config config) {
        if(mContext == null) {
            mContext = new StartCrawler(config);
            return mContext;
        }
        return mContext;
    }

    private Config config;

    private Site site = Site.me().setRetryTimes(3).setSleepTime(50).setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36");

    public StartCrawler(Config config) {
        this.config = config;
    }

    @Override
    public void process(Page page) {
        List<String> strings = page.getHtml().links().regex(config.getRegex()).all();
        page.addTargetRequests(strings);
        Map<String, Map<String, String>> results = config.getResults();
        for(Map.Entry<String, Map<String, String>> entry : results.entrySet()) {
            String entityName = entry.getKey();
            Map<String, String> rules = results.get(entityName);
            Map<String, String> result = new HashMap<>();
            for(Map.Entry<String, String> rule : rules.entrySet()) {
                String ruleName = rule.getKey();
                result.put(ruleName, page.getHtml().xpath(rule.getValue()).toString());
            }
            page.putField(entityName, result);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void run(String masterIp) {
        int threadNum = 5;
        System.out.println("Running crawler with thread of "+threadNum+" default is 5");
        crawler = Crawler.create(new StartCrawler(config)).addUrl(config.getStartUrls()).setScheduler(new RedisScheduler(masterIp)).setThread(threadNum).addPipeline(new JsonFilePipeline());
        crawler.start();
    }

    public void stop() {
        try {
            crawler.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
