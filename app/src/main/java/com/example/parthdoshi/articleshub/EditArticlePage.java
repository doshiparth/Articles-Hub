package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import me.anwarshahriar.calligrapher.Calligrapher;

import static com.neel.articleshubapi.restapi.request.HeaderTools.CONTENT_TYPE_JSON;

public class EditArticlePage extends AppCompatActivity {

    //UI references
    EditText newArticleTitleText;
    EditText newArticleContentText;
    TextView newArticleTagsText;
    Button saveChangesButton;
    Button deleteArticleButton;

    //For Search tag purposes
    EditText userSearchText;
    Button userSearchButton;

    List<String> listAdded = new ArrayList<>();

    ArticleDetail articleDetail;

    Boolean NO_SELECTION_FLAG = true;
    Boolean TAG_ALREADY_PRESENT = false;

    //Local variables to send through the REST Api
    String title = "";
    String singleTag = "";
    String tagArray[];
    String contentArray[];
    Set<String> newTags = new HashSet<>();

    String[] content = null;

    String contentString = "";

    SharedPreferences sharedPref;
    String token = null;
    String userName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calligrapher calligrapher = new Calligrapher(EditArticlePage.this);
        calligrapher.setFont(EditArticlePage.this, FixedVars.FONT_NAME, true);

        //Checking for internet connection
        if (NetworkStatus.getInstance(this).isOnline()) {
            setContentView(R.layout.activity_edit_article_page);


            //Getting data from SharedPreferences
            sharedPref = getSharedPreferences(FixedVars.PREF_NAME, Context.MODE_PRIVATE);
            userName = sharedPref.getString(FixedVars.PREF_USER_NAME, "");
            token = sharedPref.getString(FixedVars.PREF_LOGIN_TOKEN, "");

            Bundle articleData = getIntent().getExtras();
            if (articleData == null) {
                return;
            }

            final String articleLink = articleData.getString("ArticleLink");

            RequestTask<ArticleDetail> getArticleData = new RequestTask<>(ArticleDetail.class, CONTENT_TYPE_JSON);
            getArticleData.execute(articleLink);
            articleDetail = getArticleData.getObj();

            Log.i("----------ArticleDetail", articleDetail.toString());

            newArticleTitleText = (EditText) findViewById(R.id.txt_edit_article_title);
            userSearchText = (EditText) findViewById(R.id.edit_txt_user_search);
            userSearchButton = (Button) findViewById(R.id.edit_btn_user_search);
            newArticleTagsText = (TextView) findViewById(R.id.txt_edit_article_selected_tags);
            newArticleContentText = (EditText) findViewById(R.id.txt_edit_article_content);
            saveChangesButton = (Button) findViewById(R.id.save_edited_changes_button);
            deleteArticleButton = (Button) findViewById(R.id.delete_article_button);

            //Setting the old data into the empty fields
            newArticleTitleText.setText(articleDetail.getTitle());
            Log.i("----------Title", articleDetail.getTitle());

            tagArray = new String[articleDetail.getTag().size()];
            Iterator<String> itr1 = articleDetail.getTag().iterator();
            for (int i = 0; i < tagArray.length; i++)
                tagArray[i] = itr1.next();
            for (String aTagArray : tagArray) {
                listAdded.add(aTagArray);
                newTags.add(aTagArray);
                singleTag += aTagArray + ", ";
            }

            newArticleTagsText.setText(singleTag);
            Log.i("----------Tag", singleTag);


            contentArray = new String[articleDetail.getContent().size()];
            Iterator<String> itr2 = articleDetail.getContent().iterator();
            for (int i = 0; i < contentArray.length; i++)
                contentArray[i] = itr2.next();
            for (String aContentArray : contentArray)
                contentString += aContentArray + "\n";

            newArticleContentText.setText(contentString);
            Log.i("----------Content", contentString);

            userSearchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String usersTag = userSearchText.getText().toString().trim().toLowerCase();
                    RequestTask<TagDetail> tagRead = new RequestTask<>(TagDetail.class, CONTENT_TYPE_JSON);
                    tagRead.execute(FixedVars.BASE_URL + "/tag/" + usersTag);
                    TagDetail tag = tagRead.getObj();
                    HttpStatus status = tagRead.getHttpStatus();

                    if (status == HttpStatus.OK && tag != null) {
                        //To check if the tag user has selected is already present in his selection list
                        for (String addedTag : listAdded) {
                            if ((usersTag.equals(addedTag))) {
                                Toast.makeText(EditArticlePage.this, "You already selected this tag!!", Toast.LENGTH_LONG).show();
                                TAG_ALREADY_PRESENT = true;
                                userSearchText.setText("");
                            }
                        }
                        //If the tag is not already present and is available in the Database, ENTER it into the list
                        if (!TAG_ALREADY_PRESENT) {
                            userSearchText.setText("");
                            newTags.add(usersTag);
                            singleTag = singleTag + usersTag + ", ";
                            newArticleTagsText.setText(singleTag);
                            listAdded.add(usersTag);
                            NO_SELECTION_FLAG = false;
                        }
                    } else if (usersTag.equals(""))
                        Toast.makeText(EditArticlePage.this, "Please Enter something!!!", Toast.LENGTH_LONG).show();
                    else{
                        Toast.makeText(EditArticlePage.this, "The entered tag does not exist in the database.... Please try another tag", Toast.LENGTH_LONG).show();
                        userSearchText.setText("");
                    }
                }
            });

            saveChangesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Storing data into local variables
                    title = newArticleTitleText.getText().toString();
                    //splits the string based on string
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
                    else if (newTags.isEmpty())
                        newArticleTagsText.setError(getString(R.string.error_field_required));
                    else if (contentList.isEmpty())
                        newArticleContentText.setError(getString(R.string.error_field_required));
                    else {
                        articleDetail.setAuthor(articleDetail.getAuthor());
                        articleDetail.setTitle(title);
                        articleDetail.setArticleId(articleDetail.getArticleId());
                        articleDetail.setDate(articleDetail.getDate());
                        articleDetail.setTag(newTags);
                        articleDetail.setContent(contentList);

                        Log.i("new article", "new " + articleDetail.getAuthor());

                        AddRequestTask<String, ArticleDetail> editArticleRequest = new AddRequestTask<String, ArticleDetail>(String.class,
                                articleDetail, HttpMethod.PUT, HeaderTools.CONTENT_TYPE_JSON,
                                HeaderTools.makeAuth(token));
                        editArticleRequest.execute(FixedVars.BASE_URL + "/article/" + articleDetail.getArticleId());
                        editArticleRequest.getObj();

                        Intent myIntent = new Intent(EditArticlePage.this, HomeArticlesPage.class);
                        startActivity(myIntent);
                        finish();
                    }
                }
            });

            deleteArticleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RequestTask<String> deleteArticleRequest = new RequestTask<String>(String.class, HttpMethod.DELETE,
                            HeaderTools.ACCEPT_JSON,
                            HeaderTools.makeAuth(token));
                    deleteArticleRequest.execute(FixedVars.BASE_URL + "/article/" + articleDetail.getArticleId());
                    deleteArticleRequest.getObj();

                    Toast.makeText(EditArticlePage.this, "Article deleted", Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(EditArticlePage.this, HomeArticlesPage.class);
                    startActivity(myIntent);
                    finish();
                }
            });
        } else
            NetworkStatus.getInstance(this).buildDialog(this).show();
    }
}
