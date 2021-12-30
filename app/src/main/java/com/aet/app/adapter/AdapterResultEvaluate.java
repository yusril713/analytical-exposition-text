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

public class AdapterResultEvaluate extends ArrayAdapter<String> {
    private Context context;
    private List<String> narasi_id, user_id, nis, nama, tanggal, narasi, komentar, reply, nilai;
    ViewHolder holder;

    public AdapterResultEvaluate(@NonNull Context context, List<String> narasi_id,
                                 List<String> user_id, List<String> nis, List<String> nama,
                                 List<String> tanggal, List<String> narasi,
                                 List<String> komentar, List<String> reply, List<String> nilai) {
        super(context, R.layout.adapter_result_evaluate, narasi_id);
        this.context = context;
        this.narasi_id = narasi_id;
        this.user_id = user_id;
        this.nis = nis;
        this.nama = nama;
        this.tanggal = tanggal;
        this.narasi = narasi;
        this.komentar = komentar;
        this.reply = reply;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_result_evaluate, parent, false);
            holder = new ViewHolder();
            holder.tvNis = convertView.findViewById(R.id.tvNis);
            holder.tvNama = convertView.findViewById(R.id.tvNama);
            holder.tvTanggal = convertView.findViewById(R.id.tvTanggal);
            holder.tvNarasi = convertView.findViewById(R.id.tvNarasi);
            holder.tvKomentar = convertView.findViewById(R.id.tvKomentar);
            holder.tvReply = convertView.findViewById(R.id.tvReply);
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
        holder.tvKomentar.setText(komentar.get(position));
        holder.tvReply.setText(reply.get(position));
        holder.tvNilai.setText(nilai.get(position));

        return convertView;
    }

    public class ViewHolder {
        TextView tvNis, tvNama, tvTanggal, tvNarasi, tvKomentar, tvReply, tvNilai;
    }
}
