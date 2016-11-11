package com.moecheng.distributedcrawler.slaver.downloader;


import com.moecheng.distributedcrawler.slaver.model.Page;
import com.moecheng.distributedcrawler.slaver.model.URLRequest;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public interface Downloader {

    Page download(URLRequest request);

    void setThread(int num);
}
