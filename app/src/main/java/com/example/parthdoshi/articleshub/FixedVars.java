package com.example.parthdoshi.articleshub;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class FixedVars {

    // User name (make variable public to access from outside)
    public static final String PREF_USER_NAME = "userName";

    // Token received from server (make variable public to access from outside)
    public static final String PREF_LOGIN_TOKEN = "loginToken";

    // Token received from server (make variable public to access from outside)
    public static final String PREF_NAME = "tokenInfo";


    //The static list of the tags selected by the user maintained for further editing
    //List<String> listSelected = new ArrayList<>();


    //The base URL for server connection
    public static final String BASE_URL = "https://articleshub.herokuapp.com/api";

    public static final String FONT_NAME = "ExoRegular.ttf";

}
