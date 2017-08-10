package com.example.parthdoshi.articleshub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class SelectTagPage extends AppCompatActivity {
    ListView lv;
    SelectPageModel[] TagList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tag_page);
        lv = (ListView) findViewById(R.id.select_page_listview);
        TagList = new SelectPageModel[15];
        TagList[0] = new SelectPageModel("Science", 0);
        TagList[1] = new SelectPageModel("Education", 1);
        TagList[2] = new SelectPageModel("Technology", 1);
        TagList[3] = new SelectPageModel("Fashion", 0);
        TagList[4] = new SelectPageModel("Gaming", 1);
        TagList[5] = new SelectPageModel("Tag5", 1);
        TagList[6] = new SelectPageModel("Tag6", 1);
        TagList[7] = new SelectPageModel("Tag7", 1);
        TagList[8] = new SelectPageModel("Tag8", 1);
        TagList[9] = new SelectPageModel("Tag9", 1);
        TagList[10] = new SelectPageModel("Tag10", 1);
        TagList[11] = new SelectPageModel("Tag11", 1);
        TagList[12] = new SelectPageModel("Tag12", 1);
        TagList[13] = new SelectPageModel("Tag13", 1);
        TagList[14] = new SelectPageModel("Tag14", 1);

        SelectPageCustomAdapter adapter = new SelectPageCustomAdapter(this, TagList);
        lv.setAdapter(adapter);
    }

    public void goToHome(View v){
        Intent myIntent = new Intent(SelectTagPage.this, HomePage.class);
        SelectTagPage.this.startActivity(myIntent);
    }
}
