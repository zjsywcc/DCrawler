package com.moecheng.distributedcrawler.slaver.pipeline;

import com.moecheng.distributedcrawler.slaver.model.ResultItems;
import com.moecheng.distributedcrawler.slaver.network.SocketClient;
import com.moecheng.distributedcrawler.slaver.network.model.Command;

import java.util.Map;

/**
 * Created by mengchenyun on 2016/11/12.
 */
public class SocketPipeline implements Pipeline {

    private SocketClient socketClient = SocketClient.getInstance();

    @Override
    public void process(ResultItems resultItems) {
        System.out.println("get page: " + resultItems.getURLRequest().getUrl());
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            System.out.println(entry.getKey() + ":\t" + entry.getValue());
            socketClient.send(new Command(Command.CMD_MSG, entry.getKey() + ":\t" + entry.getValue()));
        }
    }
}
