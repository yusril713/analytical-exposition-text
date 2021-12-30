package com.aet.app.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aet.app.R;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddEvaluateActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private Button btnSimpan;
    private TextView txtNarasi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_evaluate);
        getSupportActionBar().hide();
        InitView();
    }

    private void InitView() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());

        txtNarasi = findViewById(R.id.txtNarasi);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(v -> {
            if (!txtNarasi.getText().toString().trim().equals("")) {
                new PostNarasi().execute();
            } else {
                Toast.makeText(AddEvaluateActivity.this, "Input narasi...", Toast.LENGTH_LONG).show();
            }
        });
    }

    class PostNarasi extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;
        private JSONParser jsonParser = new JSONParser();
        private String URL_POST = Constants.BASE_URL + "api/evaluate/store";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddEvaluateActivity.this);
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
                String message = jsonObject.getString("message");
                Toast.makeText(AddEvaluateActivity.this, message, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("narasi", txtNarasi.getText().toString().trim()));
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_POST, "POST", params);
            progressDialog.cancel();
            return jsonObject.toString();
        }
    }
}