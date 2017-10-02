package com.example.parthdoshi.articleshub;

import com.neel.articleshubapi.restapi.beans.ShortArticleDetail;

class ArticlesEditListModel {
    //private String articleHeading;
    //private String articleMetadata;
    private ShortArticleDetail articleDetail;

    ArticlesEditListModel(ShortArticleDetail articleDetail) {
        this.articleDetail = articleDetail;
    }

    String getArticleHeading() {
        return articleDetail.getTitle();
    }

    String getArticleDate() {
        return articleDetail.getDate();
    }

    ShortArticleDetail getShortArticleDetail() {
        return articleDetail;
    }
}
