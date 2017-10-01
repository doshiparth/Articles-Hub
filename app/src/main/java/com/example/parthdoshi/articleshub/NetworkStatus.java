package com.example.parthdoshi.articleshub;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatus {
    static Context context;
    private static NetworkStatus instance = new NetworkStatus();
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

    public AlertDialog.Builder buildDialog(final Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have a working internet connection to access this application.\n\n" +
                "Click on RETRY after connecting to the internet");

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent myIntent = new Intent(c, HomePage.class);
                c.startActivity(myIntent);
            }
        });
        return builder;
    }
}
