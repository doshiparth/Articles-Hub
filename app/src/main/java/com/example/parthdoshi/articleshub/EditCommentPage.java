package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.neel.articleshubapi.restapi.beans.ArticleDetail;
import com.neel.articleshubapi.restapi.beans.CommentDetail;
import com.neel.articleshubapi.restapi.request.AddRequestTask;
import com.neel.articleshubapi.restapi.request.HeaderTools;
import com.neel.articleshubapi.restapi.request.RequestTask;

import org.springframework.http.HttpMethod;

import me.anwarshahriar.calligrapher.Calligrapher;

public class EditCommentPage extends AppCompatActivity {

    EditText commentText;
    Button editCommentButton;
    Button deleteCommentButton;

    CommentDetail comment;
    ArticleDetail article;

    String token = null, userName = null;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking for internet connection
        if (NetworkStatus.getInstance(this).isOnline())
            setContentView(R.layout.activity_edit_comment_page);
        else
            NetworkStatus.getInstance(this).buildDialog(this).show();

        Calligrapher calligrapher = new Calligrapher(EditCommentPage.this);
        calligrapher.setFont(EditCommentPage.this, FixedVars.FONT_NAME, true);

        sharedPref = getSharedPreferences(FixedVars.PREF_NAME, Context.MODE_PRIVATE);
        userName = sharedPref.getString(FixedVars.PREF_USER_NAME, "");
        token = sharedPref.getString(FixedVars.PREF_LOGIN_TOKEN, "");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Main Code
        commentText = (EditText) findViewById(R.id.txt_comment_edit);
        editCommentButton = (Button) findViewById(R.id.btn_edit_comment_save);
        deleteCommentButton = (Button) findViewById(R.id.btn_delete_comment);

        Intent intent = getIntent();
        final Long cid = intent.getExtras().getLong("cid");
        String commentContent = intent.getExtras().getString("commentContent");
        final Long aid = intent.getExtras().getLong("aid");

        Log.i("CID--------", "" + cid);
        Log.i("AID--------", "" + aid);
        Log.i("commentContent--------", commentContent);

        commentText.setText(commentContent);

        editCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentText.getText().toString().isEmpty()) {
                    Toast.makeText(EditCommentPage.this, "Please enter something!!", Toast.LENGTH_SHORT).show();
                } else {
                    comment = new CommentDetail();

                    comment.setArticleId(aid);
                    comment.setCommentId(cid);
                    comment.setContent(commentText.getText().toString());
                    comment.setUserName(userName);

                    AddRequestTask<String, CommentDetail> commentRequest = new AddRequestTask<String, CommentDetail>(String.class,
                            comment, HttpMethod.PUT, HeaderTools.CONTENT_TYPE_JSON,
                            HeaderTools.makeAuth(token));
                    commentRequest.execute(FixedVars.BASE_URL + "/comment/" + cid);
                    commentText.setText("");
                    Toast.makeText(EditCommentPage.this, "Comment edited successfully", Toast.LENGTH_LONG).show();
                }
            }
        });

        deleteCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentDetail comment = new CommentDetail();
                RequestTask<String> deleteCommentRequest = new RequestTask<String>(String.class, HttpMethod.DELETE,
                        HeaderTools.CONTENT_TYPE_JSON,
                        HeaderTools.makeAuth(token));
                deleteCommentRequest.execute(FixedVars.BASE_URL + "/comment/" + cid);
                commentText.setText("");
                Toast.makeText(EditCommentPage.this, "Comment deleted", Toast.LENGTH_LONG).show();
            }
        });
    }
}
