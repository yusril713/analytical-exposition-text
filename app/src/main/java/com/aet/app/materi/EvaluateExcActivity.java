package com.aet.app.materi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.aet.app.R;
import com.aet.app.adapter.AdapterEvaluateExc;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;
import com.aet.app.utils.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EvaluateExcActivity extends AppCompatActivity {
//    private ListView listNarasi;
    private List<String> list;
    private ImageButton btnBack;
    private List<String> id, narasi;
    private Button btnSimpan;
    private TextView tvNarasi;
    private String narasiId;
    private EditText txtKomentar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_exc);
        getSupportActionBar().hide();

        InitView();
        new GetEvaluate().execute();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void InitView() {
//        listNarasi = findViewById(R.id.listNarasi);
        tvNarasi = findViewById(R.id.tvNarasi);
        txtKomentar = findViewById(R.id.txtKomentar);
        txtKomentar.setOnTouchListener((v, event) -> {
            if (txtKomentar.hasFocus()) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_SCROLL:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                }
            }
            return false;
        });
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());

        btnSimpan = findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(v -> {
//            getAllValues();
            new PostEvaluate().execute();
        });
    }

    class GetEvaluate extends AsyncTask<String, String, String> {
        private String URL_GET_EVALUATE = Constants.BASE_URL + "api/evaluate";
        private ProgressDialog progressDialog;
        private JSONParser jsonParser = new JSONParser();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            id = new ArrayList<>();
            narasi = new ArrayList<>();

            progressDialog = new ProgressDialog(EvaluateExcActivity.this);
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
                    for (int i = 0; i < jsonArray.length (); i++) {
                        JSONObject job = jsonArray.getJSONObject(i);
//                        id.add(job.optString("id"));
//                        narasi.add(job.optString("narasi"));

                        narasiId = job.optString("id");
                        tvNarasi.setText(job.optString("narasi"));
                    }

//                    SetAdapter(id, narasi);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<>();
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_GET_EVALUATE, "GET", params);
            progressDialog.cancel();
            return jsonObject.toString();
        }
    }

    class PostEvaluate extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;
        private String URL_POST_EVALUATE = Constants.BASE_URL + "api/detail-evaluate/store";
        private JSONParser jsonParser = new JSONParser();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EvaluateExcActivity.this);
            progressDialog.setMessage("Wait");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            finish();
        }

        @Override
        protected String doInBackground(String... strings) {
            String userId = null;
            try {
                JSONObject jsonObject = new JSONObject(PreferenceUtils.GetUser(getApplicationContext()));
                JSONObject obUser = new JSONObject(jsonObject.getString("user"));
                userId = obUser.optString("id");
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("authorization", Constants.AUTHORIZATION));
            params.add(new BasicNameValuePair("user_id", userId));
            params.add(new BasicNameValuePair("narasi_id", narasiId));
            params.add(new BasicNameValuePair("komentar", txtKomentar.getText().toString()));
            params.add(new BasicNameValuePair("intent", "com.aet.app.settings.results.ResultEvaluateActivity"));
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_POST_EVALUATE, "POST", params);
            progressDialog.cancel();
            Log.d("store", jsonObject.toString());
            return jsonObject.toString();
        }
    }

//    private void SetAdapter(List<String> id, List<String> narasi) {
//        AdapterEvaluateExc adapter = new AdapterEvaluateExc(getApplicationContext(), id, narasi);
//        listNarasi.setAdapter(adapter);
//    }
}