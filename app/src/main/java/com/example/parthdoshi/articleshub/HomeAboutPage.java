package com.example.parthdoshi.articleshub;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.neel.articleshubapi.restapi.beans.TagDetail;
import com.neel.articleshubapi.restapi.request.RequestTask;

import org.springframework.http.HttpStatus;

import me.anwarshahriar.calligrapher.Calligrapher;

import static com.neel.articleshubapi.restapi.request.HeaderTools.CONTENT_TYPE_JSON;

public class HomeAboutPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;
    EditText newTagSearchBox;
    Button newTagReqBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking for internet connection
        if (NetworkStatus.getInstance(this).isOnline())
            setContentView(R.layout.activity_home_about_page);
        else
            NetworkStatus.getInstance(this).buildDialog(this).show();

        Calligrapher calligrapher = new Calligrapher(HomeAboutPage.this);
        calligrapher.setFont(HomeAboutPage.this, FixedVars.FONT_NAME, true);

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

        newTagSearchBox = (EditText) findViewById(R.id.txt_user_search_add_tag);
        newTagReqBtn = (Button) findViewById(R.id.btn_user_search_add_tag);
        newTagReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usersTag = newTagSearchBox.getText().toString().trim().toLowerCase();
                RequestTask<TagDetail> tagRead = new RequestTask<>(TagDetail.class, CONTENT_TYPE_JSON);
                tagRead.execute(FixedVars.BASE_URL + "/tag/" + usersTag);
                TagDetail tag = tagRead.getObj();
                HttpStatus status = tagRead.getHttpStatus();

                if (status == HttpStatus.OK && tag != null) {
                    //To check if the tag user has selected is already present in his selection list
                    Toast.makeText(HomeAboutPage.this, "This tag is already present in the database.\n" +
                            "Use it directly or request for some other tag that is not currently available",
                            Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(HomeAboutPage.this, "The request for adding "+usersTag+" into our database has been " +
                            "successfully registered.\nWe'll verify it and add it as soon as possible",
                            Toast.LENGTH_LONG).show();
                newTagSearchBox.setText("");
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
        if (id == R.id.action_help) {
            Intent myIntent = new Intent(HomeAboutPage.this, HelpPage.class);
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
                Intent home = new Intent(HomeAboutPage.this, HomePage.class);
                startActivity(home);
                finish();
                break;
            case R.id.nav_home_profile_page:
                Intent profile = new Intent(HomeAboutPage.this, HomeProfilePage.class);
                startActivity(profile);
                finish();
                break;
            case R.id.nav_home_articles_page:
                Intent articles = new Intent(HomeAboutPage.this, HomeArticlesPage.class);
                startActivity(articles);
                finish();
                break;
            case R.id.nav_home_tags_page:
                Intent tags = new Intent(HomeAboutPage.this, HomeTagsPage.class);
                startActivity(tags);
                finish();
                break;
            case R.id.nav_home_comments_page:
                Intent comments = new Intent(HomeAboutPage.this, HomeCommentsPage.class);
                startActivity(comments);
                finish();
                break;
            case R.id.nav_home_like_history_page:
                Intent likes = new Intent(HomeAboutPage.this, HomeLikeHistoryPage.class);
                startActivity(likes);
                finish();
                break;
            case R.id.nav_home_about_page:
                Intent about = new Intent(HomeAboutPage.this, HomeAboutPage.class);
                startActivity(about);
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
