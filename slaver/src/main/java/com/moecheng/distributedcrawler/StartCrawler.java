package com.moecheng.distributedcrawler;

import com.alibaba.fastjson.JSON;
import com.moecheng.distributedcrawler.model.Site;
import com.moecheng.distributedcrawler.pipeline.SocketPipeline;
import com.moecheng.distributedcrawler.processor.Processor;
import com.moecheng.distributedcrawler.model.Page;
import com.moecheng.distributedcrawler.network.model.Config;
import com.moecheng.distributedcrawler.scheduler.RedisScheduler;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        Crawler.create(new StartCrawler(config)).addUrl(config.getStartUrls()).setScheduler(new RedisScheduler("127.0.0.1")).setThread(threadNum).addPipeline(new SocketPipeline()).run();
    }
}
