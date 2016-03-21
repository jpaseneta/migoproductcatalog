package com.example.v15.migoproductcatalog.Model;

/**
 * Created by V15 on 18/03/2016.
 */
public class Books extends Products{

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    private String Author;

}
