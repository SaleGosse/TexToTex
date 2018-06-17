package textotex.textotex;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    public static final int READ_TIMEOUT=15000;
    public static final int CONNECTION_TIMEOUT=10000;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private Button signInButton;
    private Button signUpButton;

    private Random rn = new Random();
    private int range = 4;
    private int randomNum =  rn.nextInt(range) ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);

        if( randomNum == 0 )
        {
            setContentView(R.layout.activity_login1);
        }
        else if(randomNum == 1)
        {
            setContentView(R.layout.activity_login2);
        }
        else if(randomNum == 2)
        {
            setContentView(R.layout.activity_login3);
        }
        else
        {
            setContentView(R.layout.activity_login4);
        }

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        signUpButton = findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                laodSignUpActivity();
            }
        });
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
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);

        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        else {
            String res = isPasswordValid(password);
            if(res.compareTo("true") != 0) {
                mPasswordView.setError(res);
                focusView = mPasswordView;
                cancel = true;
            }
        }

        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else {
            String res = isUsernameValid(username);
            if(res.compareTo("true") != 0) {
                mUsernameView.setError(res);
                focusView = mUsernameView;
                cancel = true;
            }
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute();
        }
    }

    private String isUsernameValid(String email) {
        //TODO: Replace this with your own logic
        if(email.contains("'"))
            return getString(R.string.error_xss);

        return "true";
    }

    private String isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        if(password.length() < 5)
            return getString(R.string.error_password_size);

        if(password.contains("'"))
            return getString(R.string.error_xss);

        return "true";
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<String, String, String> {

        private final String mUsername;
        private final String mPassword;

        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);
        HttpURLConnection mConn;
        URL mURL = null;
        String mCookie = "null", mErrorStr = "null";
        int mUserID = -1;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }


        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your php file resides
                mURL = new URL(getString(R.string.url_base) + getString(R.string.url_login));

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                mConn = (HttpURLConnection)mURL.openConnection();
                mConn.setReadTimeout(READ_TIMEOUT);
                mConn.setConnectTimeout(CONNECTION_TIMEOUT);
                mConn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                mConn.setDoInput(true);
                mConn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("login", mUsername)
                        .appendQueryParameter("password", mPassword);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = mConn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                mConn.connect();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception: cannot connect";
            }
            try {

                int response_code = mConn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = mConn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    String line = reader.readLine();
                    String result = "";

                    while( line != null) {
                        if(line.contains("true")) {
                            result = "true";
                        }
                        else if (line.contains("false")) {
                            result = "false";
                        }
                        else if (line.contains("error: ")) {
                            mErrorStr = line.substring(line.indexOf("error: ") + "error: ".length() + 1);
                        }
                        else if (line.contains("cookie: ")) {
                            mCookie = line.substring(line.indexOf("cookie: ") + "cookie: ".length());
                        }
                        else if (line.contains("idUser: ")) {
                            mUserID = Integer.parseInt(line.substring(line.indexOf("idUser: ") + "idUser: ".length()));
                        }

                        line = reader.readLine();
                    }

                    // Pass data to onPostExecute method
                    return(result);

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception: cannot get response code";
            } finally {
                mConn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();

            //this method will be running on UI thread
            if(result.contains("true") && mUserID != -1 && mCookie != "null")
            {
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putBoolean(getString(R.string.is_logged_key), true);
                editor.putString(getString(R.string.login_key), mUsername);
                editor.putInt(getString(R.string.user_id_key), mUserID);
                editor.putString(getString(R.string.cookie_key), mCookie);

                editor.apply();

                finish();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

            }else if (result.contains("false")){
                // If username and password does not match display a error message
                Toast.makeText(LoginActivity.this, getString(R.string.error_wrong_creds), Toast.LENGTH_LONG).show();

                finish();
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));

            } else if (result.contains("exception") || result.contains("unsuccessful")) {
                Toast.makeText(LoginActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            pdLoading.dismiss();
        }
    }

     public void laodSignUpActivity()
    {
        finish();
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }
}

