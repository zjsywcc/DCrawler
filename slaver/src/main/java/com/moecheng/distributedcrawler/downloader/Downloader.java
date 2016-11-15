package com.moecheng.distributedcrawler.downloader;


import com.moecheng.distributedcrawler.model.URLRequest;
import com.moecheng.distributedcrawler.model.Page;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public interface Downloader {

    Page download(URLRequest request);

    void setThread(int num);
}
