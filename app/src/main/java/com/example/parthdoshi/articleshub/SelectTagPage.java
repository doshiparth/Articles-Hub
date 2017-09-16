package com.example.parthdoshi.articleshub;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class SelectTagPage extends AppCompatActivity {
    ListView lv;
    MaterialSearchView searchView;

    ArrayList<String> listSource = new ArrayList<>();
    ArrayList<String> listSelected = new ArrayList<>();
    ArrayList<String> listFound = new ArrayList<>();

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

        //Initializing Tags
        listSource.add("tutorial");
        listSource.add("story");
        listSource.add("fashion");
        listSource.add("science");
        listSource.add("game");
        listSource.add("smartphone");
        listSource.add("philosophy");
        listSource.add("programming");
        listSource.add("study");
        listSource.add("news");
        listSource.add("movie");
        listSource.add("ai");
        listSource.add("future");
        listSource.add("book");

        lv = (ListView)findViewById(R.id.select_page_listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, listSource);
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

            //Check if the entered Text in the search box matches with our list of tags
            //If it does load those items in the new ArrayAdapter object
            //else display the adapter with the old list
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && !newText.isEmpty()){
                    for(String item:listSource){
                        if(item.contains(newText))
                            listFound.add(item);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(SelectTagPage.this, android.R.layout.simple_list_item_1, listFound);
                    lv.setAdapter(adapter);
                }
                else{
                    //if search text is null
                    //return default
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(SelectTagPage.this, android.R.layout.simple_list_item_1, listSource);
                    lv.setAdapter(adapter);
                }
                return true;
            }

        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listSelected.add(listSource.get(i));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_tag_page_search_view,menu);
        MenuItem item = menu.findItem(R.id.select_tag_page_action_search);
        searchView.setMenuItem(item);
        return true;
    }


    public void goToHome(View v){
        //Converting ArrayList to list of strings
        String selectedTags[] = new String[listSelected.size()];
        for(int j =0;j<listSelected.size();j++){
            selectedTags[j] = listSelected.get(j);
        }

        Intent myIntent = new Intent(SelectTagPage.this, HomePage.class);
        SelectTagPage.this.startActivity(myIntent);
        myIntent.putExtra("selectedTags", selectedTags);
        startActivity(myIntent);
    }
}
