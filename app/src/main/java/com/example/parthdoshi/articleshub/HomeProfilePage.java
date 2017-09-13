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
import android.widget.Button;
import android.widget.TextView;

import com.neel.articleshubapi.restapi.request.HeaderTools;
import com.neel.articleshubapi.restapi.request.RequestTask;

import org.springframework.http.HttpMethod;

public class HomeProfilePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;

    TextView userEmailID;
    TextView userName;
    TextView userInfo;
    Button editDetailButton;
    Button logoutButton;


    //Declaring SharedPreferences to access data from other login activity
    public SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking for internet connection
        if(NetworkStatus.getInstance(this).isOnline())
            setContentView(R.layout.activity_home_profile_page);
        else
            NetworkStatus.getInstance(this).buildDialog(this).show();

        //Getting data from SharedPreferences
        sharedPref = getSharedPreferences(FixedVars.PREF_USER_NAME, Context.MODE_PRIVATE);

        String uName = "";
        if (sharedPref.contains(FixedVars.PREF_USER_NAME))
        {
            uName = sharedPref.getString(FixedVars.PREF_USER_NAME, "");
        }
        //String[] tagList = uName.;


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

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Below is the original code for displaying content on HomeProfilePage

        userEmailID = (TextView)findViewById(R.id.text_profile_page_emailid);
        userName = (TextView)findViewById(R.id.text_profile_page_username);
        userInfo = (TextView)findViewById(R.id.text_profile_page_userinfo);
        editDetailButton = (Button) findViewById(R.id.btn_edit_detail);
        logoutButton = (Button) findViewById(R.id.btn_logout);
        String email;
        //userEmailID.setText();
        //userName.setText();
        //userInfo.setText();

        editDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomeProfilePage.this, EditDetailPage.class);
                startActivity(myIntent);
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestTask<String> rt5=new RequestTask<String>(String.class, HttpMethod.DELETE,
                        new HeaderTools.EntryImp("token","2c91a00e5e74e4b2015e758850c90003"));
                rt5.execute(FixedVars.BASE_URL+"/authentication/doshi2");
            }
        });
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
                Intent home = new Intent(HomeProfilePage.this, HomePage.class);
                startActivity(home);
                break;
            case R.id.nav_home_profile_page:
                Intent profile = new Intent(HomeProfilePage.this, HomeProfilePage.class);
                startActivity(profile);
                break;
            case R.id.nav_home_tags_page:
                Intent tags = new Intent(HomeProfilePage.this, HomeTagsPage.class);
                startActivity(tags);
                break;
            case R.id.nav_home_about_page:
                Intent about = new Intent(HomeProfilePage.this, HomeAboutPage.class);
                startActivity(about);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
