package com.aet.app.settings.reports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aet.app.R;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportEvaluateActivity extends AppCompatActivity {
    DatePickerDialog picker;
    private EditText txtMulai, txtSampai;
    private Spinner spinnerFilter;
    private TextView tvSd;
    private Button btnPrint;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_evaluate);
        getSupportActionBar().hide();
        //permission
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        InitView();
    }

    private void InitView() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v->super.onBackPressed());

        spinnerFilter = findViewById(R.id.spinnerFilter);
        tvSd = findViewById(R.id.tvSd);
        txtMulai = findViewById(R.id.txtMulai);
        txtMulai.setInputType(InputType.TYPE_NULL);
        txtSampai = findViewById(R.id.txtSampai);
        txtSampai.setInputType(InputType.TYPE_NULL);
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    txtMulai.setVisibility(View.INVISIBLE);
                    txtSampai.setVisibility(View.INVISIBLE);
                    tvSd.setVisibility(View.INVISIBLE);
                } else {
                    txtMulai.setVisibility(View.VISIBLE);
                    txtSampai.setVisibility(View.VISIBLE);
                    tvSd.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtMulai.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(ReportEvaluateActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> txtMulai.setText(year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth), year, month, day);
            picker.show();
        });

        txtSampai.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(ReportEvaluateActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> txtSampai.setText(year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth), year, month, day);
            picker.show();
        });

        btnPrint = findViewById(R.id.btnPrint);
        btnPrint.setOnClickListener(v -> {
            new GetReport().execute();
        });
    }

    class GetReport extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;
        private String URL_REPORT = Constants.BASE_URL + "api/report/evaluate";
        private JSONParser jsonParser = new JSONParser();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ReportEvaluateActivity.this);
            progressDialog.setMessage("Wait");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Toast.makeText(ReportEvaluateActivity.this, "PDF sudah dibuat di folder Downloads", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("jenis", spinnerFilter.getSelectedItem().toString()));
            JSONObject json = null;
            if (spinnerFilter.getSelectedItemPosition() == 0) {
                json = jsonParser.makeHttpRequest(URL_REPORT, "GET", params);
                Log.d("report", json.toString());
                try {
                    All(json.toString(), "evaluate_all");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                params.add(new BasicNameValuePair("mulai", txtMulai.getText().toString()));
                params.add(new BasicNameValuePair("sampai", txtSampai.getText().toString()));
                json = jsonParser.makeHttpRequest(URL_REPORT, "GET", params);
                Log.d("report", json.toString());
                try {
                    All(json.toString(), "evaluate_bydate");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            progressDialog.cancel();
            return json.toString();
        }
    }

    private void All(String json, String filename) throws IOException {
        List<String> nis = new ArrayList<>();
        List<String> nama = new ArrayList<>();
        List<String> tanggal = new ArrayList<>();
        List<String> nilai = new ArrayList<>();
        List<String> jawaban = new ArrayList<>();
        List<String> narasi = new ArrayList<>();
        List<String> komentar = new ArrayList<>();

        try {
            JSONObject ob = new JSONObject(json);
            int status = ob.getInt("status");
            if (status == 200) {
                JSONArray arr = ob.getJSONArray("data");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obData = arr.getJSONObject(i);
                    JSONObject obUser = new JSONObject(obData.getString("user"));
                    nis.add(obUser.optString("user"));
                    nama.add(obUser.optString("name"));

                    String[] tgl = obData.optString("updated_at").split("-", 2);
                    tanggal.add(obData.optString("updated_at"));
                    jawaban.add(obData.optString("komentar"));

                    JSONObject obEvaluate = new JSONObject(obData.getString("evaluate"));
                    narasi.add(obEvaluate.optString("narasi"));

                    if (obData.isNull("reply")) {
                        komentar.add("-");
                        nilai.add("-");
                    } else {
                        JSONObject obReply = new JSONObject(obData.getString("reply"));
                        komentar.add(obReply.optString("komentar"));
                        nilai.add(obReply.optString("nilai"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, filename + "_" + new SimpleDateFormat("ddmmyyHHmmss").format(new Date()) + ".pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        pdfDocument.setDefaultPageSize(PageSize.A4.rotate());

        Document document = new Document(pdfDocument);
        document.setMargins(30,30,30,30);
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);

        document.add(new Paragraph("REKAP EVALUATE").setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Narasi"));
        document.add(new Paragraph(narasi.get(0)));
        document.add(new AreaBreak());

        float[] columnWidth = {100, 120, 80, 60, 200, 200};
        Table table = new Table(columnWidth);
        table.setFont(font).setFontSize(10);
        table.addCell(Constants.createCell("Nis", 1, 1, 15));
        table.addCell(Constants.createCell("Nama", 1, 1, 15));
        table.addCell(Constants.createCell("Tanggal", 1, 1, 15));
        table.addCell(Constants.createCell("Nilai", 1, 1, 15));
        table.addCell(Constants.createCell("Jawaban", 1, 1, 15));
        table.addCell(Constants.createCell("Komentar", 1, 1, 15));
        for (int i = 0; i < nis.size(); i++) {
            String[] tgl = tanggal.get(i).split("T", 2);
            table.addCell(Constants.createCell(nis.get(i), 1, 1, 15));
            table.addCell(Constants.createCell(nama.get(i), 1, 1, 15));
            table.addCell(Constants.createCell(tgl[0], 1, 1, 15));
            table.addCell(Constants.createCell(nilai.get(i), 1, 1, 15));
            table.addCell(Constants.createCell(jawaban.get(i), 1, 1, 15));
            table.addCell(Constants.createCell(komentar.get(i), 1, 1, 15));
        }

        document.add(table.setHorizontalAlignment(HorizontalAlignment.CENTER));
        document.close();
    }
}