package com.example.parthdoshi.articleshub;

import com.neel.articleshubapi.restapi.beans.CommentDetail;
import com.neel.articleshubapi.restapi.beans.Link;

class CommentsPageListModel {
    private CommentDetail commentDetail;

    CommentsPageListModel(CommentDetail commentDetail){
        this.commentDetail = commentDetail;
    }

    String getUsersComment() {
        return commentDetail.getContent();
    }

    String getCommentDate() {
        return commentDetail.getDate();
    }

    String getCommentTime() {
        return commentDetail.getTime();
    }

    Link getCommentArticleLink(){
        return commentDetail.getLink("article");
    }
}
