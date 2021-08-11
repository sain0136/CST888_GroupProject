package com.cst2335.soccerapi;

/**
 * This class is to set up the items for Recyclerview
 */
public class NewsItem {
    private String newsTitle;
    private String newsDate;
    private String newsImg;
    private String newsLink;
    private String newsDescription;
    private long newsId;

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    public String getNewsImg() {
        return newsImg;
    }

    public void setNewsImg(String newsImg) {
        this.newsImg = newsImg;
    }

    public String getNewsLink() {
        return newsLink;
    }

    public void setNewsLink(String newsLink) {
        this.newsLink = newsLink;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }
    public long getNewsId() {
        return newsId;
    }


    public void setNewsId(long newsId) {
        this.newsId = newsId;
    }
}
