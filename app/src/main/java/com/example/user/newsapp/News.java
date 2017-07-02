package com.example.user.newsapp;

/**
 * Created by user on 6/21/2017.
 */

public class News {
    private String mTitle;
    private String mSection;
    private String mInfo;

    public News(String title, String section, String info) {
        mTitle = title;
        mSection = section;
        mInfo = info;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getInfo() {
        return mInfo;
    }
}
