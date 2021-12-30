package com.aet.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aet.app.config.JSONParser;
import com.aet.app.materi.AnalyseActivity;
import com.aet.app.materi.CreateActivity;
import com.aet.app.materi.EvaluateExcActivity;
import com.aet.app.materi.LatihanActivity;
import com.aet.app.materi.MateriActivity;
import com.aet.app.settings.SettingsActivity;
import com.aet.app.utils.Constants;
import com.aet.app.utils.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private TextView badgeNotification;
    private String URL_COUNT_NOTIFICATION = Constants.BASE_URL + "api/notification-counts/";
    private ImageButton btnSettings;
    private TextView tvUser, tvLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#ffffff\">" + getString(R.string.app_name) + "</font>"));
        Init();
//        new GetNotificationCounts().execute();
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    private void Init() {
        btnSettings = findViewById(R.id.btnSettings);
        tvLevel = findViewById(R.id.tvLevel);
        tvUser = findViewById(R.id.tvUser);
        String jk = "";
        try {
            JSONObject jsonObject = new JSONObject(PreferenceUtils.GetUser(getApplicationContext()));
            JSONObject obUser = new JSONObject(jsonObject.getString("user"));
            String user = obUser.optString("id");
            jk = obUser.optString("jenis_kelamin");
            URL_COUNT_NOTIFICATION += user;
            String str = obUser.optString("name");
            String[] arrOfStr = str.split(" ", 2);
            tvUser.setText( arrOfStr[0] + "!");
            tvLevel.setText("you're login as " + (obUser.optString("level").equals("guru") ? "teacher" : "student"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (jk.equals("L"))
            btnSettings.setImageResource(R.drawable.user_male);
        else
            btnSettings.setImageResource(R.drawable.user_female);
        new GetNotificationCounts().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.notification);
        View actionView = menuItem.getActionView();
        badgeNotification = actionView.findViewById(R.id.notification_badge);
        actionView.setOnClickListener(v -> {
            onOptionsItemSelected(menuItem);
            Intent intent = new Intent(HomeActivity.this, NotificationActivity.class);
            startActivity(intent);
        });
        return true;
    }

    private void setupBadge(int counts) {
        badgeNotification.setText(String.valueOf(counts));
    }

    public void OpenRemember(View view) {
        Intent intent = new Intent(this, MateriActivity.class);
        intent.putExtra("materi", "REMEMBER");
        startActivity(intent);
    }

    public void OpenSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void OpenAnalyse(View view) {
        Intent intent = new Intent(this, AnalyseActivity.class);
        intent.putExtra("materi", "ANALYSE");
        startActivity(intent);
    }

    public void OpenUnderstand(View view) {
        Intent intent = new Intent(this, LatihanActivity.class);
        intent.putExtra("materi", "UNDERSTAND");
        startActivity(intent);
    }

    public void OpenEvaluate(View view) {
        Intent intent = new Intent(this, EvaluateExcActivity.class);
        intent.putExtra("materi", "EVALUATE");
        startActivity(intent);
    }

    public void OpenApply(View view) {
        Intent intent = new Intent(this, MateriActivity.class);
        intent.putExtra("materi", "APPLY");
        startActivity(intent);
    }

    public void OpenCreate(View view) {
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }

    class GetNotificationCounts extends AsyncTask<String, String, String> {
        private JSONParser jsonParser = new JSONParser();
        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_COUNT_NOTIFICATION, "GET", params);
            try {
                int counts = jsonObject.getInt("counts");
                if (counts > 0)
                     setupBadge(counts);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}