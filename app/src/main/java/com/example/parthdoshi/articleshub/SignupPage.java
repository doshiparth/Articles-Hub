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
 * A signup screen that lets new user register using email address
 */
public class SignupPage extends AppCompatActivity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * <p>
     * private static final String[] DUMMY_CREDENTIALS = new String[]{
     * "doshiparth007@gmail.com:123456", "neel.patel.2012.np@gmail.com:123456", "foo@example.com:hello", "bar@example.com:world"
     * };
     */

    // UI references.
    private EditText emailText;
    private EditText userNameText;
    private EditText uinfoText;
    private EditText passwordText;
    private EditText fnameText;
    private EditText lnameText;
    Button signupButton;
    private TextView noTokenErrorText;
    public String token;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking for internet connectivity
        if (NetworkStatus.getInstance(this).isOnline())
            setContentView(R.layout.activity_signup_page);
        else
            NetworkStatus.getInstance(this).buildDialog(this).show();

        //Initializing ProgressDialog
        progressDialog = new ProgressDialog(SignupPage.this);


        //Creating a SharedPreferences file
        sharedPref = getSharedPreferences(FixedVars.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.apply();

        // Set up the login form.
        fnameText = (EditText) findViewById(R.id.signup_page_fname);
        lnameText = (EditText) findViewById(R.id.signup_page_lname);
        emailText = (EditText) findViewById(R.id.signup_page_email);
        userNameText = (EditText) findViewById(R.id.signup_page_username);
        uinfoText = (EditText) findViewById(R.id.signup_page_uinfo);
        passwordText = (EditText) findViewById(R.id.signup_page_password);
        signupButton = (Button) findViewById(R.id.btn_signup);
        noTokenErrorText = (TextView) findViewById(R.id.text_no_token_error_signup);

        passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.signup || id == EditorInfo.IME_ACTION_SEND) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        signupButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void doSignUp(UserDetail user) {
        AddRequestTask<String, UserDetail> signupRequest = new AddRequestTask<String, UserDetail>(String.class,
                user, HttpMethod.POST, HeaderTools.CONTENT_TYPE_JSON);
        signupRequest.execute(FixedVars.BASE_URL + "/user");
        signupRequest.getObj();
    }

    private void doLogin(UserDetail login) {
        AddRequestTask<String, UserDetail> loginRequest = new AddRequestTask<String, UserDetail>(String.class,
                login, HttpMethod.POST, HeaderTools.CONTENT_TYPE_JSON, HeaderTools.ACCEPT_TEXT);
        loginRequest.execute(FixedVars.BASE_URL + "/authentication/" + login.getUserName());
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Registering you as our new user");
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
        if (token != null) {
            return;
        }

        // Reset errors.
        fnameText.setError(null);
        lnameText.setError(null);
        emailText.setError(null);
        userNameText.setError(null);
        uinfoText.setError(null);
        passwordText.setError(null);
        noTokenErrorText.setError(null);

        // Store values at the time of the login attempt.
        String fname = emailText.getText().toString();
        String lname = emailText.getText().toString();
        String email = emailText.getText().toString();
        String uname = userNameText.getText().toString();
        String uinfo = uinfoText.getText().toString();
        String password = passwordText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check if the First name field is empty.
        //if (TextUtils.isEmpty(fname)) {
        if (fname.matches("")) {
            fnameText.setError(getString(R.string.error_field_required));
            focusView = fnameText;
            cancel = true;
        }// Check if the Last name field is empty.
        //if (TextUtils.isEmpty(lname)) {
        if (lname.matches("")) {
            lnameText.setError(getString(R.string.error_field_required));
            focusView = lnameText;
            cancel = true;
        }
        // Check if the Email field is empty.
        //if (TextUtils.isEmpty(email)) {
        if (email.matches("")) {
            emailText.setError(getString(R.string.error_field_required));
            focusView = emailText;
            cancel = true;
        }
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
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //Attempts login by calling the server using REST Api call
            UserDetail user = new UserDetail();
            user.setFirstName(fname);
            user.setLastName(lname);
            user.setEmailId(email);
            user.setUserName(uname);
            user.setInfo(uinfo);
            user.setPass(password);
            doSignUp(user);

            UserDetail loginObj = new UserDetail();
            loginObj.setUserName(user.getUserName());
            loginObj.setPass(user.getPass());
            doLogin(loginObj);
        }

        if (token != null && !token.equalsIgnoreCase("")) {
            // sign up successful
            Log.i("doshi signup", token);

            //Saving details for using in other activities using SharedPreferences.
            editor.putString(FixedVars.PREF_USER_NAME, userNameText.getText().toString());
            editor.putString(FixedVars.PREF_LOGIN_TOKEN, token);
            editor.apply();

            progressDialog.cancel();

            Toast.makeText(SignupPage.this, "Welcome to Articles Hub "+uname, Toast.LENGTH_LONG).show();

            Intent myIntent = new Intent(SignupPage.this, SelectTagPage.class);
            startActivity(myIntent);
            finish();
        } else {
            // sign up failed
            Log.i("doshi signup", "signup failed");

            noTokenErrorText.setEnabled(true);
            noTokenErrorText.setText(getString(R.string.invalid_or_empty_details));
            userNameText.requestFocus();
            emailText.requestFocus();
            uinfoText.requestFocus();
            passwordText.requestFocus();

            progressDialog.cancel();
        }
    }
}