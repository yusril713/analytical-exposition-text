package com.aet.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;
import com.aet.app.utils.PreferenceUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    JSONParser jParser = new JSONParser();
    private ProgressDialog pDialog;
    public static String URL = Constants.BASE_URL + "api/login";
    private EditText txtUser, txtPass;
    private String user = null;
    private String token = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("MESSAGING SERVICE", "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    token = task.getResult();
//                    Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    Log.i("My Token", token);
                });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[] {100, 200, 300, 400, 500, 400, 300, 200});
            notificationManager.createNotificationChannel(notificationChannel);
        }

        InitView();
    }

    public void Register(View v) {
        if (Constants.isConnectedToInternet(MainActivity.this)) {
            RegisterActivity.device_token = token;
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        } else
            Toast.makeText(MainActivity.this, "You dont have internet connection!", Toast.LENGTH_LONG).show();
    }

    public void SubmitLogin(View v) {
//        if (isConnectedToInternet())
            new Login().execute();
//        else
//            Toast.makeText(getApplicationContext(), "You dont have internet connection!", Toast.LENGTH_LONG).show();
    }

    private void InitView() {
        txtUser = findViewById(R.id.txtUser);
        txtPass = findViewById(R.id.txtPassword);

        if (PreferenceUtils.GetUser(MainActivity.this) != null) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean isConnectedToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    class Login extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user", txtUser.getText().toString()));
                params.add(new BasicNameValuePair("password", txtPass.getText().toString()));
                params.add(new BasicNameValuePair("device_token", token));
                // getting JSON string from URL
                JSONObject json = null;
                json = jParser.makeHttpRequest(URL, "POST", params);
                // Check your log cat for JSON reponse
                Log.d("Login status: ", URL);
                Log.d("Login status: ", json.toString());
                pDialog.cancel();
                // Checking for SUCCESS TAG
//                int status = json.getInt("status");
//                if (status == 200) {
//                    Log.i("user: ", json.getString("user"));
//                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
//                    PreferenceUtils.SaveUser(json.toString(), MainActivity.this);
//                    startActivity(i);
//                    finish();
//                } else if (status == 404) {
//                    Log.i("status: ", "" + status);
//                }
                return json.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

//        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject json = new JSONObject(s);
                int status = json.getInt("status");
                if (status == 200) {
                    Log.i("user: ", json.getString("user"));
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    PreferenceUtils.SaveUser(json.toString(), MainActivity.this);
                    startActivity(i);
                    finish();
                }
                else if (status == 404) {
                    Toast.makeText(getApplicationContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}