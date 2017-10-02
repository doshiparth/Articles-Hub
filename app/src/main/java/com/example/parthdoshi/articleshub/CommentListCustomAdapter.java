package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

class CommentListCustomAdapter extends RecyclerView.Adapter<CommentListCustomAdapter.MyViewHolder> {
    Context context;
    String currentUser;
    private CommentListModel[] commentList;
    String articleLink;

    CommentListCustomAdapter(Context context, CommentListModel[] commentList, String currentUser, String articleLink) {
        this.context = context;
        this.commentList = commentList;
        this.currentUser = currentUser;
        this.articleLink = articleLink;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_comment_list_custom_row, parent, false);
        return (new MyViewHolder(view));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.commentContent.setText(commentList[position].getUsersComment());
        holder.commentUsername.setText(commentList[position].getUsersName());
        holder.commentDate.setText(commentList[position].getCommentDate());

        if (commentList[position].getUsersName().equals(currentUser))
            holder.editComment.setVisibility(View.VISIBLE);
        else
            holder.editComment.setVisibility(View.GONE);

        holder.editComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(context, EditCommentPage.class);
                myIntent.putExtra("cid", commentList[position].getCommentID());
                Log.i("Adapter cid--------", "" + commentList[position].getCommentID());
                myIntent.putExtra("commentContent", commentList[position].getUsersComment());
                Log.i("Adapter commentContent", commentList[position].getUsersComment());
                myIntent.putExtra("aid", commentList[position].getArticleID());
                Log.i("Adapter aid------", "" + commentList[position].getArticleID());
                myIntent.putExtra("articleLink", articleLink);
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
            editComment = (Button) itemView.findViewById(R.id.btn_edit_comment);
        }
    }
}
