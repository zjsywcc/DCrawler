package com.moecheng.distributedcrawler;


import com.moecheng.distributedcrawler.model.Site;

public interface Task {

    /**
     * unique id for a task.
     *
     * @return uuid
     */
    public String getUUID();

    /**
     * site of a task
     *
     * @return site
     */
    public Site getSite();

}
