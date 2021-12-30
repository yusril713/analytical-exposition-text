package com.aet.app.materi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.aet.app.R;
import com.aet.app.adapter.AdapterLatihan;
import com.aet.app.adapter.AdapterSoal;
import com.aet.app.adapter.AdapterTrueFalse;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;
import com.aet.app.utils.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LatihanActivity extends AppCompatActivity {
    private ListView listSoal;
    private ListView listTrueFalse;
    private String URL_LATIHAN = Constants.BASE_URL + "api/latihan/";
    private String userId;
    private ImageButton btnBack;
    private Button btnSelesai;
    private String latihan_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latihan);
        getSupportActionBar().hide();
        InitView();
    }

    private void InitView() {
        URL_LATIHAN += getIntent().getStringExtra("materi") + "/" + Constants.MULTIPLE_CHOICE;
//        URL_LATIHAN += getIntent().getStringExtra("materi") + "/" + Constants.MULTIPLE_CHOICE;
        Log.d("URL", URL_LATIHAN);
        listSoal = findViewById(R.id.listSoal);
        listSoal.bringToFront();
        listTrueFalse = findViewById(R.id.listTrueFalse);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());

        try {
            JSONObject jsonObject = new JSONObject(PreferenceUtils.GetUser(getApplicationContext()));
            JSONObject obUser = new JSONObject(jsonObject.getString("user"));
            userId = obUser.optString("id");
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnSelesai = findViewById(R.id.btnSelesai);
        btnSelesai.setOnClickListener(v-> {
            new AlertDialog.Builder(LatihanActivity.this)
                    .setTitle("Are you sure?")
                    .setMessage("Do you want to submit your excercise?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        if (!latihan_id.equals(null)) {
                            NilaiActivity.latihanId = latihan_id;
                            Intent intent = new Intent(LatihanActivity.this, NilaiActivity.class);
//                            intent.putExtra("latihan_id", latihan_id);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        new SetLatihan().execute();
        new GetLatihan().execute();
//        new GetTrueFalse().execute();
    }

    class SetLatihan extends AsyncTask<String, String, String> {
        private String URL_SET_LATIHAN = Constants.BASE_URL + "api/latihan/post-latihan";
        private JSONParser jsonParser = new JSONParser();

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                Log.d("Soal", s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String user_id = null;
            try {
                JSONObject jsonObject = new JSONObject(PreferenceUtils.GetUser(getApplicationContext()));
                JSONObject obUser = new JSONObject(jsonObject.getString("user"));
                user_id = obUser.optString("id");
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("user_id", user_id));
            params.add(new BasicNameValuePair("materi", getIntent().getStringExtra("materi")));
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_SET_LATIHAN, "POST", params);
            try {
                int status = jsonObject.getInt("status");
                if (status == 200) {
                    return jsonObject.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    class GetLatihan extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;
        private JSONParser jsonParser = new JSONParser();
        private List<String> id, soal, pilA, pilB, pilC, pilD, jenis_soal, nomor;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LatihanActivity.this);
            progressDialog.setMessage("Wait");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();

            id = new ArrayList<>();
            soal = new ArrayList<>();
            pilA = new ArrayList<>();
            pilB = new ArrayList<>();
            pilC = new ArrayList<>();
            pilD = new ArrayList<>();
            nomor = new ArrayList<>();
            jenis_soal = new ArrayList<>();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                latihan_id = jsonObject.getString("latihan_id");
                Log.d("latihanid", latihan_id);
                JSONArray arr = jsonObject.getJSONArray("data");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject job = arr.getJSONObject(i);
                    id.add(job.optString("id"));
                    soal.add(job.optString("soal"));
                    nomor.add(job.optString("nomor"));
                    jenis_soal.add(job.optString("jenis_soal"));

                    if (job.optString("jenis_soal").equals(Constants.MULTIPLE_CHOICE)) {
                        if (!getIntent().getStringExtra("materi").equals("CREATE")) {
                            JSONArray arrPilihan = job.getJSONArray("pilihan");
                            JSONObject jobPil = arrPilihan.getJSONObject(0);
                            pilA.add(jobPil.optString("pilihan"));
                            jobPil = arrPilihan.getJSONObject(1);
                            pilB.add(jobPil.optString("pilihan"));
                            jobPil = arrPilihan.getJSONObject(2);
                            pilC.add(jobPil.optString("pilihan"));
                            jobPil = arrPilihan.getJSONObject(3);
                            pilD.add(jobPil.optString("pilihan"));
                        } else {
    //                        SetAdapter(listId, listSoal);
                        }
                    }
                }
                Log.d("soal", jenis_soal.toString());
                SetAdapter(userId, getIntent().getStringExtra("materi"),
                        id, soal, pilA, pilB, pilC, pilD, latihan_id, jenis_soal, nomor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("user_id", userId));
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_LATIHAN, "GET", params);
            progressDialog.cancel();
            return jsonObject.toString();
        }
    }

    class GetTrueFalse extends AsyncTask<String, String, String> {
        private String URL_TRUE_FALSE = Constants.BASE_URL + "api/latihan/" + getIntent().getStringExtra("materi") + "/" + Constants.TRUE_FALSE;
        private List<String> id, soal;
        private String latihan_id;
        private JSONParser jsonParser = new JSONParser();

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            id = new ArrayList<>();
            soal = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(s);
                latihan_id = jsonObject.getString("latihan_id");
                JSONArray arr = jsonObject.getJSONArray("data");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject job = arr.getJSONObject(i);
                    id.add(job.optString("id"));
                    soal.add(job.optString("soal"));
                }

                Log.d("soal", soal.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            SetAdapter(userId, getIntent().getStringExtra("materi"),
                    id, soal, latihan_id);
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("user_id", userId));
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_TRUE_FALSE, "GET", params);
            return jsonObject.toString();
        }
    }

    public void SetAdapter(String userId, String materi,
                           List id, List soal, List pilA,
                           List pilB, List pilC, List pilD, String latihan_id, List jenis_soal, List nomor) {
        AdapterLatihan adapter = new AdapterLatihan(getApplicationContext(), userId, materi, id, soal, pilA, pilB, pilC, pilD, latihan_id, jenis_soal, nomor);
        listSoal.setAdapter(adapter);
    }

    public void SetAdapter(String userId, String materi,
                           List id, List soal, String latihan_id) {
        AdapterTrueFalse adapter = new AdapterTrueFalse(getApplicationContext(), userId, materi, id, soal, latihan_id);
        listTrueFalse.setAdapter(adapter);
    }
}