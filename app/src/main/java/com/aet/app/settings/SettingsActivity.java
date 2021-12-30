package com.aet.app.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.aet.app.MainActivity;
import com.aet.app.R;
import com.aet.app.adapter.AdapterSettings;
import com.aet.app.config.JSONParser;
import com.aet.app.materi.PenialaianCreateActivity;
import com.aet.app.utils.Constants;
import com.aet.app.utils.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    private ListView listSettings;
    private ArrayList<String> list;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();
        listSettings = (ListView) findViewById(R.id.listSettings);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.super.onBackPressed();
            }
        });
        SetAdapter();
    }

    public void SetAdapter() {
        String level = "";
        list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(PreferenceUtils.GetUser(getApplicationContext()));
            JSONObject obUser = new JSONObject(jsonObject.getString("user"));
            level = obUser.optString("level");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (level.equals("guru")) {
            list.add("THEORY");
            list.add("UNDERSTANDS");
            list.add("EVALUATE");
            list.add("ANALYZE");
            list.add("CREATE");
            list.add("RESULTS");
            list.add("MY ACCOUNT");
            list.add("CHANGE PASSWORD");
            list.add("USER REGISTRATION");
            list.add("REPORTS");
            list.add("LOGOUT");
            AdapterSettings adapter = new AdapterSettings(getApplicationContext(), list);
            listSettings.setAdapter(adapter);
            listSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent;
                    switch (position) {
                        case 0:
                            intent = new Intent(SettingsActivity.this, MateriActivity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent(SettingsActivity.this, JenisSoalActivity.class);
                            intent.putExtra("id", "3");
                            intent.putExtra("materi", "UNDERSTAND");
                            startActivity(intent);
                            break;
                        case 2:
                            intent = new Intent(SettingsActivity.this, EvaluateActivity.class);
                            startActivity(intent);
                            break;
                        case 3:
                            intent = new Intent(SettingsActivity.this, SoalAnalyseActivity.class);
                            startActivity(intent);
                            break;
                        case 4:
                            intent = new Intent(SettingsActivity.this, PenialaianCreateActivity.class);
                            startActivity(intent);
                            break;
                        case 5:
                            intent = new Intent(SettingsActivity.this, ResultActivity.class);
                            startActivity(intent);
                            break;

                        case 6:
                            String user = null, email = null, phone = null, nama = null, jenis_kelamin=null;
                            if (Constants.isConnectedToInternet(getApplicationContext())) {
                                try {
                                    JSONObject jsonObject = new JSONObject(PreferenceUtils.GetUser(getApplicationContext()));
                                    JSONObject obUser = new JSONObject(jsonObject.getString("user"));
                                    user = obUser.optString("user");
                                    nama = obUser.optString("name");
                                    email = obUser.optString("email");
                                    phone = obUser.optString("phone");
                                    jenis_kelamin = obUser.optString("jenis_kelamin");
                                    Log.i("DATAUSER", obUser.toString());

                                    intent = new Intent(SettingsActivity.this, MyAccountActivity.class);
                                    intent.putExtra("user", user);
                                    intent.putExtra("nama", nama);
                                    intent.putExtra("email", email);
                                    intent.putExtra("phone", phone);
                                    intent.putExtra("jenis_kelamin", (jenis_kelamin.equals("L") ? "Laki-Laki" : "Perempuan"));
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "You dont have internet connection...", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 7:
                            intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                            startActivity(intent);
                            break;
                        case 8:
                            intent = new Intent(SettingsActivity.this, UserRegistrationActivity.class);
                            startActivity(intent);
                            break;
                        case 9:
                            intent = new Intent(SettingsActivity.this, ReportActivity.class);
                            startActivity(intent);
                            break;
                        case 10:
                            new AlertDialog.Builder(SettingsActivity.this)
                                    .setIcon(R.drawable.logout)
                                    .setTitle("Are you sure?")
                                    .setMessage("Do you want to logout App?")
                                    .setPositiveButton("Yes", (dialog, which) -> {
                                        if (Constants.isConnectedToInternet(getApplicationContext())) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(PreferenceUtils.GetUser(getApplicationContext()));
                                                JSONObject obUser = new JSONObject(jsonObject.getString("user"));
                                                String user1 = obUser.optString("user");
                                                new Logout().execute(user1);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            PreferenceUtils.clearLoggedInUser(getBaseContext());
                                            Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "You dont have internet connection...", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                            break;
                        default:
                            break;
                    }
                }
            });
        } else {
            list.add("RESULTS");
            list.add("MY ACCOUNT");
            list.add("CHANGE PASSWORD");
            list.add("LOGOUT");

            AdapterSettings adapter = new AdapterSettings(getApplicationContext(), list);
            listSettings.setAdapter(adapter);
            listSettings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent;
                    switch (position) {
                        case 0:
                            intent = new Intent(SettingsActivity.this, ResultActivity.class);
                            startActivity(intent);
                            break;

                        case 1:
                            String user = null, email = null, phone = null, nama = null, jenis_kelamin = null;
                            if (Constants.isConnectedToInternet(getApplicationContext())) {
                                try {
                                    JSONObject jsonObject = new JSONObject(PreferenceUtils.GetUser(getApplicationContext()));
                                    JSONObject obUser = new JSONObject(jsonObject.getString("user"));
                                    user = obUser.optString("user");
                                    nama = obUser.optString("name");
                                    email = obUser.optString("email");
                                    phone = obUser.optString("phone");
                                    jenis_kelamin = obUser.optString("jenis_kelamin");
                                    Log.i("DATAUSER", obUser.toString());

                                    intent = new Intent(SettingsActivity.this, MyAccountActivity.class);
                                    intent.putExtra("user", user);
                                    intent.putExtra("nama", nama);
                                    intent.putExtra("email", email);
                                    intent.putExtra("phone", phone);
                                    intent.putExtra("jenis_kelamin", (jenis_kelamin.equals("L") ? "Laki-Laki" : "Perempuan"));
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "You dont have internet connection...", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 2:
                            intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                            startActivity(intent);
                            break;
                        case 3:
                            new AlertDialog.Builder(SettingsActivity.this)
                                    .setIcon(R.drawable.logout)
                                    .setTitle("Are you sure?")
                                    .setMessage("Do you want to logout App?")
                                    .setPositiveButton("Yes", (dialog, which) -> {
                                        if (Constants.isConnectedToInternet(getApplicationContext())) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(PreferenceUtils.GetUser(getApplicationContext()));
                                                JSONObject obUser = new JSONObject(jsonObject.getString("user"));
                                                String user1 = obUser.optString("user");
                                                new Logout().execute(user1);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            PreferenceUtils.clearLoggedInUser(getBaseContext());
                                            Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "You dont have internet connection...", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    class Logout extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;
        private String URL_LOGOUT = Constants.BASE_URL + "api/logout";
        private JSONParser jParser = new JSONParser();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(SettingsActivity.this);
//            progressDialog.setMessage("Loading...");
//            progressDialog.setIndeterminate(false);
//            progressDialog.setCancelable(false);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("user", strings[0]));
            JSONObject json = jParser.makeHttpRequest(URL_LOGOUT, "POST", params);
//            progressDialog.cancel();
            try {
//                int status = json.getInt("status");
                Log.i("StatusLogout", json.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}