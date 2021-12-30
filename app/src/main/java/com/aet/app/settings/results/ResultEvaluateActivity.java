package com.aet.app.settings.results;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.aet.app.R;
import com.aet.app.adapter.AdapterResultEvaluate;
import com.aet.app.adapter.AdapterResultUnderstand;
import com.aet.app.config.JSONParser;
import com.aet.app.settings.ResultActivity;
import com.aet.app.utils.Constants;
import com.aet.app.utils.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResultEvaluateActivity extends AppCompatActivity {
    private ListView listResult;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_evaluate);
        getSupportActionBar().hide();
        InitView();
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    private void InitView() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());
        listResult = findViewById(R.id.listResultEvaluate);
        new GetResultEvaluate().execute();
    }

    class GetResultEvaluate extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;
        private List<String> narasi_id, user_id,
            nis, nama, tanggal, narasi, komentar, detail_evaluate_id, reply, nilai;
        private String URL_RESULT = Constants.BASE_URL + "api/result/evaluate";
        private JSONParser jsonParser = new JSONParser();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            narasi_id = new ArrayList<>();
            user_id = new ArrayList<>();
            nis = new ArrayList<>();
            nama = new ArrayList<>();
            tanggal = new ArrayList<>();
            narasi = new ArrayList<>();
            komentar = new ArrayList<>();
            detail_evaluate_id = new ArrayList<>();
            reply = new ArrayList<>();
            nilai = new ArrayList<>();

            progressDialog = new ProgressDialog(ResultEvaluateActivity.this);
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
                    user_id.add(job.optString("user_id"));
                    narasi_id.add(job.optString("evaluate_id"));
                    komentar.add(job.optString("komentar"));
                    tanggal.add(job.optString("updated_at"));
                    detail_evaluate_id.add(job.optString("id"));

                    String user = job.optString("user");
                    JSONObject jsonUser = new JSONObject(user);
                    nis.add(jsonUser.optString("user"));
                    nama.add(jsonUser.optString("name"));

                    String evaluate = job.optString("evaluate");
                    JSONObject jsonNarasi = new JSONObject(evaluate);
                    narasi.add(jsonNarasi.optString("narasi"));

                    if (job.isNull("reply")) {
                        reply.add("-");
                        nilai.add("-");
                    } else {
                        String re = job.optString("reply");
                        JSONObject jsonReply = new JSONObject(re);
                        nilai.add(jsonReply.optString("nilai"));
                        reply.add(jsonReply.optString("komentar"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("narasi", komentar.toString());
            SetAdapter(narasi_id, user_id, nis, nama, tanggal, narasi, komentar, detail_evaluate_id, reply, nilai);
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

    private void SetAdapter(List<String> narasi_id, List<String> user_id,
                            List<String> nis, List<String> nama,
                            List<String> tanggal, List<String> narasi,
                            List<String> komentar, List<String> detail_evaluate_id,
                            List<String> reply, List<String> nilai) {
        AdapterResultEvaluate adapter = new AdapterResultEvaluate(getApplicationContext(),
                narasi_id, user_id, nis, nama, tanggal, narasi, komentar, reply, nilai);
        listResult.setAdapter(adapter);
        listResult.setOnItemClickListener((parent, view, position, id) -> {
            if (reply.get(position).equals("-")) {
                Intent intent = new Intent(ResultEvaluateActivity.this, ReplyEvaluateActivity.class);
                intent.putExtra("user_id", user_id.get(position));
                intent.putExtra("detail_evaluate_id", detail_evaluate_id.get(position));
                intent.putExtra("narasi", narasi.get(position));
                intent.putExtra("komentar", komentar.get(position));
                startActivity(intent);
            }
        });
    }
}