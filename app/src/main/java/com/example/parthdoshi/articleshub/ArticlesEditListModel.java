package com.example.parthdoshi.articleshub;

import com.neel.articleshubapi.restapi.beans.ShortArticleDetail;

public class ArticlesEditListModel {
    //private String articleHeading;
    //private String articleMetadata;
    private ShortArticleDetail articleDetail;

    public ArticlesEditListModel(ShortArticleDetail articleDetail) {
        this.articleDetail = articleDetail;
    }

    public String getArticleHeading() {
        return articleDetail.getTitle();
    }

    public String getArticleDate() {
        return articleDetail.getDate();
    }

    public ShortArticleDetail getShortArticleDetail() {
        return articleDetail;
    }
}
