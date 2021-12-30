package com.aet.app.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aet.app.R;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddSoalAnalyseActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private EditText txtThesis, txtArgument1,
        txtArgument2, txtArgument3, txtReiteration;
    private List<String> soal;
    private List<String> keterangan;
    private Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_soal_analyse);
        getSupportActionBar().hide();
        InitView();
    }

    private void InitView() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());

        txtThesis = findViewById(R.id.txtThesis);
        txtArgument1 = findViewById(R.id.txtArgument1);
        txtArgument2 = findViewById(R.id.txtArgument2);
        txtArgument3 = findViewById(R.id.txtArgument3);
        txtReiteration = findViewById(R.id.txtReiteration);

        btnSimpan = findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(v -> {
            soal = new ArrayList<>();
            keterangan = new ArrayList<>();
            if (!txtThesis.getText().toString().equals("")) {
                soal.add(txtThesis.getText().toString());
                keterangan.add("THESIS");
            }

            if (!txtArgument1.getText().toString().equals("")) {
                soal.add(txtArgument1.getText().toString());
                keterangan.add("ARGUMENT 1");
            }

            if (!txtArgument2.getText().toString().equals("")) {
                soal.add(txtArgument2.getText().toString());
                keterangan.add("ARGUMENT 2");
            }

            if (!txtArgument3.getText().toString().equals("")) {
                soal.add(txtArgument3.getText().toString());
                keterangan.add("ARGUMENT 3");
            }

            if (!txtReiteration.getText().toString().equals("")) {
                soal.add(txtReiteration.getText().toString());
                keterangan.add("REITERATION");
            }
            new PostSoal().execute();
        });
    }

    class PostSoal extends AsyncTask<String, String, String> {
        private JSONParser jsonParser = new JSONParser();
        private String URL_POST = Constants.BASE_URL + "api/analyse/soal/post";
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddSoalAnalyseActivity.this);
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
            List<NameValuePair> params = new ArrayList<>();
            String teks = "";
            for (int i = 0; i < soal.size(); i++) {
                teks += soal.get(i) + " ";
                params.add(new BasicNameValuePair("soal[" + i + "]", soal.get(i)));
                params.add(new BasicNameValuePair("keterangan[" + i + "]", keterangan.get(i)));
            }
            params.add(new BasicNameValuePair("teks", teks));

            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_POST, "POST", params);
            progressDialog.cancel();
            Log.d("analyse", soal.toString());
            return jsonObject.toString();
        }
    }
}