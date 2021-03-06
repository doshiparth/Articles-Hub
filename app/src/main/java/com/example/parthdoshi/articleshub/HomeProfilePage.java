package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.neel.articleshubapi.restapi.beans.UserDetail;
import com.neel.articleshubapi.restapi.request.HeaderTools;
import com.neel.articleshubapi.restapi.request.RequestTask;

import org.springframework.http.HttpMethod;

import me.anwarshahriar.calligrapher.Calligrapher;

import static com.neel.articleshubapi.restapi.request.HeaderTools.CONTENT_TYPE_JSON;

public class HomeProfilePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Declaring SharedPreferences to access data from other login activity
    public SharedPreferences sharedPref;
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;
    TextView profileHeading;
    TextView userEmailID;
    TextView userFname;
    TextView userLname;
    TextView userInfo;
    TextView uname;
    Button editDetailButton;
    Button logoutButton;
    String token = null, userName = null;
    String ufname = null, ulname = null, uinfo = null, uemailid = null;
    UserDetail ud;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calligrapher calligrapher = new Calligrapher(HomeProfilePage.this);
        calligrapher.setFont(HomeProfilePage.this, FixedVars.FONT_NAME, true);

        //Getting data from SharedPreferences
        sharedPref = getSharedPreferences(FixedVars.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        userName = sharedPref.getString(FixedVars.PREF_USER_NAME, "");
        token = sharedPref.getString(FixedVars.PREF_LOGIN_TOKEN, "");

        //Checking if the user has a profile or not
        if (token.isEmpty()) {
            Toast.makeText(HomeProfilePage.this, "You need to be a logged in user to access your profile page", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(HomeProfilePage.this, StartPage.class);
            startActivity(myIntent);
        } else {

            //Checking for internet connection
            if (NetworkStatus.getInstance(this).isOnline()) {
                setContentView(R.layout.activity_home_profile_page);

                toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.addDrawerListener(toggle);
                toggle.syncState();

                navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);

                //Below is the original code for displaying content on HomeProfilePage

                profileHeading = (TextView) findViewById(R.id.text_profile_page_heading);
                uname = (TextView) findViewById(R.id.text_profile_page_uname);
                userEmailID = (TextView) findViewById(R.id.text_profile_page_emailid);
                userFname = (TextView) findViewById(R.id.text_profile_page_userfname);
                userLname = (TextView) findViewById(R.id.text_profile_page_userlname);
                userInfo = (TextView) findViewById(R.id.text_profile_page_userinfo);
                editDetailButton = (Button) findViewById(R.id.btn_edit_detail);
                logoutButton = (Button) findViewById(R.id.btn_logout);

                RequestTask<UserDetail> rt =
                        new RequestTask<>(UserDetail.class, HttpMethod.GET, CONTENT_TYPE_JSON);
                rt.execute(FixedVars.BASE_URL + "/user/" + userName);
                // initiate waiting logic
                ud = rt.getObj();
                // terminate waiting logic

                uname.setText(userName);
                ufname = ud.getFirstName();
                ulname = ud.getLastName();
                uinfo = ud.getInfo();
                uemailid = ud.getEmailId();

                profileHeading.setText("" + ufname + "'s Profile");
                userEmailID.setText(uemailid);
                userFname.setText(ufname);
                userLname.setText(ulname);
                userInfo.setText(uinfo);

                editDetailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myIntent = new Intent(HomeProfilePage.this, EditDetailPage.class);
                        startActivity(myIntent);
                        finish();
                    }
                });
                logoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RequestTask<String> logoutRequest = new RequestTask<String>(String.class, HttpMethod.DELETE,
                                new HeaderTools.EntryImp("token", token));
                        logoutRequest.execute(FixedVars.BASE_URL + "/authentication/" + ud.getUserName());
                        editor.clear();
                        editor.apply();
                        //listSelected.clear();
                        Intent myIntent = new Intent(HomeProfilePage.this, StartPage.class);
                        startActivity(myIntent);
                        Snackbar.make(view, "You have successfully logged out!!!", Snackbar.LENGTH_LONG).show();
                        finish();
                    }
                });
            } else
                NetworkStatus.getInstance(this).buildDialog(this).show();
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
        if (id == R.id.action_help) {
            Intent myIntent = new Intent(HomeProfilePage.this, HelpPage.class);
            startActivity(myIntent);
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
                finish();
                break;
            case R.id.nav_home_profile_page:
                Intent profile = new Intent(HomeProfilePage.this, HomeProfilePage.class);
                startActivity(profile);
                finish();
                break;
            case R.id.nav_home_articles_page:
                Intent articles = new Intent(HomeProfilePage.this, HomeArticlesPage.class);
                startActivity(articles);
                finish();
                break;
            case R.id.nav_home_tags_page:
                Intent tags = new Intent(HomeProfilePage.this, HomeTagsPage.class);
                startActivity(tags);
                finish();
                break;
            case R.id.nav_home_comments_page:
                Intent comments = new Intent(HomeProfilePage.this, HomeCommentsPage.class);
                startActivity(comments);
                finish();
                break;
            case R.id.nav_home_like_history_page:
                Intent likes = new Intent(HomeProfilePage.this, HomeLikeHistoryPage.class);
                startActivity(likes);
                finish();
                break;
            case R.id.nav_home_about_page:
                Intent about = new Intent(HomeProfilePage.this, HomeAboutPage.class);
                startActivity(about);
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
