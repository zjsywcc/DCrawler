package com.moecheng.distributedcrawler.test;

import com.moecheng.distributedcrawler.pipeline.Pipeline;
import com.moecheng.distributedcrawler.model.ResultItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by mengchenyun on 2016/11/11.
 */
public class BookInfoPipelineRemote implements Pipeline {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void process(ResultItems resultItems) {
//        ApplicationContext ac = new FileSystemXmlApplicationContext("src/main/resources/spring/applicationContext-myBatis.xml");
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext*.xml");
        BookInfoDAO bookInfoDAO = (BookInfoDAO) ac.getBean(BookInfoDAO.class);

        BookInfo bookInfo = resultItems.get("bookInfo");
        bookInfoDAO.add(bookInfo);
        logger.info(bookInfo.toString());
    }
}
