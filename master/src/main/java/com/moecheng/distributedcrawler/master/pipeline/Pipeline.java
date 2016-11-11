package com.moecheng.distributedcrawler.master.pipeline;


import com.moecheng.distributedcrawler.master.model.ResultItems;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public interface Pipeline {

    void process(ResultItems resultItems);
}
