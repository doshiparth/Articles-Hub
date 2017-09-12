package com.example.parthdoshi.articleshub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.neel.articleshubapi.restapi.beans.ArticleDetail;
import com.neel.articleshubapi.restapi.request.AddRequestTask;
import com.neel.articleshubapi.restapi.request.HeaderTools;

import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class WriteArticlePage extends AppCompatActivity {

    private String BASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_article_page);
        BASE_URL = getResources().getString(R.string.BASE_URL);
        ArticleDetail article = new ArticleDetail();
        article.setAuthor("doshi2");
        article.setTitle("android test article");
        //article.setArticleId(1);
        Set<String> tags=new HashSet<String>();
        tags.add("tag1");
        tags.add("tag2");
        article.setTag(tags);
        article.setContent(new ArrayList<String>());
        AddRequestTask<String,ArticleDetail> rt6=new AddRequestTask<String, ArticleDetail>(String.class,
                article, HttpMethod.POST, HeaderTools.CONTENT_TYPE_JSON,
                HeaderTools.makeAuth("2c91a00e5e74e4b2015e758850c90003"));
        rt6.execute(BASE_URL+"/article");
    }
}
