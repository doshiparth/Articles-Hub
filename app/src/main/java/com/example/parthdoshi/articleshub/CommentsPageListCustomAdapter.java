package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CommentsPageListCustomAdapter extends RecyclerView.Adapter<CommentsPageListCustomAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private CommentsPageListModel[] commentList;

    CommentsPageListCustomAdapter(Context context, CommentsPageListModel[] commentList) {
        //inflater = LayoutInflater.from(context);
        this.commentList = commentList;
    }

    @Override
    public CommentsPageListCustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_comments_page_list_custom_row, parent, false);
        return (new MyViewHolder(view));
    }

    @Override
    public void onBindViewHolder(CommentsPageListCustomAdapter.MyViewHolder holder, int position) {
        holder.commentContent.setText(commentList[position].getUsersComment());
        holder.commentDate.setText(commentList[position].getCommentDate());
        holder.commentTime.setText(commentList[position].getCommentTime());
    }

    @Override
    public int getItemCount() {
        return commentList.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView commentContent;
        TextView commentDate;
        TextView commentTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            commentContent = (TextView) itemView.findViewById(R.id.txt_comments_page_content);
            commentDate = (TextView) itemView.findViewById(R.id.txt_comments_page_date);
            commentTime = (TextView) itemView.findViewById(R.id.txt_comments_page_time);
        }
    }
}