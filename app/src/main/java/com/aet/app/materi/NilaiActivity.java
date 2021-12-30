package com.aet.app.materi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aet.app.R;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NilaiActivity extends AppCompatActivity {
    private TextView tvNilai, tvKeterangan;
    public static String latihanId;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nilai);
        getSupportActionBar().hide();
        InitView();
        new PostNilai().execute();
    }

    private void InitView() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());
        tvNilai = findViewById(R.id.tvTanggal);
        tvKeterangan = findViewById(R.id.tvKeterangan);
    }

    class PostNilai extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;
        private String URL_POST_NILAI = Constants.BASE_URL + "api/post-nilai";
        private JSONParser jsonParser = new JSONParser();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NilaiActivity.this);
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
                Log.d("nilai", jsonObject.toString());
                String nilai = jsonObject.getString("nilai");
                tvNilai.setText(nilai);
//                JSONArray arrBenar = jsonObject.getJSONArray("benar");

                JSONArray arr = jsonObject.getJSONArray("detail");
//                String jenis = "";
                String mcBenar= "Multiple Choice\nCorrect answer: ", mcSalah = "\nWrong answer: ",
                        tfBenar = "\n\nTrue or False\nCorrec answer: ", tfSalah = "\nWrong answer: ";
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obDetail = arr.getJSONObject(i);
                    JSONObject obSoal = new JSONObject(obDetail.getString("soal"));
                    int grade = obDetail.optInt("grade");
                    String jenisSoal = obSoal.optString("jenis_soal");
                    if (jenisSoal.equals("multiple_choice")) {
                        if (grade == 1 ){
                            mcBenar += obSoal.optString("nomor") + " ";
                        } else {
                            mcSalah += obSoal.optString("nomor") + " ";
                        }
                    } else {
                        if (grade == 1 ){
                            tfBenar += obSoal.optString("nomor") + " ";
                        } else {
                            tfSalah += obSoal.optString("nomor") + " ";
                        }
                    }
//                    int grade = obSoal.optInt("grade");
//                    if (grade == 0)
//                    if (!jenis.equals(obSoal.optString("jenis_soal"))) {
//                        if (i != 0) {
//                            str += "\n";
//                        }
//                        str += obSoal.optString("jenis_soal") + ": \nNo. ";
//                    }
//                    str += obSoal.optString("nomor") + " ";
//                    jenis = obSoal.optString("jenis_soal");
                }
                tvKeterangan.setText(mcBenar + mcSalah + tfBenar + tfSalah);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("_method", "PUT"));
            params.add(new BasicNameValuePair("latihan_id", latihanId));
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_POST_NILAI, "POST", params);
            progressDialog.cancel();
            return jsonObject.toString();
        }
    }
}