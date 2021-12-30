package com.aet.app.materi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class CreateActivity extends AppCompatActivity {
    private EditText txtThesis, txtArgument1,
            txtArgument2, txtArgument3, txtReiteration;
    private Button btnSubmit;
    private ImageButton btnBack;
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        getSupportActionBar().hide();
        InitView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void InitView() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());

        txtThesis = findViewById(R.id.txtThesis);
        txtThesis.setOnTouchListener((v, event) -> {
            if (txtThesis.hasFocus()) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_SCROLL:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                }
            }
            return false;
        });
        txtArgument1 = findViewById(R.id.txtArgument1);
        txtArgument1.setOnTouchListener((v, event) -> {
            if (txtArgument1.hasFocus()) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_SCROLL:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                }
            }
            return false;
        });

        txtArgument2 = findViewById(R.id.txtArgument2);
        txtArgument2.setOnTouchListener((v, event) -> {
            if (txtArgument2.hasFocus()) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_SCROLL:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                }
            }
            return false;
        });
        txtArgument3 = findViewById(R.id.txtArgument3);
        txtArgument3.setOnTouchListener((v, event) -> {
            if (txtArgument3.hasFocus()) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_SCROLL:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                }
            }
            return false;
        });
        txtReiteration = findViewById(R.id.txtReiteration);
        txtReiteration.setOnTouchListener((v, event) -> {
            if (txtReiteration.hasFocus()) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_SCROLL:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                }
            }
            return false;
        });

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> {
            new PostCreate().execute();
        });
    }

    class PostCreate extends AsyncTask<String, String, String> {
        private String URL_POST = Constants.BASE_URL + "api/create/store";
        private ProgressDialog progressDialog;
        private JSONParser jsonParser = new JSONParser();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CreateActivity.this);
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
                Toast.makeText(CreateActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject jsonObject = new JSONObject(PreferenceUtils.GetUser(getApplicationContext()));
                JSONObject obUser = new JSONObject(jsonObject.getString("user"));
                user_id = obUser.optString("id");
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("authorization", Constants.AUTHORIZATION));
            params.add(new BasicNameValuePair("user_id", user_id));
            params.add(new BasicNameValuePair("thesis", txtThesis.getText().toString()));
            params.add(new BasicNameValuePair("argument_1", txtArgument1.getText().toString()));
            params.add(new BasicNameValuePair("argument_2", txtArgument2.getText().toString()));
            params.add(new BasicNameValuePair("argument_3", txtArgument3.getText().toString()));
            params.add(new BasicNameValuePair("reiteration", txtReiteration.getText().toString()));
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_POST, "POST", params);
            progressDialog.cancel();
            Log.d("create", jsonObject.toString());
            return jsonObject.toString();
        }
    }
}