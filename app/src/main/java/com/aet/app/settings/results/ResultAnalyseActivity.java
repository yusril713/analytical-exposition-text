package com.aet.app.settings.results;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.aet.app.R;
import com.aet.app.adapter.AdapterResultAnalyse;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;
import com.aet.app.utils.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResultAnalyseActivity extends AppCompatActivity {
    private ExpandableListView listAnalyse;
    List<String> listGroup;
    HashMap<String, List<String>> listItem;
    AdapterResultAnalyse adapter;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_analyse);
        getSupportActionBar().hide();
        InitView();
    }

    private void InitView() {
        btnBack= findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());
        listAnalyse = findViewById(R.id.ListAnalyse);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();
        new GetResultAnalyse().execute();
    }

    class GetResultAnalyse extends AsyncTask<String, String, String> {
        private String URL_RESULT = Constants.BASE_URL + "api/analyse/result-analyse";
        private JSONParser jsonParser = new JSONParser();
        private ProgressDialog progressDialog;
        private List<String> nis, nama, tanggal, nilai;
        private List<List<String>> soal, keterangan;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nis = new ArrayList<>();
            nama = new ArrayList<>();
            tanggal = new ArrayList<>();
            nilai = new ArrayList<>();

            soal = new ArrayList<>();
            keterangan = new ArrayList<>();
            progressDialog = new ProgressDialog(ResultAnalyseActivity.this);
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
                JSONArray arr = jsonObject.getJSONArray("data");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject ob = arr.getJSONObject(i);
                    JSONObject obUser = new JSONObject(ob.getString("user"));
                    listGroup.add(ob.optString("id"));
                    nis.add(obUser.optString("user"));
                    nama.add(obUser.optString("name"));

                    tanggal.add(ob.optString("updated_at"));
                    nilai.add(ob.optString("nilai"));

                    List<String> detail = new ArrayList<>();
                    JSONArray arrDetail = ob.getJSONArray("detail");
                    soal.add(new ArrayList<>());
                    keterangan.add(new ArrayList<>());
                    for (int j = 0; j < arrDetail.length(); j++) {
                        JSONObject obDetail = arrDetail.getJSONObject(j);
                        detail.add(obDetail.optString("detail_analyse_id"));
                        JSONObject obSoal = new JSONObject(obDetail.getString("soal"));
                        soal.get(i).add(obSoal.optString("teks"));
                        if (obDetail.isNull("keterangan")) {
                            keterangan.get(i).add("-");
                        } else
                            keterangan.get(i).add(obDetail.optString("keterangan"));
                    }
                    Log.d("listDetail", detail.toString());
                    listItem.put(listGroup.get(i), detail);
                }
                Log.d("listItem", listItem.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            adapter = new AdapterResultAnalyse(ResultAnalyseActivity.this, nis, nama, tanggal, nilai, soal, keterangan, listGroup, listItem);
            listAnalyse.setAdapter(adapter);
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
//            Log.d("result", jsonObject.toString());
            return jsonObject.toString();
        }
    }
}