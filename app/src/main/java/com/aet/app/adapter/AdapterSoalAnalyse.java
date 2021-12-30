package com.aet.app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aet.app.R;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdapterSoalAnalyse extends ArrayAdapter<String> {
    private Context context;
    private List<String> analyseId, teks1, teks2, teks3, teks4, teks5,
        keterangan1, keterangan2, keterangan3, keterangan4, keterangan5;
    private AdapterSoalAnalyse adapter;

    ViewHolder holder;
    public AdapterSoalAnalyse(@NonNull Context context,
                              List<String> analyseId, List<String> teks1,
                              List<String> teks2, List<String> teks3,
                              List<String> teks4, List<String> teks5,
                              List<String> keterangan1, List<String> keterangan2,
                              List<String> keterangan3, List<String> keterangan4,
                              List<String> keterangan5) {
        super(context, R.layout.adapter_soal_analyse, analyseId);
        this.analyseId = analyseId;
        this.teks1 = teks1;
        this.teks2 = teks2;
        this.teks3 = teks3;
        this.teks4 = teks4;
        this.teks5 = teks5;
        this.keterangan1 = keterangan1;
        this.keterangan2 = keterangan2;
        this.keterangan3 = keterangan3;
        this.keterangan4 = keterangan4;
        this.keterangan5 = keterangan5;
        this.adapter = this;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (analyseId.size() > 0) {
            return analyseId.size();
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.adapter_soal_analyse, parent, false);
            holder = new ViewHolder();
            holder.tvTeks1 = convertView.findViewById(R.id.tvTeks1);
            holder.tvTeks2 = convertView.findViewById(R.id.tvTeks2);
            holder.tvTeks3 = convertView.findViewById(R.id.tvTeks3);
            holder.tvTeks4 = convertView.findViewById(R.id.tvTeks4);
            holder.tvTeks5 = convertView.findViewById(R.id.tvTeks5);

            holder.tvKet1 = convertView.findViewById(R.id.tvKet1);
            holder.tvKet2 = convertView.findViewById(R.id.tvKet2);
            holder.tvKet3 = convertView.findViewById(R.id.tvKet3);
            holder.tvKet4 = convertView.findViewById(R.id.tvKet4);
            holder.tvKet5 = convertView.findViewById(R.id.tvKet5);

            holder.btnHapus = convertView.findViewById(R.id.btnHapus);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTeks1.setText(teks1.get(position));
        holder.tvTeks2.setText(teks2.get(position));
        holder.tvTeks3.setText(teks3.get(position));
        holder.tvTeks4.setText(teks4.get(position));
        holder.tvTeks5.setText(teks5.get(position));

        holder.tvKet1.setText(keterangan1.get(position));
        holder.tvKet2.setText(keterangan2.get(position));
        holder.tvKet3.setText(keterangan3.get(position));
        holder.tvKet4.setText(keterangan4.get(position));
        holder.tvKet5.setText(keterangan5.get(position));

        View finalConvertView = convertView;
        holder.btnHapus.setOnClickListener(v -> {
            new AlertDialog.Builder(finalConvertView.getRootView().getContext())
                    .setIcon(R.drawable.delete)
                    .setTitle("Are you sure?")
                    .setMessage("Do you want to remove this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        new DestroySoal().execute(analyseId.get(position));
                        analyseId.remove(position);
                        teks1.remove(position);
                        teks2.remove(position);
                        teks3.remove(position);
                        teks4.remove(position);
                        teks5.remove(position);

                        keterangan1.remove(position);
                        keterangan2.remove(position);
                        keterangan3.remove(position);
                        keterangan4.remove(position);
                        keterangan5.remove(position);
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
        return convertView;
    }

    public class ViewHolder {
        private TextView tvTeks1, tvTeks2, tvTeks3, tvTeks4, tvTeks5,
            tvKet1, tvKet2, tvKet3, tvKet4, tvKet5;
        private ImageButton btnHapus;
    }

    class DestroySoal extends AsyncTask<String, String, String> {
        private String URL_DESTROY = Constants.BASE_URL + "api/analyse/soal/destroy/";
        private JSONParser jsonParser = new JSONParser();

        @Override
        protected String doInBackground(String... strings) {
            URL_DESTROY += strings[0];
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("_method", Constants.DELETE));
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_DESTROY, "POST", params);
            return null;
        }
    }
}
