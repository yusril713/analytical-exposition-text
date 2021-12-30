package com.aet.app.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.aet.app.R;
import com.aet.app.materi.PenialaianCreateActivity;
import com.aet.app.settings.results.ResultAnalyseActivity;
import com.aet.app.settings.results.ResultCreateActivity;
import com.aet.app.settings.results.ResultEvaluateActivity;
import com.aet.app.settings.results.ResultUnderstandActivity;
import com.aet.app.utils.PreferenceUtils;

import org.json.JSONObject;

public class ResultActivity extends AppCompatActivity {
    private Button btnUnderstand, btnEvaluate, btnAnalyse, btnCreate;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().hide();
        InitView();
    }

    private void InitView() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());
        btnUnderstand = findViewById(R.id.btnUnderstand);
        btnUnderstand.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, ResultUnderstandActivity.class);
            startActivity(intent);
        });

        btnEvaluate = findViewById(R.id.btnEvaluate);
        btnEvaluate.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, ResultEvaluateActivity.class);
            startActivity(intent);
        });

        btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(v -> {
            String level = "";
            try {
                JSONObject jsonObject = new JSONObject(PreferenceUtils.GetUser(getApplicationContext()));
                JSONObject obUser = new JSONObject(jsonObject.getString("user"));
                level = obUser.optString("level");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (level.equals("guru")) {
                Intent intent = new Intent(ResultActivity.this, PenialaianCreateActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(ResultActivity.this, ResultCreateActivity.class);
                startActivity(intent);
            }
        });

        btnAnalyse = findViewById(R.id.btnAnalyse);
        btnAnalyse.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, ResultAnalyseActivity.class);
            startActivity(intent);
        });
    }
}