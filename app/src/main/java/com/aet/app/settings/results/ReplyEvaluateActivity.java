package com.aet.app.settings.results;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aet.app.R;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;
import com.aet.app.utils.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReplyEvaluateActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private TextView tvNarasi, tvKomentar;
    private EditText txtReply, txtNilai;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_evaluate);
        getSupportActionBar().hide();
        InitView();
    }

    private void InitView() {
        tvNarasi = findViewById(R.id.tvNarasi);
        tvKomentar = findViewById(R.id.tvKomentar);
        txtReply = findViewById(R.id.txtReply);
        txtNilai = findViewById(R.id.txtNilai);

        tvNarasi.setText(getIntent().getStringExtra("narasi"));
        tvKomentar.setText(getIntent().getStringExtra("komentar"));

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> {
            if (!txtReply.getText().toString().equals("")) {
                new PostReply().execute();
            }
        });
    }

    class PostReply extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;
        private String URL_POST_REPLY = Constants.BASE_URL + "api/result/evaluate/post-reply";
        private JSONParser jsonParser = new JSONParser();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ReplyEvaluateActivity.this);
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
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();
        }

        @Override
        protected String doInBackground(String... strings) {
            String userId = null;
            try {
                JSONObject jsonObject = new JSONObject(PreferenceUtils.GetUser(getApplicationContext()));
                JSONObject obUser = new JSONObject(jsonObject.getString("user"));
                userId = obUser.optString("id");
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("authorization", Constants.AUTHORIZATION));
            params.add(new BasicNameValuePair("detail_evaluate_id", getIntent().getStringExtra("detail_evaluate_id")));
            params.add(new BasicNameValuePair("from", userId));
            params.add(new BasicNameValuePair("to", getIntent().getStringExtra("user_id")));
            params.add(new BasicNameValuePair("komentar", txtReply.getText().toString()));
            params.add(new BasicNameValuePair("nilai", txtNilai.getText().toString()));
            params.add(new BasicNameValuePair("intent", "com.aet.app.settings.results.ResultEvaluateActivity"));
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_POST_REPLY, "POST", params);
            return jsonObject.toString();
        }
    }
}