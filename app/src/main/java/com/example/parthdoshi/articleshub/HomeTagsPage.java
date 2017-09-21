package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.neel.articleshubapi.restapi.beans.TagDetail;
import com.neel.articleshubapi.restapi.request.AddRequestTask;
import com.neel.articleshubapi.restapi.request.HeaderTools;
import com.neel.articleshubapi.restapi.request.RequestTask;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

//import java.util.ArrayList;
//import java.util.List;

import static com.neel.articleshubapi.restapi.request.HeaderTools.CONTENT_TYPE_JSON;

public class HomeTagsPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;

    ListView lv_selected;
    //MaterialSearchView searchView;
    EditText userSearchText;
    Button userSearchButton;
    SharedPreferences sharedPref;
    String token = null;
    String userName = null;
    Boolean NO_SELECTION_FLAG = true;
    Boolean TAG_ALREADY_PRESENT = false;

    //List<String> listSelected = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Getting user details from the shared preferences file
        sharedPref = getSharedPreferences(FixedVars.PREF_NAME, Context.MODE_PRIVATE);
        token = sharedPref.getString(FixedVars.PREF_LOGIN_TOKEN, "");
        userName = sharedPref.getString(FixedVars.PREF_USER_NAME, "");

        //Checking if the user has a profile or not
        if (token.isEmpty()) {
            Toast.makeText(HomeTagsPage.this, "You need to be a logged in user to access the profile page", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(HomeTagsPage.this, StartPage.class);
            startActivity(myIntent);
        } else {

            //Checking for internet connection
            if (NetworkStatus.getInstance(this).isOnline())
                setContentView(R.layout.activity_home_tags_page);
            else
                NetworkStatus.getInstance(this).buildDialog(this).show();

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

            userSearchText = (EditText) findViewById(R.id.home_txt_user_search);
            userSearchButton = (Button) findViewById(R.id.home_btn_user_search);

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            lv_selected = (ListView) findViewById(R.id.home_select_page_listview);
            ArrayAdapter<String> selectedAdapter = new ArrayAdapter<>(HomeTagsPage.this, android.R.layout.simple_list_item_1, FixedVars.listSelected);
            lv_selected.setAdapter(selectedAdapter);

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
                        for (String addedTag : FixedVars.listSelected) {
                            if ((usersTag.equals(addedTag))) {
                                Toast.makeText(HomeTagsPage.this, "You already selected this tag!!", Toast.LENGTH_LONG).show();
                                TAG_ALREADY_PRESENT = true;
                            }
                        }
                        //If the tag is not already present and is available in the Database, ENTER it into the list
                        if (!TAG_ALREADY_PRESENT) {
                            FixedVars.listSelected.add(tag.getTagName());
                            userSearchText.setText("");
                            ArrayAdapter<String> selectedAdapter = new ArrayAdapter<>(HomeTagsPage.this, android.R.layout.simple_list_item_1, FixedVars.listSelected);
                            lv_selected.setAdapter(selectedAdapter);
                            NO_SELECTION_FLAG = false;
                        }
                    } else if (usersTag.equals(""))
                        Toast.makeText(HomeTagsPage.this, "Enter something man!!!", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(HomeTagsPage.this, "The entered tag does not exist in the database.... Please try another tag", Toast.LENGTH_LONG).show();
                }
            });
            lv_selected.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    //Remove the selected tag from the list
                    FixedVars.listSelected.remove(FixedVars.listSelected.get(i));

                    ArrayAdapter<String> selectedAdapter = new ArrayAdapter<>(HomeTagsPage.this, android.R.layout.simple_list_item_1, FixedVars.listSelected);
                    lv_selected.setAdapter(selectedAdapter);
                }
            });
        }
    }

    public void goToHome(View v) {
        //Converting ArrayList to list of strings
        //String selectedTags[] = new String[listSelected.size()];
        //for(int j =0;j<listSelected.size();j++){
        //    selectedTags[j] = listSelected.get(j);
        //}


        //Sending user's favorite tags to the server

        if (!FixedVars.listSelected.isEmpty()) {
            for (String str : FixedVars.listSelected) {
                System.out.println("Selected tags");
                System.out.println(str);
            }
            TagDetail[] tagDetails = new TagDetail[FixedVars.listSelected.size()];
            for (int i = 0; i < FixedVars.listSelected.size(); i++) {
                TagDetail tagDetail = new TagDetail();
                tagDetail.setTagName(FixedVars.listSelected.get(i));
                tagDetails[i] = tagDetail;
            }
            AddRequestTask<String, TagDetail[]> rt = new AddRequestTask<String, TagDetail[]>(String.class,
                    tagDetails, HttpMethod.POST, CONTENT_TYPE_JSON,
                    HeaderTools.makeAuth(token));
            rt.execute(FixedVars.BASE_URL + "/user/" + userName + "/favorite-tags");
            // initiate waiting logic
            rt.getObj();
            // terminate waiting logic
            HttpStatus status = rt.getHttpStatus();
            if (status == HttpStatus.OK)
                Toast.makeText(HomeTagsPage.this, "Tags saved", Toast.LENGTH_LONG).show();

            Intent myIntent = new Intent(HomeTagsPage.this, HomePage.class);
            HomeTagsPage.this.startActivity(myIntent);
            startActivity(myIntent);
            finish();
        } else {
            Toast.makeText(HomeTagsPage.this, "You need to select atleast one tag to go ahead", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {

            case R.id.nav_home_page:
                Intent home = new Intent(HomeTagsPage.this, HomePage.class);
                startActivity(home);
                break;
            case R.id.nav_home_profile_page:
                Intent profile = new Intent(HomeTagsPage.this, HomeProfilePage.class);
                startActivity(profile);
                break;
            case R.id.nav_home_tags_page:
                Intent tags = new Intent(HomeTagsPage.this, HomeTagsPage.class);
                startActivity(tags);
                break;
            case R.id.nav_home_about_page:
                Intent about = new Intent(HomeTagsPage.this, HomeAboutPage.class);
                startActivity(about);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
