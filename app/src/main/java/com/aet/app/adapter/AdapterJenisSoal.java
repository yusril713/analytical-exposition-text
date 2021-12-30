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

public class AdapterJenisSoal extends ArrayAdapter<String> {
    private Context context;
    private String materiId;
    private List<String> jenis_soal;
    ViewHolder viewHolder;

    public AdapterJenisSoal(@NonNull Context context, String materiId, List<String> jenis_soal) {
        super(context, R.layout.adapter_jenis_soal, jenis_soal);
        this.materiId = materiId;
        this.jenis_soal = jenis_soal;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (jenis_soal.size() > 0) {
            return jenis_soal.size();
        } else {
            return 1;
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_jenis_soal, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvJenisSoal = convertView.findViewById(R.id.tvJenisSoal);
            convertView.setTag(viewHolder);
        }
        String jenis = null;
        switch (jenis_soal.get(position)) {
            case "multiple_choice":
                jenis = "Multiple Choice";
                break;

            case "true_or_false":
                jenis = "True or False";
                break;
            case "essay":
                jenis = "Essay";
                break;
            default:
                break;
        }

        viewHolder.tvJenisSoal.setText(jenis);
        return convertView;
    }

    public class  ViewHolder {
        TextView tvJenisSoal;
    }
}
