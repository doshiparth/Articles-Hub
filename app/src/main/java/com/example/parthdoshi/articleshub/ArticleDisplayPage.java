package com.example.parthdoshi.articleshub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ArticleDisplayPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_display_page);

        Bundle articleData = getIntent().getExtras();
        if(articleData==null){
            return;
        }
        String articleHeading = articleData.getString("ArticleHeading");
        final TextView articleTitle = (TextView) findViewById(R.id.text_article_title);
        articleTitle.setText(articleHeading);
    }
}
