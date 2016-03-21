package com.example.v15.migoproductcatalog.Model;

import android.graphics.drawable.Drawable;

/**
 * Created by V15 on 18/03/2016.
 */
public class Products {

    private String Title;
    private int GenreID;
    private String GenreName;
    private int resId;
    private int externalFlag;
    private String fileName;
    private String otherInfo;

    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getExternalFlag() {
        return externalFlag;
    }

    public void setExternalFlag(int externalFlag) {
        this.externalFlag = externalFlag;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getGenreID() {
        return GenreID;
    }

    public void setGenreID(int genreID) {
        this.GenreID = genreID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getGenreName() {
        return GenreName;
    }

    public void setGenreName(String genreName) {
        this.GenreName = genreName;
    }

}
