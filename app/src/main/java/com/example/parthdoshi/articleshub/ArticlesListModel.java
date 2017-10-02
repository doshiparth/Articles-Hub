package com.example.parthdoshi.articleshub;

import com.neel.articleshubapi.restapi.beans.ShortArticleDetail;

class ArticlesListModel {
    //private String articleHeading;
    //private String articleMetadata;
    private ShortArticleDetail articleDetail;

    ArticlesListModel(ShortArticleDetail articleDetail) {
        this.articleDetail = articleDetail;
    }

    String getArticleHeading() {
        return articleDetail.getTitle();
    }

    String getArticleAuthorName() {
        return articleDetail.getAuthor();
    }

    String getArticleDate() {
        return articleDetail.getDate();
    }

    ShortArticleDetail getShortArticleDetail() {
        return articleDetail;
    }
}
