package com.aet.app.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aet.app.R;
import com.aet.app.adapter.AdapterSoal;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SoalActivity extends AppCompatActivity {
    private TextView tvMateri;
    private ImageButton btnAdd, btnBack;
    private ProgressDialog progressDialog;
    private ListView listSoal;
    private String URL_SOAL = Constants.BASE_URL + "api/soal/";
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soal);
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
        tvMateri = findViewById(R.id.tvMateri);
        tvMateri.setText(getIntent().getStringExtra("materi"));
        listSoal = findViewById(R.id.listSoal);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            super.onBackPressed();
        });

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> {
            AddSoalActivity.edit = false;
            Intent intent = new Intent(this, AddSoalActivity.class);
            intent.putExtra("id", getIntent().getStringExtra("id"));
            startActivity(intent);
        });
        URL_SOAL += getIntent().getStringExtra("id") + "/" + getIntent().getStringExtra("jenis_soal");
        new GetSoal().execute(getIntent().getStringExtra("jenis_soal"));
    }


    class GetSoal extends AsyncTask<String, String, String> {
        private ArrayList<String>  listId, listSoal, listA, listB, listC, listD, listJawaban, listJenis;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SoalActivity.this);
            progressDialog.setMessage("Wait");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
            listId = new ArrayList<>();
            listSoal = new ArrayList<>();
            listJenis = new ArrayList<>();
            listA = new ArrayList<>();
            listB = new ArrayList<>();
            listC = new ArrayList<>();
            listD = new ArrayList<>();
            listJawaban = new ArrayList<>();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String jenis_soal = null;
            Log.d("URLSOAL", URL_SOAL);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray arr = jsonObject.getJSONArray("data");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject job = arr.getJSONObject(i);
                    Log.d("soal", job.optString("soal"));
                    listJenis.add(job.optString("jenis_soal"));
                    jenis_soal = job.getString("jenis_soal");
                    Log.d("jenissoal", jenis_soal);

                    if (jenis_soal.equals(Constants.MULTIPLE_CHOICE)) {
                        listJawaban.add(job.optString("jawaban"));
                        listId.add(job.optString("id"));
                        listSoal.add(job.optString("soal"));
                        JSONArray arrPilihan = job.getJSONArray("pilihan");
                        JSONObject jobPil = arrPilihan.getJSONObject(0);
                        listA.add(jobPil.optString("pilihan"));
                        jobPil = arrPilihan.getJSONObject(1);
                        listB.add(jobPil.optString("pilihan"));
                        jobPil = arrPilihan.getJSONObject(2);
                        listC.add(jobPil.optString("pilihan"));
                        jobPil = arrPilihan.getJSONObject(3);
                        listD.add(jobPil.optString("pilihan"));
                        SetAdapter(listId, listSoal, listA, listB, listC, listD, listJawaban, listJenis);
                    } else if (jenis_soal.equals(Constants.TRUE_FALSE)) {
                        listId.add(job.optString("id"));
                        listSoal.add(job.optString("soal"));
                        listJawaban.add(job.optString("jawaban"));
                        SetAdapter(listId, listSoal, listJawaban, listJenis);
                    } else if (jenis_soal.equals(Constants.ESSAY)) {
                        listId.add(job.optString("id"));
                        listSoal.add(job.optString("soal"));
                        SetAdapter(listId, listSoal);
                    }

//                    if (!getIntent().getStringExtra("materi").equals("CREATE")) {
//                        listJawaban.add(job.optString("jawaban"));
//
//                        if (jenis_soal.equals(Constants.MULTIPLE_CHOICE)) {
//                            listId.add(job.optString("id"));
//                            listSoal.add(job.optString("soal"));
//                            JSONArray arrPilihan = job.getJSONArray("pilihan");
//                            JSONObject jobPil = arrPilihan.getJSONObject(0);
//                            listA.add(jobPil.optString("pilihan"));
//                            jobPil = arrPilihan.getJSONObject(1);
//                            listB.add(jobPil.optString("pilihan"));
//                            jobPil = arrPilihan.getJSONObject(2);
//                            listC.add(jobPil.optString("pilihan"));
//                            jobPil = arrPilihan.getJSONObject(3);
//                            listD.add(jobPil.optString("pilihan"));
//                            SetAdapter(listId, listSoal, listA, listB, listC, listD, listJawaban, listJenis);
//                        } else if (jenis_soal.equals(Constants.TRUE_FALSE)) {
//                            listId.add(job.optString("id"));
//                            listSoal.add(job.optString("soal"));
////                            SetAdapter(listId, listSoal, listJawaban, listJenis);
//                        }
//                    } else {
//                        listId.add(job.optString("id"));
//                        listSoal.add(job.optString("soal"));
//                        SetAdapter(listId, listSoal);
//                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if (jenis_soal.equals(Constants.MULTIPLE_CHOICE)) {
//                SetAdapter(listId, listSoal, listA, listB, listC, listD, listJawaban, listJenis);
//            } else if (jenis_soal.equals(Constants.TRUE_FALSE)) {
//                SetAdapter(listId, listSoal, listJawaban, listJenis);
//            } else if (jenis_soal.equals(Constants.ESSAY)) {
//                SetAdapter(listId, listSoal);
//            }
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject json = jsonParser.makeHttpRequest(URL_SOAL, "GET", params);
            progressDialog.cancel();
//            Log.i("data soal", json.toString());
            return json.toString();
        }
    }

    public void SetAdapter(ArrayList listId, ArrayList listSoal,
                           ArrayList listA, ArrayList listB,
                           ArrayList listC, ArrayList listD,
                           ArrayList jawaban, ArrayList jenis_soal) {
        AdapterSoal adapter = new AdapterSoal(getApplicationContext(), listId, listSoal, listA, listB, listC, listD, jawaban, jenis_soal);
        AdapterSoal.essay = false;
        this.listSoal.setAdapter(adapter);
    }

    public void SetAdapter(ArrayList listId, ArrayList listSoal) {
        AdapterSoal adapter = new AdapterSoal(getApplicationContext(), listId, listSoal);
        AdapterSoal.essay = true;
        this.listSoal.setAdapter(adapter);
    }

    public void SetAdapter(ArrayList listId, ArrayList listSoal,
                           ArrayList jawaban, ArrayList jenis_soal) {
        AdapterSoal adapter = new AdapterSoal(getApplicationContext(), listId, listSoal, jawaban, jenis_soal);
        AdapterSoal.essay = false;
        this.listSoal.setAdapter(adapter);
    }
}