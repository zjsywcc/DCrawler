package com.moecheng.distributedcrawler.network.model;



import java.io.Serializable;
import java.util.Map;

/**
 * Created by mengchenyun on 2016/11/10.
 */
public class Config implements Serializable {

    private static final long serialVersionUID = -6107079102565303122L;

    /*model config*/
    private String[] startUrls;

    private String regex;

    private Map<String, Map<String, String>> results;

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

    public Map<String, Map<String, String>> getResults() {
        return results;
    }

    public void setResults(Map<String, Map<String, String>> results) {
        this.results = results;
    }

}
