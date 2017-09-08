package com.example.parthdoshi.articleshub;

/**
 * Created by Parth on 09-09-2017.
 */

public class HomePageModel {
    private String articleHeading;
    private String articleMetadata;

    public HomePageModel() {
    }

    public HomePageModel(String articleHeading, String articleMetadata) {
        this.articleHeading = articleHeading;
        this.articleMetadata = articleMetadata;
    }

    public String getArticleHeading() {
        return articleHeading;
    }

    public void setArticleHeading(String articleHeading) {
        this.articleHeading = articleHeading;
    }

    public String getArticleMetadata() {
        return articleMetadata;
    }

    public void setArticleMetadata(String articleMetadata) {
        this.articleMetadata = articleMetadata;
    }
}
