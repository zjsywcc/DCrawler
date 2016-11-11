package com.moecheng.distributedcrawler.slaver.scheduler;

import com.moecheng.distributedcrawler.slaver.Task;
import com.moecheng.distributedcrawler.slaver.model.URLRequest;
import com.moecheng.distributedcrawler.slaver.scheduler.component.DuplicateRemover;
import com.moecheng.distributedcrawler.slaver.scheduler.component.HashSetDuplicateRemover;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public class QueueScheduler implements Scheduler {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private BlockingDeque<URLRequest> queue = new LinkedBlockingDeque<>();

    private DuplicateRemover duplicateRemover = new HashSetDuplicateRemover();

    @Override
    public void push(URLRequest request, Task task) {
        logger.trace("get a candidate url {}", request.getUrl());
        if(!duplicateRemover.isDuplicate(request, task)) {
            logger.debug("push to queue {}", request.getUrl());
            queue.push(request);
        }
    }

    @Override
    public URLRequest poll(Task task) {
        return queue.poll();
    }
}
