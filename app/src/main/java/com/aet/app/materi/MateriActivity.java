package com.aet.app.materi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aet.app.R;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;
import com.aet.app.utils.PreferenceUtils;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MateriActivity extends AppCompatActivity {
    MenuItem menuAdd;
    private ProgressDialog progressDialog;
    private String URL_REMEMBER = Constants.BASE_URL + "api/materi/";
    private ArrayList<String> listNama;
    public static ArrayList<String> PDF_URL;
    JSONParser jsonParser = new JSONParser();
    private PDFView pdfView;
    private Button btnLatihan;
    private TextView tvTitle;
    private ImageButton btnBack;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remember);
        getSupportActionBar().hide();
        Init();

        new GetDataRemember().execute();
    }

    private void Init() {
        URL_REMEMBER += getIntent().getStringExtra("materi");

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());

        listNama = new ArrayList<>();
        PDF_URL = new ArrayList<>();
        pdfView = findViewById(R.id.pdfView);
        btnLatihan = findViewById(R.id.btnLatihan);
//        btnLatihan.setOnClickListener(v -> {
//            new AlertDialog.Builder(MateriActivity.this)
//                    .setTitle("Konfirmasi")
//                    .setMessage("Yakin ingin masuk ke latihan soal?")
//                    .setPositiveButton("Yes", ((dialog, which) -> {
//                        Intent intent = new Intent(MateriActivity.this, LatihanActivity.class);
//                        intent.putExtra("materi", getIntent().getStringExtra("materi"));
//                        startActivity(intent);
//                    }))
//                    .setNegativeButton("No", null)
//                    .show();
//
//        });
        btnLatihan.setVisibility(View.INVISIBLE);

        if (getIntent().getStringExtra("materi").equals("APPLY")) {
            img = findViewById(R.id.imageView4);
            img.setImageResource(R.drawable.apply_img);
            img.setAdjustViewBounds(true);
            img.setScaleType(ImageView.ScaleType.FIT_START);
        } else {
            img = findViewById(R.id.imageView4);
            img.setImageResource(R.drawable.remember_img);
            img.setAdjustViewBounds(true);
            img.setScaleType(ImageView.ScaleType.FIT_START);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customize_menu, menu);

        menuAdd = menu.findItem(R.id.menuAdd);

        View actionView = menuAdd.getActionView();
        menuAdd.setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(MateriActivity.this, AddRememberActivity.class);
            startActivity(intent);
            return false;
        });
        return true;
    }

    class GetDataRemember extends AsyncTask<String, String, String> {
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
            JSONObject json = jsonParser.makeHttpRequest(URL_REMEMBER, "GET", params);
            progressDialog.cancel();
            Log.i("data remember", json.toString());
            return json.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                String data = jsonObject.getString("data");
                JSONObject json = new JSONObject(data);
                String pdfUrl = json.getString("file");
                new RetrievePDF().execute(Constants.STORAGE_PATH + pdfUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class RetrievePDF extends AsyncTask<String, Void, InputStream> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            super.onPostExecute(inputStream);
            pdfView.fromStream(inputStream).load();
        }

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                Log.i("PDF URL: ", strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }
    }
}