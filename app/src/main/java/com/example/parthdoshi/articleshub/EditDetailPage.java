package com.example.parthdoshi.articleshub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.neel.articleshubapi.restapi.beans.UserDetail;
import com.neel.articleshubapi.restapi.request.AddRequestTask;
import com.neel.articleshubapi.restapi.request.HeaderTools;
import com.neel.articleshubapi.restapi.request.RequestTask;

import org.springframework.http.HttpMethod;

import me.anwarshahriar.calligrapher.Calligrapher;

import static com.neel.articleshubapi.restapi.request.HeaderTools.CONTENT_TYPE_JSON;

public class EditDetailPage extends AppCompatActivity {

    // UI references.
    EditText emailText;
    EditText uinfoText;
    EditText fnameText;
    EditText lnameText;
    EditText oldPasswordText;
    EditText newPasswordText;
    Button saveChangesButton;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    String token = null, userName = null, password = null;

    String ufname = null, ulname = null, uinfo = null, uemailid = null;
    UserDetail ud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Creating a SharedPreferences file
        sharedPref = getSharedPreferences(FixedVars.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.apply();

        Calligrapher calligrapher = new Calligrapher(EditDetailPage.this);
        calligrapher.setFont(EditDetailPage.this, FixedVars.FONT_NAME, true);

        //Checking for internet connection
        if (NetworkStatus.getInstance(this).isOnline()) {
            setContentView(R.layout.activity_edit_detail_page);


            userName = sharedPref.getString(FixedVars.PREF_USER_NAME, "");
            token = sharedPref.getString(FixedVars.PREF_LOGIN_TOKEN, "");
            password = sharedPref.getString(FixedVars.PREF_USER_PASSWORD, "");

            // Set up the login form.
            fnameText = (EditText) findViewById(R.id.edit_detail_page_fname);
            lnameText = (EditText) findViewById(R.id.edit_detail_page_lname);
            emailText = (EditText) findViewById(R.id.edit_detail_page_email);
            uinfoText = (EditText) findViewById(R.id.edit_detail_page_uinfo);
            oldPasswordText = (EditText) findViewById(R.id.edit_detail_page_old_password);
            newPasswordText = (EditText) findViewById(R.id.edit_detail_page_new_password);
            saveChangesButton = (Button) findViewById(R.id.btn_save_changes);

            RequestTask<UserDetail> getUserRequest =
                    new RequestTask<>(UserDetail.class, HttpMethod.GET, CONTENT_TYPE_JSON);
            getUserRequest.execute(FixedVars.BASE_URL + "/user/" + userName);
            // initiate waiting logic
            ud = getUserRequest.getObj();
            // terminate waiting logic

            //Getting current user profile details
            ufname = ud.getFirstName();
            ulname = ud.getLastName();
            uinfo = ud.getInfo();
            uemailid = ud.getEmailId();

            //Setting the current details int the edit texts
            emailText.setText(uemailid);
            fnameText.setText(ufname);
            lnameText.setText(ulname);
            uinfoText.setText(uinfo);

            saveChangesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String fname = fnameText.getText().toString();
                    String lname = lnameText.getText().toString();
                    String email = emailText.getText().toString();
                    String userinfo = uinfoText.getText().toString();
                    String oldPassword = oldPasswordText.getText().toString();
                    String newPassword = newPasswordText.getText().toString();
                    String finalPassword = password;

                    boolean cancel = false;
                    View focusView = null;

                    // Check if the First name field is empty.
                    //if (TextUtils.isEmpty(fname)) {
                    if (fname.matches("")) {
                        fnameText.setError(getString(R.string.error_field_required));
                        focusView = fnameText;
                        cancel = true;
                    } else if (lname.matches("")) {
                        lnameText.setError(getString(R.string.error_field_required));
                        focusView = lnameText;
                        cancel = true;
                    } else if (email.matches("")) {
                        emailText.setError(getString(R.string.error_field_required));
                        focusView = emailText;
                        cancel = true;
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailText.setError("Please enter a valid email address");
                        cancel = true;
                    } else if (oldPassword.matches("")) {
                        oldPasswordText.setError(getString(R.string.error_field_required));
                        focusView = oldPasswordText;
                        cancel = true;
                    } else if (!oldPassword.matches("") && !oldPassword.equals(password)) {
                        oldPasswordText.setError("Invalid Password!!! Please Verify");
                        cancel = true;
                    } else if (oldPassword.equals(password)) {
                        finalPassword = oldPassword;
                    }

                    if (oldPassword.equals(password) && !newPassword.matches(""))
                        finalPassword = newPassword;

                    //Only makes a call to the REST Server API if none of the required fields are empty
                    if (cancel) {
                        // There was an error; don't attempt login and focus the first
                        // form field with an error.
                        if (focusView != null) {
                            focusView.requestFocus();
                        }
                    } else {

                        if (!newPassword.isEmpty()) {
                            editor.putString(FixedVars.PREF_USER_PASSWORD, newPasswordText.getText().toString());
                            editor.apply();
                        }

                        UserDetail user = new UserDetail();
                        user.setEmailId(email);
                        user.setInfo(userinfo);
                        user.setPass(finalPassword);
                        user.setFirstName(fname);
                        user.setLastName(lname);
                        user.setUserName(userName);
                        AddRequestTask<String, UserDetail> userUpdateRequest = new AddRequestTask<String, UserDetail>(String.class,
                                user, HttpMethod.PUT, HeaderTools.CONTENT_TYPE_JSON,
                                HeaderTools.makeAuth(token));
                        userUpdateRequest.execute(FixedVars.BASE_URL + "/user/" + userName);
                        userUpdateRequest.getObj();

                        Intent myIntent = new Intent(EditDetailPage.this, HomeProfilePage.class);
                        startActivity(myIntent);
                        finish();
                    }
                }
            });
        } else
            NetworkStatus.getInstance(this).buildDialog(this).show();
    }
}
