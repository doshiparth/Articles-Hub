package com.example.parthdoshi.articleshub;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class SelectTagPage extends AppCompatActivity {
    ListView lv;
    //SelectTagPageModel[] TagList;
    MaterialSearchView searchView;

    String[] listSource = {
            "Science",
            "Maths",
            "Fashion",
            "Technology",
            "Gaming",
            "Tag1",
            "Tag2",
            "Three",
            "Four",
            "Five",
            "Six",
            "Seven",
            "Eight",
            "Nine",
            "Ten"
    };

    String[] listSelected = {
            "Tag1",
            "Tag2"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking for internet connection
        if(NetworkStatus.getInstance(this).isOnline())
            setContentView(R.layout.activity_select_tag_page);
        else
            NetworkStatus.getInstance(this).buildDialog(this).show();

        //Code to display and manage the search view in the toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tag Search");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        lv = (ListView)findViewById(R.id.select_page_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listSource);
        lv.setAdapter(adapter);


        searchView = (MaterialSearchView)findViewById(R.id.select_tag_page_search_view);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
                //If closed Search View , list view will return default
                lv = (ListView)findViewById(R.id.select_tag_page_search_view);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(SelectTagPage.this,android.R.layout.simple_list_item_1,listSource);
                lv.setAdapter(adapter);
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && !newText.isEmpty()){
                    List<String> listFound = new ArrayList<String>();
                    for(String item:listSource){
                        if(item.contains(newText))
                            listFound.add(item);
                    }

                    ArrayAdapter adapter = new ArrayAdapter(SelectTagPage.this, android.R.layout.simple_list_item_1, listFound);
                    lv.setAdapter(adapter);
                }
                else{
                    //if search text is null
                    //return default
                    ArrayAdapter adapter = new ArrayAdapter(SelectTagPage.this, android.R.layout.simple_list_item_1, listSource);
                    lv.setAdapter(adapter);
                }
                return true;
            }

        });


        /*
        TagList = new SelectTagPageModel[20];
        TagList[0] = new SelectTagPageModel("Science", 0);
        TagList[1] = new SelectTagPageModel("Education", 1);
        TagList[2] = new SelectTagPageModel("Technology", 1);
        TagList[3] = new SelectTagPageModel("Fashion", 0);
        TagList[4] = new SelectTagPageModel("Gaming", 1);
        TagList[5] = new SelectTagPageModel("Tag5", 1);
        TagList[6] = new SelectTagPageModel("Tag6", 0);
        TagList[7] = new SelectTagPageModel("Tag7", 1);
        TagList[8] = new SelectTagPageModel("Tag8", 1);
        TagList[9] = new SelectTagPageModel("Tag9", 1);
        TagList[10] = new SelectTagPageModel("Tag10", 1);
        TagList[11] = new SelectTagPageModel("Tag11", 0);
        TagList[12] = new SelectTagPageModel("Tag12", 1);
        TagList[13] = new SelectTagPageModel("Tag13", 1);
        TagList[14] = new SelectTagPageModel("Tag14", 1);
        TagList[15] = new SelectTagPageModel("Tag15", 0);
        TagList[16] = new SelectTagPageModel("Tag16", 1);
        TagList[17] = new SelectTagPageModel("Tag17", 0);
        TagList[18] = new SelectTagPageModel("Tag18", 1);
        TagList[19] = new SelectTagPageModel("Tag19", 0);


        SelectTagPageCustomAdapter adapter = new SelectTagPageCustomAdapter(this, TagList);
        lv.setAdapter(adapter);
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_tag_page_search_view,menu);
        MenuItem item = menu.findItem(R.id.select_tag_page_action_search);
        searchView.setMenuItem(item);
        return true;
    }


    public void goToHome(View v){
        Intent myIntent = new Intent(SelectTagPage.this, HomePage.class);
        SelectTagPage.this.startActivity(myIntent);
        myIntent.putExtra("selectedTags", listSelected);
        startActivity(myIntent);
    }
}
