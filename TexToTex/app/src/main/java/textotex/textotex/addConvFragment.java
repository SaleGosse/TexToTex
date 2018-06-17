package textotex.textotex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class addConvFragment extends Fragment
{
    private EditText mUsernameView;

    public addConvFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState){
        final View rootView = inflater.inflate(R.layout.add_conv_fragment, container, false);

        mUsernameView = rootView.findViewById(R.id.add_conv_username);

        final Button mainButton = (Button) rootView.findViewById(R.id.bt_add_conv);
        mainButton.setOnClickListener(new View.OnClickListener() {
            final EditText mTargetUsername = (EditText) getActivity().findViewById(R.id.add_conv_username);

            @Override
            public void onClick(View v) {
                if(!((EditText)rootView.findViewById(R.id.add_conv_username)).getText().toString().isEmpty() && !((EditText)rootView.findViewById(R.id.add_conv_convName)).getText().toString().isEmpty())
                    attemptAddConv();
            }
        });

        return rootView;
    }

    private void attemptAddConv() {

        final SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        final int mUserID = sharedPref.getInt(getString(R.string.user_id_key), -1);
        final String mCookie = sharedPref.getString(getString(R.string.cookie_key), "null");

        final String mTargetUsername = ((EditText) getActivity().findViewById(R.id.add_conv_username)).getText().toString();
        final String mConvName = ((EditText) getActivity().findViewById(R.id.add_conv_convName)).getText().toString();

        try
        {
            final KeyPair kp = KeyPairGenerator.getInstance("RSA").generateKeyPair();

            final RequestQueue mQueue = Volley.newRequestQueue(getContext());

            mQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                @Override
                public void onRequestFinished(Request<Object> request) {
                }
            });

            //The request..
            StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.url_base) + getString(R.string.url_start_conv),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.contains("true")){
                                //Call fab's conv list with the id of the conv or smthq
                                int conversationID = -1;
                                String pubkey;

                                Scanner reader = new Scanner(response);

                                while(reader.hasNextLine())
                                {
                                    String line = reader.nextLine();

                                    if(line.contains("true"))
                                        continue;
                                    else if(line.contains("convID: "))
                                        conversationID = Integer.parseInt(line.substring(line.indexOf("convID: ") + "convID: ".length()));
                                }

                                SecureClass sClass = new SecureClass(getContext(), sharedPref);
                                sClass.insertKeys(conversationID, kp, String.valueOf(new Random().nextInt()));

                                getActivity().finish();

                                Intent newIntent = new Intent(getContext(), Chatroom.class);
                                newIntent.putExtra("conversationID", conversationID);
                                newIntent.putExtra("convName", mConvName);

                                startActivity(newIntent);

                                Toast.makeText(getActivity(), getActivity().getString(R.string.invite_success), Toast.LENGTH_LONG).show();
                            }
                            else if (response.contains("false")) {
                                //Put invalid username error prompt
                                if(response.contains("error: "))
                                {
                                    String error_txt = response.substring(response.indexOf("error: ") + "error: ".length());

                                    Toast.makeText(getActivity(), error_txt, Toast.LENGTH_LONG).show();
                                }
                                else
                                    Toast.makeText(getActivity(), "Internal error.", Toast.LENGTH_LONG).show();

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
                    params.put("userID", Integer.toString(mUserID));
                    params.put("cookie", mCookie);
                    params.put("target", mTargetUsername);
                    params.put("convName", mConvName);
                    params.put("modulus", ((RSAPublicKey)kp.getPublic()).getModulus().toString());
                    params.put("exponent", ((RSAPublicKey)kp.getPublic()).getPublicExponent().toString());

                    return params;
                }
            };
            mQueue.add(stringRequest);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


}
