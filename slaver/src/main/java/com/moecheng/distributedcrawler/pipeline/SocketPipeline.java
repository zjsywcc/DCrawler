package com.moecheng.distributedcrawler.pipeline;

import com.moecheng.distributedcrawler.StartSlaver;
import com.moecheng.distributedcrawler.network.model.Command;
import com.moecheng.distributedcrawler.model.ResultItems;
import com.moecheng.distributedcrawler.network.SocketClient;

import java.util.Map;

/**
 * Created by mengchenyun on 2016/11/12.
 */
public class SocketPipeline implements Pipeline {

    private StartSlaver startSlaver = StartSlaver.getInstance();

    @Override
    public void process(ResultItems resultItems) {
        System.out.println("get page: " + resultItems.getURLRequest().getUrl());
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            System.out.println(entry.getKey() + ":\t" + entry.getValue());
            startSlaver.send(new Command(Command.CMD_MSG, entry.getKey() + ":\t" + entry.getValue()));
        }
    }
}
