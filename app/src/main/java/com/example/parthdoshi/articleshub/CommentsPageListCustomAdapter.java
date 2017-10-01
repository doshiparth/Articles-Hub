package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


class CommentsPageListCustomAdapter extends RecyclerView.Adapter<CommentsPageListCustomAdapter.MyViewHolder> {
    Context context;
    private LayoutInflater inflater;
    private CommentsPageListModel[] commentList;
    private String submittedTxt;
    private String titleTxt;

    CommentsPageListCustomAdapter(Context context, CommentsPageListModel[] commentList) {
        //inflater = LayoutInflater.from(context);
        this.commentList = commentList;
        this.context = context;
    }

    @Override
    public CommentsPageListCustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_comments_page_list_custom_row, parent, false);
        return (new MyViewHolder(view));
    }

    @Override
    public void onBindViewHolder(CommentsPageListCustomAdapter.MyViewHolder holder, final int position) {
        holder.commentContent.setText(commentList[position].getUsersComment());
        submittedTxt = "Submitted On " + commentList[position].getCommentDate() + " at " + commentList[position].getCommentTime();
        holder.commentDate.setText(submittedTxt);

        holder.gotoArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, ArticleDisplayPage.class);
                myIntent.putExtra("ArticleLink", commentList[position].getCommentArticleLink().getUrl());
                context.startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView commentContent;
        TextView commentDate;
        Button gotoArticle;

        MyViewHolder(View itemView) {
            super(itemView);
            commentContent = (TextView) itemView.findViewById(R.id.txt_comments_page_content);
            commentDate = (TextView) itemView.findViewById(R.id.txt_comments_page_date);
            gotoArticle = (Button) itemView.findViewById(R.id.btn_gotoarticle);
        }
    }
}