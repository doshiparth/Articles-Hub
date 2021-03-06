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

import com.neel.articleshubapi.restapi.beans.ShortArticleDetail;
import com.neel.articleshubapi.restapi.request.HeaderTools;
import com.neel.articleshubapi.restapi.request.RequestTask;

import org.springframework.http.HttpMethod;

import me.anwarshahriar.calligrapher.Calligrapher;

public class HomeArticlesPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;

    //For Recycler view
    RecyclerView aprv;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    ArticlesEditListModel[] articleList;

    //UI References
    TextView heading;

    String token = null, userName = null;
    ShortArticleDetail[] articleDetails;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Calligrapher calligrapher = new Calligrapher(HomeArticlesPage.this);
        calligrapher.setFont(HomeArticlesPage.this, FixedVars.FONT_NAME, true);

        //Getting data from SharedPreferences
        sharedPref = getSharedPreferences(FixedVars.PREF_NAME, Context.MODE_PRIVATE);
        userName = sharedPref.getString(FixedVars.PREF_USER_NAME, "");
        token = sharedPref.getString(FixedVars.PREF_LOGIN_TOKEN, "");


        //Checking if the user has a profile or not
        if (token.isEmpty()) {
            Toast.makeText(HomeArticlesPage.this, "You need to be a logged in user to manage your articles", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(HomeArticlesPage.this, StartPage.class);
            startActivity(myIntent);
        } else {
            //Checking for internet connection
            if (NetworkStatus.getInstance(this).isOnline()) {
                setContentView(R.layout.activity_home_articles_page);

                toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.addDrawerListener(toggle);
                toggle.syncState();

                navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);

                //Main code for displaying list of articles created by that user

                heading = (TextView) findViewById(R.id.txt_no_articles_yet);
                aprv = (RecyclerView) findViewById(R.id.articles_page_recycler_view);
                layoutManager = new LinearLayoutManager(HomeArticlesPage.this);
                aprv.setLayoutManager(layoutManager);

                RequestTask<ShortArticleDetail[]> articlesRequest =
                        new RequestTask<>(ShortArticleDetail[].class, HttpMethod.GET,
                                HeaderTools.CONTENT_TYPE_JSON,
                                HeaderTools.makeAuth(token));
                articlesRequest.execute(FixedVars.BASE_URL + "/user/" + userName + "/articles");
                // initiate waiting logic
                articleDetails = articlesRequest.getObj();
                // terminate waiting logic

                if (articleDetails.length == 0) {
                    heading.setVisibility(View.VISIBLE);
                    aprv.setVisibility(View.GONE);
                    //Toast.makeText(HomeArticlesPage.this, "Error!!! No articles to display", Toast.LENGTH_LONG).show();
                } else {
                    //If execution is correct and the list of articles is received, then and only then the following will take place
                    Log.i("Articles List", articleDetails.toString());
                    heading.setVisibility(View.GONE);
                    aprv.setVisibility(View.VISIBLE);
                    articleList = new ArticlesEditListModel[articleDetails.length];
                    for (int i = 0; i < articleDetails.length; i++) {
                        articleList[i] = new ArticlesEditListModel(articleDetails[i]);
                    }
                    if (aprv == null)
                        Log.i("aprv", "aprv is null");
                    else
                        Log.i("articleList", aprv.toString());
                    adapter = new ArticlesEditListCustomAdapter(HomeArticlesPage.this, articleList);
                    if (adapter.getItemCount() == 0)
                        Log.i("CustomAdapter", "articlesListEditCustomAdapter is null");
                    else
                        Log.i("CustomAdapter", adapter.toString());

                    //try {

                    aprv.setAdapter(adapter);
                    //} catch (NullPointerException e) {
                    //    e.printStackTrace();
                    //}
                    //try {
/*                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Intent myIntent = new Intent(getApplicationContext(), ArticleDisplayPage.class);
                                    myIntent.putExtra("ArticleLink", articleList[i].getShortArticleDetail().getLink());
                                    startActivity(myIntent);
                                    //finish();
                                }
                            }
                    );
                //} catch (NullPointerException e) {
                //    e.printStackTrace();
*/                //}
                }

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
            Intent myIntent = new Intent(HomeArticlesPage.this, HelpPage.class);
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
                finish();
                break;
            case R.id.nav_home_profile_page:
                Intent profile = new Intent(HomeArticlesPage.this, HomeProfilePage.class);
                startActivity(profile);
                finish();
                break;
            case R.id.nav_home_articles_page:
                Intent articles = new Intent(HomeArticlesPage.this, HomeArticlesPage.class);
                startActivity(articles);
                finish();
                break;
            case R.id.nav_home_tags_page:
                Intent tags = new Intent(HomeArticlesPage.this, HomeTagsPage.class);
                startActivity(tags);
                finish();
                break;
            case R.id.nav_home_comments_page:
                Intent comments = new Intent(HomeArticlesPage.this, HomeCommentsPage.class);
                startActivity(comments);
                finish();
                break;
            case R.id.nav_home_like_history_page:
                Intent likes = new Intent(HomeArticlesPage.this, HomeLikeHistoryPage.class);
                startActivity(likes);
                finish();
                break;
            case R.id.nav_home_about_page:
                Intent about = new Intent(HomeArticlesPage.this, HomeAboutPage.class);
                startActivity(about);
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
