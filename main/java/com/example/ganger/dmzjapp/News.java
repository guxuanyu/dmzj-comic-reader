package com.example.ganger.dmzjapp;

/**
 * Created by ganger on 2017/6/15.
 */
public class News {
    String recentUrl;
    String title;
    String recentTitle;
    String allUrl;
    String imageUrl;
    String author;

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "News{" +
                "recentUrl='" + recentUrl + '\'' +
                ", title='" + title + '\'' +
                ", recentTitle='" + recentTitle + '\'' +
                ", allUrl='" + allUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRecentUrl() {
        return recentUrl;
    }

    public void setRecentUrl(String recentUrl) {
        this.recentUrl = recentUrl;
    }

    public String getRecentTitle() {
        return recentTitle;
    }

    public void setRecentTitle(String recentTitle) {
        this.recentTitle = recentTitle;
    }

    public String getAllUrl() {
        return allUrl;
    }

    public void setAllUrl(String allUrl) {
        this.allUrl = allUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
