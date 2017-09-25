package com.example.parthdoshi.articleshub;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.neel.articleshubapi.restapi.beans.UserDetail;
import com.neel.articleshubapi.restapi.request.AddRequestTask;
import com.neel.articleshubapi.restapi.request.HeaderTools;

import org.springframework.http.HttpMethod;

/**
 * A login screen that offers login via username and password.
 */
public class LoginPage extends AppCompatActivity {

    /**
     * A dummy authentication store containing known user names and passwords.

     private static final String[] DUMMY_CREDENTIALS = new String[]{
     "doshiparth007@gmail.com:123456", "neel.patel.2012.np@gmail.com:123456", "foo@example.com:hello", "bar@example.com:world"
     };
     */

    // UI references.
    private EditText userNameText;
    private EditText passwordText;
    Button loginPageButton;
    private TextView noTokenErrorText;
    public String token;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking for internet connectivity
        if(NetworkStatus.getInstance(this).isOnline())
            setContentView(R.layout.activity_login_page);
        else
            NetworkStatus.getInstance(this).buildDialog(this).show();

        //Initializing ProgressDialog
        progressDialog = new ProgressDialog(LoginPage.this);


        //Creating a SharedPreferences file
        sharedPref = getSharedPreferences(FixedVars.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.apply();

        // Set up the login form.
        userNameText = (EditText) findViewById(R.id.login_page_uname);
        passwordText = (EditText) findViewById(R.id.login_page_password);
        noTokenErrorText = (TextView) findViewById(R.id.text_no_token_error_login);

        //If user clicks send/next button on keyboard, login would still be initialized directly
        passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_ACTION_SEND) {
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
                noTokenErrorText.setError(null);
                attemptLogin();
            }
        });
    }

    private void doLogin(UserDetail login){
        AddRequestTask<String,UserDetail> loginRequest = new AddRequestTask<String, UserDetail>(String.class,
                login, HttpMethod.POST, HeaderTools.CONTENT_TYPE_JSON, HeaderTools.ACCEPT_TEXT);
        loginRequest.execute(FixedVars.BASE_URL+"/authentication/"+login.getUserName());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        token = loginRequest.getObj();
        //return token;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        //Check if some other user is already registered
        if (token != null) {
            return;
        }

        // Reset errors.
        userNameText.setError(null);
        passwordText.setError(null);
        noTokenErrorText.setError(null);

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

        //Only makes a call to the REST Server API if none of the required fields are empty
        if (cancel) {
            // There was an error;
            // So don't attempt login and focus the first form field with an error.
            focusView.requestFocus();
        } else {
            //Attempts login by calling the server using REST Api call
            UserDetail loginObj = new UserDetail();
            loginObj.setUserName(uname);
            loginObj.setPass(password);
            doLogin(loginObj);

            //Checks whether login attempt was successful or not
            if(token!=null && !token.equalsIgnoreCase("")){

                // login successful
                Log.i("doshi login", token);

                //Saving details for using in other activities using SharedPreferences.
                editor.putString(FixedVars.PREF_USER_NAME, userNameText.getText().toString());
                editor.putString(FixedVars.PREF_LOGIN_TOKEN, token);
                editor.apply();

                progressDialog.cancel();

                Toast.makeText(LoginPage.this, "Login Successful", Toast.LENGTH_LONG).show();

                Intent myIntent = new Intent(LoginPage.this, HomePage.class);
                startActivity(myIntent);
                finish();
            }
            else if(token == null){
                // login fail
                Log.i("doshi login", "login fail");
                noTokenErrorText.setEnabled(true);
                noTokenErrorText.setText(getString(R.string.invalid_login_credentials));
                userNameText.requestFocus();
                passwordText.requestFocus();
                progressDialog.cancel();
            }
        }
    }
}