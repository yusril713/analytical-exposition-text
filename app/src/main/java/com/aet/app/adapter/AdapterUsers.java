package com.aet.app.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.aet.app.R;
import com.aet.app.config.JSONParser;
import com.aet.app.settings.UserRegistrationActivity;
import com.aet.app.utils.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdapterUsers extends ArrayAdapter<String> {
    private Context context;
    private List<String> id, nis, nama, jenis_kelamin;
    private ViewHolder holder;
    AdapterUsers adapter;

    public AdapterUsers(@NonNull Context context, List<String> id,
                        List<String> nis, List<String> nama,
                        List<String> jenis_kelamin) {
        super(context, R.layout.adapter_users, id);
        this.context = context;
        this.id = id;
        this.nis = nis;
        this.nama = nama;
        this.jenis_kelamin = jenis_kelamin;
        this.adapter = this;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (id.size() > 0) {
            return id.size();
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_users, parent, false);
            holder = new ViewHolder();
            holder.tvNis = convertView.findViewById(R.id.tvNis);
            holder.tvNama = convertView.findViewById(R.id.tvNama);
            holder.tvJenisKelamin = convertView.findViewById(R.id.tvJenisKelamin);
            holder.btnActivate = convertView.findViewById(R.id.btnActivate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvNis.setText(": " + nis.get(position));
        holder.tvNama.setText(": " + nama.get(position));
        holder.tvJenisKelamin.setText(": " + jenis_kelamin.get(position));

        holder.btnActivate.setOnClickListener(v -> {
            new Activate(context).execute(id.get(position));
        });
        return convertView;
    }

    public class ViewHolder {
        TextView tvNis, tvNama, tvJenisKelamin;
        Button btnActivate;
    }

    class Activate extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;
        private String URL_ACTIVATE = Constants.BASE_URL + "api/activate";
        private JSONParser jsonParser = new JSONParser();
        private Context mContext;

        public Activate(Context context) {
            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
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
                Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ((Activity)context).finish();
        }

        @Override
        protected String doInBackground(String... strings) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("_method", Constants.PUT));
            params.add(new BasicNameValuePair("status", "active"));
            params.add(new BasicNameValuePair("user_id", strings[0]));
            params.add(new BasicNameValuePair("authorization", Constants.AUTHORIZATION));
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_ACTIVATE, "POST", params);
            progressDialog.cancel();
            return jsonObject.toString();
        }
    }
}
