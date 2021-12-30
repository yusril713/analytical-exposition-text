package com.aet.app.materi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aet.app.R;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NilaiCreateActivity extends AppCompatActivity {
    private TextView tvNilai, tvKomentar;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nilai_create);
        getSupportActionBar().hide();
        InitView();
    }

    private void InitView() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());
        tvNilai = findViewById(R.id.tvTanggal);
        tvKomentar = findViewById(R.id.tvKomentar);
        new GetNilai().execute();
    }

    class GetNilai extends AsyncTask<String, String, String> {
        private String URL_GET_NILAI = Constants.BASE_URL + "api/create/get-nilai/" + getIntent().getStringExtra("detail_id");
        private JSONParser jsonParser = new JSONParser();
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NilaiCreateActivity.this);
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
                tvKomentar.setText(jsonObject.optString("komentar"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<>();
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_GET_NILAI, "GET", params);
            progressDialog.cancel();
            return jsonObject.toString();
        }
    }
}