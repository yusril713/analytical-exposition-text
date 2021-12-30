package com.aet.app.settings;

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

import com.aet.app.R;
import com.aet.app.adapter.AdapterJenisSoal;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JenisSoalActivity extends AppCompatActivity {
    private ListView listJenisSoal;
    private ImageButton btnBack, btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jenis_soal);
        getSupportActionBar().hide();
        InitView();
        new GetJenisSoal().execute();
    }

    private void InitView() {
        listJenisSoal = findViewById(R.id.listJeniSoal);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> { super.onBackPressed(); });

        btnAdd = findViewById(R.id.btnAdd) ;
        btnAdd.setOnClickListener(v -> {
            AddSoalActivity.edit = false;
            Intent intent = new Intent(this, AddSoalActivity.class);
            intent.putExtra("id", getIntent().getStringExtra("id"));
            startActivity(intent);
        });
    }

    class GetJenisSoal extends AsyncTask<String, String, String> {
        private String URL_JENIS_SOAL = Constants.BASE_URL + "api/jenis-soal/" + getIntent().getStringExtra("id");
        private ProgressDialog progressDialog;
        private List<String> jenisSoal;
        private JSONParser jsonParser = new JSONParser();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            jenisSoal = new ArrayList<>();
            progressDialog = new ProgressDialog(JenisSoalActivity.this);
            progressDialog.setMessage("Wait");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray arr = jsonObject.getJSONArray("data");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject job = arr.getJSONObject(i);
                    Log.d("jenis_soal", job.optString("jenis_soal"));
                    jenisSoal.add(job.optString("jenis_soal"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            SetAdapter(getIntent().getStringExtra("id"), jenisSoal);
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<>();
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_JENIS_SOAL, "GET", params);
            progressDialog.cancel();
//            Log.i("data soal", json.toString());
            return jsonObject.toString();
        }
    }

    private void SetAdapter(String id, List<String> jenisSoal) {
        AdapterJenisSoal adapter = new AdapterJenisSoal(getApplicationContext(), id, jenisSoal);
        listJenisSoal.setAdapter(adapter);
        listJenisSoal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(JenisSoalActivity.this, SoalActivity.class);
                intent.putExtra("id", getIntent().getStringExtra("id"));
                intent.putExtra("materi", getIntent().getStringExtra("materi"));
                intent.putExtra("jenis_soal", jenisSoal.get(position));
                startActivity(intent);
            }
        });
    }
}