package com.moecheng.distributedcrawler.pipeline;


import com.moecheng.distributedcrawler.model.ResultItems;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public interface Pipeline {

    void process(ResultItems resultItems);
}
