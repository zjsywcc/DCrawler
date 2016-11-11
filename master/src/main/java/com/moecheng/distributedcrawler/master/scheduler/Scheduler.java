package com.moecheng.distributedcrawler.master.scheduler;


import com.moecheng.distributedcrawler.master.Task;
import com.moecheng.distributedcrawler.master.model.URLRequest;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public interface Scheduler {

    void push(URLRequest request, Task task);

    URLRequest poll(Task task);
}
