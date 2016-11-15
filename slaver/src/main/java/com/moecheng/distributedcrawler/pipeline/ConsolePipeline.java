package com.moecheng.distributedcrawler.pipeline;


import com.moecheng.distributedcrawler.model.ResultItems;

import java.util.Map;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public class ConsolePipeline implements Pipeline {

    @Override
    public void process(ResultItems resultItems) {
        System.out.println("get page: " + resultItems.getURLRequest().getUrl());
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            System.out.println(entry.getKey() + ":\t" + entry.getValue());
        }
    }
}
