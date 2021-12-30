package com.aet.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.aet.app.R;

import java.util.HashMap;
import java.util.List;

public class AdapterResultAnalyse extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listGroup, nis, nama, tanggal, nilai;
    private HashMap<String, List<String>> listItem;
    private List<List<String>> soal, keterangan;

    public AdapterResultAnalyse(Context context, List<String> nis, List<String>nama,
                                List<String> tanggal, List<String> nilai,
                                List<List<String>> soal, List<List<String>> keterangan,
                                List<String> lisGroup, HashMap<String,
            List<String>> listItem) {
        this.context = context;
        this.listGroup = lisGroup;
        this.listItem = listItem;
        this.nama = nama;
        this.nilai = nilai;
        this.nis = nis;
        this.tanggal = tanggal;
        this.soal = soal;
        this.keterangan = keterangan;
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listItem.get(this.listGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listItem.get(this.listGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String group = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group_analyse, null);
        }
        TextView tvNis = convertView.findViewById(R.id.tvNis);
        tvNis.setText(nis.get(groupPosition));
        TextView tvNama = convertView.findViewById(R.id.tvNama);
        tvNama.setText(nama.get(groupPosition));
        TextView tvTanggal = convertView.findViewById(R.id.tvTanggal);
        tvTanggal.setText(tanggal.get(groupPosition));
        TextView tvNilai = convertView.findViewById(R.id.tvNilai);
        tvNilai.setText(nilai.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String child = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_analyse, null);
        }
        TextView tvSoal = convertView.findViewById(R.id.tvSoal);
        tvSoal.setText("• " + soal.get(groupPosition).get(childPosition));
        TextView tvKeterangan = convertView.findViewById(R.id.tvKeterangan);
        tvKeterangan.setText(keterangan.get(groupPosition).get(childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
