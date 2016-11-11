package com.moecheng.distributedcrawler.slaver;


import com.moecheng.distributedcrawler.slaver.model.Site;

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
