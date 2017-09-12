package com.example.parthdoshi.articleshub;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.neel.articleshubapi.restapi.beans.ArticleDetail;
import com.neel.articleshubapi.restapi.beans.ShortArticleDetail;
import com.neel.articleshubapi.restapi.request.RequestTask;

import static com.neel.articleshubapi.restapi.request.HeaderTools.CONTENT_TYPE_JSON;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;
    String BASE_URL;
    HomePageModel[] articleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
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

        //Below is the original code for displaying content on HomePage

        BASE_URL = getResources().getString(R.string.BASE_URL);
        ShortArticleDetail[] articleDetails = getArticles();
        articleList = new HomePageModel[articleDetails.length];
        for(int i=0; i < articleDetails.length; i++){
            articleList[i] = new HomePageModel(articleDetails[i]);
        }

        /*ArticleList[0] = new HomePageModel("Article 0", "This is the metadata description of Article 0");
        ArticleList[1] = new HomePageModel("Article 1", "This is the metadata description of Article 1");
        ArticleList[2] = new HomePageModel("Article 2", "This is the metadata description of Article 2");
        ArticleList[3] = new HomePageModel("Article 3", "This is the metadata description of Article 3");
        ArticleList[4] = new HomePageModel("Article 4", "This is the metadata description of Article 4");
        ArticleList[5] = new HomePageModel("Article 5", "This is the metadata description of Article 5");
        ArticleList[6] = new HomePageModel("Article 6", "This is the metadata description of Article 6");
        ArticleList[7] = new HomePageModel("Article 7", "This is the metadata description of Article 7");
        ArticleList[8] = new HomePageModel("Article 8", "This is the metadata description of Article 8");
        ArticleList[9] = new HomePageModel("Article 9", "This is the metadata description of Article 9");
        ArticleList[10] = new HomePageModel("Article 10", "This is the metadata description of Article 10");
        ArticleList[11] = new HomePageModel("Article 11", "This is the metadata description of Article 11");
        ArticleList[12] = new HomePageModel("Article 12", "This is the metadata description of Article 12");
        */

        ListView hplv = (ListView) findViewById(R.id.home_page_listview);
        HomePageCustomAdapter homePageCustomAdapter = new HomePageCustomAdapter(this, articleList);
        hplv.setAdapter(homePageCustomAdapter);

        hplv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String articleHeading = articleList[i].getArticleHeading();
                        Intent myIntent = new Intent(getApplicationContext(), ArticleDisplayPage.class);
                        myIntent.putExtra("ArticleLink", articleList[i].getShortArticleDetail().getLink());
                        startActivity(myIntent);
                    }
                }
        );
    }

    private ShortArticleDetail[] getArticles (){
        RequestTask<ShortArticleDetail[]> rt2=
                new RequestTask<>(ShortArticleDetail[].class,CONTENT_TYPE_JSON);
        rt2.execute(BASE_URL+"/tag/tag1/articles");
        ShortArticleDetail[] ud=rt2.getObj();
        return ud;
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
        switch (id){

            case R.id.nav_home_page:
                Intent home= new Intent(HomePage.this,HomePage.class);
                startActivity(home);
                break;
            case R.id.nav_home_profile_page:
                Intent profile= new Intent(HomePage.this,HomeProfilePage.class);
                startActivity(profile);
                break;
            case R.id.nav_home_tags_page:
                Intent tags= new Intent(HomePage.this,HomeTagsPage.class);
                startActivity(tags);
                break;
            case R.id.nav_home_about_page:
                Intent about= new Intent(HomePage.this,HomeAboutPage.class);
                startActivity(about);
                break;

        /*
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_home_page) {
            //fragmentManager.beginTransaction().replace(R.id.content_home_frame, new HomeArticlesPage()).commit();
            Intent myIntent = new Intent(HomePage.this, HomeArticlesPage.class);
            //myIntent.putExtra("key", value); //Optional parameters
            HomePage.this.startActivity(myIntent);

        } else if (id == R.id.nav_home_profile_page) {
            //fragmentManager.beginTransaction().replace(R.id.content_home_frame, new HomeProfilePage()).commit();
            Intent myIntent = new Intent(HomePage.this, HomeProfilePage.class);
            //myIntent.putExtra("key", value); //Optional parameters
            HomePage.this.startActivity(myIntent);

        } else if (id == R.id.nav_home_tags_page) {
            //fragmentManager.beginTransaction().replace(R.id.content_home_frame, new HomeTagsPage()).commit();
            Intent myIntent = new Intent(HomePage.this, HomeTagsPage.class);
            //myIntent.putExtra("key", value); //Optional parameters
            HomePage.this.startActivity(myIntent);

        } else if (id == R.id.nav_home_about_page) {
            //fragmentManager.beginTransaction().replace(R.id.content_home_frame, new HomeAboutPage()).commit();
            Intent myIntent = new Intent(HomePage.this, HomeAboutPage.class);
            //myIntent.putExtra("key", value); //Optional parameters
            HomePage.this.startActivity(myIntent);

//        } else if (id == R.id.nav_manage) {

//        } else if (id == R.id.nav_share) {

//        } else if (id == R.id.nav_send) {
*/
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
