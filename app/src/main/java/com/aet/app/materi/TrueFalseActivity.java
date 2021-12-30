package com.aet.app.materi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.aet.app.R;
import com.aet.app.utils.Constants;

public class TrueFalseActivity extends AppCompatActivity {
    private ListView listEssay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_false);
    }

    private void IniView() {
        listEssay = findViewById(R.id.listEssay);
    }

    class GetEssay extends AsyncTask<String, String, String> {
        private String URL_GET_ESSAY = Constants.BASE_URL
                + "/latihan/" + getIntent().getStringExtra("materi")
                + "/true_or_false";
        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }
}