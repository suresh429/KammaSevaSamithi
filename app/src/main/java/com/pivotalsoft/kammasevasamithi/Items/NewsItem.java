package com.pivotalsoft.kammasevasamithi.Items;

/**
 * Created by Gangadhar on 10/30/2017.
 */

public class NewsItem {
    private String newsid;
    private String title;
    private String description;
   // private String time;
    private String date;

    public NewsItem(String newsid, String title, String description, String date) {
        this.newsid = newsid;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getNewsid() {
        return newsid;
    }

    public void setNewsid(String newsid) {
        this.newsid = newsid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
