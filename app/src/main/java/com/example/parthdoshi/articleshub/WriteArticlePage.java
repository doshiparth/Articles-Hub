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
import android.widget.TextView;
import android.widget.Toast;

import com.neel.articleshubapi.restapi.beans.ArticleDetail;
import com.neel.articleshubapi.restapi.beans.TagDetail;
import com.neel.articleshubapi.restapi.request.AddRequestTask;
import com.neel.articleshubapi.restapi.request.HeaderTools;
import com.neel.articleshubapi.restapi.request.RequestTask;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.anwarshahriar.calligrapher.Calligrapher;

import static com.neel.articleshubapi.restapi.request.HeaderTools.CONTENT_TYPE_JSON;

public class WriteArticlePage extends AppCompatActivity {

    //UI references
    EditText newArticleTitleText;
    EditText newArticleContentText;
    TextView newArticleTagsText;
    Button publishArticleButton;

    Boolean NO_SELECTION_FLAG = true;
    Boolean TAG_ALREADY_PRESENT = false;

    //For Search tag purposes
    EditText userSearchText;
    Button userSearchButton;
    List<String> listAdded = new ArrayList<>();

    //Local variables to send through the REST Api
    String title = "";
    String singleTag = "Tags: ";
    Set<String> tags = new HashSet<>();
    String[] content;
    SharedPreferences sharedPref;
    String token = null;
    String userName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calligrapher calligrapher = new Calligrapher(WriteArticlePage.this);
        calligrapher.setFont(WriteArticlePage.this, FixedVars.FONT_NAME, true);

        //Getting data from SharedPreferences
        sharedPref = getSharedPreferences(FixedVars.PREF_NAME, Context.MODE_PRIVATE);
        userName = sharedPref.getString(FixedVars.PREF_USER_NAME, "");
        token = sharedPref.getString(FixedVars.PREF_LOGIN_TOKEN, "");

        //Checking if the user has a profile or not
        if (token.isEmpty()) {
            Toast.makeText(WriteArticlePage.this, "You need to be a logged in user to write a new article", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(WriteArticlePage.this, StartPage.class);
            startActivity(myIntent);
        } else {

            //Checking for internet connection
            if (NetworkStatus.getInstance(this).isOnline())
                setContentView(R.layout.activity_write_article_page);
            else
                NetworkStatus.getInstance(this).buildDialog(this).show();


            newArticleTitleText = (EditText) findViewById(R.id.txt_write_article_title);
            newArticleContentText = (EditText) findViewById(R.id.txt_write_article_content);
            newArticleTagsText = (TextView) findViewById(R.id.txt_write_article_selected_tags);
            publishArticleButton = (Button) findViewById(R.id.publish_button);

            userSearchText = (EditText) findViewById(R.id.txt_user_search);
            userSearchButton = (Button) findViewById(R.id.btn_user_search);
            newArticleTagsText.setText(singleTag);

            userSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String usersTag = userSearchText.getText().toString().trim().toLowerCase();
                    RequestTask<TagDetail> tagRead = new RequestTask<>(TagDetail.class, CONTENT_TYPE_JSON);
                    tagRead.execute(FixedVars.BASE_URL + "/tag/" + usersTag);
                    // initiate waiting logic
                    TagDetail tag = tagRead.getObj();
                    // terminate waiting logic
                    HttpStatus status = tagRead.getHttpStatus();

                    if (status == HttpStatus.OK && tag != null) {
                        //To check if the tag user has selected is already present in his selection list
                        for (String addedTag : listAdded) {
                            if ((usersTag.equals(addedTag))) {
                                Toast.makeText(WriteArticlePage.this, "You already selected this tag!!", Toast.LENGTH_LONG).show();
                                TAG_ALREADY_PRESENT = true;
                            }
                        }
                        //If the tag is not already present and is available in the Database, ENTER it into the list
                        if (!TAG_ALREADY_PRESENT) {
                            userSearchText.setText("");
                            tags.add(usersTag);
                            singleTag = singleTag + usersTag + ", ";
                            newArticleTagsText.setText(singleTag);
                            listAdded.add(usersTag);
                            NO_SELECTION_FLAG = false;
                        }
                    } else if (usersTag.equals(""))
                        Toast.makeText(WriteArticlePage.this, "Enter something man!!!", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(WriteArticlePage.this, "The entered tag does not exist in the database.... Please try another tag", Toast.LENGTH_LONG).show();
                }
            });

            //publishArticleButton.setOnClickListener(new View.OnClickListener() {
            //    @Override
            //    public void onClick(View view) {
            //    }
            //});
        }
    }

    public void publishArticle(View v1) {

        ArticleDetail article = new ArticleDetail();

        //Storing data into local variables
        title = newArticleTitleText.getText().toString();
        Log.i("Title before publish", title);
        Log.i("Tags", tags.toString());
        //Log.i("Title", title);
        //splits the string after each new line
        content = newArticleContentText.getText().toString().split("\\r?\\n");
        ArrayList<String> contentList = new ArrayList<>();
        int i = 0;
        while (i < content.length) {
            contentList.add(content[i]);
            i++;
        }
        if (contentList.isEmpty()) {
            Log.i("ContentList", "content list is empty");
        }
        if (title.equals(""))
            newArticleTitleText.setError(getString(R.string.error_field_required));
        else if (tags.isEmpty())
            newArticleTagsText.setError(getString(R.string.error_field_required));
        else if (contentList.isEmpty())
            newArticleContentText.setError(getString(R.string.error_field_required));
        else {
            article.setAuthor(userName);
            article.setContent(contentList);
            article.setTitle(title);
            article.setTag(tags);
            AddRequestTask<String, ArticleDetail> createArticleRequest = new AddRequestTask<String, ArticleDetail>(String.class,
                    article, HttpMethod.POST, HeaderTools.CONTENT_TYPE_JSON,
                    HeaderTools.makeAuth(token));
            createArticleRequest.execute(FixedVars.BASE_URL + "/article");
            //HttpStatus status = createArticleRequest.getHttpStatus();

            //if (status == HttpStatus.CREATED) {
            Intent myIntent = new Intent(WriteArticlePage.this, HomePage.class);
            startActivity(myIntent);
            Toast.makeText(WriteArticlePage.this, "Article created... \nGo to \"Manage your articles\" to view your created article", Toast.LENGTH_LONG).show();
            finish();
            //} else
            //Toast.makeText(WriteArticlePage.this, "Error!! Article not created", Toast.LENGTH_LONG).show();
        }
    }
}