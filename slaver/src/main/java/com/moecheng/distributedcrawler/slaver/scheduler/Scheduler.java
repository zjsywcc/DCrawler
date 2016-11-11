package com.moecheng.distributedcrawler.slaver.scheduler;


import com.moecheng.distributedcrawler.slaver.Task;
import com.moecheng.distributedcrawler.slaver.model.URLRequest;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public interface Scheduler {

    void push(URLRequest request, Task task);

    URLRequest poll(Task task);
}
