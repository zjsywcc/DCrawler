package com.moecheng.distributedcrawler.slaver.downloader;


import com.moecheng.distributedcrawler.slaver.model.URLRequest;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public abstract class AbstractDownloader implements Downloader {

    void onSuccess(URLRequest request) {}

    void onError(URLRequest request) {}
}
