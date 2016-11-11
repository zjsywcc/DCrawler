package com.moecheng.distributedcrawler.slaver.downloader;


import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

/**
 * Created by mengchenyun on 2016/10/25.
 */
class OkHttpClientGenerator {

    private OkHttpClient okHttpClient;
    private ConnectionPool connectionPool;

    private static OkHttpClientGenerator mContext;

    OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    static OkHttpClientGenerator getInstance() {
        if(mContext == null) {
            return new OkHttpClientGenerator();
        }
        return mContext;
    }

    private OkHttpClientGenerator() {
        this.okHttpClient = new OkHttpClient();
        this.connectionPool = new ConnectionPool();
    }

    OkHttpClientGenerator setPoolSize(int poolSize) {
        connectionPool = new ConnectionPool(poolSize, 5L, TimeUnit.MINUTES);
        okHttpClient.newBuilder().connectionPool(connectionPool);
        return this;
    }

}
