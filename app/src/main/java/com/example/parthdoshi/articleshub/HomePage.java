package com.example.parthdoshi.articleshub;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.neel.articleshubapi.restapi.beans.ShortArticleDetail;
import com.neel.articleshubapi.restapi.request.HeaderTools;
import com.neel.articleshubapi.restapi.request.RequestTask;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import me.anwarshahriar.calligrapher.Calligrapher;

import static com.neel.articleshubapi.restapi.request.HeaderTools.CONTENT_TYPE_JSON;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;

    ArticlesListModel[] articleList;
    ListView hplv;
    //String[] selectedTags;
    String token = null, userName = null;
    ShortArticleDetail[] articleDetails;
    SharedPreferences sharedPref;
    SwipeRefreshLayout swippy;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking for internet connection
        if (NetworkStatus.getInstance(this).isOnline())
            setContentView(R.layout.activity_home_page);
        else
            NetworkStatus.getInstance(this).buildDialog(this).show();

        Calligrapher calligrapher = new Calligrapher(HomePage.this);
        calligrapher.setFont(HomePage.this, FixedVars.FONT_NAME, true);

        sharedPref = getSharedPreferences(FixedVars.PREF_NAME, Context.MODE_PRIVATE);
        userName = sharedPref.getString(FixedVars.PREF_USER_NAME, "");
        token = sharedPref.getString(FixedVars.PREF_LOGIN_TOKEN, "");

        Log.i("Home Page Token", token);

        if (userName.equals("")) {
            Intent myIntent = new Intent(HomePage.this, StartPage.class);
            startActivity(myIntent);
            //Toast.makeText(HomePage.this, "Welcome again "+userName, Toast.LENGTH_LONG).show();
            finish();
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing ProgressDialog
        progressDialog = new ProgressDialog(HomePage.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Registering you as our new user");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        //progressDialog.setProgress(0);

        //Floating Button for writing new Article.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent myIntent = new Intent(HomePage.this, WriteArticlePage.class);
                startActivity(myIntent);
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        swippy = (SwipeRefreshLayout) findViewById(R.id.home_page_swippy);

        loadHomePage();
        //progressDialog.dismiss();

        /*
        ArticleList[0] = new ArticlesListModel("Article 0", "This is the metadata description of Article 0");
        ArticleList[1] = new ArticlesListModel("Article 1", "This is the metadata description of Article 1");
        ArticleList[2] = new ArticlesListModel("Article 2", "This is the metadata description of Article 2");
        ArticleList[3] = new ArticlesListModel("Article 3", "This is the metadata description of Article 3");
        ArticleList[4] = new ArticlesListModel("Article 4", "This is the metadata description of Article 4");
        ArticleList[5] = new ArticlesListModel("Article 5", "This is the metadata description of Article 5");
        ArticleList[6] = new ArticlesListModel("Article 6", "This is the metadata description of Article 6");
        ArticleList[7] = new ArticlesListModel("Article 7", "This is the metadata description of Article 7");
        ArticleList[8] = new ArticlesListModel("Article 8", "This is the metadata description of Article 8");
        ArticleList[9] = new ArticlesListModel("Article 9", "This is the metadata description of Article 9");
        ArticleList[10] = new ArticlesListModel("Article 10", "This is the metadata description of Article 10");
        ArticleList[11] = new ArticlesListModel("Article 11", "This is the metadata description of Article 11");
        ArticleList[12] = new ArticlesListModel("Article 12", "This is the metadata description of Article 12");
        */
        swippy.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swippy.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
                loadHomePage();
            }
        });

    }

    public void loadHomePage() {
        //Below is the original code for displaying content on HomePage

        if (token != null && !token.equalsIgnoreCase("")) {

            RequestTask<ShortArticleDetail[]> regUserArticleRequest =
                    new RequestTask<>(ShortArticleDetail[].class, HttpMethod.GET, CONTENT_TYPE_JSON,
                            HeaderTools.makeAuth(token));
            regUserArticleRequest.execute(FixedVars.BASE_URL + "/home/" + userName);
            // initiate waiting logic
            progressDialog.show();
            articleDetails = regUserArticleRequest.getObj();
            HttpStatus status = regUserArticleRequest.getHttpStatus();
            // terminate waiting logic
            progressDialog.dismiss();

        }/*else if(token != null && !FixedVars.TAG_SELECTED_FLAG) {
            Toast.makeText(HomePage.this, "Logged in but tags not selected.... Select tags first", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(HomePage.this, SelectTagPage.class);
            startActivity(myIntent);
        }*/ else if (token == null || token.equalsIgnoreCase("")) {

            RequestTask<ShortArticleDetail[]> unregUserArticleRequest =
                    new RequestTask<>(ShortArticleDetail[].class, HttpMethod.GET, CONTENT_TYPE_JSON);
            unregUserArticleRequest.execute(FixedVars.BASE_URL + "/home");
            // initiate waiting logic
            articleDetails = unregUserArticleRequest.getObj();
            HttpStatus status = unregUserArticleRequest.getHttpStatus();
            if (status == HttpStatus.OK) {
                progressDialog.dismiss();
            }
            // terminate waiting logic
        }

        if (articleDetails == null)
            Toast.makeText(HomePage.this, "Error!!! No articles to display \n Please check your internet connection", Toast.LENGTH_LONG).show();
        else {
            //If execution is correct and the list of articles is received, then and only then the following will take place
            articleList = new ArticlesListModel[articleDetails.length];
            for (int i = 0; i < articleDetails.length; i++) {
                articleList[i] = new ArticlesListModel(articleDetails[i]);
            }
            hplv = (ListView) findViewById(R.id.home_page_listview);
            ArticlesListCustomAdapter articlesListCustomAdapter = new ArticlesListCustomAdapter(HomePage.this, articleList);
            hplv.setAdapter(articlesListCustomAdapter);

            Log.i("articleList", hplv.toString());

            hplv.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent myIntent = new Intent(getApplicationContext(), ArticleDisplayPage.class);
                            myIntent.putExtra("ArticleLink", articleList[i].getShortArticleDetail().getLink());
                            myIntent.putExtra("ArticleAuthor", false);
                            startActivity(myIntent);
                            //finish();
                        }
                    }
            );
        }
        swippy.setRefreshing(false);
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
            Intent myIntent = new Intent(HomePage.this, HelpPage.class);
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
                Intent home = new Intent(HomePage.this, HomePage.class);
                startActivity(home);
                break;
            case R.id.nav_home_profile_page:
                Intent profile = new Intent(HomePage.this, HomeProfilePage.class);
                startActivity(profile);
                break;
            case R.id.nav_home_articles_page:
                Intent articles = new Intent(HomePage.this, HomeArticlesPage.class);
                startActivity(articles);
                break;
            case R.id.nav_home_tags_page:
                Intent tags = new Intent(HomePage.this, HomeTagsPage.class);
                startActivity(tags);
                break;
            case R.id.nav_home_about_page:
                Intent about = new Intent(HomePage.this, HomeAboutPage.class);
                startActivity(about);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
