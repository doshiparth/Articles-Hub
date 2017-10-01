package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import me.anwarshahriar.calligrapher.Calligrapher;

public class StartPage extends AppCompatActivity {

    SharedPreferences sharedPref;
    String userName = null, token = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking for internet connection
        if (NetworkStatus.getInstance(this).isOnline())
            setContentView(R.layout.activity_start_page);
        else
            NetworkStatus.getInstance(this).buildDialog(this).show();

        Calligrapher calligrapher = new Calligrapher(StartPage.this);
        calligrapher.setFont(StartPage.this, FixedVars.FONT_NAME, true);

        sharedPref = getSharedPreferences(FixedVars.PREF_NAME, Context.MODE_PRIVATE);
        userName = sharedPref.getString(FixedVars.PREF_USER_NAME, "");
        token = sharedPref.getString(FixedVars.PREF_LOGIN_TOKEN, "");
    }

    //Applying actions on all three buttons
    public void signinOnClick(View v1) {
        Intent myIntent = new Intent(StartPage.this, SignupPage.class);
        //myIntent.putExtra("key", value); //Optional parameters
        StartPage.this.startActivity(myIntent);
    }

    public void loginOnClick(View v2) {
        Intent myIntent = new Intent(StartPage.this, LoginPage.class);
        //myIntent.putExtra("key", value); //Optional parameters
        StartPage.this.startActivity(myIntent);
    }

    public void skipOnClick(View v3) {
        Intent myIntent = new Intent(StartPage.this, HomePage.class);
        FixedVars.SENTFROMSTARTPAGE = true;
        StartPage.this.startActivity(myIntent);
    }
}
