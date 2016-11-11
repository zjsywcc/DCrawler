package com.moecheng.distributedcrawler.master.model;


import com.moecheng.distributedcrawler.master.model.selector.Html;
import com.moecheng.distributedcrawler.master.model.selector.Selectable;
import com.moecheng.distributedcrawler.master.utils.UrlUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public class Page {
    private URLRequest URLRequest;

    private ResultItems resultItems = new ResultItems();

    private Html html;

    private String rawText;

    private Selectable url;

    private int statusCode;

    private boolean needCycleRetry;

    private List<URLRequest> targetURLRequests = new ArrayList<URLRequest>();

    public Page() {
    }


    /**
     * store extract results
     *
     * @param key key
     * @param field field
     */
    public void putField(String key, Object field) {
        resultItems.put(key, field);
    }

    /**
     * get html content of page
     *
     * @return html
     */
    public Html getHtml() {
        if (html == null) {
            html = new Html(UrlUtils.fixAllRelativeHrefs(rawText, URLRequest.getUrl()));
        }
        return html;
    }



    public List<URLRequest> getTargetURLRequests() {
        return targetURLRequests;
    }

    /**
     * add urls to fetch
     *
     * @param requests requests
     */
    public void addTargetRequests(List<String> requests) {
        synchronized (targetURLRequests) {
            for (String s : requests) {
                if (StringUtils.isBlank(s) || s.equals("#") || s.startsWith("javascript:")) {
                    continue;
                }
                s = UrlUtils.canonicalizeUrl(s, url.toString());
                targetURLRequests.add(new URLRequest(s));
            }
        }
    }


    /**
     * add requests to fetch
     *
     * @param URLRequest URLRequest
     */
    public void addTargetRequest(URLRequest URLRequest) {
        synchronized (targetURLRequests) {
            targetURLRequests.add(URLRequest);
        }
    }

    /**
     * get url of current page
     *
     * @return url of current page
     */
    public Selectable getUrl() {
        return url;
    }

    public void setUrl(Selectable url) {
        this.url = url;
    }

    /**
     * get URLRequest of current page
     *
     * @return URLRequest
     */
    public URLRequest getURLRequest() {
        return URLRequest;
    }

    public boolean isNeedCycleRetry() {
        return needCycleRetry;
    }

    public void setNeedCycleRetry(boolean needCycleRetry) {
        this.needCycleRetry = needCycleRetry;
    }

    public void setURLRequest(URLRequest URLRequest) {
        this.URLRequest = URLRequest;
        this.resultItems.setURLRequest(URLRequest);
    }

    public ResultItems getResultItems() {
        return resultItems;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getRawText() {
        return rawText;
    }

    public Page setRawText(String rawText) {
        this.rawText = rawText;
        return this;
    }

    @Override
    public String toString() {
        return "Page{" +
                "URLRequest=" + URLRequest +
                ", resultItems=" + resultItems +
                ", rawText='" + rawText + '\'' +
                ", url=" + url +
                ", statusCode=" + statusCode +
                ", targetURLRequests=" + targetURLRequests +
                '}';
    }
}
