package com.aet.app.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
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

public class DetailPenilaianActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private Button btnSubmit;
    private TextView tvNis, tvNama, tvTanggal,
            tvThesis, tvArgument1, tvArgument2, tvArgument3,
            tvReiteration;
    private EditText txtNilai, txtKomentar;
    private String createId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_penilaian);
        getSupportActionBar().hide();
        InitView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void InitView () {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());
        tvNis = findViewById(R.id.tvNis);
        tvNama = findViewById(R.id.tvNama);
        tvTanggal = findViewById(R.id.tvTanggal);
        tvThesis = findViewById(R.id.tvThesis);
        tvArgument1 = findViewById(R.id.tvArgument1);
        tvArgument2 = findViewById(R.id.tvArgument2);
        tvArgument3 = findViewById(R.id.tvArgument3);
        tvReiteration = findViewById(R.id.tvReiteration);
        txtNilai = findViewById(R.id.txtNilai);
        txtKomentar = findViewById(R.id.txtKomentar);
        txtKomentar.setOnTouchListener((v, event) -> {
            if (txtKomentar.hasFocus()) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_SCROLL:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                }
            }
            return false;
        });

        tvNis.setText(getIntent().getStringExtra("nis"));
        tvNama.setText(getIntent().getStringExtra("nama"));
        tvTanggal.setText(getIntent().getStringExtra("tanggal"));
        tvThesis.setText(getIntent().getStringExtra("thesis"));
        tvArgument1.setText(getIntent().getStringExtra("argument1"));
        tvArgument2.setText(getIntent().getStringExtra("argument2"));
        tvArgument3.setText(getIntent().getStringExtra("argument3"));
        tvReiteration.setText(getIntent().getStringExtra("reiteration"));
        if (!getIntent().getStringExtra("nilai").equals("-")) {
            txtNilai.setText(getIntent().getStringExtra("nilai"));
        }

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> {
            createId = getIntent().getStringExtra("create_id");
            new PostNilai().execute();
        });
    }

    class PostNilai extends AsyncTask<String, String, String> {
        private String URL_POST = Constants.BASE_URL + "api/create/post-nilai";
        private ProgressDialog progressDialog;
        private JSONParser jsonParser = new JSONParser();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DetailPenilaianActivity.this);
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
                Toast.makeText(DetailPenilaianActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String user_id = "";
            try {
                JSONObject jsonObject = new JSONObject(PreferenceUtils.GetUser(getApplicationContext()));
                JSONObject obUser = new JSONObject(jsonObject.getString("user"));
                user_id = obUser.optString("id");
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("_method", Constants.PUT));
            params.add(new BasicNameValuePair("intent", "com.aet.app.materi.NilaiCreateActivity"));
            params.add(new BasicNameValuePair("authorization", Constants.AUTHORIZATION));
            params.add(new BasicNameValuePair("user_admin",  user_id));
            params.add(new BasicNameValuePair("create_id", createId));
            params.add(new BasicNameValuePair("nilai", txtNilai.getText().toString()));
            params.add(new BasicNameValuePair("komentar", txtKomentar.getText().toString()));
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_POST, "POST", params);
            progressDialog.cancel();
            return jsonObject.toString();
        }
    }
}