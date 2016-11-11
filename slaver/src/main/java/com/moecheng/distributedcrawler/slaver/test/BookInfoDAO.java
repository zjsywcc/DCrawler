package com.moecheng.distributedcrawler.slaver.test;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;


/**
 * @author code4crafer@gmail.com
 *         Date: 13-6-23
 *         Time: 下午4:27
 */
public interface BookInfoDAO {

    @Insert("insert into bookInfo (bookUrl,bookName,bookAuthor,bookPress,bookPrice,bookImage,bookEdition,bookISBN,update_time) values (#{bookInfo.bookUrl},#{bookInfo.bookName},#{bookInfo.bookAuthor},#{bookInfo.bookPress},#{bookInfo.bookPrice},#{bookInfo.bookImage},#{bookInfo.bookEdition},#{bookInfo.bookISBN},now())")
    @Options(useGeneratedKeys = true, keyProperty = "bookInfo.id")
    public int add(@Param("bookInfo") BookInfo bookInfo);
}
