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
        createJSONFile3();
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

    public static void createJSONFile1() {
        Config config = new Config();
        config.setStartUrls("http://dapenti.com/blog/index.asp");
        config.setRegex("more.asp?name=xilei&id=[0-9]{6}.html");
        Map<String, Map<String, String>> results = new HashMap<>();
        Map<String, String> xpath = new HashMap<>();
        xpath.put("title", "/html/body/table/tbody/tr/td[1]/div/table[1]/tbody/tr[1]/td/div/span/span/a[2]");
        xpath.put("content", "/html/body/table/tbody/tr/td[1]/div/table[1]/tbody/tr[2]/td/div[1]");
        results.put("ArticleInfo", xpath);
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

    public static void createJSONFile2() {
        Config config = new Config();
        config.setStartUrls("http://www.gamersky.com/");
        config.setRegex("http://www.gamersky.com/news/201611/[0-9]{6}.shtml");
        Map<String, Map<String, String>> results = new HashMap<>();
        Map<String, String> xpath = new HashMap<>();
        xpath.put("title", "/html/body/div[7]/div[2]/div[1]/div[1]/div[2]/h1");
        xpath.put("content", "/html/body/div[7]/div[2]/div[1]/div[1]/div[3]");
        results.put("ArticleInfo", xpath);
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

    public static void createJSONFile3() {
        Config config = new Config();
        String[] startUrls = new String[831];
        for (int i = 1; i < 832; i++) {
            startUrls[i - 1] = "http://www.acfun.tv/v/list73/index" + "_" + i + ".htm";
        }
        config.setStartUrls(startUrls);
        config.setRegex("/a/ac[0-9]{7}");
        Map<String, Map<String, String>> results = new HashMap<>();
        Map<String, String> xpath = new HashMap<>();
        xpath.put("title", "//*[@id=\"title_1\"]/span[2]");
        xpath.put("content", "//*[@id=\"area-player\"]");
        results.put("ArticleInfo", xpath);
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
