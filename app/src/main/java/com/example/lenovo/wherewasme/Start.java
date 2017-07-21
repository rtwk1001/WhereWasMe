package com.example.lenovo.wherewasme;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Start extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    SQLiteDatabase db;
    private GoogleApiClient client;
    Button conti,signin,signup;
    EditText loginid,loginpass,full,unique,user,pass1,pass2;
ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        db = openOrCreateDatabase("localdb ",MODE_PRIVATE,null);
         conti =(Button)findViewById(R.id.getdata);
        conti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Start.this);
                dialog.setContentView(R.layout.logrex);
                dialog.setTitle("Excuse Me !");
                Button b1 = (Button) dialog.findViewById(R.id.login);
                Button b2 = (Button) dialog.findViewById(R.id.register);
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog login = new Dialog(Start.this);
                        login.setContentView(R.layout.login);
                        login.setTitle("Login with us!");
                        signin=(Button)login.findViewById(R.id.signin);
                        loginid=(EditText)login.findViewById(R.id.loginid);
                        loginpass=(EditText)login.findViewById(R.id.loginpass);
                        final String emal=loginid.getText().toString();
                        final String pas= loginpass.getText().toString();
                       signin.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View v) {



                                   login(emal,pas);

                           }
                       });


                        login.show();
                    }
                });
                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog registration= new Dialog(Start.this);
                        registration.setCancelable(true);
                        registration.setContentView(R.layout.register);
                        registration.setTitle("Register Yourself");
                        signup=(Button)registration.findViewById(R.id.Reg);
                        full=(EditText)registration.findViewById(R.id.full);
                        unique=(EditText)registration.findViewById(R.id.phoneid);
                        user=(EditText)registration.findViewById(R.id.username);
                        pass1=(EditText)registration.findViewById(R.id.pass1);
                        pass2=(EditText)registration.findViewById(R.id.pass2);
                        db.execSQL("Create Table If Not Exists Users(fullName Varchar,uniqueid Varchar,email Varchar,password Varchar);");
                        final String fullname =full.getText().toString();
                        final String uniqueid = unique.getText().toString();
                        final String username = user.getText().toString();
                        final String pass=pass1.getText().toString();
                        final String passcon=pass2.getText().toString();

            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fullname!=""&&uniqueid!=""&&username!=""&&pass!="") {
                        if(pass.equals(passcon)){
                        new Senddataserver().execute();
                        Toast.makeText(Start.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                        }
                        else
                            pass2.setError("Password does not match");
                    }
                    else {
                        Toast.makeText(Start.this,"Invalid Entry",Toast.LENGTH_SHORT).show();

                    }
                }
            });
                 registration.show();   }
                });
                 dialog.show();
            }
        });



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Start Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.lenovo.wherewasme/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Start Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.lenovo.wherewasme/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    private void login(final String username, String password) {

        class LoginAsync extends AsyncTask<String, Void, String>{

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(Start.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String uname = params[0];
                String pass = params[1];

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("username", uname));
                nameValuePairs.add(new BasicNameValuePair("password", pass));
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://rtwk1001.esy.es/login.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                String s = result.trim();
                loadingDialog.dismiss();
                if(s.equalsIgnoreCase("success")){
                    Intent intent = new Intent(Start.this,Showdata.class);
                    intent.putExtra("User", username);
                    finish();
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(username, password);

    }
    public class Senddataserver extends AsyncTask<String, Void, String> {
        final String fullname =full.getText().toString();
        final String uniqueid = unique.getText().toString();
        final String username = user.getText().toString();
        final String pass=pass1.getText().toString();
        final String passcon=pass2.getText().toString();

        protected void onPreExecute(){
            pd=new ProgressDialog(Start.this);
            pd.setMessage("Plese wait");
            pd.show();
        }

        protected String doInBackground(String... arg0) {

            try{

                URL url = new URL("http://rtwk1001.esy.es/insert.php");

                JSONObject postDataParams = new JSONObject();


                postDataParams.put("fullname",fullname );
                postDataParams.put("username",username);
                postDataParams.put("unique_id",uniqueid);
                postDataParams.put("password", pass);


                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
            pd.cancel();

        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
