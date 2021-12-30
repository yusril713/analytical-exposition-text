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

public class AdapterPenilaian extends ArrayAdapter<String> {
    private Context context;
    private List<String> create_id, nis, nama, tanggal, nilai, komentar;
    private ViewHolder holder;

    public AdapterPenilaian(@NonNull Context context,
                            List<String> create_id, List<String> nis,
                            List<String> nama, List<String> tanggal,
                            List<String> nilai, List<String> komentar) {
        super(context, R.layout.adapter_penilaian, create_id);
        this.context = context;
        this.create_id = create_id;
        this.nis = nis;
        this.nama = nama;
        this.tanggal = tanggal;
        this.nilai = nilai;
        this.komentar = komentar;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (create_id.size() > 0) {
            return create_id.size();
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_penilaian, parent, false);
            holder = new ViewHolder();
            holder.tvNis = convertView.findViewById(R.id.tvNis);
            holder.tvNama = convertView.findViewById(R.id.tvNama);
            holder.tvTanggal = convertView.findViewById(R.id.tvTanggal);
            holder.tvNilai = convertView.findViewById(R.id.tvNilai);
            holder.tvKomentar = convertView.findViewById(R.id.tvKomentar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvNis.setText(nis.get(position));
        holder.tvNama.setText(nama.get(position));
        holder.tvTanggal.setText(tanggal.get(position));
        holder.tvNilai.setText(nilai.get(position));
        holder.tvKomentar.setText(komentar.get(position));
        return convertView;
    }

    public class ViewHolder {
        TextView tvNis, tvNama, tvTanggal, tvNilai, tvKomentar;
    }
}
