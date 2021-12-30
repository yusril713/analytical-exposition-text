package com.aet.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aet.app.R;

import java.util.List;

public class AdapterResultUnderstand extends ArrayAdapter<String> {
    private Context context;
    private List<String> nis, nama, tanggal, nilai;
    private ViewHolder holder;

    public AdapterResultUnderstand(@NonNull Context context, List<String> nis,
                                   List<String> nama, List<String> tanggal,
                                   List<String> nilai) {
        super(context, R.layout.adapter_result_understand, nis);
        this.context = context;
        this.nis = nis;
        this.nama = nama;
        this.tanggal = tanggal;
        this.nilai = nilai;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (nis.size() > 0) {
            return nis.size();
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_result_understand, parent, false);
            holder = new ViewHolder();
            holder.tvNis = convertView.findViewById(R.id.tvNis);
            holder.tvNama = convertView.findViewById(R.id.tvNama);
            holder.tvTanggal = convertView.findViewById(R.id.tvTanggal);
            holder.tvNilai = convertView.findViewById(R.id.tvNilai);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvNis.setText(nis.get(position));
        holder.tvNama.setText(nama.get(position));
        holder.tvTanggal.setText(tanggal.get(position).
                replace("T", " ").
                replace("000000Z", "").
                replace(".", ""));
        holder.tvNilai.setText(nilai.get(position) + "/100");
        return convertView;
    }

    public class ViewHolder {
        TextView tvNis, tvNama, tvTanggal, tvNilai;
    }
}
