package com.example.parthdoshi.articleshub;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.neel.articleshubapi.restapi.beans.ArticleDetail;
import com.neel.articleshubapi.restapi.beans.UserDetail;
import com.neel.articleshubapi.restapi.request.AddRequestTask;
import com.neel.articleshubapi.restapi.request.HeaderTools;
import com.neel.articleshubapi.restapi.request.RequestTask;

import org.springframework.http.HttpMethod;

import static com.neel.articleshubapi.restapi.request.HeaderTools.CONTENT_TYPE_JSON;

public class EditDetailPage extends AppCompatActivity {

    // UI references.
    EditText emailText;
    EditText uinfoText;
    EditText fnameText;
    EditText lnameText;
    EditText passwordText;
    Button saveChangesButton;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    String token = null, userName = null;

    String ufname = null, ulname = null, uinfo = null, uemailid = null, upass = null;
    UserDetail ud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Checking for internet connection
        if(NetworkStatus.getInstance(this).isOnline())
            setContentView(R.layout.activity_edit_detail_page);
        else
            NetworkStatus.getInstance(this).buildDialog(this).show();

        //Creating a SharedPreferences file
        sharedPref = getSharedPreferences(FixedVars.PREF_NAME, Context.MODE_PRIVATE);
        userName = sharedPref.getString(FixedVars.PREF_USER_NAME, "");
        token = sharedPref.getString(FixedVars.PREF_LOGIN_TOKEN, "");

        // Set up the login form.
        fnameText = (EditText) findViewById(R.id.edit_detail_page_fname);
        lnameText = (EditText) findViewById(R.id.edit_detail_page_lname);
        emailText = (EditText) findViewById(R.id.edit_detail_page_email);
        //userNameText = (EditText) findViewById(R.id.edit_detail_page_username);
        uinfoText = (EditText) findViewById(R.id.edit_detail_page_uinfo);
        passwordText = (EditText) findViewById(R.id.edit_detail_page_password);
        saveChangesButton = (Button) findViewById(R.id.btn_save_changes);

        RequestTask<UserDetail> getUserRequest =
                new RequestTask<>(UserDetail.class, HttpMethod.GET, CONTENT_TYPE_JSON);
        getUserRequest.execute(FixedVars.BASE_URL+"/user/"+userName);
        // initiate waiting logic
        ud = getUserRequest.getObj();
        // terminate waiting logic

        //Getting current user profile details
        ufname = ud.getFirstName();
        ulname = ud.getLastName();
        uinfo = ud.getInfo();
        uemailid = ud.getEmailId();
        upass = ud.getPass();

        //Setting the current details int the edit texts
        emailText.setText(uemailid);
        fnameText.setText(ufname);
        lnameText.setText(ulname);
        uinfoText.setText(uinfo);
        passwordText.setText(upass);

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fname = fnameText.getText().toString();
                String lname = lnameText.getText().toString();
                String email = emailText.getText().toString();
                String userinfo = uinfoText.getText().toString();
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
                // Check if the password field is empty.
                //if (!TextUtils.isEmpty(password)) {
                /*
                if (password.matches("")) {
                    passwordText.setError(getString(R.string.error_field_required));
                    focusView = passwordText;
                    cancel = true;
                }
                */
                //Only makes a call to the REST Server API if none of the required fields are empty
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    UserDetail user = new UserDetail();
                    user.setEmailId(email);
                    user.setInfo(userinfo);
                    user.setPass(password);
                    user.setFirstName(fname);
                    user.setLastName(lname);
                    user.setUserName(userName);
                    AddRequestTask<String, UserDetail> userUpdateRequest = new AddRequestTask<String, UserDetail>(String.class,
                            user, HttpMethod.PUT, HeaderTools.CONTENT_TYPE_JSON,
                            HeaderTools.makeAuth(token));
                    userUpdateRequest.execute(FixedVars.BASE_URL+"/user/"+userName);
                    userUpdateRequest.getObj();

                    Intent myIntent = new Intent(EditDetailPage.this, HomeProfilePage.class);
                    startActivity(myIntent);
                    finish();
                }
            }
        });
    }
}