package com.aet.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private AwesomeValidation av;
    private EditText txtUser, txtPass, txtPassConfirmation,
        txtName, txtPhone, txtEmail;
    private Spinner spinJenisKelamin;
    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    private String REGISTER_URL = Constants.BASE_URL + "api/register";
    public static String device_token = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        av = new AwesomeValidation(ValidationStyle.BASIC);
        InitView();
    }

    public void InitView() {
        txtName = findViewById(R.id.tvNama);
        txtUser = findViewById(R.id.txtUser);
        txtPass = findViewById(R.id.txtPassword);
        txtPassConfirmation = findViewById(R.id.txtPasswordConfirmation);
        txtPhone = findViewById(R.id.txtPhone);
        txtEmail = findViewById(R.id.txtEmail);
        spinJenisKelamin = findViewById(R.id.spinnerJenisKelamin);
        AddValidationToViews();
    }

    public void AddValidationToViews() {
        av.addValidation(this, R.id.tvNama, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.err_name);
        av.addValidation(this, R.id.txtEmail, Patterns.EMAIL_ADDRESS, R.string.err_email);
        av.addValidation(this, R.id.txtPassword, RegexTemplate.NOT_EMPTY, R.string.err_pass);
        av.addValidation(this, R.id.txtPhone, RegexTemplate.TELEPHONE, R.string.err_phone);
    }

    private void submitForm() {
        //first validate the form then move ahead
        //if this becomes true that means validation is successfull
        if (av.validate()) {
            new PostRegistrationForm().execute();
        }
    }

    public void SubmitRegistration(View view) {
        if (!txtPass.getText().toString().equals(txtPassConfirmation.getText().toString())) {
            av.addValidation(this, R.id.txtPasswordConfirmation, RegexTemplate.NOT_EMPTY, R.string.err_pass_conf);
        }
        submitForm();
    }

    public void LoginNow(View view) {
        finish();
    }

    class PostRegistrationForm extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage("Loading");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            finish();
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("user", txtUser.getText().toString()));
            params.add(new BasicNameValuePair("email", txtEmail.getText().toString()));
            params.add(new BasicNameValuePair("name", txtName.getText().toString()));
            params.add(new BasicNameValuePair("password", txtPass.getText().toString()));
            params.add(new BasicNameValuePair("password_confirmation", txtPassConfirmation.getText().toString()));
            params.add(new BasicNameValuePair("phone", txtPhone.getText().toString()));
            params.add(new BasicNameValuePair("device_token", device_token));
            params.add(new BasicNameValuePair("jenis_kelamin", spinJenisKelamin.getSelectedItem().toString()));
            params.add(new BasicNameValuePair("authorization", Constants.AUTHORIZATION));
            params.add(new BasicNameValuePair("intent", "com.aet.app.settings.UserRegistrationActivity"));

            JSONObject json = jParser.makeHttpRequest(REGISTER_URL, "POST", params);
            Log.i("Status register", json.toString());
            pDialog.cancel();
            try {
                String message = json.getString("message");
                return message;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}