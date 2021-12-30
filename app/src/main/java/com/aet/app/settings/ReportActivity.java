package com.aet.app.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.aet.app.R;
import com.aet.app.settings.reports.ReportAnalyzeActivity;
import com.aet.app.settings.reports.ReportCreateActivity;
import com.aet.app.settings.reports.ReportEvaluateActivity;
import com.aet.app.settings.reports.ReportUnderstandActivity;

public class ReportActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private Button btnUnderstand, btnEvaluate, btnAnalyze, btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getSupportActionBar().hide();
        InitView();
    }

    private void InitView() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());

        btnUnderstand = findViewById(R.id.btnUnderstand);
        btnUnderstand.setOnClickListener(v -> {
            Intent intent = new Intent(ReportActivity.this, ReportUnderstandActivity.class);
            startActivity(intent);
        });

        btnEvaluate = findViewById(R.id.btnEvaluate);
        btnEvaluate.setOnClickListener(v -> {
            Intent intent = new Intent(ReportActivity.this, ReportEvaluateActivity.class);
            startActivity(intent);
        });

        btnAnalyze = findViewById(R.id.btnAnalyse);
        btnAnalyze.setOnClickListener(v -> {
            Intent intent = new Intent(ReportActivity.this, ReportAnalyzeActivity.class);
            startActivity(intent);
        });

        btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(v -> {
            Intent intent = new Intent(ReportActivity.this, ReportCreateActivity.class);
            startActivity(intent);
        });
    }
}