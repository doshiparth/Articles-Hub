package com.example.parthdoshi.articleshub;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Parth on 30-07-2017.
 */

public class HomePageRefreshService extends IntentService {

    public HomePageRefreshService() {
        super("HomePageRefreshService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        //Code to refresh after server connection

    }
}
