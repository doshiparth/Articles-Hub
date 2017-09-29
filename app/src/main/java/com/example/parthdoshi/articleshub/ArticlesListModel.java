package com.example.parthdoshi.articleshub;

import com.neel.articleshubapi.restapi.beans.ShortArticleDetail;

public class ArticlesListModel {
    //private String articleHeading;
    //private String articleMetadata;
    private ShortArticleDetail articleDetail;

    public ArticlesListModel() {
    }

    public ArticlesListModel(ShortArticleDetail articleDetail) {
        this.articleDetail = articleDetail;
    }

    public String getArticleHeading() {
        return articleDetail.getTitle();
    }

    public String getArticleAuthorName() {
        return articleDetail.getAuthor();
    }

    public String getArticleDate() {
        return articleDetail.getDate();
    }

    public ShortArticleDetail getShortArticleDetail() {
        return articleDetail;
    }
}
