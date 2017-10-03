package com.example.parthdoshi.articleshub;

import com.neel.articleshubapi.restapi.beans.CommentDetail;

class CommentListModel {

    private CommentDetail commentDetail;

    CommentListModel(CommentDetail commentDetail){
        this.commentDetail = commentDetail;
    }

    String getUsersComment() {
        return commentDetail.getContent();
    }

    String getUsersName() {
        return commentDetail.getUserName();
    }

    String getCommentDate() {
        return commentDetail.getDate();
    }

    String getCommentTime() {
        return commentDetail.getTime();
    }

    Long getCommentID(){
        return commentDetail.getCommentId();
    }

    Long getArticleID() {
        return commentDetail.getArticleId();
    }
}
