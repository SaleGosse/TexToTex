package textotex.textotex;


import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class cookieManager {

    final Activity mActivity;
    final SharedPreferences mSharedPref;
    final SharedPreferences.Editor mEditor;
    final ProgressDialog pdLoading;

    cookieManager(Activity activity) {
        this.mActivity = activity;
        this.mSharedPref = this.mActivity.getSharedPreferences(this.mActivity.getString(R.string.preference_file), Context.MODE_PRIVATE);
        this.mEditor = this.mSharedPref.edit();
        this.pdLoading = new ProgressDialog(this.mActivity);
    }

    public void checkSession() {

        //Resetting the isLogged boolean to check everything
        mEditor.putBoolean(mActivity.getString(R.string.is_logged_key), false);
        mEditor.commit();

        //Getting the cookie
        final String cookie = this.mSharedPref.getString(this.mActivity.getString(R.string.cookie_key), "null");

        //If no cookie, put isLogged/cookie to "null"/false and start Logging Activity
        if(cookie == null || cookie.compareTo("null") == 0) {
            mEditor.putString(mActivity.getString(R.string.cookie_key), "null");
            mEditor.commit();

            this.callLogin();

            return;
        }

        //Getting the login, if no, same
        final int userID = mSharedPref.getInt(mActivity.getString(R.string.user_id_key), -1);
        if(userID == -1){
            mEditor.putString(mActivity.getString(R.string.cookie_key), "null");
            mEditor.putInt(mActivity.getString(R.string.user_id_key), -1);
            mEditor.commit();

            this.callLogin();

            return;
        }

        //The request queue..
        final RequestQueue queue = Volley.newRequestQueue(mActivity);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                pdLoading.dismiss();
            }
        });

        //TODO: Put a loading screen to prevent user inputs

        //The request..
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, mActivity.getString(R.string.url_base) + mActivity.getString(R.string.url_check_cookie),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("true"))
                        {
                            mEditor.putBoolean(mActivity.getString(R.string.is_logged_key), true);
                            mEditor.commit();
                        }
                        else if(response.contains("false"))
                        {
                            if(response.contains("error: ")) {
                                String error_str = response.substring(response.indexOf("error: ") + "error: ".length());
                                Toast.makeText(mActivity, error_str, Toast.LENGTH_LONG);
                            }
                            callLogin();
                        }
                        else
                        {
                            callLogin();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userID", Integer.toString(userID));
                params.put("cookie", cookie);

                return params;
            }
        };

        pdLoading.setMessage("\tLoading...");
        pdLoading.setCancelable(false);
        pdLoading.show();

       queue.add(stringRequest);
    }

    public void callLogin() {
        //Creating the new intent
        Intent newIntent = new Intent(mActivity, LoginActivity.class);

        newIntent.putExtra("login", mSharedPref.getString(mActivity.getString(R.string.login_key), "login"));
        //Putting info in the bundle

        //TODO: Finish the calling activity
        mActivity.finish();

        //Starting the new activity
        mActivity.startActivity(newIntent);

    }


}
