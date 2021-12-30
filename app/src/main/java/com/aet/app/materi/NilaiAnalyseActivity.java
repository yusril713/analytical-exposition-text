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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NilaiAnalyseActivity extends AppCompatActivity {
    public static String latihan_id;
    private TextView tvNilai;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nilai_analyse);
        getSupportActionBar().hide();
        InitView();
        new GetNilai().execute();
    }

    private void InitView() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());
        tvNilai = findViewById(R.id.tvTanggal); }

    class GetNilai extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;
        private String URL_GET_NILAI = Constants.BASE_URL + "api/analyse/get-nilai/" + latihan_id;
        private JSONParser jsonParser = new JSONParser();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NilaiAnalyseActivity.this);
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
                tvNilai.setText(jsonObject.optString("nilai"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<>();
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_GET_NILAI, "GET", params);
            Log.d("urlnilai", URL_GET_NILAI);
            progressDialog.cancel();
            return jsonObject.toString();
        }
    }
}