package com.culturage.oceans_eleven.culturage.signup_login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.culturage.oceans_eleven.culturage.R;
import com.culturage.oceans_eleven.culturage.network.PostJSON;
import com.culturage.oceans_eleven.culturage.newsFeed.NewsFeedActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LoginActivity extends AppCompatActivity {

    private static final String registerURI = "auth/login/";
    private EditText mUsernameView;
    private EditText mPasswordView;
    private ProgressBar mProgressBar;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String username, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        forceInternetConnection();

        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        String token = preferences.getString("token", "null");
        if (!token.equals("null")) {
            startActivity(new Intent(LoginActivity.this, NewsFeedActivity.class));
        }
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_login);

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private void forceInternetConnection() {
        ConnectivityManager conMan = (ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMan.getActiveNetworkInfo() == null || !conMan.getActiveNetworkInfo().isConnected()) {
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(LoginActivity.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(LoginActivity.this);
            }
            builder.setTitle("No Internet Connection")
                    .setMessage("Please connect to the internet")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }


    private void attemptLogin() {

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        username = mUsernameView.getText().toString();
        password = mPasswordView.getText().toString();

//        if (username.equals("r") && password.equals("1")) {
//            Intent intent = new Intent(LoginActivity.this, NewsFeedActivity.class);
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, R.string.unsuccessful_login, Toast.LENGTH_SHORT).show();
//            mUsernameView.requestFocus();
//        }
        new LoginRequest(LoginActivity.this).execute();
    }

    private class LoginRequest extends AsyncTask<String, String, String> {

        private String resp;
        String returnedToken;
        Context mContext;

        public LoginRequest(Context context) {
            mContext = context;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                returnedToken = sendLoginRequest();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            mProgressBar.setVisibility(View.GONE);
            editor = preferences.edit();
            editor.putString("token", returnedToken);
            editor.apply();
            startActivity(new Intent(LoginActivity.this, NewsFeedActivity.class));
        }

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

    }

    private String sendLoginRequest() {
        JSONObject json = new JSONObject();
        try {
            json.put("username", username);
            json.put("password", password);
            Log.v("logintag: json", json.toString());
        } catch (JSONException e) {
            // TODO: Handle exception
            Log.v("logintag", "Error in Json construction");
            e.printStackTrace();
        }

        String result = null;
        try {
            result = PostJSON.postToApi(json, registerURI);
        } catch (Exception e) {
            // TODO: Handle exception
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            Log.v("logintag", exceptionAsString);
        }
        if (result.equals("Error")) {
            Log.v("logintag", "400 returned from server");
        }
        return parseJson(result);
    }

    private String parseJson(String result) {
        String token = null;
        try {
            JSONObject json = new JSONObject(result);
            token = json.getString("token");
            Log.v("logintag", "JSon is extracted. Token: " + token);
        } catch (JSONException e) {
            Log.e("logintag", "Problem parsing the JSON results", e);
        }
        return token;
    }
}
