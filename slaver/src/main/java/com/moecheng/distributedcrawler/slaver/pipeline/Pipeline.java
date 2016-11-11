package com.moecheng.distributedcrawler.slaver.pipeline;


import com.moecheng.distributedcrawler.slaver.model.ResultItems;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public interface Pipeline {

    void process(ResultItems resultItems);
}
