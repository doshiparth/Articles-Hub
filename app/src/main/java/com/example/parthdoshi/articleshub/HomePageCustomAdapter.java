package com.example.parthdoshi.articleshub;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class HomePageCustomAdapter extends ArrayAdapter {
    HomePageModel[] ArticleList = null;
    Context context;

    public HomePageCustomAdapter(@NonNull Context context, HomePageModel[] resource) {
        super(context, R.layout.activity_home_page_custom_row, resource);
        this.context = context;
        this.ArticleList = resource;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.activity_home_page_custom_row, parent, false);
        TextView articleHeading = (TextView) convertView.findViewById(R.id.text_article_heading);
        TextView articleMetadata = (TextView) convertView.findViewById(R.id.text_article_metadata);
        articleHeading.setText(ArticleList[position].getArticleHeading());
        articleMetadata.setText(ArticleList[position].getArticleMetadata());

        return convertView;
    }

}
