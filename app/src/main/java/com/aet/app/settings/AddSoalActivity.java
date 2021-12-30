package com.aet.app.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aet.app.R;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class AddSoalActivity extends AppCompatActivity {
    public static boolean edit = false;
    private EditText txtSoal, txtA, txtB, txtC, txtD, txtNo;
    private TextView tvPilA, tvPilB, tvPilC, tvPilD, tvJawaban;
    private RadioGroup radioGroup;
    private RadioButton radTrue, radFalse;
    private ImageButton btnBack;
    private Button btnSimpan;
    private ProgressDialog progressDialog;
    private String URL_SIMPAN = Constants.BASE_URL + "api/soal/";
    private String materid_id, jawaban;
    private Spinner spinner, spinnerJenis;
    private AwesomeValidation av;
    JSONParser jParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_soal);
        getSupportActionBar().hide();
        InitView();
    }

    private void InitView() {
        av = new AwesomeValidation(ValidationStyle.BASIC);

        tvPilA = findViewById(R.id.tvPilA);
        tvPilB = findViewById(R.id.tvPilB);
        tvPilC = findViewById(R.id.tvPilC);
        tvPilD = findViewById(R.id.tvPilD);
        tvJawaban = findViewById(R.id.tvJawaban);

        txtNo = findViewById(R.id.txtNomor);
        txtSoal = findViewById(R.id.txtSoal);
        txtA = findViewById(R.id.txtA);
        txtB = findViewById(R.id.txtB);
        txtC = findViewById(R.id.txtC);
        txtD = findViewById(R.id.txtD);
        spinner = findViewById(R.id.spinner2);
        spinnerJenis = findViewById(R.id.spinnerJenis);
        radioGroup = findViewById(R.id.radioGroupTrueFalse);
        radTrue = findViewById(R.id.radioButtonTrue);
        radFalse = findViewById(R.id.radioButtonFalse);
        spinnerJenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    tvPilA.setVisibility(View.VISIBLE);
                    tvPilB.setVisibility(View.VISIBLE);
                    tvPilC.setVisibility(View.VISIBLE);
                    tvPilD.setVisibility(View.VISIBLE);
                    tvJawaban.setVisibility(View.VISIBLE);

                    radioGroup.setVisibility(View.INVISIBLE);
                    radTrue.setVisibility(View.INVISIBLE);
                    radFalse.setVisibility(View.INVISIBLE);

                    txtA.setVisibility(View.VISIBLE);
                    txtB.setVisibility(View.VISIBLE);
                    txtC.setVisibility(View.VISIBLE);
                    txtD.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                }

                else if (position == 1) {
                    tvPilA.setVisibility(View.INVISIBLE);
                    tvPilB.setVisibility(View.INVISIBLE);
                    tvPilC.setVisibility(View.INVISIBLE);
                    tvPilD.setVisibility(View.INVISIBLE);
                    tvJawaban.setVisibility(View.INVISIBLE);

                    txtA.setVisibility(View.INVISIBLE);
                    txtB.setVisibility(View.INVISIBLE);
                    txtC.setVisibility(View.INVISIBLE);
                    txtD.setVisibility(View.INVISIBLE);

                    radioGroup.setVisibility(View.VISIBLE);
                    radTrue.setVisibility(View.VISIBLE);
                    radFalse.setVisibility(View.VISIBLE);

                    spinner.setVisibility(View.INVISIBLE);
                }

                else if (position == 2) {
                    tvPilA.setVisibility(View.INVISIBLE);
                    tvPilB.setVisibility(View.INVISIBLE);
                    tvPilC.setVisibility(View.INVISIBLE);
                    tvPilD.setVisibility(View.INVISIBLE);
                    tvJawaban.setVisibility(View.INVISIBLE);

                    radioGroup.setVisibility(View.INVISIBLE);
                    radTrue.setVisibility(View.INVISIBLE);
                    radFalse.setVisibility(View.INVISIBLE);

                    txtA.setVisibility(View.INVISIBLE);
                    txtB.setVisibility(View.INVISIBLE);
                    txtC.setVisibility(View.INVISIBLE);
                    txtD.setVisibility(View.INVISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        AddValidationToViews();

        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(v -> {
            if (av.validate())
                Simpan();
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            super.onBackPressed();
        });

        if (edit) {
            Edit();
        }
    }

    private void AddValidationToViews() {
        av.addValidation(this, R.id.txtSoal, RegexTemplate.NOT_EMPTY, R.string.err_soal);
        if (spinnerJenis.getSelectedItemPosition() == 0) {
            av.addValidation(this, R.id.txtA, RegexTemplate.NOT_EMPTY, R.string.err_pila);
            av.addValidation(this, R.id.txtB, RegexTemplate.NOT_EMPTY, R.string.err_pilb);
            av.addValidation(this, R.id.txtC, RegexTemplate.NOT_EMPTY, R.string.err_pilc);
            av.addValidation(this, R.id.txtD, RegexTemplate.NOT_EMPTY, R.string.err_pild);
        }
    }

    private void Edit() {
        txtSoal.setText(getIntent().getStringExtra("soal"));
        txtA.setText(getIntent().getStringExtra("a"));
        txtB.setText(getIntent().getStringExtra("b"));
        txtC.setText(getIntent().getStringExtra("c"));
        txtD.setText(getIntent().getStringExtra("d"));

        if (getIntent().getStringExtra("jawaban").equals(txtA.getText().toString().trim()))
            spinner.setSelection(0);
        else if (getIntent().getStringExtra("jawaban").equals(txtB.getText().toString().trim()))
            spinner.setSelection(1);
        else if (getIntent().getStringExtra("jawaban").equals(txtC.getText().toString().trim()))
            spinner.setSelection(2);
        else if (getIntent().getStringExtra("jawaban").equals(txtD.getText().toString().trim()))
            spinner.setSelection(3);
    }

    private void Simpan() {
        switch (spinner.getSelectedItemPosition()){
            case 0:
                jawaban = txtA.getText().toString().trim();
                break;
            case 1:
                jawaban = txtB.getText().toString().trim();
                break;
            case 2:
                jawaban = txtC.getText().toString().trim();
                break;
            case 3:
                jawaban = txtD.getText().toString().trim();
                break;
            default:
                break;
        }

        materid_id = getIntent().getStringExtra("id");
        new SimpanSoal().execute();
    }

    class SimpanSoal extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddSoalActivity.this);
            progressDialog.setMessage("Uploading file to server...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String materi_id = null;
            String materi = null;
            try {
                JSONObject json = new JSONObject(s);
                Toast.makeText(AddSoalActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                int status = json.getInt("status");
                if (status == 200) {
                    JSONObject object = new JSONObject(json.getString("data"));
                    materi_id = object.getString("id");
                    materi = object.getString("materi");
                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
            finish();
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            if (edit)
                params.add(new BasicNameValuePair("_method", "PUT"));
            params.add(new BasicNameValuePair("nomor", txtNo.getText().toString()));
            params.add(new BasicNameValuePair("soal", txtSoal.getText().toString().trim()));

            if (spinnerJenis.getSelectedItemPosition() == 0) {
                params.add(new BasicNameValuePair("jawaban", jawaban));
                params.add(new BasicNameValuePair("jenis_soal", "multiple_choice"));
                params.add(new BasicNameValuePair("pilihan[0]", txtA.getText().toString().trim()));
                params.add(new BasicNameValuePair("pilihan[1]", txtB.getText().toString().trim()));
                params.add(new BasicNameValuePair("pilihan[2]", txtC.getText().toString().trim()));
                params.add(new BasicNameValuePair("pilihan[3]", txtD.getText().toString().trim()));
            } else if (spinnerJenis.getSelectedItemPosition() == 1) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radio = findViewById(selectedId);
                params.add(new BasicNameValuePair("jawaban", radio.getText().toString()));
                params.add(new BasicNameValuePair("jenis_soal", "true_or_false"));
            } else {
                params.add(new BasicNameValuePair("jenis_soal", "essay"));
            }
            // getting JSON string from URL
            JSONObject json = null;
            if (!edit)
                json = jParser.makeHttpRequest(URL_SIMPAN + materid_id + "/simpan", "POST", params);
            else
                json = jParser.makeHttpRequest(URL_SIMPAN + getIntent().getStringExtra("id") + "/update", "POST", params);

            // Check your log cat for JSON reponse
            Log.d("Materi: ", json.toString());
            progressDialog.cancel();
            return json.toString();
        }
    }
}