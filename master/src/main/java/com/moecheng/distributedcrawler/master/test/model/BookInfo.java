package com.moecheng.distributedcrawler.master.test.model;


import java.io.Serializable;

/**
 * Created by fenghongqin on 2016/09/03.
 */
public class BookInfo implements Serializable {

    private static final long serialVersionUID = -4836641624153289759L;

    private Integer id;

    private String bookUrl;

    private String bookAuthor;

    private String bookEdition;

    private String bookISBN;

    private String bookName;

    private String bookPress;

    private String bookPrice;

    private String bookImage;

    public BookInfo() {
        bookUrl = "";
        bookAuthor = "";
        bookEdition = "";
        bookISBN = "";
        bookName = "";
        bookPress = "";
        bookPrice = "";
        bookImage = "";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookUrl() {
        return bookUrl;
    }

    public void setBookUrl(String bookUrl) {
        this.bookUrl = bookUrl;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookEdition() {
        return bookEdition;
    }

    public void setBookEdition(String bookEdition) {
        this.bookEdition = bookEdition;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookPress() {
        return bookPress;
    }

    public void setBookPress(String bookPress) {
        this.bookPress = bookPress;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookImage() {
        return bookImage;
    }

    public void setBookImage(String bookImage) {
        this.bookImage = bookImage;
    }

    @Override
    public String toString() {
        return "BookInfo{" +
                "id=" + id +
                ", bookUrl='" + bookUrl + '\'' +
                ", bookAuthor='" + bookAuthor + '\'' +
                ", bookEdition='" + bookEdition + '\'' +
                ", bookISBN='" + bookISBN + '\'' +
                ", bookName='" + bookName + '\'' +
                ", bookPress='" + bookPress + '\'' +
                ", bookPrice='" + bookPrice + '\'' +
                ", bookImage='" + bookImage + '\'' +
                '}';
    }
}
