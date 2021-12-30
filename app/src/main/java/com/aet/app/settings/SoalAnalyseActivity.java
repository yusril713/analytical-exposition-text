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
import com.aet.app.adapter.AdapterSoalAnalyse;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SoalAnalyseActivity extends AppCompatActivity {
    private ImageButton btnBack, btnAdd;
    private ListView listSoalAnalyse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soal_analyse);
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
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(SoalAnalyseActivity.this, AddSoalAnalyseActivity.class);
            startActivity(intent);
        });

        listSoalAnalyse = findViewById(R.id.listSoalAnalyse);
        new GetSoalAnalyse().execute();
    }

    class GetSoalAnalyse extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;
        private String URL_GET_ANALYSE = Constants.BASE_URL + "api/analyse/soal";
        private JSONParser jsonParser = new JSONParser();
        private List<String> analyseId, teks1,
            teks2, teks3, teks4, teks5, keterangan1,
            keterangan2, keterangan3, keterangan4, keterangan5;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            analyseId = new ArrayList<>();
            teks1 = new ArrayList<>();
            teks2 = new ArrayList<>();
            teks3 = new ArrayList<>();
            teks4 = new ArrayList<>();
            teks5 = new ArrayList<>();

            keterangan1 = new ArrayList<>();
            keterangan2 = new ArrayList<>();
            keterangan3 = new ArrayList<>();
            keterangan4 = new ArrayList<>();
            keterangan5 = new ArrayList<>();

            progressDialog = new ProgressDialog(SoalAnalyseActivity.this);
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
                Log.d("analyse", s);
                JSONArray arr = jsonObject.getJSONArray("data");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject ob = arr.getJSONObject(i);

                    analyseId.add(ob.optString("id"));
                    JSONArray arrDetail = ob.getJSONArray("detail");
                    if (arrDetail.length() == 5) {
                        JSONObject obDetail = arrDetail.getJSONObject(0);
                        teks1.add(obDetail.optString("teks"));
                        keterangan1.add(obDetail.optString("keterangan"));
                        obDetail = arrDetail.getJSONObject(1);
                        teks2.add(obDetail.optString("teks"));
                        keterangan2.add(obDetail.optString("keterangan"));
                        obDetail = arrDetail.getJSONObject(2);
                        teks3.add(obDetail.optString("teks"));
                        keterangan3.add(obDetail.optString("keterangan"));
                        obDetail = arrDetail.getJSONObject(3);
                        teks4.add(obDetail.optString("teks"));
                        keterangan4.add(obDetail.optString("keterangan"));
                        obDetail = arrDetail.getJSONObject(4);
                        teks5.add(obDetail.optString("teks"));
                        keterangan5.add(obDetail.optString("keterangan"));
                    } else if (arrDetail.length() == 4) {
                        JSONObject obDetail = arrDetail.getJSONObject(0);
                        teks1.add(obDetail.optString("teks"));
                        keterangan1.add(obDetail.optString("keterangan"));
                        obDetail = arrDetail.getJSONObject(1);
                        teks2.add(obDetail.optString("teks"));
                        keterangan2.add(obDetail.optString("keterangan"));
                        obDetail = arrDetail.getJSONObject(2);
                        teks3.add(obDetail.optString("teks"));
                        keterangan3.add(obDetail.optString("keterangan"));
                        obDetail = arrDetail.getJSONObject(3);
                        teks4.add(obDetail.optString("teks"));
                        keterangan4.add(obDetail.optString("keterangan"));
                        teks5.add("");
                        keterangan5.add("");

                    } else if (arrDetail.length() == 3) {
                        JSONObject obDetail = arrDetail.getJSONObject(0);
                        teks1.add(obDetail.optString("teks"));
                        keterangan1.add(obDetail.optString("keterangan"));
                        obDetail = arrDetail.getJSONObject(1);
                        teks2.add(obDetail.optString("teks"));
                        keterangan2.add(obDetail.optString("keterangan"));
                        obDetail = arrDetail.getJSONObject(2);
                        teks3.add(obDetail.optString("teks"));
                        teks4.add("");
                        keterangan4.add("");
                        teks5.add("");
                        keterangan5.add("");
                    }
                }
                Log.d("analyse", teks1.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            SetAdapter(analyseId, teks1, teks2, teks3, teks4, teks5,
                    keterangan1, keterangan2, keterangan3, keterangan4, keterangan5);
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<>();
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_GET_ANALYSE, "GET", params);
            progressDialog.cancel();
            return jsonObject.toString();
        }
    }

    private void SetAdapter(List<String> analyseId, List<String> teks1,
                            List<String> teks2, List<String> teks3,
                            List<String> teks4, List<String> teks5,
                            List<String> keterangan1, List<String> keterangan2,
                            List<String> keterangan3, List<String> keterangan4,
                            List<String> keterangan5) {
        AdapterSoalAnalyse adapter = new AdapterSoalAnalyse(
                getApplicationContext(), analyseId, teks1, teks2,
                teks3, teks4, teks5, keterangan1, keterangan2,
                keterangan3, keterangan4, keterangan5
        );
        listSoalAnalyse.setAdapter(adapter);
    }
}