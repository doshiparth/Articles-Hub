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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.neel.articleshubapi.restapi.beans.ShortArticleDetail;
import com.neel.articleshubapi.restapi.request.HeaderTools;
import com.neel.articleshubapi.restapi.request.RequestTask;

import org.springframework.http.HttpMethod;

import me.anwarshahriar.calligrapher.Calligrapher;

public class HomeLikeHistoryPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;

    String token = null, userName = null;
    SharedPreferences sharedPref;

    //For Recycler Ciew
    RecyclerView lprv;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    ArticlesListModel[] articleList;

    //UI References
    TextView heading;

    //API Objects
    ShortArticleDetail[] articleDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calligrapher calligrapher = new Calligrapher(HomeLikeHistoryPage.this);
        calligrapher.setFont(HomeLikeHistoryPage.this, FixedVars.FONT_NAME, true);

        //Getting data from SharedPreferences
        sharedPref = getSharedPreferences(FixedVars.PREF_NAME, Context.MODE_PRIVATE);
        userName = sharedPref.getString(FixedVars.PREF_USER_NAME, "");
        token = sharedPref.getString(FixedVars.PREF_LOGIN_TOKEN, "");


        //Checking if the user has a profile or not
        if (token.isEmpty()) {
            Toast.makeText(HomeLikeHistoryPage.this, "You need to be a logged in user to see all your history", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(HomeLikeHistoryPage.this, StartPage.class);
            startActivity(myIntent);
        } else {
            //Checking for internet connection
            if (NetworkStatus.getInstance(this).isOnline())
                setContentView(R.layout.activity_home_like_history_page);
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

            //Main code
            heading = (TextView) findViewById(R.id.txt_no_likes_yet);
            lprv = (RecyclerView) findViewById(R.id.like_page_recycler_view);
            layoutManager = new LinearLayoutManager(HomeLikeHistoryPage.this);
            lprv.setLayoutManager(layoutManager);


            //Getting users liked articles from server
            RequestTask<ShortArticleDetail[]> getUsersLikes = new RequestTask<ShortArticleDetail[]>(ShortArticleDetail[].class,
                    HttpMethod.GET,
                    HeaderTools.CONTENT_TYPE_JSON,
                    HeaderTools.makeAuth(token));
            getUsersLikes.execute(FixedVars.BASE_URL + "/user/" + userName + "/likes");
            articleDetails = getUsersLikes.getObj();

            if (articleDetails.length==0) {
                heading.setVisibility(View.VISIBLE);
                lprv.setVisibility(View.GONE);
                //Toast.makeText(HomeLikeHistoryPage.this, "Nothing liked yet", Toast.LENGTH_LONG).show();
            } else {
                heading.setVisibility(View.GONE);
                lprv.setVisibility(View.VISIBLE);
                articleList = new ArticlesListModel[articleDetails.length];
                for (int i = 0; i < articleDetails.length; i++) {
                    articleList[i] = new ArticlesListModel(articleDetails[i]);
                }
                adapter = new ArticlesListCustomAdapter(HomeLikeHistoryPage.this, articleList);
                lprv.setAdapter(adapter);
            }
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
                Intent home = new Intent(HomeLikeHistoryPage.this, HomePage.class);
                startActivity(home);
                finish();
                break;
            case R.id.nav_home_profile_page:
                Intent profile = new Intent(HomeLikeHistoryPage.this, HomeProfilePage.class);
                startActivity(profile);
                finish();
                break;
            case R.id.nav_home_articles_page:
                Intent articles = new Intent(HomeLikeHistoryPage.this, HomeArticlesPage.class);
                startActivity(articles);
                finish();
                break;
            case R.id.nav_home_tags_page:
                Intent tags = new Intent(HomeLikeHistoryPage.this, HomeTagsPage.class);
                startActivity(tags);
                finish();
                break;
            case R.id.nav_home_comments_page:
                Intent comments = new Intent(HomeLikeHistoryPage.this, HomeCommentsPage.class);
                startActivity(comments);
                finish();
                break;
            case R.id.nav_home_like_history_page:
                Intent likes = new Intent(HomeLikeHistoryPage.this, HomeLikeHistoryPage.class);
                startActivity(likes);
                finish();
                break;
            case R.id.nav_home_about_page:
                Intent about = new Intent(HomeLikeHistoryPage.this, HomeAboutPage.class);
                startActivity(about);
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
