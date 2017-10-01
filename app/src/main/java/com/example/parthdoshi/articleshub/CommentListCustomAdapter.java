package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

class CommentListCustomAdapter extends RecyclerView.Adapter<CommentListCustomAdapter.MyViewHolder> {
    Context context;
    private CommentListModel[] commentList;

    CommentListCustomAdapter(Context context, CommentListModel[] commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_comment_list_custom_row, parent, false);
        return (new MyViewHolder(view));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.commentContent.setText(commentList[position].getUsersComment());
        holder.commentUsername.setText(commentList[position].getUsersName());
        holder.commentDate.setText(commentList[position].getCommentDate());
        holder.editComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, EditCommentPage.class);
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
        TextView commentUsername;
        TextView commentDate;
        Button editComment;

        public MyViewHolder(View itemView) {
            super(itemView);
            commentContent = (TextView) itemView.findViewById(R.id.txt_custom_row_comment);
            commentUsername = (TextView) itemView.findViewById(R.id.txt_custom_row_username);
            commentDate = (TextView) itemView.findViewById(R.id.txt_custom_row_date);
            editComment =(Button) itemView.findViewById(R.id.btn_edit_comment);
        }
    }
}
