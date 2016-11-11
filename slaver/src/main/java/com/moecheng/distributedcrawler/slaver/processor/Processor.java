package com.moecheng.distributedcrawler.slaver.processor;


import com.moecheng.distributedcrawler.slaver.model.Page;
import com.moecheng.distributedcrawler.slaver.model.Site;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public interface Processor {

    void process(Page page);

    public Site getSite();

}
