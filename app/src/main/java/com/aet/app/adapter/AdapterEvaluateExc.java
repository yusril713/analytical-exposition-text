package com.aet.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aet.app.R;

import java.util.List;

public class AdapterEvaluateExc extends ArrayAdapter {
    private Context context;
    private List<String> id;
    private List<String> narasi;
    ViewHolder holder;

    public AdapterEvaluateExc(@NonNull Context context, List<String> id,
                              List<String> narasi) {
        super(context, R.layout.adapter_evaluate_exc, id);
        this.context = context;
        this.id = id;
        this.narasi = narasi;
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

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.adapter_evaluate_exc, parent, false);
            holder = new ViewHolder();
            holder.tvNarasi = convertView.findViewById(R.id.tvNarasi);
            holder.txtKomentar = convertView.findViewById(R.id.txtKomentar);
            holder.txtKomentar.setOnTouchListener((v, event) -> {
                if (holder.txtKomentar.hasFocus()) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_SCROLL:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            return true;
                    }
                }
                return false;
            });
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvNarasi.setText(narasi.get(position));
        return convertView;
    }

    public class ViewHolder {
        TextView tvNarasi;
        EditText txtKomentar;
    }
}
