package com.aet.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.aet.app.adapter.AdapterNotification;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;
import com.aet.app.utils.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    private ListView listNotification;
    private String URL_GET_NOTIFICATION = Constants.BASE_URL + "api/notification/";
//    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().hide();
        InitView();
    }

    private void InitView() {
        listNotification = findViewById(R.id.listNotifiation);
//        btnBack = findViewById(R.id.btnBack);
//        btnBack.setOnClickListener(v -> super.onBackPressed());

        try {
            JSONObject jsonObject = new JSONObject(PreferenceUtils.GetUser(getApplicationContext()));
            JSONObject obUser = new JSONObject(jsonObject.getString("user"));
            URL_GET_NOTIFICATION += obUser.optString("id");

            Log.i("URLNOTIF", URL_GET_NOTIFICATION);
            new GetNotification().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(() -> {
            new GetNotification().execute();
            pullToRefresh.setRefreshing(false);
        });
    }

    class GetNotification extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;
        private JSONParser jsonParser = new JSONParser();
        private List<String> id, title, body, time, intent ,key, val;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            id = new ArrayList<>();
            title = new ArrayList<>();
            body = new ArrayList<>();
            time = new ArrayList<>();
            intent = new ArrayList<>();
            key = new ArrayList<>();
            val = new ArrayList<>();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray arr = jsonObject.getJSONArray("data");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject job = arr.getJSONObject(i);
                    id.add(job.optString("id"));
                    title.add(job.optString("title"));
                    body.add(job.optString("body"));
                    time.add(job.optString("created_at"));

                    if (job.isNull("detail")) {
                        key.add("");
                        val.add("");
                    } else {
                        JSONObject obDetail = new JSONObject(job.getString("detail"));
                        key.add(obDetail.optString("key"));
                        val.add(obDetail.optString("val"));
                    }
                    if (job.isNull("intent"))
                        intent.add(job.optString("-"));
                    else intent.add(job.optString("intent"));
                }
                Log.d("notif", intent.toString());
                SetAdapter(id, title, body, time, intent, key, val);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_GET_NOTIFICATION, "GET", params);
            return jsonObject.toString();
        }
    }

    public void SetAdapter(List<String> id, List<String> title, List<String> body,
                           List<String> time, List<String> intent, List<String> key, List<String> val) {
        AdapterNotification adapter = new AdapterNotification(getApplicationContext(), id, title, body, time);
        listNotification.setAdapter(adapter);
        listNotification.setOnItemClickListener((parent, view, position, id1) -> {
            new ReadNotif().execute(id.get(position));
            if (!intent.get(position).equals("-")) {
                try {
                    Class<?> c = Class.forName(intent.get(position));
                    Intent i = new Intent(NotificationActivity.this, c);
                    if (!key.get(position).equals("")) {
                        i.putExtra(key.get(position), val.get(position));
                    }
                    startActivity(i);
                } catch (ClassNotFoundException e) {}
            }
        });
    }

    class ReadNotif extends AsyncTask<String, String, String> {
        private String URL_READ = Constants.BASE_URL + "api/notification/read/";
        private JSONParser jsonParser = new JSONParser();
        @Override
        protected String doInBackground(String... strings) {
            URL_READ += strings[0];
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("_method", Constants.PUT));
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_READ, "POST", params);
            Log.d("notif", jsonObject.toString());
            return null;
        }
    }
}