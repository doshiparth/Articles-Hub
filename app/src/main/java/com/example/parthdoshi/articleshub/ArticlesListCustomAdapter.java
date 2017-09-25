package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class ArticlesListCustomAdapter extends ArrayAdapter {
    //ArticlesListModel[] ArticleList = null;
    //Context context;

    private String date = "";
    private String authorName = "";

    ArticlesListCustomAdapter(@NonNull Context context, ArticlesListModel[] ArticleList) {
        super(context, R.layout.activity_articles_list_custom_row, ArticleList);
        //this.context = context;
        //this.ArticleList = resource;
    }

    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        //convertView = inflater.inflate(R.layout.activity_articles_list_custom_row, parent, false);
        ArticlesListModel currentArticle = (ArticlesListModel) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_articles_list_custom_row, parent, false);
        }
        TextView articleHeading = (TextView) convertView.findViewById(R.id.text_article_heading);
        TextView articleAuthorName = (TextView) convertView.findViewById(R.id.text_article_author_name);
        TextView articleDate = (TextView) convertView.findViewById(R.id.text_article_card_date);
        articleHeading.setText(currentArticle.getArticleHeading());
        authorName = "By " + currentArticle.getArticleAuthorName();
        articleAuthorName.setText(authorName);
        date = "On " + currentArticle.getArticleDate();
        articleDate.setText(date);

        return convertView;
    }

}
