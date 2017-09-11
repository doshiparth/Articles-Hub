package com.example.parthdoshi.articleshub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ArticleDisplayPage extends AppCompatActivity {
    TextView articleTitle;
    TextView authorName;
    TextView articleDate;
    TextView articleTag;
    TextView articleContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_display_page);

        articleTitle = (TextView) findViewById(R.id.text_article_title);
        authorName = (TextView) findViewById(R.id.text_author_name);
        articleDate = (TextView) findViewById(R.id.text_article_date);
        articleTag = (TextView) findViewById(R.id.text_article_tag);
        Bundle articleData = getIntent().getExtras();
        if(articleData==null){
            return;
        }
        String articleHeading = articleData.getString("ArticleHeading");
        articleTitle.setText(articleHeading);
        articleTag.setText();
        articleDate.setText();
        articleTag.setText();
        articleContent.setText();
    }
}
