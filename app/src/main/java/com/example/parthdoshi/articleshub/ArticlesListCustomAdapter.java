package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class ArticlesListCustomAdapter extends RecyclerView.Adapter<ArticlesListCustomAdapter.MyViewHolder> {
    private Context mContext;
    private ArticlesListModel[] articleList;

    ArticlesListCustomAdapter(Context context, ArticlesListModel[] articleList) {
        this.mContext = context;
        this.articleList = articleList;
    }

    @Override
    public ArticlesListCustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_articles_list_custom_row, parent, false);
        return (new MyViewHolder(view));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.articleHeading.setText(articleList[position].getArticleHeading());
        String authorName = "By " + articleList[position].getArticleAuthorName();
        holder.articleAuthorName.setText(authorName);
        String date = "On " + articleList[position].getArticleDate();
        holder.articleDate.setText(date);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mContext, ArticleDisplayPage.class);
                myIntent.putExtra("ArticleLink", articleList[holder.getAdapterPosition()].getShortArticleDetail().getLink());
                myIntent.putExtra("ArticleAuthor", false);
                mContext.startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView articleHeading;
        TextView articleAuthorName;
        TextView articleDate;

        MyViewHolder(View itemView) {
            super(itemView);
            articleHeading = (TextView) itemView.findViewById(R.id.text_article_heading);
            articleAuthorName = (TextView) itemView.findViewById(R.id.text_article_author_name);
            articleDate = (TextView) itemView.findViewById(R.id.text_article_card_date);
        }
    }
}