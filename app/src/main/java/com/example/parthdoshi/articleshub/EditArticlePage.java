package com.example.parthdoshi.articleshub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.neel.articleshubapi.restapi.beans.ArticleDetail;
import com.neel.articleshubapi.restapi.request.AddRequestTask;
import com.neel.articleshubapi.restapi.request.HeaderTools;
import com.neel.articleshubapi.restapi.request.RequestTask;

import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.neel.articleshubapi.restapi.request.HeaderTools.CONTENT_TYPE_JSON;

public class EditArticlePage extends AppCompatActivity {

    private String BASE_URL;
    Button deleteArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking for internet connection
        if(NetworkStatus.getInstance(this).isOnline())
            setContentView(R.layout.activity_write_article_page);
        else
            NetworkStatus.getInstance(this).buildDialog(this).show();

        BASE_URL = getResources().getString(R.string.BASE_URL);
        ArticleDetail articleNew = new ArticleDetail();

        RequestTask<ArticleDetail> rt=new RequestTask<>(ArticleDetail.class,CONTENT_TYPE_JSON);
        rt.execute(BASE_URL+"/article/"+21);             //Change Article id from last page
        final ArticleDetail articleOld=rt.getObj();
        //if(articleOld==null)
        Log.i("Sorry","author old "+articleOld.getAuthor());
        Log.i("Sorry","author ID old "+articleOld.getArticleId());
        articleNew.setAuthor(articleOld.getAuthor());
        articleNew.setTitle("android test article3");
        //int aid = (int) article.getArticleId();
        articleNew.setArticleId(articleOld.getArticleId());
        articleNew.setDate(articleOld.getDate());
        Set<String> tags=new HashSet<String>();
        tags.add("tag1");
        tags.add("tag2");
        articleNew.setTag(tags);
        List<String> content = new ArrayList<>();
        content.add("vhjhhgjgj");
        content.add("vhjj");
        content.add("hhgjgj");
        content.add("hfhhgjgj");
        content.add("khkjhkijhhgjgj");
        articleNew.setContent(content);
        Log.i("new article","new "+articleNew.getAuthor());
        AddRequestTask<String,ArticleDetail> rt6=new AddRequestTask<String, ArticleDetail>(String.class,
                articleNew, HttpMethod.PUT, HeaderTools.CONTENT_TYPE_JSON,
                HeaderTools.makeAuth("2c91a00e5e74e4b2015e758850c90003"));
        rt6.execute(BASE_URL+"/article/"+articleNew.getArticleId());
        rt6.getObj();

        deleteArticle = (Button) findViewById(R.id.btn_delete_article);
        deleteArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestTask<String> rt5=new RequestTask<String>(String.class, HttpMethod.DELETE,
                        HeaderTools.ACCEPT_JSON,
                        HeaderTools.makeAuth("2c91a00e5e74e4b2015e758850c90003"));
                rt5.execute(BASE_URL+"/article/"+articleOld.getArticleId());
                rt5.getObj();
            }
        });
    }
}
