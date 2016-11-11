package com.moecheng.distributedcrawler.slaver.scheduler.component;


import com.moecheng.distributedcrawler.slaver.Task;
import com.moecheng.distributedcrawler.slaver.scheduler.Scheduler;

/**
 * The scheduler whose requests can be counted for monitor.
 *
 * @author code4crafter@gmail.com
 * @since 0.5.0
 */
public interface MonitorableScheduler extends Scheduler {

    public int getLeftRequestsCount(Task task);

    public int getTotalRequestsCount(Task task);

}