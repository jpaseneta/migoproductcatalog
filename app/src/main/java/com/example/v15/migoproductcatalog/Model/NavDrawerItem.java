package com.example.v15.migoproductcatalog.Model;

/**
 * Created by V15 on 18/03/2016.
 */
import android.graphics.drawable.Drawable;

public class NavDrawerItem {
    private boolean showNotify;
    private String title;
    private Drawable drawable;

    public NavDrawerItem() {

    }

    public NavDrawerItem(boolean showNotify, String title, Drawable drawable) {
        this.showNotify = showNotify;
        this.title = title;
        this.drawable = drawable;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getDrawable() { return drawable; }

    public void setDrawable(Drawable drawable) { this.drawable = drawable;}
}
