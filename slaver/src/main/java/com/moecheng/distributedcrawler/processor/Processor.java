package com.moecheng.distributedcrawler.processor;


import com.moecheng.distributedcrawler.model.Page;
import com.moecheng.distributedcrawler.model.Site;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public interface Processor {

    void process(Page page);

    public Site getSite();

}
