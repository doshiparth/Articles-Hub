package com.example.parthdoshi.articleshub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking for internet connection
        if(NetworkStatus.getInstance(this).isOnline())
            setContentView(R.layout.activity_start_page);
        else
            NetworkStatus.getInstance(this).buildDialog(this).show();
    }

    //Applying actions on all three buttons
    public void signinOnClick(View v1){
        Intent myIntent = new Intent(StartPage.this, SignupPage.class);
        //myIntent.putExtra("key", value); //Optional parameters
        StartPage.this.startActivity(myIntent);
    }
    public void loginOnClick(View v2){
        Intent myIntent = new Intent(StartPage.this, LoginPage.class);
        //myIntent.putExtra("key", value); //Optional parameters
        StartPage.this.startActivity(myIntent);
    }
    public void skipOnClick(View v3){
        Intent myIntent = new Intent(StartPage.this, HomePage.class);
        //myIntent.putExtra("key", value); //Optional parameters
        StartPage.this.startActivity(myIntent);
    }
}
