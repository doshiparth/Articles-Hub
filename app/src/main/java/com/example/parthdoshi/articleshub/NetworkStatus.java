package com.example.parthdoshi.articleshub;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Parth on 12-09-2017.
 */

public class NetworkStatus {
    private static NetworkStatus instance = new NetworkStatus();
    static Context context;
    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    boolean connected = false;

    public static NetworkStatus getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    public boolean isOnline() {
        connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        connected = (networkInfo != null && networkInfo.isConnectedOrConnecting());
        return connected;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have a working internet connection to access this application");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
                //finish();
            }
        });

        return builder;
    }
}
