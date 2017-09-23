package com.example.parthdoshi.articleshub;

import com.neel.articleshubapi.restapi.beans.ArticleDetail;
import com.neel.articleshubapi.restapi.beans.ShortArticleDetail;

public class ArticlesListModel {
    //private String articleHeading;
    //private String articleMetadata;
    private ShortArticleDetail articleDetail;

    public ArticlesListModel() {
    }

    public ArticlesListModel(ShortArticleDetail articleDetail){
        this.articleDetail=articleDetail;
    }

    /*public ArticlesListModel(String articleHeading, String articleMetadata) {
        this.articleHeading = articleHeading;
        this.articleMetadata = articleMetadata;
    }*/

    public String getArticleHeading() {
        return articleDetail.getTitle();
    }

    //public void setArticleHeading(String articleHeading) {
    //    this.articleHeading = articleHeading;
    //}

    public String getArticleMetadata() {
        return "none";
    }

    //public void setArticleMetadata(String articleMetadata) {
    //    this.articleMetadata = articleMetadata;
    //}

    public ShortArticleDetail getShortArticleDetail(){
        return articleDetail;
    }
}