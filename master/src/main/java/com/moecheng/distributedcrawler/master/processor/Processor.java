package com.moecheng.distributedcrawler.master.processor;


import com.moecheng.distributedcrawler.master.model.Page;
import com.moecheng.distributedcrawler.master.model.Site;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public interface Processor {

    void process(Page page);

    public Site getSite();

}
