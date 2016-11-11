package com.moecheng.distributedcrawler.slaver.scheduler.component;


import com.moecheng.distributedcrawler.slaver.Task;
import com.moecheng.distributedcrawler.slaver.model.URLRequest;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public interface DuplicateRemover {

    boolean isDuplicate(URLRequest request, Task task);

    /**
     * Reset duplicate check.
     * @param task task
     */
    public void resetDuplicateCheck(Task task);

    /**
     * Get TotalRequestsCount for monitor.
     * @param task task
     * @return number of total request
     */
    public int getTotalRequestsCount(Task task);

}
