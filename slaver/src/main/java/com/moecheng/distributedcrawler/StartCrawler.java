package com.moecheng.distributedcrawler;

import com.moecheng.distributedcrawler.model.Site;
import com.moecheng.distributedcrawler.pipeline.SocketPipeline;
import com.moecheng.distributedcrawler.processor.Processor;
import com.moecheng.distributedcrawler.model.Page;
import com.moecheng.distributedcrawler.network.model.Config;
import com.moecheng.distributedcrawler.scheduler.RedisScheduler;
import com.moecheng.distributedcrawler.test.BookInfo;

import java.util.List;

/**
 * Created by mengchenyun on 2016/11/11.
 */
public class StartCrawler implements Processor {

    private static StartCrawler mContext = null;

    public static StartCrawler getInstance(Config config) {
        if(mContext == null) {
            return new StartCrawler(config);
        }
        return mContext;
    }

    private Config config;

    private Site site = Site.me().setRetryTimes(3).setSleepTime(0).setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36");

    public StartCrawler(Config config) {
        this.config = config;
    }

    @Override
    public void process(Page page) {
        List<String> strings = page.getHtml().links().regex(config.getRegex()).all();
        page.addTargetRequests(strings);
        BookInfo bookInfo = config.getBookInfo();
        bookInfo.setBookAuthor(page.getHtml().xpath(config.getXpath().get("BookAuthor")).toString());
        bookInfo.setBookEdition(page.getHtml().xpath(config.getXpath().get("BookEdition")).toString());
        bookInfo.setBookISBN(page.getHtml().xpath(config.getXpath().get("BookISBN")).toString());
        bookInfo.setBookName(page.getHtml().xpath(config.getXpath().get("BookName")).toString());
        bookInfo.setBookPress(page.getHtml().xpath(config.getXpath().get("BookPress")).toString());
        bookInfo.setBookPrice(page.getHtml().xpath(config.getXpath().get("BookPrice")).toString());
        bookInfo.setBookImage(page.getHtml().xpath(config.getXpath().get("BookImage")).toString());
        page.putField(config.getEntityName(), bookInfo);
    }

    @Override
    public Site getSite() {
        return site;
    }

    public void run(String masterIp) {
        int threadNum = 5;
        System.out.println("Running crawler with thread of "+threadNum+" default is 5");
        Crawler.create(new StartCrawler(config)).addUrl(config.getStartUrls()).setScheduler(new RedisScheduler("127.0.0.1")).setThread(threadNum).addPipeline(new SocketPipeline()).run();
    }
}
