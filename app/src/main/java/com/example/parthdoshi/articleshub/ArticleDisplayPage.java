package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.neel.articleshubapi.restapi.beans.ArticleDetail;
import com.neel.articleshubapi.restapi.beans.CommentDetail;
import com.neel.articleshubapi.restapi.beans.ShortUserDetail;
import com.neel.articleshubapi.restapi.request.AddRequestTask;
import com.neel.articleshubapi.restapi.request.HeaderTools;
import com.neel.articleshubapi.restapi.request.RequestTask;

import org.springframework.http.HttpMethod;

import java.util.Iterator;

import me.anwarshahriar.calligrapher.Calligrapher;

import static com.neel.articleshubapi.restapi.request.HeaderTools.CONTENT_TYPE_JSON;

public class ArticleDisplayPage extends AppCompatActivity {
    TextView articleTitle;
    TextView authorName;
    TextView articleDate;
    TextView articleContent;
    TextView articleLikes;
    String tagArray[];
    String tagString = "";
    String contentArray[];
    String contentString = "";
    String usersComment = "";
    TextView articleTag;
    ToggleButton likeButton;
    EditText commentText;
    ToggleButton commentButton;
    Button commentDeleteButton;

    //For Recycler View
    RecyclerView aprv;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    String token = null, userName = null;
    ShortUserDetail[] totalLikesObj;
    CommentDetail[] articleCommentsObj;
    CommentListModel[] commentList;

    String singleUser;
    SharedPreferences sharedPref;

    //Final String variables to show data in a better way
    String finalAuthorName = "";
    String finalTags = "";
    String finalDate = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking for internet connection
        if (NetworkStatus.getInstance(this).isOnline())
            setContentView(R.layout.activity_article_display_page);
        else
            NetworkStatus.getInstance(this).buildDialog(this).show();

        Calligrapher calligrapher = new Calligrapher(ArticleDisplayPage.this);
        calligrapher.setFont(ArticleDisplayPage.this, FixedVars.FONT_NAME, true);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layout_article_display_page);
        relativeLayout.requestFocus();
        articleTitle = (TextView) findViewById(R.id.text_article_title);
        authorName = (TextView) findViewById(R.id.text_author_name);
        articleDate = (TextView) findViewById(R.id.text_article_date);
        articleTag = (TextView) findViewById(R.id.text_article_tag);
        articleContent = (TextView) findViewById(R.id.text_article_content);
        likeButton = (ToggleButton) findViewById(R.id.btn_like);
        articleLikes = (TextView) findViewById(R.id.total_likes);
        commentText = (EditText) findViewById(R.id.edit_comment);
        commentButton = (ToggleButton) findViewById(R.id.btn_comment);
        commentDeleteButton = (Button) findViewById(R.id.btn_delete_comment);

        //Recycler View
        aprv = (RecyclerView) findViewById(R.id.rv_all_comments);
        layoutManager = new LinearLayoutManager(ArticleDisplayPage.this);
        aprv.setLayoutManager(layoutManager);

        sharedPref = getSharedPreferences(FixedVars.PREF_NAME, Context.MODE_PRIVATE);
        userName = sharedPref.getString(FixedVars.PREF_USER_NAME, "");
        token = sharedPref.getString(FixedVars.PREF_LOGIN_TOKEN, "");

        Bundle articleData = getIntent().getExtras();
        if (articleData == null) {
            return;
        }

        final String articleLink = articleData.getString("ArticleLink");

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
            contentString += aContentArray + "\n";

        //Check if the user has liked this article previously
        RequestTask<ShortUserDetail[]> getLikesRequest =
                new RequestTask<>(ShortUserDetail[].class, CONTENT_TYPE_JSON);
        getLikesRequest.execute(FixedVars.BASE_URL + "/article/" + article.getArticleId() + "/likes");
        totalLikesObj = getLikesRequest.getObj();

        //Check if the user has commented on this article previously
        RequestTask<CommentDetail[]> getCommentsRequest =
                new RequestTask<>(CommentDetail[].class, CONTENT_TYPE_JSON);
        getCommentsRequest.execute(FixedVars.BASE_URL + "/article/" + article.getArticleId() + "/comments");
        articleCommentsObj = getCommentsRequest.getObj();

        for (int i = 0; i < articleCommentsObj.length; i++) {
            if (articleCommentsObj[i].getUserName().equals(userName)) {
                usersComment = articleCommentsObj[i].getContent();
                commentText.setText(usersComment);
                commentText.setEnabled(false);
                commentButton.setChecked(true);
            }
        }

        finalAuthorName = "Written by " + article.getAuthor();
        tagString = tagString.substring(0, tagString.length() - 2);
        finalTags = "Tags : " + tagString;
        finalDate = "Last modified on " + article.getDate();
        articleTitle.setText(article.getTitle());
        authorName.setText(finalAuthorName);
        articleTag.setText(finalTags);
        articleDate.setText(finalDate);
        articleContent.setText(contentString);
        articleLikes.setText("Total likes: " + totalLikesObj.length);

        //for(String s : article.getContent())
        //    Log.i("doshi",s);

        for (int i = 0; i < totalLikesObj.length; i++) {
            singleUser = totalLikesObj[i].getUserName();
            if (singleUser.equals(userName))
                likeButton.setChecked(true);
        }

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (likeButton.isChecked()) {
                    RequestTask<String> likeRequest = new RequestTask<String>(String.class, HttpMethod.POST,
                            HeaderTools.CONTENT_TYPE_JSON,
                            HeaderTools.makeAuth(token));
                    likeRequest.execute(FixedVars.BASE_URL + "/user/" + userName + "/like/" + article.getArticleId());
                    Toast.makeText(ArticleDisplayPage.this, "Liked", Toast.LENGTH_SHORT).show();

                    RequestTask<ShortUserDetail[]> getLikesRequest =
                            new RequestTask<>(ShortUserDetail[].class, CONTENT_TYPE_JSON);
                    getLikesRequest.execute(FixedVars.BASE_URL + "/article/" + article.getArticleId() + "/likes");
                    totalLikesObj = getLikesRequest.getObj();
                    articleLikes.setText("Total likes: " + totalLikesObj.length);

                } else {
                    RequestTask<String> unlikeRequest = new RequestTask<String>(String.class, HttpMethod.DELETE,
                            HeaderTools.CONTENT_TYPE_JSON,
                            HeaderTools.makeAuth(token));
                    unlikeRequest.execute(FixedVars.BASE_URL + "/user/" + userName + "/like/" + article.getArticleId());
                    Toast.makeText(ArticleDisplayPage.this, "Like removed", Toast.LENGTH_SHORT).show();

                    RequestTask<ShortUserDetail[]> getLikesRequest =
                            new RequestTask<>(ShortUserDetail[].class, CONTENT_TYPE_JSON);
                    getLikesRequest.execute(FixedVars.BASE_URL + "/article/" + article.getArticleId() + "/likes");
                    totalLikesObj = getLikesRequest.getObj();
                    articleLikes.setText("Total likes: " + totalLikesObj.length);

                }
            }
        });

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commentButton.isChecked()) {
                    if (commentText.getText().toString().isEmpty()) {
                        Toast.makeText(ArticleDisplayPage.this, "Please enter something!!", Toast.LENGTH_SHORT).show();
                        commentButton.setChecked(false);
                    } else {
                        CommentDetail comment = new CommentDetail();
                        comment.setArticleId(article.getArticleId());
                        comment.setUserName(userName);
                        comment.setContent(String.valueOf(commentText.getText()));
                        AddRequestTask<String, CommentDetail> commentRequest = new AddRequestTask<String, CommentDetail>(String.class,
                                comment, HttpMethod.POST, HeaderTools.CONTENT_TYPE_JSON,
                                HeaderTools.makeAuth(token));
                        commentRequest.execute(FixedVars.BASE_URL + "/comment");
                        Toast.makeText(ArticleDisplayPage.this, "Commented successfully", Toast.LENGTH_SHORT).show();
                        commentDeleteButton.setVisibility(View.VISIBLE);
                        commentText.setEnabled(false);
                    }
                } else {
                    commentText.setEnabled(true);
                    commentDeleteButton.setVisibility(View.VISIBLE);
                }
            }
        });
        commentDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentDetail comment = new CommentDetail();
                RequestTask<String> deleteCommentRequest = new RequestTask<String>(String.class, HttpMethod.DELETE,
                        HeaderTools.CONTENT_TYPE_JSON,
                        HeaderTools.makeAuth(token));
                deleteCommentRequest.execute(FixedVars.BASE_URL + "/comment/" + comment.getCommentId());
                Toast.makeText(ArticleDisplayPage.this, "Comment deleted", Toast.LENGTH_SHORT).show();
                commentText.setText("");
                commentText.setEnabled(true);
                commentButton.setChecked(true);
                commentDeleteButton.setVisibility(View.GONE);
            }
        });

        if (!(articleCommentsObj == null)) {
            commentList = new CommentListModel[articleCommentsObj.length];
            for (int i = 0; i < articleCommentsObj.length; i++) {
                commentList[i] = new CommentListModel(articleCommentsObj[i]);
            }
            aprv = (RecyclerView) findViewById(R.id.rv_all_comments);
            adapter = new CommentListCustomAdapter(ArticleDisplayPage.this, commentList);
            aprv.setAdapter(adapter);

            Log.i("commentList", aprv.toString());
        }
    }
}
