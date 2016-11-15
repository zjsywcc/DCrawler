package com.moecheng.distributedcrawler.network.model;

import com.moecheng.distributedcrawler.test.model.BookInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mengchenyun on 2016/11/10.
 */
public class Config implements Serializable {

    private static final long serialVersionUID = -6107079102565303122L;


    /*model config*/
    private String[] startUrls;

    private String regex;

    private String entityName;

    private BookInfo bookInfo;

    private Map<String, String> xpath;


    public String[] getStartUrls() {
        return startUrls;
    }

    public void setStartUrls(String... startUrls) {
        this.startUrls = startUrls;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }


    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }

    public Map<String, String> getXpath() {
        return xpath;
    }

    public void setXpath(Map<String, String> xpath) {
        this.xpath = xpath;
    }

}
