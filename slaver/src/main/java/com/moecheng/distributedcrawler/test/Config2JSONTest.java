package com.moecheng.distributedcrawler.test;

import com.alibaba.fastjson.JSON;
import com.moecheng.distributedcrawler.network.model.Config;
import com.moecheng.distributedcrawler.utils.FileReaderUtil;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mengchenyun on 2016/11/16.
 */
public class Config2JSONTest {

    public static void main(String[] args) {
        createJSONFile();
//        readJSONFile();
    }

    public static void createJSONFile() {
        Config config = new Config();
        config.setStartUrls("http://www.bookuu.com/");
        config.setRegex("http://detail.bookuu.com/[0-9]{7}.html");
        Map<String, Map<String, String>> results = new HashMap<>();
        Map<String, String> xpath = new HashMap<>();
        xpath.put("bookAuthor", "//div[@class='parameter']/ul/li[3]/a/text()");
        xpath.put("bookEdition", "//div[@class='parameter']/ul/li[9]/text()");
        xpath.put("bookISBN", "//div[@class='parameter']/ul/li/span/text()");
        xpath.put("bookName", "//div[@id='name']/h2/text()");
        xpath.put("bookPress", "//div[@class='parameter']/ul/li[1]/a/text()");
        xpath.put("bookPrice", "//span[@id='bk-d-pricing']/text()");
        xpath.put("bookImage", "//div[@class='amplify']/a/@href");
        results.put("BookInfo", xpath);
        config.setResults(results);
        String jsonStr = JSON.toJSONString(config);
        try {
            PrintWriter out = new PrintWriter("config.txt");
            out.println(jsonStr);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void readJSONFile() {
        String configJsonStr = FileReaderUtil.readFile("config.txt");
        System.out.println(configJsonStr);
    }
}
