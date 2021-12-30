package com.aet.app.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.aet.app.R;
import com.aet.app.adapter.AdapterMateri;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MateriActivity extends AppCompatActivity {
    private ImageButton btnBack, btnEdit;
    private ListView listMateri;
    private ProgressDialog progressDialog;
    private String URL_MATERI = Constants.BASE_URL + "api/materi";
    private ArrayList<String> list;
    private ArrayList<String> listPDF;
    private ArrayList<String> listId;
    JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi);
        getSupportActionBar().hide();
        InitView();
        new GetMateri().execute();
    }

    private void InitView() {
        list = new ArrayList<>();
        listPDF = new ArrayList<>();
        listId = new ArrayList<>();
        listMateri = (ListView) findViewById(R.id.listMateri);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MateriActivity.super.onBackPressed();
            }
        });

        btnEdit = (ImageButton) findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MateriActivity.this, AddMateriActivity.class);
                startActivity(intent);
            }
        });
    }

    class GetMateri extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MateriActivity.this);
            progressDialog.setMessage("Wait");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject json = jsonParser.makeHttpRequest(URL_MATERI, "GET", params);
            progressDialog.cancel();
            Log.i("data remember", json.toString());
            return json.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray arr = jsonObject.getJSONArray("data");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject job = arr.getJSONObject(i);
                    listId.add(job.optString("id"));
                    list.add(job.optString("materi"));
                    listPDF.add(Constants.STORAGE_PATH + job.optString("file"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SetListAdapter(listId, listPDF, list);
        }
    }

    public void SetListAdapter(ArrayList<String> id, ArrayList<String> pdf, ArrayList<String> name) {
        AdapterMateri adapter = new AdapterMateri(getApplicationContext(), name, pdf, id);
        listMateri.setAdapter(adapter);
    }
}