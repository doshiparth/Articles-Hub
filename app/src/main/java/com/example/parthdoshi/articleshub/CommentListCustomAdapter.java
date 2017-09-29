package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class CommentListCustomAdapter extends RecyclerView.Adapter<CommentListCustomAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private CommentListModel[] commentList;

    CommentListCustomAdapter(Context context, CommentListModel[] commentList) {
        //inflater = LayoutInflater.from(context);
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
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView commentContent;
        TextView commentUsername;
        TextView commentDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            commentContent = (TextView) itemView.findViewById(R.id.txt_custom_row_comment);
            commentUsername = (TextView) itemView.findViewById(R.id.txt_custom_row_username);
            commentDate = (TextView) itemView.findViewById(R.id.txt_custom_row_date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
