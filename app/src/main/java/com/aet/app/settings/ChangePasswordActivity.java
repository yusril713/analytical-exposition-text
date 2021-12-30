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
import com.aet.app.materi.CreateActivity;
import com.aet.app.utils.Constants;
import com.aet.app.utils.PreferenceUtils;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChangePasswordActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private EditText txtPassword, txtPasswordCofirmation;
    private Button btnSimpan;
    private AwesomeValidation av;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().hide();
        InitView();
    }

    private void InitView() {
        txtPassword = findViewById(R.id.txtPassword);
        txtPasswordCofirmation = findViewById(R.id.txtPasswordConfirmation);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());
        btnSimpan = findViewById(R.id.btnChangePassword);

        av = new AwesomeValidation(ValidationStyle.BASIC);
        av.addValidation(this, R.id.txtPassword, RegexTemplate.NOT_EMPTY, R.string.err_pass);

        btnSimpan.setOnClickListener(v -> {
            if (av.validate()) {
                if (!txtPassword.getText().toString().trim().equals(txtPasswordCofirmation.getText().toString().trim())) {
                    Toast.makeText(ChangePasswordActivity.this, "Password confirmation is not valid", Toast.LENGTH_LONG).show();
                } else {
                    new ChangePassword().execute();
                }
            }
        });
    }

    class ChangePassword extends AsyncTask<String, String, String> {
        private String URL_CHANGE_PASS = Constants.BASE_URL + "api/change-password/";
        private ProgressDialog progressDialog;
        private JSONParser jsonParser = new JSONParser();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ChangePasswordActivity.this);
            progressDialog.setMessage("Wait");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject json = new JSONObject(s);
                int status = json.optInt("status");
                if (status == 200)
                    Toast.makeText(ChangePasswordActivity.this, "Password successfully changed...", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(ChangePasswordActivity.this, "Failed change password. Please try again later...", Toast.LENGTH_LONG).show();
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
            URL_CHANGE_PASS += user_id;
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("_method", "PUT"));
            params.add(new BasicNameValuePair("password", txtPassword.getText().toString().trim()));
            JSONObject json = jsonParser.makeHttpRequest(URL_CHANGE_PASS, "POST", params);
            progressDialog.cancel();
            Log.d("cpass", json.toString());
            return json.toString();
        }
    }
}