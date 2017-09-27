package com.example.parthdoshi.articleshub;

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

public class EditArticlePage extends AppCompatActivity {

    //UI references
    EditText newArticleTitleText;
    EditText newArticleContentText;
    TextView newArticleTagsText;
    Button saveChangesButton;

    //For Search tag purposes
    EditText userSearchText;
    Button userSearchButton;
    Button deleteArticleButton;
    List<String> listAdded = new ArrayList<>();

    ArticleDetail articleOld;
    ArticleDetail articleNew;

    Boolean NO_SELECTION_FLAG = true;
    Boolean TAG_ALREADY_PRESENT = false;

    //Local variables to send through the REST Api
    String title = "";
    String singleTag = "";
    Set<String> newTags = new HashSet<>();
    ArrayList<String> contentList = new ArrayList<>();

    String[] oldContent = null;
    String[] newContent = null;

    String txt = "";

    SharedPreferences sharedPref;
    String token = null;
    String userName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calligrapher calligrapher = new Calligrapher(EditArticlePage.this);
        calligrapher.setFont(EditArticlePage.this, FixedVars.FONT_NAME, true);

        //Checking for internet connection
        if (NetworkStatus.getInstance(this).isOnline())
            setContentView(R.layout.activity_write_article_page);
        else
            NetworkStatus.getInstance(this).buildDialog(this).show();

        Bundle articleData = getIntent().getExtras();
        if (articleData == null) {
            return;
        }

        final String articleLink = articleData.getString("ArticleLink");


        RequestTask<ArticleDetail> getArticleRequest = new RequestTask<>(ArticleDetail.class, CONTENT_TYPE_JSON);
        getArticleRequest.execute(articleLink);

        articleOld = getArticleRequest.getObj();

        articleNew = new ArticleDetail();

        newArticleTitleText = (EditText) findViewById(R.id.txt_edit_article_title);
        newArticleContentText = (EditText) findViewById(R.id.txt_edit_article_content);
        newArticleTagsText = (TextView) findViewById(R.id.txt_edit_article_selected_tags);
        saveChangesButton = (Button) findViewById(R.id.btn_edit_article);
        deleteArticleButton = (Button) findViewById(R.id.delete_article_button);

        userSearchText = (EditText) findViewById(R.id.edit_txt_user_search);
        userSearchButton = (Button) findViewById(R.id.edit_btn_user_search);

        //Setting the old data into the empty fields
        newArticleTitleText.setText(articleOld.getTitle());

        while (!(articleOld.getTag().iterator().next().isEmpty())) {
            newTags.add(articleOld.getTag().iterator().next());
            singleTag = singleTag + articleOld.getTag().iterator().next() + ",";
        }
        newArticleTagsText.setText(singleTag);

        oldContent = (String[]) articleOld.getContent().toArray();
        for (int i = 0; !(oldContent[i].equals("")); i++) {
            txt += oldContent[i] + "\n";
        }
        newArticleContentText.setText(txt);

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
                            Toast.makeText(EditArticlePage.this, "You already selected this tag!!", Toast.LENGTH_LONG).show();
                            TAG_ALREADY_PRESENT = true;
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
                    Toast.makeText(EditArticlePage.this, "Enter something man!!!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(EditArticlePage.this, "The entered tag does not exist in the database.... Please try another tag", Toast.LENGTH_LONG).show();
            }
        });

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Storing data into local variables
                title = newArticleTitleText.getText().toString();
                //splits the string based on string
                newContent = newArticleContentText.getText().toString().split("\n");

                int i = 0;
                while (!(newContent[i].isEmpty())) {
                    contentList.add(newContent[i]);
                    i++;
                }

                Log.i("author", "author old " + articleOld.getAuthor());
                Log.i("article id", "author ID old " + articleOld.getArticleId());

                if (title.equals(""))
                    newArticleTitleText.setError(getString(R.string.error_field_required));
                else if (newTags.isEmpty())
                    newArticleTagsText.setError(getString(R.string.error_field_required));
                else if (contentList.isEmpty())
                    newArticleContentText.setError(getString(R.string.error_field_required));
                else {
                    articleNew.setAuthor(articleOld.getAuthor());
                    articleNew.setTitle(title);
                    articleNew.setArticleId(articleOld.getArticleId());
                    articleNew.setDate(articleOld.getDate());
                    articleNew.setTag(newTags);
                    articleNew.setContent(contentList);

                    Log.i("new article", "new " + articleNew.getAuthor());

                    AddRequestTask<String, ArticleDetail> editArticleRequest = new AddRequestTask<String, ArticleDetail>(String.class,
                            articleNew, HttpMethod.PUT, HeaderTools.CONTENT_TYPE_JSON,
                            HeaderTools.makeAuth(token));
                    editArticleRequest.execute(FixedVars.BASE_URL + "/article/" + articleNew.getArticleId());
                    editArticleRequest.getObj();
                }
            }
        });

        deleteArticleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestTask<String> deleteArticleRequest = new RequestTask<String>(String.class, HttpMethod.DELETE,
                        HeaderTools.ACCEPT_JSON,
                        HeaderTools.makeAuth(token));
                deleteArticleRequest.execute(FixedVars.BASE_URL + "/article/" + articleOld.getArticleId());
                deleteArticleRequest.getObj();

                Toast.makeText(EditArticlePage.this, "Article deleted", Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(EditArticlePage.this, ArticleDisplayPage.class);
                startActivity(myIntent);
                finish();
            }
        });
    }
}
