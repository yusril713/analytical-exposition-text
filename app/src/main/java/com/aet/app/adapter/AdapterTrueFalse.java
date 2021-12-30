package com.aet.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aet.app.R;

import java.util.List;

public class AdapterTrueFalse extends ArrayAdapter<String> {
    private Context context;
    private List<String> id;
    private List<String> soal;
    private String user_id, materi;
    ViewHolder holder;

    public AdapterTrueFalse(@NonNull Context context, String userId, String materi,
                          List id, List soal, String latihan_id) {
        super(context, R.layout.adapter_essay, soal);
        this.context = context;
        this. id = id;
        this.soal = soal;
        this.user_id = user_id;
        this.materi = materi;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_essay, parent, false);
            holder = new ViewHolder();
            holder.tvNo = convertView.findViewById(R.id.tvNo);
            holder.tvSoal = convertView.findViewById(R.id.tvSoal);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvNo.setText("" + (position + 1) + ". ");
        holder.tvSoal.setText(soal.get(position));
        return convertView;
    }

    public class ViewHolder {
        TextView tvNo, tvSoal;
        RadioGroup radioGroup;
    }
}
