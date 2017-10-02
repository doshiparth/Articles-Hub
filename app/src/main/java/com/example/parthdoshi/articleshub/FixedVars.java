package com.example.parthdoshi.articleshub;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class FixedVars {

    // File name for storing shared preferences
    public static final String PREF_NAME = "tokenInfo";

    // User name (make variable public to access from outside)
    public static final String PREF_USER_NAME = "userName";

    // Token received from server (make variable public to access from outside)
    public static final String PREF_LOGIN_TOKEN = "loginToken";

    //Password entered by the user
    public static final String PREF_USER_PASSWORD = "password";

    //The base URL for server connection
    public static final String BASE_URL = "https://articleshub.herokuapp.com/api";

    public static final String FONT_NAME = "ExoRegular.ttf";

    public static Boolean SENTFROMSTARTPAGE = false ;

    public static Boolean signedUp  = false;

    public static Boolean loggedIn = false;
}
