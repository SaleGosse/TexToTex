package textotex.textotex;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class profileFragment extends Fragment {

    public int  userID ;
    public TextView nbConversation;
    public TextView nbFollowers;
    public TextView nbMessage;
    public TextView displayUserName;
    public profileFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);

        nbConversation = rootView.findViewById(R.id.nbConversation);
        nbFollowers = rootView.findViewById(R.id.nbFollowers);
        nbMessage = rootView.findViewById(R.id.nbMessage);
        displayUserName = rootView.findViewById(R.id.userName);
        this.attemptGetInfoUser();
        return rootView;
    }


    private void attemptGetInfoUser()
    {

        final SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        userID = sharedPref.getInt(getString(R.string.user_id_key), -1);
        final String mCookie = sharedPref.getString(getString(R.string.cookie_key), "null");
        final String mUsername = sharedPref.getString(getString(R.string.login_key), "null");
        final RequestQueue mQueue = Volley.newRequestQueue(getContext());

        mQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
            }
        });

        //The request..
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.url_base) + getString(R.string.url_getInfoUser),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            //we have the array named hero inside the object
                            //so here we are getting that json array
                            JSONArray dataArray = new JSONArray(response);
                            JSONObject dataObject ;//= new JSONObject(response);
                            //now looping through all the elements of the json array
                           // for (int i = 0; i < dataArray.length(); i++)
                            {
                                //getting the json object of the particular index inside the array
                              dataObject = dataArray.getJSONObject(0);
                                displayUserName.setText(mUsername);
                                nbConversation.setText(dataObject.getString("NbConversation"));
                                nbFollowers.setText(dataObject.getString("Friends"));
                                nbMessage.setText(dataObject.getString("NbMessage"));
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //put connection problem or smth
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idUser", String.valueOf(userID));
                params.put("cookie", mCookie);
                return params;
            }

        };
        mQueue.add(stringRequest);
    }
}
