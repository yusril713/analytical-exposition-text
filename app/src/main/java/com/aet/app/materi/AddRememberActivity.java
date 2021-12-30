package com.aet.app.materi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aet.app.R;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;
import com.aet.app.utils.Ext;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AddRememberActivity extends AppCompatActivity {
    //Image request code
    private int REQ_PDF = 21;
    private String encodedPdf, extension;
    private Button btnSelect, btnUpload;
    private EditText txtFileName, txtNamaMeteri;
    private ProgressDialog pDialog;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    private Uri filePath;
    private String UPLOAD_URL = Constants.BASE_URL + "api/remember/store";
    JSONParser jParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remember);
        //Requesting storage permission
        requestStoragePermission();
        InitView();
    }

    public void InitView() {
        txtFileName = (EditText) findViewById(R.id.txtFileName);
        txtFileName.setEnabled(false);
        txtNamaMeteri = (EditText) findViewById(R.id.txtNamaMateri);
        btnSelect = (Button) findViewById(R.id.btnSelect);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UploadFile().execute();
            }
        });
    }


    //method to show file chooser
    private void showFileChooser() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("application/pdf");
        chooseFile = Intent.createChooser(chooseFile, "Choose pdf file");
        startActivityForResult(chooseFile, REQ_PDF);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_PDF && resultCode == RESULT_OK && data != null) {
            filePath = data.getData();
            extension = Ext.getMimeType(AddRememberActivity.this, filePath);
            txtFileName.setText("" + filePath);
            InputStream inputStream = null;
            try {
                inputStream = AddRememberActivity.this.getContentResolver().openInputStream(filePath);
                byte[] pdfInBytes = new byte[inputStream.available()];
                inputStream.read(pdfInBytes);
                encodedPdf = Base64.encodeToString(pdfInBytes, Base64.DEFAULT);
                Toast.makeText(this, "Document selected", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    class UploadFile extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddRememberActivity.this);
            pDialog.setMessage("Uploading file to server...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject json = new JSONObject(s);
                Toast.makeText(AddRememberActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();

                encodedPdf = null;
                txtNamaMeteri.setText(null);
                txtFileName.setText(null);
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", txtNamaMeteri.getText().toString()));
            params.add(new BasicNameValuePair("pdf", encodedPdf));
            params.add(new BasicNameValuePair("ext", extension));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(UPLOAD_URL, "POST", params);
            // Check your log cat for JSON reponse
            Log.d("Login status: ", json.toString());
            pDialog.cancel();
            return json.toString();
        }
    }
}