package com.example.parthdoshi.articleshub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.anwarshahriar.calligrapher.Calligrapher;

public class HelpPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_page);

        Calligrapher calligrapher = new Calligrapher(HelpPage.this);
        calligrapher.setFont(HelpPage.this, FixedVars.FONT_NAME, true);
    }
}
