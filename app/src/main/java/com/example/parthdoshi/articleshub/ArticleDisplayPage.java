package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.content.Intent;
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
    TextView articleTag;
    ToggleButton likeButton;
    EditText commentText;
    Button commentButton;

    //For Recycler View
    RecyclerView aprv;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    ShortUserDetail[] totalLikesObj;
    CommentDetail[] articleCommentsObj;
    CommentDetail[] articleCommentsObj2;
    CommentListModel[] commentList;

    String singleUser;

    String token = null, userName = null;
    SharedPreferences sharedPref;

    //Final String variables to show data in a better way
    String finalAuthorName = "";
    String finalTags = "";
    String finalDate = "";

    int totalLikes = 0;

    String articleLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking for internet connection
        if (NetworkStatus.getInstance(this).isOnline()) {
            setContentView(R.layout.activity_article_display_page);

            Calligrapher calligrapher = new Calligrapher(ArticleDisplayPage.this);
            calligrapher.setFont(ArticleDisplayPage.this, FixedVars.FONT_NAME, true);

            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.layout_article_display_page);
            relativeLayout.requestFocus();
            articleTitle = (TextView) findViewById(R.id.text_article_title);
            authorName = (TextView) findViewById(R.id.text_author_name);
            articleDate = (TextView) findViewById(R.id.text_article_date);
            articleTag = (TextView) findViewById(R.id.text_article_tag);
            articleContent = (TextView) findViewById(R.id.text_article_content);
            articleLikes = (TextView) findViewById(R.id.total_likes);
            likeButton = (ToggleButton) findViewById(R.id.btn_like);
            commentText = (EditText) findViewById(R.id.write_comment);
            commentButton = (Button) findViewById(R.id.btn_comment);

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

            articleLink = articleData.getString("ArticleLink");

            //To get the article's details from the server using articleLink
            RequestTask<ArticleDetail> getArticleRequest = new RequestTask<>(ArticleDetail.class, CONTENT_TYPE_JSON);
            getArticleRequest.execute(articleLink);
            final ArticleDetail article = getArticleRequest.getObj();


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

            Log.i("article links----------", article.getLink("likes").toString());

            //Check if the user has liked this article previously
            RequestTask<ShortUserDetail[]> getLikesRequest =
                    new RequestTask<>(ShortUserDetail[].class, CONTENT_TYPE_JSON);
            getLikesRequest.execute(FixedVars.BASE_URL + "/article/" + article.getArticleId() + "/likes");
            totalLikesObj = getLikesRequest.getObj();

            //Get all comments previously done on this article to load them in the list below
            RequestTask<CommentDetail[]> getArticlesCommentsRequest =
                    new RequestTask<>(CommentDetail[].class, CONTENT_TYPE_JSON);
            getArticlesCommentsRequest.execute(FixedVars.BASE_URL + "/article/" + article.getArticleId() + "/comments");
            articleCommentsObj = getArticlesCommentsRequest.getObj();

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
            totalLikes = totalLikesObj.length;

            //for(String s : article.getContent())
            //    Log.i("doshi",s);

            for (ShortUserDetail aTotalLikesObj : totalLikesObj) {
                singleUser = aTotalLikesObj.getUserName();
                if (singleUser.equals(userName))
                    likeButton.setChecked(true);
            }

            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (token.isEmpty()) {{
                        Toast.makeText(ArticleDisplayPage.this, "You need to be logged in to like this article", Toast.LENGTH_LONG).show();
                        likeButton.setChecked(false);
                    }
                    } else {
                        if (likeButton.isChecked()) {
                            //Like the article
                            RequestTask<String> likeRequest = new RequestTask<String>(String.class, HttpMethod.POST,
                                    HeaderTools.CONTENT_TYPE_JSON,
                                    HeaderTools.makeAuth(token));
                            likeRequest.execute(FixedVars.BASE_URL + "/user/" + userName + "/like/" + article.getArticleId());
                            Toast.makeText(ArticleDisplayPage.this, "Liked", Toast.LENGTH_SHORT).show();

                            /*
                            //Update the text next to like button to show perfect number of likes
                            RequestTask<ShortUserDetail[]> getLikesRequest =
                                    new RequestTask<>(ShortUserDetail[].class, CONTENT_TYPE_JSON);
                            getLikesRequest.execute(FixedVars.BASE_URL + "/article/" + article.getArticleId() + "/likes");
                            totalLikesObj = getLikesRequest.getObj();
                            */
                            totalLikes += 1;

                            articleLikes.setText("Total likes: " + totalLikes);

                        } else {
                            //Remove like from the article if previously liked
                            RequestTask<String> unlikeRequest = new RequestTask<String>(String.class, HttpMethod.DELETE,
                                    HeaderTools.CONTENT_TYPE_JSON,
                                    HeaderTools.makeAuth(token));
                            unlikeRequest.execute(FixedVars.BASE_URL + "/user/" + userName + "/like/" + article.getArticleId());
                            Toast.makeText(ArticleDisplayPage.this, "Like removed", Toast.LENGTH_SHORT).show();

                            /*
                            //Update the text next to like button to show perfect number of likes
                            RequestTask<ShortUserDetail[]> getLikesRequest =
                                    new RequestTask<>(ShortUserDetail[].class, CONTENT_TYPE_JSON);
                            getLikesRequest.execute(FixedVars.BASE_URL + "/article/" + article.getArticleId() + "/likes");
                            totalLikesObj = getLikesRequest.getObj();
                            */
                            totalLikes -= 1;
                            articleLikes.setText("Total likes: " + totalLikes);

                        }
                    }
                }
            });

            Log.i("AID----------", Long.toString(article.getArticleId()));

            articleLikes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (totalLikesObj.length > 0) {
                        Long aid = article.getArticleId();
                        Intent myIntent = new Intent(ArticleDisplayPage.this, ArticleLikesPage.class);
                        myIntent.putExtra("aid", aid);
                        startActivity(myIntent);
                    }
                }
            });


            if (!(articleCommentsObj == null)) {
                commentList = new CommentListModel[articleCommentsObj.length];
                for (int i = 0; i < articleCommentsObj.length; i++) {
                    commentList[i] = new CommentListModel(articleCommentsObj[i]);
                }
                aprv = (RecyclerView) findViewById(R.id.rv_all_comments);
                adapter = new CommentListCustomAdapter(ArticleDisplayPage.this, commentList, userName, articleLink);
                aprv.setAdapter(adapter);
                Log.i("commentList------------", aprv.toString());
                Log.i("Comment List's Length--", "" + commentList.length);
            } else {
                commentList = null;
            }

            commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (token.isEmpty()) {
                        Toast.makeText(ArticleDisplayPage.this, "You need to be logged in to comment on this article", Toast.LENGTH_SHORT).show();
                    } else if (commentText.getText().toString().isEmpty())
                        Toast.makeText(ArticleDisplayPage.this, "Please enter something!!", Toast.LENGTH_LONG).show();
                    else {
                        CommentDetail comment = new CommentDetail();
                        comment.setArticleId(article.getArticleId());
                        comment.setUserName(userName);
                        comment.setContent(String.valueOf(commentText.getText()));

                        //Post a comment on that particular article
                        AddRequestTask<String, CommentDetail> commentRequest = new AddRequestTask<String, CommentDetail>(String.class,
                                comment, HttpMethod.POST, HeaderTools.CONTENT_TYPE_JSON,
                                HeaderTools.makeAuth(token));
                        commentRequest.execute(FixedVars.BASE_URL + "/comment");
                        Toast.makeText(ArticleDisplayPage.this, "Commented successfully", Toast.LENGTH_SHORT).show();
                        commentText.setText("");

                        //Get all comments previously done on this article
                        RequestTask<CommentDetail[]> getArticlesCommentsRequest =
                                new RequestTask<>(CommentDetail[].class, CONTENT_TYPE_JSON);
                        getArticlesCommentsRequest.execute(FixedVars.BASE_URL + "/article/" + article.getArticleId() + "/comments");
                        articleCommentsObj2 = getArticlesCommentsRequest.getObj();

                        if (!(articleCommentsObj2 == null)) {
                            commentList = new CommentListModel[articleCommentsObj2.length];
                            for (int i = 0; i < articleCommentsObj2.length; i++) {
                                commentList[i] = new CommentListModel(articleCommentsObj2[i]);
                            }
                            aprv = (RecyclerView) findViewById(R.id.rv_all_comments);
                            adapter = new CommentListCustomAdapter(ArticleDisplayPage.this, commentList, userName, articleLink);
                            aprv.setAdapter(adapter);

                            Log.i("commentList", aprv.toString());
                        } else {
                            commentList = null;
                        }
                    }
                }
            });
        } else
            NetworkStatus.getInstance(this).buildDialog(this).show();
    }
}
