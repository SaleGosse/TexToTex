package textotex.textotex;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Chatroom extends AppCompatActivity {
    private ChatViewMessage chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private RelativeLayout invitation;
    private boolean side = false;
    private cookieManager cm;

    private int conversationID = -1;
    private int userID = -1;
    private int invitationID = -1;
    private String fromFirstName;
    private String fromLastName;
    private String fromLogin;

    public  JSONArray jsonArray ;
    public String chatName;
    private String cookie;
    public  TextView txt;
    public String messageNow;
    public static List<List<String>> reponses ;
    public int type;

    public static final int READ_TIMEOUT=15000;
    public static final int CONNECTION_TIMEOUT=10000;

    @Override
    public void onNewIntent(Intent newIntent) {
        this.setIntent(newIntent);
        }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chatroom_main);

        cm = new cookieManager(this);

        cm.checkSession();

        buttonSend =  findViewById(R.id.send);

        listView =  findViewById(R.id.msgview);

        invitation = findViewById(R.id.invitation);

        invitation.setVisibility(View.GONE);

        conversationID = getIntent().getIntExtra("conversationID", -1);
        chatName = getIntent().getStringExtra("conversationName");

        setTitle(chatName);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);

        userID = sharedPref.getInt(getString(R.string.user_id_key), -1);
        cookie = sharedPref.getString(getString(R.string.cookie_key), "null");

        chatArrayAdapter = new ChatViewMessage(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);
        chatText = findViewById(R.id.msg);

        try {
            getUserInfo ();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        chatText.setOnKeyListener(
                new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    try
                    {
                        return sendChatMessage();
                    }
                    catch (ExecutionException e)
                    {
                        e.printStackTrace();
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    if(!chatText.getText().toString().isEmpty())
                        sendChatMessage();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged()
            {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
    }

    private void getUserInfo () throws ExecutionException, InterruptedException, JSONException  {
        AsyncTask toto = new getUsers().execute();
        String result = (String) toto.get();

        if(type == 1)
        {
            try {
                jsonArray = new JSONArray(result.trim());

                JSONObject obj = null;

                reponses = new ArrayList<List<String>>();

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    try {
                        reponses.add(i,new ArrayList<String>(1));
                        obj = jsonArray.getJSONObject(i);

                        sendMessageSide((userID == Integer.valueOf(obj.getString("idUser"))) ? true : false, obj.getString("content"), obj.getString("date"),obj.getString("lastName") + " " + obj.getString("firstName"));
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jsonArray != null)
            {
            }
        }
        else if(type == 2)
        {
            String content = fromFirstName + " " + fromLastName + " (" + fromLogin + ") wants to chat with you !";
            ((TextView)invitation.findViewById(R.id.invitation_text)).setText(content);

            ((Button)invitation.findViewById(R.id.bt_accept)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptInvitation(invitationID);
                }
            });

            ((Button)invitation.findViewById(R.id.bt_decline)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    declineInvitation(invitationID);

                    Chatroom.this.finish();
                    Intent newIntent = new Intent(Chatroom.this, convListFragment.class);
                }
            });

            invitation.setVisibility(View.VISIBLE);
            chatText.setEnabled(false);
            buttonSend.setEnabled(false);
        }
        else if(type == 3)
        {
            txt.setEnabled(false);
            buttonSend.setEnabled(false);
        }

    }

    private class getUsers extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(Chatroom.this);
        HttpURLConnection conn;
        URL url = null;
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
            url = new URL( getString(R.string.url_base)+ getString(R.string.url_display_text));

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "exception";
        }
        try
        {
            // Setup HttpURLConnection class to send and receive data from php and mysql
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");
            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("cookie", cookie)
                    .appendQueryParameter("conversationID", Integer.toString(conversationID))
                    .appendQueryParameter("userID", Integer.toString(userID));

            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return "exception";
        }
        try {
            int response_code = conn.getResponseCode();

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                //String result;
                String line;

                while ((line = reader.readLine()) != null) {
                    if(line.contains("true"))
                        continue;
                    else if(line.contains("false"))
                    {
                        type = -1;
                        //TODO: Parse and print the error.
                        return "Internal error.";
                    }
                    else if(line.contains("type: "))
                    {
                        type = Integer.valueOf(line.substring(line.indexOf("type: ") + "type: ".length()));
                    }
                    else if(line.contains("error: "))
                    {
                        String error_txt = line.substring(line.indexOf("error: ") + "error: ".length());
                        Toast.makeText(Chatroom.this, error_txt, Toast.LENGTH_LONG).show();
                    }

                    if(type == 2)
                    {
                        if(line.contains("invitationID: "))
                            invitationID = Integer.valueOf(line.substring(line.indexOf("invitationID: ") + "invitationID: ".length()));
                        else if(line.contains("firstName: "))
                            fromFirstName = line.substring(line.indexOf("firstName: ") + "firstName: ".length());
                        else if(line.contains("lastName: "))
                            fromLastName = line.substring(line.indexOf("lastName: ") + "lastName: ".length());
                        else if(line.contains("login: "))
                            fromLogin = line.substring(line.indexOf("login: ") + "login: ".length());
                    }
                    else {
                        result.append(line + "\n");
                    }
                }

                // Pass data to onPostExecute method
                if(type == 1) {
                    if(result.toString().contains("[{") && result.toString().contains("}]"))
                        return result.toString().substring(result.indexOf("[{"), result.indexOf("}]") + 2);

                    return "";
                }

                return(result.toString());

            }else{

                return("unsuccessful");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "exception";
        }
        finally
        {
            conn.disconnect();
        }
        }
        @Override
        protected void onPostExecute(String result)
        {
            //this method will be running on UI thread
            pdLoading.dismiss();
        }

    }

    private class SendMessageServer extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(Chatroom.this);
        HttpURLConnection conn;
        URL url = null;
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
                url = new URL( getString(R.string.url_base)+ getString(R.string.url_add_message));

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "exception";
            }
            try
            {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);
                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("cookie", cookie)
                        .appendQueryParameter("userID", String.valueOf(userID))
                        .appendQueryParameter("content",messageNow)
                        .appendQueryParameter("conversationID", String.valueOf(conversationID));
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return "exception";
            }
            try {
                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    //String result;
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line + "\n");
                    }
                    // Pass data to onPostExecute method
                    return(result.toString());

                }else{

                    return("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "exception";
            }
            finally
            {
                conn.disconnect();
            }
        }
        @Override
        protected void onPostExecute(String result)
        {
            //this method will be running on UI thread
            //Dismiss the loading popup
            pdLoading.dismiss();

            //Parsing the result string
            if(result.contains("true"))
            {
                Toast.makeText(Chatroom.this, "Sent", Toast.LENGTH_LONG).show();
            }
            else if (result.contains("error: "))
            {
                    String error_txt = result.substring(result.indexOf("error: ") + "error: ".length());

                    Toast.makeText(Chatroom.this, error_txt, Toast.LENGTH_LONG).show();

                    if (error_txt.contains("cookie")) {
                        Chatroom.this.cm.checkSession();
                    }
            }
            else
                Toast.makeText(Chatroom.this, "Internal error..", Toast.LENGTH_LONG).show();
        }

    }

    private boolean sendChatMessage() throws ExecutionException, InterruptedException {
        side = true;
        messageNow = chatText.getText().toString();
        if(messageNow.isEmpty())
        {
            return false;
        }
        AsyncTask toto = new  SendMessageServer().execute();
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss");
        chatArrayAdapter.add(new ManagerChat(side, messageNow, ft.format(date),"Me"));
        chatText.setText("");
        side = !side;
        return true;
    }

    private boolean sendMessageSide(boolean pside, String message , String date , String user)
    {
        chatArrayAdapter.add(new ManagerChat(pside, message,date,user));
        chatText.setText("");
        pside = !pside;
        return pside;
    }

    private void acceptInvitation(final int invitationID)
    {
        final SecureClass sClass = new SecureClass(this, this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE));

        try
        {
             final KeyPair kp = KeyPairGenerator.getInstance("RSA").generateKeyPair();

             StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.url_base) + getString(R.string.url_respond_invit),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Scanner reader = new Scanner(response);
                            Boolean success = false;
                            String error_txt = "Internal error.";

                            while (reader.hasNextLine())
                            {
                                String line = reader.nextLine();

                                if(line.contains("true"))
                                    success = true;
                                else if(line.contains("false"))
                                    success = false;
                                else if(line.contains("error: "))
                                {
                                    success = false;
                                    error_txt = line.substring(line.indexOf("error: ") + "error: ".length());
                                }
                            }

                            if(!success)
                                Toast.makeText(Chatroom.this, error_txt, Toast.LENGTH_LONG).show();
                            else
                            {
                                Toast.makeText(Chatroom.this, "Success...", Toast.LENGTH_LONG).show();
                                sClass.insertKeys(conversationID, kp, Integer.toString(new Random().nextInt()));

                                Intent newIntent = new Intent(Chatroom.this, Chatroom.class);

                                newIntent.putExtra("conversationID", conversationID);
                                newIntent.putExtra("conversationName", chatName);

                                startActivity(newIntent);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("cookie", cookie);
                    params.put("userID", Integer.toString(userID));
                    params.put("invitationID", Integer.toString(invitationID));
                    params.put("conversationID", Integer.toString(conversationID));
                    params.put("modulus", ((RSAPublicKey)kp.getPublic()).getModulus().toString());
                    params.put("exponent", ((RSAPublicKey)kp.getPublic()).getPublicExponent().toString());
                    params.put("action", "accept");

                    return params;
                }
            };



            RequestQueue queue = Volley.newRequestQueue(Chatroom.this);

            queue.add(stringRequest);
        }
        catch (Exception e)
        {

        }


    }

    private void declineInvitation(final int invitationID)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.url_base) + getString(R.string.url_respond_invit),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Scanner reader = new Scanner(response);
                        boolean success = false;
                        String error_txt = "Internal error.";

                        while (reader.hasNextLine())
                        {
                            String line = reader.nextLine();
                            if(line.contains("true"))
                                success = true;
                            else if(line.contains("false"))
                            {
                                success = false;
                            }
                            else if (line.contains("error: "))
                            {
                                error_txt = line.substring(line.indexOf("error: ") + "error: ".length());
                                success = false;
                            }

                        }

                        if(!success)
                            Toast.makeText(Chatroom.this, error_txt, Toast.LENGTH_LONG).show();
                        else
                        {
                            Toast.makeText(Chatroom.this, "Done.", Toast.LENGTH_LONG).show();

                            Intent newIntent = new Intent(Chatroom.this, convListFragment.class);


                            startActivity(newIntent);

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userID", Integer.toString(userID));
                params.put("cookie", cookie);
                params.put("conversationID", Integer.toString(conversationID));
                params.put("invitationID", Integer.toString(invitationID));
                params.put("action", "decline");

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Chatroom.this);

        queue.add(stringRequest);
    }
}


