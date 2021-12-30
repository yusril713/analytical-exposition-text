package com.aet.app.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.aet.app.R;
import com.aet.app.config.JSONParser;
import com.aet.app.settings.EvaluateActivity;
import com.aet.app.utils.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdapterEvaluate extends ArrayAdapter<String> {
    private List<String> id;
    private List<String> narasi;
    private Context context;
    private AdapterEvaluate adapter;

    ViewHolder holder;
    public AdapterEvaluate(@NonNull Context context, List<String> id, List<String> narasi) {
        super(context, R.layout.adapter_evaluate, id);
        this.context = context;
        this.id = id;
        this.narasi = narasi;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_evaluate, parent, false);
            holder = new ViewHolder();
            holder.tvNo = convertView.findViewById(R.id.tvNo);
            holder.tvNarasi = convertView.findViewById(R.id.tvNarasi);
            holder.btnRemove = convertView.findViewById(R.id.btnRemove);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvNo.setText("" + (position + 1) + ". ");
        holder.tvNarasi.setText(narasi.get(position));
        View finalConvertView = convertView;
        holder.btnRemove.setOnClickListener(v -> {
            new AlertDialog.Builder(finalConvertView.getRootView().getContext())
                    .setIcon(R.drawable.delete)
                    .setTitle("Are you sure?")
                    .setMessage("Do you want to remove this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        new DestroyEvaluate().execute(id.get(position));
                        id.remove(position);
                        narasi.remove(position);
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        return convertView;
    }

    public class ViewHolder {
        private TextView tvNo, tvNarasi;
        private ImageButton btnRemove;
    }

    class DestroyEvaluate extends AsyncTask<String, String, String> {
        private String URL_DESTROY = Constants.BASE_URL + "api/evaluate/";
        private JSONParser jsonParser = new JSONParser();

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String message = jsonObject.getString("message");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String id = strings[0];
            URL_DESTROY += id + "/destroy";
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("_method", Constants.DELETE));
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_DESTROY, "POST", params);
            return jsonObject.toString();
        }
    }
}
