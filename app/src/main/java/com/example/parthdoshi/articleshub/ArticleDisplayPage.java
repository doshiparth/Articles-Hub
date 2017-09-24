package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.neel.articleshubapi.restapi.beans.ArticleDetail;
import com.neel.articleshubapi.restapi.beans.CommentDetail;
import com.neel.articleshubapi.restapi.beans.ShortArticleDetail;
import com.neel.articleshubapi.restapi.beans.UserDetail;
import com.neel.articleshubapi.restapi.request.AddRequestTask;
import com.neel.articleshubapi.restapi.request.HeaderTools;
import com.neel.articleshubapi.restapi.request.RequestTask;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

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
    Button commentEditButton;
    Button articleEditButton;
    String token = null, userName = null;
    ShortArticleDetail[] articleDetails;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking for internet connection
        if (NetworkStatus.getInstance(this).isOnline())
            setContentView(R.layout.activity_article_display_page);
        else
            NetworkStatus.getInstance(this).buildDialog(this).show();

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layout_article_display_page);
        relativeLayout.requestFocus();
        articleTitle = (TextView) findViewById(R.id.text_article_title);
        authorName = (TextView) findViewById(R.id.text_author_name);
        articleDate = (TextView) findViewById(R.id.text_article_date);
        articleTag = (TextView) findViewById(R.id.text_article_tag);
        articleContent = (TextView) findViewById(R.id.text_article_content);
        articleEditButton = (Button) findViewById(R.id.btn_edit_article);

        sharedPref = getSharedPreferences(FixedVars.PREF_NAME, Context.MODE_PRIVATE);
        userName = sharedPref.getString(FixedVars.PREF_USER_NAME, "");
        token = sharedPref.getString(FixedVars.PREF_LOGIN_TOKEN, "");

        Bundle articleData = getIntent().getExtras();
        if (articleData == null) {
            return;
        }

        final String articleLink = articleData.getString("ArticleLink");
        Boolean articleAuthor = articleData.getBoolean("ArticleAuthor");

        if(articleAuthor)
            articleEditButton.setVisibility(View.VISIBLE);

        RequestTask<ArticleDetail> rt = new RequestTask<>(ArticleDetail.class, CONTENT_TYPE_JSON);
        rt.execute(articleLink);
        final ArticleDetail article = rt.getObj();

        tagArray = new String[article.getTag().size()];
        Iterator<String> itr1 = article.getTag().iterator();
        for (int i = 0; i < tagArray.length; i++)
            tagArray[i] = itr1.next();
        for (String aTagArray : tagArray)
            tagString += aTagArray + ", ";

        contentArray = new String[article.getContent().size()];
        Iterator<String> itr2 = article.getContent().iterator();
        for (int i = 0; i < contentArray.length; i++)
            contentArray[i] = itr2.next();
        for (String aContentArray : contentArray)
            contentString += aContentArray + "." + "\n";

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
                RequestTask<String> likeRequest = new RequestTask<String>(String.class, HttpMethod.POST,
                        HeaderTools.CONTENT_TYPE_JSON,
                        HeaderTools.makeAuth(token));
                likeRequest.execute(FixedVars.BASE_URL + "/user/" + userName + "/like/" + article.getArticleId());
                HttpStatus status = likeRequest.getHttpStatus();

                //if (status == HttpStatus.ACCEPTED)
                    Toast.makeText(ArticleDisplayPage.this, "Liked", Toast.LENGTH_LONG).show();
                //else
                //    Toast.makeText(ArticleDisplayPage.this, "Error!! Unable to like", Toast.LENGTH_LONG).show();
            }
        });
        commentText = (EditText) findViewById(R.id.edit_comment);
        commentButton = (Button) findViewById(R.id.btn_comment);
        commentEditButton = (Button) findViewById(R.id.btn_edit_comment);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commentText.getText().toString().isEmpty()) {
                    Toast.makeText(ArticleDisplayPage.this, "Please enter something!!", Toast.LENGTH_LONG).show();
                } else {
                    CommentDetail comment = new CommentDetail();
                    comment.setArticleId(article.getArticleId());
                    comment.setUserName(userName);
                    comment.setContent(String.valueOf(commentText.getText()));
                    AddRequestTask<String, CommentDetail> commentRequest = new AddRequestTask<String, CommentDetail>(String.class,
                            comment, HttpMethod.POST, HeaderTools.CONTENT_TYPE_JSON,
                            HeaderTools.makeAuth(token));
                    commentRequest.execute(FixedVars.BASE_URL + "/comment");
                    HttpStatus status = commentRequest.getHttpStatus();
                    //if (status == HttpStatus.CREATED) {
                        Toast.makeText(ArticleDisplayPage.this, "Commented successfully", Toast.LENGTH_LONG).show();
                        commentButton.setVisibility(View.GONE);
                        commentEditButton.setVisibility(View.VISIBLE);
                        commentText.setEnabled(false);
                    //} else
                    //    Toast.makeText(ArticleDisplayPage.this, "Error!! Unable to like", Toast.LENGTH_LONG).show();
                }
            }
        });
        commentEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commentText.setEnabled(true);
                commentButton.setVisibility(View.VISIBLE);
                commentEditButton.setVisibility(View.GONE);
            }
        });
        articleEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ArticleDisplayPage.this, EditArticlePage.class);
                startActivity(myIntent);
                myIntent.putExtra("ArticleLink", articleLink);
            }
        });
    }
}
