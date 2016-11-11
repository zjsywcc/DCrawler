package com.moecheng.distributedcrawler.slaver.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public class ResultItems {

    private Map<String, Object> fields = new LinkedHashMap<String, Object>();

    private URLRequest URLRequest;

    private boolean skip;

    public <T> T get(String key) {
        Object o = fields.get(key);
        if (o == null) {
            return null;
        }
        return (T) fields.get(key);
    }

    public Map<String, Object> getAll() {
        return fields;
    }

    public <T> ResultItems put(String key, T value) {
        fields.put(key, value);
        return this;
    }

    public URLRequest getURLRequest() {
        return URLRequest;
    }

    public ResultItems setURLRequest(URLRequest URLRequest) {
        this.URLRequest = URLRequest;
        return this;
    }

    /**
     * Whether to skip the result.<br>
     * Result which is skipped will not be processed by Pipeline.
     *
     * @return whether to skip the result
     */
    public boolean isSkip() {
        return skip;
    }



    @Override
    public String toString() {
        return "ResultItems{" +
                "fields=" + fields +
                ", URLRequest=" + URLRequest +
                ", skip=" + skip +
                '}';
    }
}
