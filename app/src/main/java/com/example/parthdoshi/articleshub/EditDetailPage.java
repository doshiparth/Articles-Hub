package com.example.parthdoshi.articleshub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.neel.articleshubapi.restapi.beans.ArticleDetail;
import com.neel.articleshubapi.restapi.beans.UserDetail;
import com.neel.articleshubapi.restapi.request.AddRequestTask;
import com.neel.articleshubapi.restapi.request.HeaderTools;

import org.springframework.http.HttpMethod;

public class EditDetailPage extends AppCompatActivity {
    private String BASE_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detail_page);
        BASE_URL = getResources().getString(R.string.BASE_URL);
        UserDetail user = new UserDetail();
        user.setEmailId("dos@hskdhd.skjsb");
        user.setInfo("android test doshi2");
        user.setPass("456");
        user.setUserName("doshi2");
        AddRequestTask<String,UserDetail> rt6=new AddRequestTask<String, UserDetail>(String.class,
                user, HttpMethod.PUT, HeaderTools.CONTENT_TYPE_JSON,
                HeaderTools.makeAuth("2c91a00e5e74e4b2015e758850c90003"));
        rt6.execute(BASE_URL+"/user/"+user.getUserName());
        rt6.getObj();
    }
}
