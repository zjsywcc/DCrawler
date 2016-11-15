package com.moecheng.distributedcrawler.test;

import com.moecheng.distributedcrawler.Crawler;
import com.moecheng.distributedcrawler.model.Site;
import com.moecheng.distributedcrawler.processor.Processor;
import com.moecheng.distributedcrawler.model.Page;
import com.moecheng.distributedcrawler.scheduler.RedisScheduler;
import org.apache.commons.cli.*;

import java.util.List;

/**
 * Created by mengchenyun on 2016/11/11.
 */
public class BookuuCrawlerDistributed implements Processor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(0).setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36");

    @Override
    public void process(Page page) {
        List<String> strings = page.getHtml().links().regex("http://detail.bookuu.com/[0-9]{7}.html").all();
        page.addTargetRequests(strings);
        BookInfo bookInfo = new BookInfo();
        bookInfo.setBookAuthor(page.getHtml().xpath("//div[@class='parameter']/ul/li[3]/a/text()").toString());
        bookInfo.setBookEdition(page.getHtml().xpath("//div[@class='parameter']/ul/li[9]/text()").toString());
        bookInfo.setBookISBN(page.getHtml().xpath("//div[@class='parameter']/ul/li/span/text()").toString());
        bookInfo.setBookName(page.getHtml().xpath("//div[@id='name']/h2/text()").toString());
        bookInfo.setBookPress(page.getHtml().xpath("//div[@class='parameter']/ul/li[1]/a/text()").toString());
        bookInfo.setBookPrice(page.getHtml().xpath("//span[@id='bk-d-pricing']/text()").toString());
        bookInfo.setBookImage(page.getHtml().xpath("//div[@class='amplify']/a/@href").toString());

        page.putField("bookInfo", bookInfo);
    }

    @Override
    public Site getSite() {
        return site;
    }

    static Options opts = new Options();

    static {
        // 配置两个参数
        // -h --help 帮助文档
        // -f --file file参数
        opts.addOption("h", "help", false, "The command help");
        opts.addOption("t", "thread", true,
                "Customize the thread number.");
    }

    /**
     * 提供程序的帮助文档
     */
    static void printHelp(Options opts) {
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("The crawler app. Show how to use crawler app.", opts);
    }

    public static void main(String[] args) throws ParseException {
        int threadNum = 5;
        // 解析参数
        CommandLineParser parser = new PosixParser();
        CommandLine cl = parser.parse(opts, args);

        if(cl.hasOption("h")) {
            printHelp(opts);
            return;
        }
        if(cl.hasOption("t")) {
            threadNum = Integer.parseInt(cl.getOptionValue("thread"));
        }
        System.out.println("Running crawler with thread of "+threadNum+" default is 5");
        Crawler.create(new BookuuCrawlerTest()).addUrl("http://www.bookuu.com/").setScheduler(new RedisScheduler("127.0.0.1")).setThread(threadNum).addPipeline(new BookInfoPipeline()).run();
    }
}
