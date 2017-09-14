package com.example.parthdoshi.articleshub;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.neel.articleshubapi.restapi.beans.UserDetail;
import com.neel.articleshubapi.restapi.request.AddRequestTask;
import com.neel.articleshubapi.restapi.request.HeaderTools;

import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via username and password.
 */
public class LoginPage extends AppCompatActivity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "doshiparth007@gmail.com:123456", "neel.patel.2012.np@gmail.com:123456", "foo@example.com:hello", "bar@example.com:world"
    };
     */
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private EditText userNameText;
    private EditText passwordText;
    Button loginPageButton;
    private TextView noTokenErrorText;
    public String token;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking for internet connectivity
        if(NetworkStatus.getInstance(this).isOnline())
            setContentView(R.layout.activity_login_page);
        else
            NetworkStatus.getInstance(this).buildDialog(this).show();


        //setupActionBar();

        //Creating a SharedPreferences file
        sharedPref = getSharedPreferences(FixedVars.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.apply();

        // Set up the login form.
        userNameText = (EditText) findViewById(R.id.login_page_uname);
        passwordText = (EditText) findViewById(R.id.login_page_password);

        //If user clicks send/next button on keyboard, login would still be initialized directly
        passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        loginPageButton = (Button) findViewById(R.id.btn_log_in_page);
        loginPageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void doLogin(UserDetail login){
        AddRequestTask<String,UserDetail> loginRequest = new AddRequestTask<String, UserDetail>(String.class,
                login, HttpMethod.POST, HeaderTools.CONTENT_TYPE_JSON, HeaderTools.ACCEPT_TEXT);
        loginRequest.execute(FixedVars.BASE_URL+"/authentication/"+userNameText.toString());
        token = loginRequest.getObj();
        //return token;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        userNameText.setError(null);
        passwordText.setError(null);

        // Store values at the time of the login attempt.
        String uname = userNameText.getText().toString();
        String password = passwordText.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check if the username field is empty.
        //if (TextUtils.isEmpty(uname)) {
        if (uname.matches("")) {
            userNameText.setError(getString(R.string.error_field_required));
            focusView = userNameText;
            cancel = true;
        }
        // Check if the password field is empty.
        //if (!TextUtils.isEmpty(password)) {
        if (password.matches("")) {
            passwordText.setError(getString(R.string.error_field_required));
            focusView = passwordText;
            cancel = true;
        }

        if (cancel) {
            // There was an error;
            // So don't attempt login and focus the first form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            mAuthTask = new UserLoginTask(uname, password);
            mAuthTask.execute((Void) null);
        }

    }



    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    /*
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    */


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String aUname;
        private final String aPassword;

        UserLoginTask(String uname, String password) {
            aUname = uname;
            aPassword = password;
        }

        //This method runs on the background thread
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            UserDetail loginObj =new UserDetail();
            loginObj.setUserName(aUname);
            loginObj.setPass(aPassword);
            doLogin(loginObj);
            if(token!=null && !token.equalsIgnoreCase("")){
                // login successful
                Log.i("doshi login", token);
            }

            /*
            else if(token == null){

            }
            else{
                // login fail
                Log.i("doshi login", "login fail");
            }
            */
            /*
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }*/

            /*
            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }
            */

            // TODO: register the new account here.
            return true;     //changed to false in video
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            //showProgress(false);

            if (success) {
                finish();

                //Saving details for using in other activities using SharedPreferences.
                editor.putString(FixedVars.PREF_USER_NAME, userNameText.getText().toString());
                editor.putString(FixedVars.PREF_LOGIN_TOKEN, token);
                editor.apply();

                Toast.makeText(LoginPage.this, "Login Successful", Toast.LENGTH_LONG).show();

                Intent myIntent = new Intent(LoginPage.this, HomePage.class);
                startActivity(myIntent);
            } else {
                noTokenErrorText.setEnabled(true);
                noTokenErrorText.setText("Invalid Login Credentials");
                userNameText.requestFocus();
                passwordText.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }
    }
}

