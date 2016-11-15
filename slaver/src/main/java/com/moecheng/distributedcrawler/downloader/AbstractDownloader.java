package com.moecheng.distributedcrawler.downloader;


import com.moecheng.distributedcrawler.model.URLRequest;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public abstract class AbstractDownloader implements Downloader {

    void onSuccess(URLRequest request) {}

    void onError(URLRequest request) {}
}
