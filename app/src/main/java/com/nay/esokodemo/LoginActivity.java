package com.nay.esokodemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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

import com.nay.esokodemo.Models.LoginResponse;
import com.nay.esokodemo.Models.User;
import com.nay.esokodemo.Util.App;
import com.nay.esokodemo.Util.ConnectionDetector;
import com.nay.esokodemo.Util.Constants;
import com.nay.esokodemo.Util.DBHelper;
import com.nay.esokodemo.Util.EsokoDemoService;
import com.nay.esokodemo.Util.Helpers;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.Manifest.permission.READ_CONTACTS;


public class LoginActivity extends AppCompatActivity  {


    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView mRegisterBtn;


    private String TAG=getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Helpers.getLoginStatus(getApplicationContext(),Constants.LOGIN_STATUS)){
            startActivity(new Intent(LoginActivity.this,Home.class));
            finish();
        }
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mRegisterBtn=(TextView) findViewById(R.id.register_btn);


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    if (new ConnectionDetector(getApplicationContext()).isConnectingToInternet())
                    attemptLogin();
                    else{
                        Toast.makeText(LoginActivity.this, "Check your Internet Connection!", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new ConnectionDetector(getApplicationContext()).isConnectingToInternet())
                attemptLogin();
                else{
                    Toast.makeText(LoginActivity.this, "Check your Internet Connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mRegisterBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });
    }



    private void attemptLogin() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
           final User user=new User();
            user.email=email;
            user.password=password;
            App.esokoDemoService.login(user, new Callback<LoginResponse>() {
                @Override
                public void success(LoginResponse loginResponse, Response response) {
                    Log.d(TAG,loginResponse.toString());
                    showProgress(false);
                    if (loginResponse.error == null){
                        SharedPreferences pref = getApplication().getSharedPreferences("Credentials", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(Constants.AUTH_TOKEN, loginResponse.token) ;
                        editor.commit() ;
                        Toast.makeText(LoginActivity.this, pref.getString(Constants.AUTH_TOKEN,""), Toast.LENGTH_SHORT).show();
                        Helpers.setLoginStatus(LoginActivity.this,Constants.LOGIN_STATUS,true);
                        DBHelper.saveCurrentUser(user);
                        startActivity(new Intent(LoginActivity.this,Home.class));
                        finish();
                    }else{
                        Toast.makeText(LoginActivity.this, loginResponse.error, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
//                    Log.d(TAG,error.printStackTrace());
                    error.printStackTrace();

                    showProgress(false);
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}

