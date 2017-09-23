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

    public ArticlesListCustomAdapter(@NonNull Context context, ArticlesListModel[] ArticleList) {
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
        TextView articleMetadata = (TextView) convertView.findViewById(R.id.text_article_metadata);
        articleHeading.setText(currentArticle.getArticleHeading());
        articleMetadata.setText(currentArticle.getArticleMetadata());

        return convertView;
    }

}
