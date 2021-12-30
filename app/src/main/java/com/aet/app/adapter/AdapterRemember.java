package com.aet.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aet.app.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterRemember extends ArrayAdapter<String> {
    Context context;
    ViewHolder viewHolder;
    ArrayList<String> allPdf;

    public AdapterRemember(@NonNull Context context, ArrayList<String> allPdf) {
        super(context, R.layout.adapter_remember, allPdf);
        this.context = context;
        this.allPdf = allPdf;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (allPdf.size() > 0) {
            return allPdf.size();
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_remember, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvFileName = (TextView) convertView.findViewById(R.id.tvNamaMateri);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvFileName.setText(allPdf.get(position));
        return  convertView;
    }

    public class ViewHolder {
        TextView tvFileName;
    }
}
