package com.example.parthdoshi.articleshub;

import android.content.SharedPreferences;

import java.util.ArrayList;

public class FixedVars {

    // User name (make variable public to access from outside)
    public static final String PREF_USER_NAME = "userName";

    // Token received from server (make variable public to access from outside)
    public static final String PREF_LOGIN_TOKEN = "loginToken";

    // Token received from server (make variable public to access from outside)
    public static final String PREF_NAME = "tokenInfo";


    //The base URL for server connection
    public static final String BASE_URL = "https://articleshub.herokuapp.com";

    //The list of fixed tags
    public static ArrayList<String> TAG_LIST = new ArrayList<>();

    public static ArrayList<String> addTag(String tag){
        TAG_LIST.add(tag);
        return TAG_LIST;
    }

}
