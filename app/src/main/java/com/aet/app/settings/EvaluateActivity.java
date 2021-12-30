package com.aet.app.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;

import com.aet.app.R;
import com.aet.app.adapter.AdapterEvaluate;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EvaluateActivity extends AppCompatActivity {
    private ListView listEvaluate;
    private ImageButton btnBack, btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        getSupportActionBar().hide();
        InitView();
        new GetEvaluate().execute();
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    private void InitView() {
        listEvaluate = findViewById(R.id.listEvaluate);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(EvaluateActivity.this, AddEvaluateActivity.class);
            startActivity(intent);
        });
    }

    class GetEvaluate extends AsyncTask<String, String, String> {
        private String URL_GET_EVALUATE = Constants.BASE_URL + "api/evaluate";
        private JSONParser jsonParser = new JSONParser();
        private ProgressDialog progressDialog;
        private List<String> listId, listNarasi;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            listId = new ArrayList<>();
            listNarasi = new ArrayList<>();

            progressDialog = new ProgressDialog(EvaluateActivity.this);
            progressDialog.setMessage("Wait");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                int status = jsonObject.getInt("status");
                if (status == 200) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject job = jsonArray.getJSONObject(i);
                        listId.add(job.optString("id"));
                        listNarasi.add(job.optString("narasi"));
                    }

                    SetAdapter(listId, listNarasi);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<>();
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_GET_EVALUATE, "GET", params);
            Log.d("narasi", jsonObject.toString());
            progressDialog.cancel();
            return jsonObject.toString();
        }
    }

    private void SetAdapter(List id, List narasi) {
        AdapterEvaluate adapter = new AdapterEvaluate(getApplicationContext(), id, narasi);
        listEvaluate.setAdapter(adapter);
    }
}