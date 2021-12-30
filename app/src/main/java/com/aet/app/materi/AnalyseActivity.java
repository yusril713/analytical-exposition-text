package com.aet.app.materi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aet.app.R;
import com.aet.app.adapter.AdapterAnalyse;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;
import com.aet.app.utils.PreferenceUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnalyseActivity extends AppCompatActivity {
    private ListView listAnalyse;
    private ImageButton btnBack;
    private Button btnSelesai;
    private List<String> teks, keterangan, listId, finalId;
    private String user_id = "";
    View parentView = null;
    AdapterAnalyse adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyse);
        getSupportActionBar().hide();
        InitView();
    }

    private void InitView() {
        listAnalyse = findViewById(R.id.listAnalyse);
        new SetLatihan().execute();
        new GetAnalyse().execute();

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> super.onBackPressed());
        btnSelesai = findViewById(R.id.btnSelesai);
        btnSelesai.setOnClickListener(v -> {
            teks = new ArrayList<>();
            keterangan = new ArrayList<>();
            finalId = new ArrayList<>();
            for (int i = 0; i < listAnalyse.getCount(); i++) {
                parentView = getViewByPosition(i, listAnalyse);
                String teks = ((TextView) parentView
                        .findViewById(R.id.tvTeks)).getText().toString();
//                String ket = ((EditText) parentView
//                        .findViewById(R.id.txtKeterangan)).getText().toString();
//                RadioGroup rg = (RadioGroup) parentView.findViewById(R.id.radioGroupAnalyze);
//                rg.setOnCheckedChangeListener((group, checkedId) -> {
//                    this.keterangan.add(((RadioButton) parentView.findViewById(checkedId)).getText().toString());
//                });
//                this.teks.add(teks);
                this.keterangan.add(adapter.GetKeterangan(i));
                this.finalId.add(listId.get(i));
            }
            Log.d("keterangan", keterangan.toString());
            new PostAnalyse().execute();
        });
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition
                + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    class SetLatihan extends AsyncTask<String, String, String> {
        private String URL_SET_LATIHAN = Constants.BASE_URL + "api/analyse/post-latihan";
        private JSONParser jsonParser = new JSONParser();

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject jsonObject = new JSONObject(PreferenceUtils.GetUser(getApplicationContext()));
                JSONObject obUser = new JSONObject(jsonObject.getString("user"));
                user_id = obUser.optString("id");
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("user_id", user_id));
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_SET_LATIHAN, "POST", params);
            try {
                int status = jsonObject.getInt("status");
                if (status == 200) {
                    return jsonObject.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class GetAnalyse extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;
        private JSONParser jsonParser = new JSONParser();
        private String URL_GET = Constants.BASE_URL + "api/analyse";
        private List<String> listNo, listTeks;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listId = new ArrayList<>();
            listTeks = new ArrayList<>();
            listNo = new ArrayList<>();
            progressDialog = new ProgressDialog(AnalyseActivity.this);
            progressDialog.setMessage("Wait");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray arr = jsonObject.getJSONArray("data");
                String temp = "";
                int no = 0;
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject ob = arr.getJSONObject(i);
                    if (!ob.optString("analyse_id").equals(temp)) {
                        no += 1;
                        listNo.add("" + no);
                    } else
                        listNo.add(" ");
                    listTeks.add(ob.optString("teks"));
                    listId.add(ob.optString("id"));
                    temp = ob.optString("analyse_id");
                }
                SetAdapter(listNo, listId, listTeks);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<>();
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_GET, "GET", params);
            progressDialog.cancel();
            return jsonObject.toString();
        }
    }

    class PostAnalyse extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;
        private JSONParser jsonParser = new JSONParser();
        private String URL_POST = Constants.BASE_URL + "api/analyse/post-detail-latihan";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AnalyseActivity.this);
            progressDialog.setMessage("Wait");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                Toast.makeText(AnalyseActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                String data = jsonObject.getString("data");
                JSONObject jsonData = new JSONObject(data);
                NilaiAnalyseActivity.latihan_id = jsonData.optString("id");
                Intent intent = new Intent(AnalyseActivity.this, NilaiAnalyseActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("user_id", user_id));
            for (int i = 0; i < finalId.size(); i++) {
                params.add(new BasicNameValuePair("detail_analyse_id[" + i + "]", finalId.get(i)));
                params.add(new BasicNameValuePair("keterangan[" + i + "]", keterangan.get(i)));
            }
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_POST, "POST", params);
            progressDialog.cancel();
            return jsonObject.toString();
        }
    }

    private void SetAdapter(List<String> no, List<String> detail_id, List<String> teks) {
        adapter = new AdapterAnalyse(getApplicationContext(), no, detail_id, teks);
        listAnalyse.setAdapter(adapter);
    }
}