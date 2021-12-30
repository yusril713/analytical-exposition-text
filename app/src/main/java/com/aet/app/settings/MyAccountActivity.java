package com.aet.app.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.aet.app.MainActivity;
import com.aet.app.R;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;
import com.aet.app.utils.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyAccountActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private Button btnSimpan;
    private EditText txtUser, txtEmail, txtNama, txtNoTelp;
    private Spinner spinJenisKelamin;
    private String URL_UPDATE = Constants.BASE_URL + "api/user/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        getSupportActionBar().hide();
        InitView();
    }

    private void InitView() {
        URL_UPDATE += getIntent().getStringExtra("user") + "/update";
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            super.onBackPressed();
        });

        txtUser = findViewById(R.id.txtUser);
        txtUser.setEnabled(false);
        txtUser.setText(getIntent().getStringExtra("user"));

        txtEmail = findViewById(R.id.txtEmail);
        txtEmail.setText(getIntent().getStringExtra("email"));

        txtNama = findViewById(R.id.tvNama);
        txtNama.setText(getIntent().getStringExtra("nama"));

        txtNoTelp = findViewById(R.id.txtNoTelp);
        txtNoTelp.setText(getIntent().getStringExtra("phone"));

        spinJenisKelamin = findViewById(R.id.spinnerJenisKelamin);
        if (getIntent().getStringExtra("jenis_kelamin").equals("Laki-Laki"))
            spinJenisKelamin.setSelection(1);
        else
            spinJenisKelamin.setSelection(2);

        btnSimpan = findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(v -> {
            new UpdateUser().execute();
        });
    }

    class UpdateUser extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;
        private JSONParser jsonParser = new JSONParser();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MyAccountActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject json = new JSONObject(s);
                int status = json.getInt("status");
                if (status == 200) {
                    PreferenceUtils.clearLoggedInUser(getBaseContext());
                    PreferenceUtils.SaveUser(json.toString(), MyAccountActivity.this);
                    Toast.makeText(MyAccountActivity.this, "Your account successfully updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(MyAccountActivity.this, "Can't update your account. Pleas try again later...", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("_method", Constants.PUT));
            params.add(new BasicNameValuePair("email", txtEmail.getText().toString()));
            params.add(new BasicNameValuePair("name", txtNama.getText().toString()));
            params.add(new BasicNameValuePair("phone", txtNoTelp.getText().toString()));
            params.add(new BasicNameValuePair("jenis_kelamin", spinJenisKelamin.getSelectedItem().toString()));
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_UPDATE, "POST", params);
            progressDialog.cancel();
            Log.d("akun", jsonObject.toString());
            return jsonObject.toString();
        }
    }
}