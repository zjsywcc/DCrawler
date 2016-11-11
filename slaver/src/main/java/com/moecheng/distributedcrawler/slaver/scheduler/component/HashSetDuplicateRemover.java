package com.moecheng.distributedcrawler.slaver.scheduler.component;




import com.moecheng.distributedcrawler.slaver.Task;
import com.moecheng.distributedcrawler.slaver.model.URLRequest;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public class HashSetDuplicateRemover implements DuplicateRemover {

    private Set<String> urls = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    @Override
    public boolean isDuplicate(URLRequest request, Task task) {
        return !urls.add(request.getUrl());
    }

    @Override
    public void resetDuplicateCheck(Task task) {
        urls.clear();
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return urls.size();
    }

}
