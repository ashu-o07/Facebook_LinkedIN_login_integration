package com.example.hppc.tsf_social_media_integration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.DeviceLoginButton;
import com.facebook.login.widget.LoginButton;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;



import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Arrays;

public class Login extends AppCompatActivity {

    CallbackManager callbackManager;
    LoginButton lbtn;
    private static final String EMAIL = "email";
    private static final String API_KEY = "78ffalzhcb3pqc";
    private static final String SECRET_KEY = "Y7wAHwEqlOBjbnHk";

    private static final String STATE = "E3ZYKC1T6H2yP4z";
    private static final String REDIRECT_URI = "https://www.google.com/";

    private static final String SCOPES = "r_liteprofile%20r_emailaddress";

    private static final String AUTHORIZATION_URL = "https://www.linkedin.com/oauth/v2/authorization";
    private static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/oauth/v2/accessToken";
    private static final String SECRET_KEY_PARAM = "client_secret";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String RESPONSE_TYPE_VALUE ="code";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String SCOPE_PARAM = "scope";
    private static final String STATE_PARAM = "state";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";

    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";

    private static final String PROFILE_URL ="https://api.linkedin.com/v2/me/";
    private static final String OAUTH_ACCESS_TOKEN_PARAM ="oauth2_access_token";

    private WebView webView;
    private ProgressDialog pd;
    private ImageView lin;

     String aToken="";
     String f,l,u,em;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_login);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("Login Integration");
        myToolbar.setTitleTextColor(Color.BLACK);
          callbackManager = CallbackManager.Factory.create();
          lbtn=findViewById(R.id.login);
          lin=findViewById(R.id.linkedIn);
          webView=findViewById(R.id.webview);
          lbtn.setReadPermissions("email","public_profile");


          lbtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
              @Override
              public void onSuccess(LoginResult loginResult) {
                  pd = ProgressDialog.show(Login.this, "", "Loading.......",true);
                  final String[] fname = new String[1];
                  final String[] lname = new String[1];
                  final String[] email = new String[1];
                  final String[] id = new String[1];
                  final String[] url = new String[1];

                  //  Profile profile = Profile.getCurrentProfile();


                 GraphRequest request = GraphRequest.newMeRequest(
                          loginResult.getAccessToken(),
                          new GraphRequest.GraphJSONObjectCallback() {
                              @Override
                              public void onCompleted(
                                      JSONObject object,
                                      GraphResponse response) {

                                  try {

                                      fname[0] =object.getString("first_name");
                                      lname[0] =object.getString("last_name");
                                      email[0] =object.getString("email");
                                      id[0] =object.getString("id");
                                      url[0] ="http://graph.facebook.com/"+ id[0]+"/picture?type=normal";
                                      System.out.println(object);
                                      Intent intent = new Intent(Login.this,Show.class);
                                      intent.putExtra("app",0);
                                      intent.putExtra("name",fname[0]+" "+lname[0]);
                                      intent.putExtra("email",email[0]);
                                      intent.putExtra("url",url[0]);
                                      System.out.println(fname[0]+" "+lname[0]);
                                      System.out.println(email[0]);
                                      System.out.println(url[0]);
                                      pd.dismiss();
                                      startActivity(intent);

                                  } catch (JSONException e) {
                                      e.printStackTrace();
                                  }

                              }
                          });
                  Bundle parameters = new Bundle();
                  parameters.putString("fields", "first_name,last_name,email,id");
                  request.setParameters(parameters);
                  request.executeAsync();




              }

              @Override
              public void onCancel() {
                  Toast.makeText(getApplicationContext(),"Fialed to login", Toast.LENGTH_SHORT).show();

              }

              @Override
              public void onError(FacebookException error) {
                  Toast.makeText(getApplicationContext(),"Fialed to login", Toast.LENGTH_SHORT).show();

              }
          });
          lin.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {


                  webView.requestFocus(View.FOCUS_DOWN);
                  webView.setVisibility(View.VISIBLE);
                  pd = ProgressDialog.show(Login.this, "", "Loading..",true);


                  webView.setWebViewClient(new WebViewClient(){
                      @Override
                      public void onPageFinished(WebView view, String url) {

                          if(pd!=null && pd.isShowing()){
                              pd.dismiss();

                          }
                      }
                      @Override
                      public boolean shouldOverrideUrlLoading(WebView view, String authorizationUrl) {

                          if(authorizationUrl.startsWith(REDIRECT_URI)){
                              Log.i("Authorize", "");
                              Uri uri = Uri.parse(authorizationUrl);

                              String stateToken = uri.getQueryParameter(STATE_PARAM);
                              if(stateToken==null || !stateToken.equals(STATE)){
                                  Log.e("Authorize", "State token doesn't match");
                                  if(pd!=null && pd.isShowing()){
                                      pd.dismiss();

                                  }
                                  webView.setVisibility(View.GONE);
                                  return true;
                              }


                              String authorizationToken = uri.getQueryParameter(RESPONSE_TYPE_VALUE);
                              if(authorizationToken==null){
                                  Log.i("Authorize", "The user doesn't allow authorization.");
                                  if(pd!=null && pd.isShowing()){
                                      pd.dismiss();

                                  }
                                  webView.setVisibility(View.GONE);
                                  return true;
                              }
                              Log.i("Authorize", "Auth token received: "+authorizationToken);


                              String accessTokenUrl = getAccessTokenUrl(authorizationToken);

                              new PostRequestAsyncTask().execute(accessTokenUrl);
                              webView.setVisibility(View.GONE);

                          }else{

                              Log.i("Authorize","Redirecting to: "+authorizationUrl);

                              webView.loadUrl(authorizationUrl);
                          }

                          return true;
                      }
                  });


                  String authUrl = getAuthorizationUrl();
                  Log.i("Authorize","Loading Auth Url*******************************: "+authUrl);
                  System.out.println(authUrl);
                  webView.loadUrl(authUrl);


              }
          });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private static String getAccessTokenUrl(String authorizationToken){
        String URL = ACCESS_TOKEN_URL
                +QUESTION_MARK
                +GRANT_TYPE_PARAM+EQUALS+GRANT_TYPE
                +AMPERSAND
                +RESPONSE_TYPE_VALUE+EQUALS+authorizationToken
                +AMPERSAND
                +CLIENT_ID_PARAM+EQUALS+API_KEY
                +AMPERSAND
                +REDIRECT_URI_PARAM+EQUALS+REDIRECT_URI
                +AMPERSAND
                +SECRET_KEY_PARAM+EQUALS+SECRET_KEY;
        Log.i("accessToken URL",""+URL);
        return URL;
    }

    private static String getAuthorizationUrl(){
        String URL = AUTHORIZATION_URL
                +QUESTION_MARK+RESPONSE_TYPE_PARAM+EQUALS+RESPONSE_TYPE_VALUE
                +AMPERSAND  +CLIENT_ID_PARAM    +EQUALS +API_KEY
                +AMPERSAND  +SCOPE_PARAM        +EQUALS +SCOPES
                +AMPERSAND  +STATE_PARAM        +EQUALS +STATE
                +AMPERSAND  +REDIRECT_URI_PARAM +EQUALS +REDIRECT_URI;
        Log.i("authorization URL",""+URL);
        return URL;
    }
    private class PostRequestAsyncTask extends AsyncTask<String, Void, Boolean>{

        @Override
        protected void onPreExecute(){
            if(pd!=null && pd.isShowing()){
                pd.dismiss();

            }
            pd = ProgressDialog.show(Login.this, "", "Loading.....",true);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            if(urls.length>0){
                String url = urls[0];
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpost = new HttpPost(url);
                try{
                    HttpResponse response = httpClient.execute(httpost);
                    if(response!=null){

                        if(response.getStatusLine().getStatusCode()==200){
                            String result = EntityUtils.toString(response.getEntity());

                            JSONObject resultJson = new JSONObject(result);

                            int expiresIn = resultJson.has("expires_in") ? resultJson.getInt("expires_in") : 0;

                            String accessToken = resultJson.has("access_token") ? resultJson.getString("access_token") : null;
                            Log.e("Tokenm", ""+accessToken);
                            if(expiresIn>0 && accessToken!=null){
                                Log.i("Authorize", "This is the access Token: "+accessToken+". It will expires in "+expiresIn+" secs");


                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.SECOND, expiresIn);
                                long expireDate = calendar.getTimeInMillis();


                                SharedPreferences preferences = Login.this.getSharedPreferences("user_info", 0);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putLong("expires", expireDate);
                                editor.putString("accessToken", accessToken);
                                editor.commit();
                                aToken=accessToken;


                                return true;
                            }
                        }
                    }
                }catch(IOException e){
                    Log.e("Authorize","Error Http response "+e.getLocalizedMessage());
                }
                catch (ParseException e) {
                    Log.e("Authorize","Error Parsing Http response "+e.getLocalizedMessage());
                } catch (JSONException e) {
                    Log.e("Authorize","Error Parsing Http response "+e.getLocalizedMessage());
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean status){
            if(pd!=null && pd.isShowing()){
                pd.dismiss();
            }
            if(status){

                if(aToken!=null){
                    Log.i("******","***************************************************: ");
                    String profileUrl = getProfileUrl(aToken);
                    new GetProfileRequestAsyncTask().execute(profileUrl);
                }


            }
        }

    };
    private static final String getProfileUrl(String accessToken){
        return PROFILE_URL
                +"?"
                +OAUTH_ACCESS_TOKEN_PARAM+EQUALS+accessToken;
    }

    private class GetProfileRequestAsyncTask extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected void onPreExecute(){
            if(pd!=null && pd.isShowing()){
                pd.dismiss();

            }
            pd = ProgressDialog.show(Login.this, "", "Loading.....",true);
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            if(urls.length>0){
                String url = urls[0];
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(url);
                Log.i("Authorize","Loading ****** Url*******************************: "+url);
                httpget.setHeader("x-li-format", "json");
                try{
                    HttpResponse response = httpClient.execute(httpget);
                    if(response!=null){

                        if(response.getStatusLine().getStatusCode()==200){
                            String result = EntityUtils.toString(response.getEntity());

                            return new JSONObject(result);
                        }
                    }
                }catch(IOException e){
                    Log.e("Authorize","Error Http response "+e.getLocalizedMessage());
                } catch (JSONException e) {
                    Log.e("Authorize","Error Http response "+e.getLocalizedMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject data){
            if(pd!=null && pd.isShowing()){
                pd.dismiss();
            }
            if(data!=null){


                try {
                    f=data.getString("localizedFirstName");
                    l=data.getString("localizedLastName");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("---------------------------------------"+f+" "+l);
                new GetProfileRequestAsyncTask1().execute("https://api.linkedin.com/v2/me?projection=(id,profilePicture(displayImage~digitalmediaAsset:playableStreams))"+"&"+OAUTH_ACCESS_TOKEN_PARAM+EQUALS+ aToken);
            }
        }


    };

    private class GetProfileRequestAsyncTask1 extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected void onPreExecute(){
            if(pd!=null && pd.isShowing()){
                pd.dismiss();

            }
            pd = ProgressDialog.show(Login.this, "", "Loading.....",true);
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            if(urls.length>0){
                String url = urls[0];
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(url);
                Log.i("Authorize","Loading ****** Url*******************************: "+url);
                httpget.setHeader("x-li-format", "json");
                try{
                    HttpResponse response = httpClient.execute(httpget);
                    if(response!=null){

                        if(response.getStatusLine().getStatusCode()==200){
                            String result = EntityUtils.toString(response.getEntity());

                            return new JSONObject(result);
                        }
                    }
                }catch(IOException e){
                    Log.e("Authorize","Error Http response "+e.getLocalizedMessage());
                } catch (JSONException e) {
                    Log.e("Authorize","Error Http response "+e.getLocalizedMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject data){
            if(pd!=null && pd.isShowing()){
                pd.dismiss();
            }
            if(data!=null){



                try {
                    JSONArray jsn =data.getJSONObject("profilePicture").getJSONObject("displayImage~").getJSONArray("elements");
                    JSONObject obj=jsn.getJSONObject(1);
                    JSONArray arr= obj.getJSONArray("identifiers");
                    JSONObject obj1=arr.getJSONObject(0);
                    String url=obj1.getString("identifier");
                    u=url;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("---------------------------------------"+u);
                new GetProfileRequestAsyncTask2().execute("https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))"+"&"+OAUTH_ACCESS_TOKEN_PARAM+EQUALS+ aToken);
            }
        }


    };
    private class GetProfileRequestAsyncTask2 extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected void onPreExecute(){
            if(pd!=null && pd.isShowing()){
                pd.dismiss();

            }
            pd = ProgressDialog.show(Login.this, "", "Loading.......",true);
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            if(urls.length>0){
                String url = urls[0];
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(url);
                Log.i("Authorize","Loading ****** Url*******************************: "+url);
                httpget.setHeader("x-li-format", "json");
                try{
                    HttpResponse response = httpClient.execute(httpget);
                    if(response!=null){

                        if(response.getStatusLine().getStatusCode()==200){
                            String result = EntityUtils.toString(response.getEntity());

                            return new JSONObject(result);
                        }
                    }
                }catch(IOException e){
                    Log.e("Authorize","Error Http response "+e.getLocalizedMessage());
                } catch (JSONException e) {
                    Log.e("Authorize","Error Http response "+e.getLocalizedMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject data){
            if(pd!=null && pd.isShowing()){
                pd.dismiss();
            }
            if(data!=null){

                try {
                    String email=data.getJSONArray("elements").getJSONObject(0).getJSONObject("handle~").getString("emailAddress");
                    em = email;
                    System.out.println(em);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Login.this,Show.class);
                intent.putExtra("app",1);
                intent.putExtra("name",f+" "+l);
                intent.putExtra("email",em);
                intent.putExtra("url",u);
                startActivity(intent);



            }
        }


    };
}