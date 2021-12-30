package com.aet.app.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;

import com.aet.app.R;
import com.aet.app.adapter.AdapterUsers;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserRegistrationActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private ListView listUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        getSupportActionBar().hide();
        InitView();
    }

    private void InitView() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());
        listUser = findViewById(R.id.listUser);

        new GetAllUser().execute();
    }

    class GetAllUser extends AsyncTask<String, String, String> {
        private String URL_GET = Constants.BASE_URL + "api/get-all-users";
        private JSONParser jsonParser = new JSONParser();
        private ProgressDialog progressDialog;
        private List<String> id, nis, nama, jenis_kelamin;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            id = new ArrayList<>();
            nis = new ArrayList<>();
            nama = new ArrayList<>();
            jenis_kelamin = new ArrayList<>();
            progressDialog = new ProgressDialog(UserRegistrationActivity.this);
            progressDialog.setMessage("Wait");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject obUser = new JSONObject(s);
                JSONArray arr = obUser.getJSONArray("data");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject ob = arr.getJSONObject(i);
                    id.add(ob.optString("id"));
                    nis.add(ob.optString("user"));
                    nama.add(ob.optString("name"));
                    jenis_kelamin.add(ob.optString("jenis_kelamin"));
                }

                Log.d("user", nis.toString());
                SetAdapter(id, nis, nama, jenis_kelamin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<>();
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_GET, "GET", params);
            progressDialog.cancel();
            return jsonObject.toString();
        }
    }

    private void SetAdapter(List<String> id, List<String> nis, List<String> nama, List<String> jenis_kelamin) {
        AdapterUsers adapter = new AdapterUsers(UserRegistrationActivity.this, id, nis, nama, jenis_kelamin);
        listUser.setAdapter(adapter);
    }
}