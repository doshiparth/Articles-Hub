package com.example.parthdoshi.articleshub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.neel.articleshubapi.restapi.beans.ArticleDetail;
import com.neel.articleshubapi.restapi.beans.CommentDetail;
import com.neel.articleshubapi.restapi.beans.UserDetail;
import com.neel.articleshubapi.restapi.request.AddRequestTask;
import com.neel.articleshubapi.restapi.request.HeaderTools;
import com.neel.articleshubapi.restapi.request.RequestTask;

import org.springframework.http.HttpMethod;

import java.util.Iterator;

import static com.neel.articleshubapi.restapi.request.HeaderTools.CONTENT_TYPE_JSON;

public class ArticleDisplayPage extends AppCompatActivity {
    TextView articleTitle;
    TextView authorName;
    TextView articleDate;
    TextView articleContent;
    String tagArray[];
    String tagString = "";
    String contentArray[];
    String contentString = "";
    TextView articleTag;
    Button likeButton;
    EditText commentText;
    Button commentButton;
    private String BASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking for internet connection
        if(NetworkStatus.getInstance(this).isOnline())
            setContentView(R.layout.activity_article_display_page);
        else
            NetworkStatus.getInstance(this).buildDialog(this).show();


        BASE_URL = getResources().getString(R.string.BASE_URL);

        articleTitle = (TextView) findViewById(R.id.text_article_title);
        authorName = (TextView) findViewById(R.id.text_author_name);
        articleDate = (TextView) findViewById(R.id.text_article_date);
        articleTag = (TextView) findViewById(R.id.text_article_tag);
        articleContent = (TextView) findViewById(R.id.text_article_content);

        Bundle articleData = getIntent().getExtras();
        if(articleData==null){
            return;
        }

        String articleLink = articleData.getString("ArticleLink");

        RequestTask<ArticleDetail> rt=new RequestTask<>(ArticleDetail.class,CONTENT_TYPE_JSON);
        rt.execute(articleLink);
        final ArticleDetail article=rt.getObj();

        tagArray = new String[article.getTag().size()];
        Iterator<String> itr1= article.getTag().iterator();
        for(int i=0;i<tagArray.length;i++)
            tagArray[i]= itr1.next();
        for(int i=0;i<tagArray.length;i++)
            tagString += tagArray[i]+", ";

        contentArray = new String[article.getContent().size()];
        Iterator<String> itr2= article.getContent().iterator();
        for(int i=0;i<contentArray.length;i++)
            contentArray[i]= itr2.next();
        for(int i=0;i<contentArray.length;i++)
            contentString += contentArray[i]+"\n";

        articleTitle.setText(article.getTitle());
        authorName.setText(article.getAuthor());
        articleTag.setText(tagString);
        articleDate.setText(article.getDate());
        articleContent.setText(contentString);
        //for(String s : article.getContent())
        //    Log.i("doshi",s);
        likeButton = (Button) findViewById(R.id.btn_like);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestTask<String> rt5=new RequestTask<String>(String.class, HttpMethod.POST,
                        HeaderTools.CONTENT_TYPE_JSON,
                        HeaderTools.makeAuth("2c91a00e5e74e4b2015e753593120002"));
                rt5.execute(BASE_URL+"/user/"+"doshi2"+"/like/"+article.getArticleId());
                // TODO replace username with the user's name from SQLite DB.
            }
        });
        commentText = (EditText)findViewById(R.id.edit_comment);

        commentButton = (Button)findViewById(R.id.btn_comment);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentDetail comment = new CommentDetail();
                comment.setArticleId(article.getArticleId());
                comment.setUserName("doshi2");
                comment.setContent(String.valueOf(commentText.getText()));
                AddRequestTask<String,CommentDetail> rt6=new AddRequestTask<String, CommentDetail>(String.class,
                        comment, HttpMethod.POST, HeaderTools.CONTENT_TYPE_JSON,
                        HeaderTools.makeAuth("2c91a00e5e74e4b2015e758850c90003"));
                rt6.execute(BASE_URL+"/comment");
                // TODO replace username with the user's name from SQLite DB.

            }
        });
    }
}
