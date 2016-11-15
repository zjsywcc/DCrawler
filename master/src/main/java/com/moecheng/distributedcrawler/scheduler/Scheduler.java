package com.moecheng.distributedcrawler.scheduler;


import com.moecheng.distributedcrawler.Task;
import com.moecheng.distributedcrawler.model.URLRequest;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public interface Scheduler {

    void push(URLRequest request, Task task);

    URLRequest poll(Task task);
}
