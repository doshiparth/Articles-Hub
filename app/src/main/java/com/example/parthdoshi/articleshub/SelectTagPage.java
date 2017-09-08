package com.example.parthdoshi.articleshub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class SelectTagPage extends AppCompatActivity {
    ListView lv;
    SelectTagPageModel[] TagList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag_page);
        lv = (ListView) findViewById(R.id.select_page_listview);
        TagList = new SelectTagPageModel[20];
        TagList[0] = new SelectTagPageModel("Science", 0);
        TagList[1] = new SelectTagPageModel("Education", 1);
        TagList[2] = new SelectTagPageModel("Technology", 1);
        TagList[3] = new SelectTagPageModel("Fashion", 0);
        TagList[4] = new SelectTagPageModel("Gaming", 1);
        TagList[5] = new SelectTagPageModel("Tag5", 1);
        TagList[6] = new SelectTagPageModel("Tag6", 0);
        TagList[7] = new SelectTagPageModel("Tag7", 1);
        TagList[8] = new SelectTagPageModel("Tag8", 1);
        TagList[9] = new SelectTagPageModel("Tag9", 1);
        TagList[10] = new SelectTagPageModel("Tag10", 1);
        TagList[11] = new SelectTagPageModel("Tag11", 0);
        TagList[12] = new SelectTagPageModel("Tag12", 1);
        TagList[13] = new SelectTagPageModel("Tag13", 1);
        TagList[14] = new SelectTagPageModel("Tag14", 1);
        TagList[15] = new SelectTagPageModel("Tag15", 0);
        TagList[16] = new SelectTagPageModel("Tag16", 1);
        TagList[17] = new SelectTagPageModel("Tag17", 0);
        TagList[18] = new SelectTagPageModel("Tag18", 1);
        TagList[19] = new SelectTagPageModel("Tag19", 0);

        SelectTagPageCustomAdapter adapter = new SelectTagPageCustomAdapter(this, TagList);
        lv.setAdapter(adapter);
    }

    public void goToHome(View v){
        Intent myIntent = new Intent(SelectTagPage.this, HomePage.class);
        SelectTagPage.this.startActivity(myIntent);
    }
}
