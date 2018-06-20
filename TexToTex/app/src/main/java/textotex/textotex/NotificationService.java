package textotex.textotex;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

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
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {


    public static final long mDeltaTime = 10000; //ms
    public static final int READ_TIMEOUT=15000;
    public static final int CONNECTION_TIMEOUT=10000;

    private Timer mTimer = null;
    private Handler mHandler = new Handler();
    private final IBinder mBinder = new LocalBinder();

    private int mUserID;

    public List<notificationData> mDataArray;

    private class LocalBinder extends Binder {
        NotificationService getService() {
            return NotificationService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return mBinder;
    }

    @Override
    public void onCreate() {
        Log.e("IUOP", "Creating the service..");

        this.mUserID = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE).getInt(getString(R.string.user_id_key), -1);

        this.mDataArray = new ArrayList<>();

        if(this.mTimer == null)
            this.mTimer = new Timer();

        mTimer.scheduleAtFixedRate(new CheckingTask(), 0, mDeltaTime);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful

        return Service.START_STICKY;
    }

    private class CheckingTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("IUOP", "REQUEST NOW");

                    //Check internet connection -> not working
                    if(isInternetAvailable())
                    {
                        Log.e("IUOP", "INTERNET AVAILABLE");
                    }

                    CheckNewMessage check = new CheckNewMessage();
                    check.execute();

                    if(mDataArray.size() != 0)
                    {
                        for(int i =0; i < mDataArray.size(); i++) {
                            mDataArray.get(i).show(NotificationService.this);
                        }
                    }
                    //LED control
                    //Vibration control


                }
            });

        }

    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    private class CheckNewMessage extends AsyncTask<String, String, String>
    {
        HttpURLConnection conn;
        URL url = null;

       @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your php file resides
                url = new URL( getString(R.string.url_base)+ getString(R.string.url_check_message));

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
                        .appendQueryParameter("userID", String.valueOf(NotificationService.this.mUserID));
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
            boolean error = false;
            String msgObj = "";
            String invObj = "";
            String keyObj = "";
            String error_txt = "Internal error.";
            String pubExp = "";
            String modulus = "";
            int invitationID;
            int conversationID;

            if(result.contains("true")) {
                Scanner reader = new Scanner(result);

                while (reader.hasNextLine())
                {
                    String line = reader.nextLine();

                    if(line.contains("true")) {
                        continue;
                    }
                    else if(line.contains("msg: "))
                        msgObj = line.substring(line.indexOf("msg: ") + "msg: ".length(), line.indexOf(" :msg"));
                    else if(line.contains("inv:"))
                        invObj = line.substring(line.indexOf("inv: ") + "inv: ".length(), line.indexOf(" :inv"));
                    else if(line.contains("aes: "))
                        keyObj = line.substring(line.indexOf("aes: ") + "aes: ".length(), line.indexOf(" :aes"));
                    else if(line.contains("error: "))
                        error_txt = result.substring(result.indexOf("error: ") + "error: ".length());
                }
            }
            else if(result.contains("false"))
            {

            }
            else
            {

            }

            //messages
            if (msgObj != "")
            {
                try {
                    JSONArray jsonArray = new JSONArray(msgObj);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject dataObject = jsonArray.getJSONObject(i);
                        mDataArray.add(new notificationData(dataObject.getString("convName"), dataObject.getString("firstName"), dataObject.getString("date"), dataObject.getString("content"), mUserID, dataObject.getInt("conversationID")));
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //gotta send aes keys
            if(invObj != "")
            {
                try {
                    JSONArray jsonArray = new JSONArray(invObj);

                    for (int i =0; i<jsonArray.length(); i++)
                    {
                        JSONObject dataObject = jsonArray.getJSONObject(i);
                        //sClass.sendAESKey(NotificationService.this, dataObject.getInt("idTarget"), dataObject.getInt("idConversation"), dataObject.getInt("idInvitation"), dataObject.getString("pubExp"), dataObject.getString("modulus"));
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            //Received a new AES key
            if(keyObj != "")
            {
                try {
                    JSONArray jsonArray = new JSONArray(keyObj);

                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject dataObject = jsonArray.getJSONObject(i);
                        //sClass.updateAESKey(dataObject.getInt("idUser"), dataObject.getInt("idConversation"), dataObject.getString("key"));
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            //Error :(
            if(error)
            {
            }
        }

    }




}