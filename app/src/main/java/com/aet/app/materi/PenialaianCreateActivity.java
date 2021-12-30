package com.aet.app.materi;

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
import android.widget.Spinner;

import com.aet.app.R;
import com.aet.app.adapter.AdapterPenilaian;
import com.aet.app.config.JSONParser;
import com.aet.app.settings.DetailPenilaianActivity;
import com.aet.app.settings.MateriActivity;
import com.aet.app.utils.Constants;
import com.aet.app.utils.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PenialaianCreateActivity extends AppCompatActivity {
    private Spinner spinner;
    private ListView listPenilaian;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penialaian_create);
        getSupportActionBar().hide();
        InitView();
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    private void InitView() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());

        spinner = findViewById(R.id.spinnerPenilaian);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    new GetPenilaian().execute(spinner.getSelectedItem().toString());
                } else if (position == 1) {
                    new GetPenilaian().execute(spinner.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listPenilaian = findViewById(R.id.listPenilaian);
    }

    class GetPenilaian extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;
        private String URL_GET_PENILAIAN = Constants.BASE_URL + "api/create/get-penilaian";
        private JSONParser jsonParser = new JSONParser();
        private List<String> create_id, nis, nama, tanggal,
            thesis, argument1, argument2, argument3, reiteration, nilai, komentar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            create_id = new ArrayList<>();
            nis = new ArrayList<>();
            nama = new ArrayList<>();
            tanggal = new ArrayList<>();
            thesis = new ArrayList<>();
            argument1 = new ArrayList<>();
            argument2 = new ArrayList<>();
            argument3 = new ArrayList<>();
            reiteration = new ArrayList<>();
            nilai = new ArrayList<>();
            komentar = new ArrayList<>();

            progressDialog = new ProgressDialog(PenialaianCreateActivity.this);
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
                JSONArray arr = jsonObject.getJSONArray("data");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject ob = arr.getJSONObject(i);
                    JSONObject obUser = new JSONObject(ob.getString("user"));
                    nis.add(obUser.optString("user"));
                    nama.add(obUser.optString("name"));

                    create_id.add(ob.optString("id"));
                    thesis.add(ob.optString("thesis"));
                    argument1.add(ob.optString("argument_1"));
                    argument2.add(ob.optString("argument_2"));
                    argument3.add(ob.optString("argument_3"));
                    reiteration.add(ob.optString("reiteration"));
                    if (ob.isNull("nilai")) {
                        nilai.add("-");
                        komentar.add("-");
                    } else {
                        nilai.add(ob.optString("nilai"));
                        komentar.add(ob.optString("komentar"));
                    }
                    tanggal.add(ob.optString("created_at").replace("T", " ").replace(".000000Z", ""));
                }

                SetAdapter(create_id, nis, nama, tanggal, thesis, argument1, argument2, argument3, reiteration, nilai, komentar);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
//            URL_GET_PENILAIAN += strings[0];
            String user_id="";
            try {
                JSONObject jsonObject = new JSONObject(PreferenceUtils.GetUser(getApplicationContext()));
                JSONObject obUser = new JSONObject(jsonObject.getString("user"));
                user_id = obUser.optString("id");
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("status", strings[0]));
            params.add(new BasicNameValuePair("user_id", user_id));
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_GET_PENILAIAN, "GET", params);
            progressDialog.cancel();
            Log.d("hasil", jsonObject.toString());
            Log.d("item", strings[0]);
            return jsonObject.toString();
        }
    }

    private void SetAdapter(List<String> create_id, List<String> nis,
                            List<String> nama, List<String> tanggal,
                            List<String> thesis, List<String> argument1,
                            List<String> argument2, List<String> argument3,
                            List<String> reiteration, List<String> nilai, List<String> komentar) {
        AdapterPenilaian adapter = new AdapterPenilaian(getApplicationContext(), create_id, nis, nama, tanggal, nilai, komentar);
        listPenilaian.setAdapter(adapter);
        listPenilaian.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(PenialaianCreateActivity.this, DetailPenilaianActivity.class);
            intent.putExtra("create_id", create_id.get(position));
            intent.putExtra("nis", nis.get(position));
            intent.putExtra("nama", nama.get(position));
            intent.putExtra("tanggal", tanggal.get(position));
            intent.putExtra("thesis", thesis.get(position));
            intent.putExtra("argument1", argument1.get(position));
            intent.putExtra("argument2", argument2.get(position));
            intent.putExtra("argument3", argument3.get(position));
            intent.putExtra("reiteration", reiteration.get(position));
            intent.putExtra("nilai", nilai.get(position));
            startActivity(intent);
        });
    }
}