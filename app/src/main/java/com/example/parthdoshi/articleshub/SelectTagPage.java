package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.ArrayList;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.neel.articleshubapi.restapi.beans.TagDetail;
import com.neel.articleshubapi.restapi.request.AddRequestTask;
import com.neel.articleshubapi.restapi.request.HeaderTools;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;


public class SelectTagPage extends AppCompatActivity {
    ListView lv_select;
    ListView lv_selected;
    MaterialSearchView searchView;
    SharedPreferences sharedPref;
    String token = null, userName = null;

    ArrayList<String> listSource = new ArrayList<>();
    ArrayList<String> listFound = new ArrayList<>();
    ArrayList<String> listSelected = new ArrayList<>();

    ArrayAdapter<String> sourceAdapter;
    ArrayAdapter<String> selectedAdapter;

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
        getSupportActionBar().setTitle("Tag Search Bar");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        sharedPref = getSharedPreferences(FixedVars.PREF_NAME, Context.MODE_PRIVATE);
        token = sharedPref.getString(FixedVars.PREF_LOGIN_TOKEN, "");
        userName = sharedPref.getString(FixedVars.PREF_USER_NAME, "");


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

        lv_select = (ListView)findViewById(R.id.select_page_display_listview);
        lv_selected = (ListView) findViewById(R.id.select_page_selected_listview);

        sourceAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, listSource);
        lv_select.setAdapter(sourceAdapter);

        selectedAdapter = new ArrayAdapter<>(SelectTagPage.this, android.R.layout.simple_list_item_1, listSelected);
        lv_selected.setAdapter(selectedAdapter);

        searchView = (MaterialSearchView)findViewById(R.id.select_tag_page_search_view);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
                //If Search View closed, list view will return default
                lv_select = (ListView)findViewById(R.id.select_tag_page_search_view);
                lv_selected = (ListView) findViewById(R.id.select_page_selected_listview);
                sourceAdapter = new ArrayAdapter<>(SelectTagPage.this,android.R.layout.simple_list_item_1,listSource);
                lv_select.setAdapter(sourceAdapter);
                selectedAdapter = new ArrayAdapter<>(SelectTagPage.this, android.R.layout.simple_list_item_1, listSelected);
                lv_selected.setAdapter(selectedAdapter);
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
                    lv_select.setAdapter(adapter);
                    ArrayAdapter<String> selectedAdapter = new ArrayAdapter<>(SelectTagPage.this, android.R.layout.simple_list_item_1, listSelected);
                    lv_selected.setAdapter(selectedAdapter);
                }
                else{
                    //if search text is null
                    //return default
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(SelectTagPage.this, android.R.layout.simple_list_item_1, listSource);
                    lv_select.setAdapter(adapter);
                    ArrayAdapter<String> selectedAdapter = new ArrayAdapter<>(SelectTagPage.this, android.R.layout.simple_list_item_1, listSelected);
                    lv_selected.setAdapter(selectedAdapter);
                }
                return true;
            }

        });
        lv_select.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for(int j=0 ; j<listSelected.size() ; j++){
                    if(!(listSource.get(i).equalsIgnoreCase(listSelected.get(j)))){
                        listSelected.add(listSource.get(i));
                        listSource.remove((listSource.get(i)));
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(SelectTagPage.this, android.R.layout.simple_list_item_1, listSource);
                        lv_select.setAdapter(adapter);
                        ArrayAdapter<String> selectedAdapter = new ArrayAdapter<>(SelectTagPage.this, android.R.layout.simple_list_item_1, listSelected);
                        lv_selected.setAdapter(selectedAdapter);
                        return;
                    }
                }
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
        //String selectedTags[] = new String[listSelected.size()];
        //for(int j =0;j<listSelected.size();j++){
        //    selectedTags[j] = listSelected.get(j);
        //}

        //Sending user's favorite tags to the server
        TagDetail[] tagDetails = new TagDetail[listSelected.size()];
        for(int i=0;i<listSelected.size();i++){
            TagDetail tagDetail = new TagDetail();
            tagDetail.setTagName(listSelected.get(i));
            tagDetails[i] = tagDetail;
        }
        AddRequestTask<String,TagDetail[]> rt=new AddRequestTask<String, TagDetail[]>(String.class,
                tagDetails, HttpMethod.PUT, HeaderTools.CONTENT_TYPE_JSON,
                HeaderTools.makeAuth(token));
        rt.execute(FixedVars.BASE_URL+"/user/"+userName+"/favorite-tags");
        // initiate waiting logic
        rt.getObj();
        // terminate waiting logic
        HttpStatus status = rt.getHttpStatus();

        FixedVars.TAG_SELECTED_FLAG = true;
        Intent myIntent = new Intent(SelectTagPage.this, HomePage.class);
        SelectTagPage.this.startActivity(myIntent);
        startActivity(myIntent);
        finish();
    }
}
