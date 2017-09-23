package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.ListView;
import android.widget.Toast;

import com.neel.articleshubapi.restapi.beans.ShortArticleDetail;
import com.neel.articleshubapi.restapi.request.HeaderTools;
import com.neel.articleshubapi.restapi.request.RequestTask;

import org.springframework.http.HttpMethod;

public class HomeArticlesPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;

    //UI References
    ListView aplv;

    ArticlesListModel[] articleList;

    String token = null, userName = null;
    ShortArticleDetail[] articleDetails;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Getting data from SharedPreferences
        sharedPref = getSharedPreferences(FixedVars.PREF_NAME, Context.MODE_PRIVATE);
        userName = sharedPref.getString(FixedVars.PREF_USER_NAME, "");
        token = sharedPref.getString(FixedVars.PREF_LOGIN_TOKEN, "");

        //Checking if the user has a profile or not
        if (token.isEmpty()) {
            Toast.makeText(HomeArticlesPage.this, "You need to be a logged in user to access the profile page", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(HomeArticlesPage.this, StartPage.class);
            startActivity(myIntent);
        } else {
            //Checking for internet connection
            if (NetworkStatus.getInstance(this).isOnline())
                setContentView(R.layout.activity_home_profile_page);
            else
                NetworkStatus.getInstance(this).buildDialog(this).show();

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
/*
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
*/
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            //Main code for displaying list of articles created by that user

            loadArticles();
        }
    }

    public void loadArticles() {

        //Below is the original code for displaying content on HomeArticlesPage
        RequestTask<ShortArticleDetail[]> articlesRequest =
                new RequestTask<>(ShortArticleDetail[].class, HttpMethod.GET,
                        HeaderTools.CONTENT_TYPE_JSON,
                        HeaderTools.makeAuth(token));
        articlesRequest.execute(FixedVars.BASE_URL + "/user/" + userName + "/articles");
        // initiate waiting logic
        articleDetails = articlesRequest.getObj();
        // terminate waiting logic

        if (articleDetails == null)
            Toast.makeText(HomeArticlesPage.this, "Error!!! No articles to display", Toast.LENGTH_LONG).show();
        else {
            //If execution is correct and the list of articles is received, then and only then the following will take place
            articleList = new ArticlesListModel[articleDetails.length];
            for (int i = 0; i < articleDetails.length; i++) {
                articleList[i] = new ArticlesListModel(articleDetails[i]);
            }
            aplv = (ListView) findViewById(R.id.articles_page_listview);
            Log.i("articleList", aplv.toString());
            ArticlesListCustomAdapter articlesListCustomAdapter = new ArticlesListCustomAdapter(HomeArticlesPage.this, articleList);
            aplv.setAdapter(articlesListCustomAdapter);


            aplv.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent myIntent = new Intent(getApplicationContext(), ArticleDisplayPage.class);
                            myIntent.putExtra("ArticleLink", articleList[i].getShortArticleDetail().getLink());
                            myIntent.putExtra("ArticleAuthor", true);
                            startActivity(myIntent);
                            //finish();
                        }
                    }
            );
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
        getMenuInflater().inflate(R.menu.edit_article, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_article) {
            Intent myIntent = new Intent(HomeArticlesPage.this, EditArticlePage.class);
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
                Intent home = new Intent(HomeArticlesPage.this, HomePage.class);
                startActivity(home);
                break;
            case R.id.nav_home_profile_page:
                Intent profile = new Intent(HomeArticlesPage.this, HomeProfilePage.class);
                startActivity(profile);
                break;
            case R.id.nav_home_articles_page:
                Intent articles = new Intent(HomeArticlesPage.this, HomeArticlesPage.class);
                startActivity(articles);
                break;
            case R.id.nav_home_tags_page:
                Intent tags = new Intent(HomeArticlesPage.this, HomeTagsPage.class);
                startActivity(tags);
                break;
            case R.id.nav_home_about_page:
                Intent about = new Intent(HomeArticlesPage.this, HomeAboutPage.class);
                startActivity(about);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
