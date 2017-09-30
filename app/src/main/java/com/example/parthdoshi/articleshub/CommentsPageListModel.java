package com.example.parthdoshi.articleshub;

import com.neel.articleshubapi.restapi.beans.CommentDetail;

/**
 * Created by Parth on 30-09-2017.
 */

public class CommentsPageListModel {
    private CommentDetail commentDetail;

    public CommentsPageListModel(CommentDetail commentDetail){
        this.commentDetail = commentDetail;
    }

    public String getUsersComment() {
        return commentDetail.getContent();
    }

    public String getCommentDate() {
        return commentDetail.getDate();
    }

    public String getCommentTime() {
        return commentDetail.getTime();
    }
}
