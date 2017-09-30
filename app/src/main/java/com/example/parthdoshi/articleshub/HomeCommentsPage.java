package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.neel.articleshubapi.restapi.beans.CommentDetail;
import com.neel.articleshubapi.restapi.request.HeaderTools;
import com.neel.articleshubapi.restapi.request.RequestTask;

import org.springframework.http.HttpMethod;

import me.anwarshahriar.calligrapher.Calligrapher;

public class HomeCommentsPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;

    String token = null, userName = null;
    SharedPreferences sharedPref;

    //For Recycler View
    RecyclerView cprv;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    CommentsPageListModel[] commentList;

    //API Objects
    CommentDetail[] allComments;

    //UI References
    TextView heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calligrapher calligrapher = new Calligrapher(HomeCommentsPage.this);
        calligrapher.setFont(HomeCommentsPage.this, FixedVars.FONT_NAME, true);

        //Getting data from SharedPreferences
        sharedPref = getSharedPreferences(FixedVars.PREF_NAME, Context.MODE_PRIVATE);
        userName = sharedPref.getString(FixedVars.PREF_USER_NAME, "");
        token = sharedPref.getString(FixedVars.PREF_LOGIN_TOKEN, "");


        //Checking if the user has a profile or not
        if (token.isEmpty()) {
            Toast.makeText(HomeCommentsPage.this, "You need to be a logged in user to see all your comments", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(HomeCommentsPage.this, StartPage.class);
            startActivity(myIntent);
        } else {
            //Checking for internet connection
            if (NetworkStatus.getInstance(this).isOnline())
                setContentView(R.layout.activity_home_comments_page);
            else
                NetworkStatus.getInstance(this).buildDialog(this).show();

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);


            //Main Code
            heading = (TextView) findViewById(R.id.txt_no_comments_yet);
            cprv = (RecyclerView) findViewById(R.id.rv_users_comments);
            layoutManager = new LinearLayoutManager(HomeCommentsPage.this);
            cprv.setLayoutManager(layoutManager);

            //API Request to get all comments for the current user
            RequestTask<CommentDetail[]> getCommentsRequest = new RequestTask<CommentDetail[]>(CommentDetail[].class, HttpMethod.GET,
                    HeaderTools.CONTENT_TYPE_JSON,
                    HeaderTools.makeAuth(token));
            getCommentsRequest.execute(FixedVars.BASE_URL + "/user/" + userName + "/comments");
            // initiate waiting logic
            allComments = getCommentsRequest.getObj();
            // terminate waiting logic

            //Printing all the comments of that user
            if (allComments.length==0) {
                heading.setVisibility(View.VISIBLE);
                cprv.setVisibility(View.GONE);
                //Toast.makeText(HomeCommentsPage.this, "Sorry!! No comments to display", Toast.LENGTH_LONG).show();
                Log.i("Check received comments", "No Comments by this user");
            } else {
                heading.setVisibility(View.GONE);
                cprv.setVisibility(View.VISIBLE);
                //Log.i("First comment", allComments[0].getContent());
                commentList = new CommentsPageListModel[allComments.length];
                for (int i = 0; i < allComments.length; i++) {
                    commentList[i] = new CommentsPageListModel(allComments[i]);
                }
                cprv = (RecyclerView) findViewById(R.id.rv_users_comments);
                adapter = new CommentsPageListCustomAdapter(HomeCommentsPage.this, commentList);
                cprv.setAdapter(adapter);

                Log.i("commentList", cprv.toString());
            }
            cprv.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());

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
                Intent home = new Intent(HomeCommentsPage.this, HomePage.class);
                startActivity(home);
                finish();
                break;
            case R.id.nav_home_profile_page:
                Intent profile = new Intent(HomeCommentsPage.this, HomeProfilePage.class);
                startActivity(profile);
                finish();
                break;
            case R.id.nav_home_articles_page:
                Intent articles = new Intent(HomeCommentsPage.this, HomeArticlesPage.class);
                startActivity(articles);
                finish();
                break;
            case R.id.nav_home_tags_page:
                Intent tags = new Intent(HomeCommentsPage.this, HomeTagsPage.class);
                startActivity(tags);
                finish();
                break;
            case R.id.nav_home_comments_page:
                Intent comments = new Intent(HomeCommentsPage.this, HomeCommentsPage.class);
                startActivity(comments);
                finish();
                break;
            case R.id.nav_home_like_history_page:
                Intent likes = new Intent(HomeCommentsPage.this, HomeLikeHistoryPage.class);
                startActivity(likes);
                finish();
                break;
            case R.id.nav_home_about_page:
                Intent about = new Intent(HomeCommentsPage.this, HomeAboutPage.class);
                startActivity(about);
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
