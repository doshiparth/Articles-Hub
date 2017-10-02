package com.example.parthdoshi.articleshub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.neel.articleshubapi.restapi.beans.ShortUserDetail;
import com.neel.articleshubapi.restapi.request.RequestTask;

import java.util.ArrayList;
import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;

import static com.neel.articleshubapi.restapi.request.HeaderTools.CONTENT_TYPE_JSON;

public class ArticleLikesPage extends AppCompatActivity {

    ShortUserDetail[] allUsers;
    List<String> listOfUsers = new ArrayList<>();
    ListView lv_allUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking for internet connection
        if (NetworkStatus.getInstance(this).isOnline()) {
            setContentView(R.layout.activity_article_likes_page);


            Calligrapher calligrapher = new Calligrapher(ArticleLikesPage.this);
            calligrapher.setFont(ArticleLikesPage.this, FixedVars.FONT_NAME, true);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Intent mIntent = getIntent();
            Long aid = mIntent.getLongExtra("aid", 0);

            Log.i("Like page AID----", "" + aid);

            lv_allUsers = (ListView) findViewById(R.id.list_view_likes_page);

            RequestTask<ShortUserDetail[]> getAllUsersRequest =
                    new RequestTask<>(ShortUserDetail[].class, CONTENT_TYPE_JSON);
            getAllUsersRequest.execute(FixedVars.BASE_URL + "/article/" + aid + "/likes");
            allUsers = getAllUsersRequest.getObj();

            for (ShortUserDetail singleUser : allUsers) {
                listOfUsers.add(singleUser.getUserName());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ArticleLikesPage.this, android.R.layout.simple_list_item_1, listOfUsers);
            lv_allUsers.setAdapter(adapter);
        } else
            NetworkStatus.getInstance(this).buildDialog(this).show();
    }
}
