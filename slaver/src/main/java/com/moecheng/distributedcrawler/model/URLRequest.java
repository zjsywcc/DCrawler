package com.moecheng.distributedcrawler.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mengchenyun on 2016/10/27.
 */
public class URLRequest implements Serializable {

    private static final long serialVersionUID = 2062192774891352043L;

    public static final String CYCLE_TRIED_TIMES = "_cycle_tried_times";
    public static final String STATUS_CODE = "statusCode";

    private String url;


    /**
     * Store additional information in extras.
     */
    private Map<String, Object> extras;

    /**
     * Priority of the request.<br>
     * The bigger will be processed earlier. <br>
     *
     */
    private long priority;

    public URLRequest() {
    }

    public URLRequest(String url) {
        this.url = url;
    }


    /**
     * Set the priority of request for sorting.<br>
     * Need a scheduler supporting priority.<br>
     *
     *
     * @param priority priority
     * @return this
     */

    public URLRequest setPriority(long priority) {
        this.priority = priority;
        return this;
    }

    public Object getExtra(String key) {
        if (extras == null) {
            return null;
        }
        return extras.get(key);
    }

    public URLRequest putExtra(String key, Object value) {
        if (extras == null) {
            extras = new HashMap<String, Object>();
        }
        extras.put(key, value);
        return this;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }


    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        URLRequest URLRequest = (URLRequest) o;

        if (!url.equals(URLRequest.url)) return false;

        return true;
    }


    @Override
    public int hashCode() {
        return url.hashCode();
    }



    @Override
    public String toString() {
        return "URLRequest{" +
                "url='" + url + '\'' +
                ", extras=" + extras +
                ", priority=" + priority +
                '}';
    }
}
