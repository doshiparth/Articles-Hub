package com.example.parthdoshi.articleshub;

import com.neel.articleshubapi.restapi.beans.CommentDetail;

class CommentListModel {
    //String usersComment;
    //String usersName;
    //String commentDate;
    private CommentDetail commentDetail;

    public CommentListModel(CommentDetail commentDetail){
        this.commentDetail = commentDetail;
    }
    public CommentDetail getCommentDetail() {
        return commentDetail;
    }

    public String getUsersComment() {
        return commentDetail.getContent();
    }

    public String getUsersName() {
        return commentDetail.getUserName();
    }

    public String getCommentDate() {
        return commentDetail.getDate();
    }
}
