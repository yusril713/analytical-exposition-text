package com.aet.app.settings.results;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;

import com.aet.app.R;
import com.aet.app.adapter.AdapterResultUnderstand;
import com.aet.app.config.JSONParser;
import com.aet.app.settings.AddEvaluateActivity;
import com.aet.app.utils.Constants;
import com.aet.app.utils.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResultUnderstandActivity extends AppCompatActivity {
    private ListView listResult;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_understand);
        getSupportActionBar().hide();

        InitView();
    }

    private void InitView() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());
        listResult = findViewById(R.id.listResultUnderstand);

        new GetResultUnderstand().execute();
    }

    class GetResultUnderstand extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;
        private List<String> nis, nama, tanggal, nilai;
        private String URL_RESULT = Constants.BASE_URL + "api/result/understand";
        private JSONParser jsonParser = new JSONParser();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            nis = new ArrayList<>();
            nama = new ArrayList<>();
            tanggal = new ArrayList<>();
            nilai = new ArrayList<>();

            progressDialog = new ProgressDialog(ResultUnderstandActivity.this);
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
                JSONArray arr = jsonObject.getJSONArray("result");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject job = arr.getJSONObject(i);
                    nilai.add(job.optString("nilai"));
                    tanggal.add(job.optString("updated_at"));
                    String user = job.optString("user");
                    JSONObject jsonUser = new JSONObject(user);
                    nis.add(jsonUser.optString("user"));
                    nama.add(jsonUser.optString("name"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("result", s);
            SetAdapter(nis, nama, tanggal, nilai);
        }

        @Override
        protected String doInBackground(String... strings) {
            String user_id="";
            try {
                JSONObject jsonObject = new JSONObject(PreferenceUtils.GetUser(getApplicationContext()));
                JSONObject obUser = new JSONObject(jsonObject.getString("user"));
                user_id = obUser.optString("id");
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("user_id", user_id));
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_RESULT, "GET", params);
            progressDialog.cancel();
            return jsonObject.toString();
        }
    }

    private void SetAdapter(List<String> nis, List<String> nama,
                            List<String> tanggal, List<String> nilai) {
        AdapterResultUnderstand adapter = new AdapterResultUnderstand(getApplicationContext(),
                nis, nama, tanggal, nilai);
        listResult.setAdapter(adapter);
    }
}