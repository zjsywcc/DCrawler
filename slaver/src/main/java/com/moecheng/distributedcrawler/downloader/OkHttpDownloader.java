package com.moecheng.distributedcrawler.downloader;




import com.moecheng.distributedcrawler.Task;
import com.moecheng.distributedcrawler.model.Page;
import com.moecheng.distributedcrawler.model.Site;
import com.moecheng.distributedcrawler.model.URLRequest;
import com.moecheng.distributedcrawler.model.selector.PlainText;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public class OkHttpDownloader extends AbstractDownloader {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private OkHttpClientGenerator okHttpClientGenerator = OkHttpClientGenerator.getInstance();

    @Override
    public Page download(URLRequest URLRequest, Task task) {
        Site site = null;
        if(task != null) {
            site = task.getSite();
        }
        logger.info("downloading page {}", URLRequest.getUrl());
        int statusCode=0;
        Request request = new Request.Builder()
                .url(URLRequest.getUrl())
                .build();
        try {
            Response response = okHttpClientGenerator.getOkHttpClient().newCall(request).execute();
            if(response.isSuccessful()) {
                Page page = handleResponse(URLRequest, response);
                onSuccess(URLRequest);
                return page;
            } else {
                logger.warn("code error " + statusCode + "\t" + URLRequest.getUrl());
                return null;
            }
        } catch (IOException e) {
            logger.warn("download page " + URLRequest.getUrl() + " error", e);
//            if (site.getCycleRetryTimes() > 0) {
//                return addToCycleRetry(URLRequest, site);
//            }
            onError(URLRequest);
            return null;
        }
    }

    private Page handleResponse(URLRequest URLRequest, Response response) throws IOException {
        String content = getContent(response);
        Page page = new Page();
        page.setRawText(content);
        page.setUrl(new PlainText(URLRequest.getUrl()));
        page.setURLRequest(URLRequest);
        page.setStatusCode(response.code());
        return page;
    }

    private String getContent(Response response) throws IOException {
        String content = response.body().string();
        return content;
    }



    @Override
    public void setThread(int thread) {
        okHttpClientGenerator.setPoolSize(thread);
    }

}
