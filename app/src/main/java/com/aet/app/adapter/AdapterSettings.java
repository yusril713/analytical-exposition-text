package com.aet.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aet.app.R;

import java.util.ArrayList;

public class AdapterSettings extends ArrayAdapter<String> {
    ViewHolder viewHolder;
    Context context;
    ArrayList<String> text;

    public AdapterSettings(@NonNull Context context, ArrayList<String> text) {
        super(context, R.layout.adapter_settings, text);
        this.context = context;
        this.text = text;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (text.size() > 0) {
            return text.size();
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_settings, parent, false);
            viewHolder = new AdapterSettings.ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(R.id.tvSettings);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.imgSettings);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AdapterSettings.ViewHolder) convertView.getTag();
        }
        viewHolder.text.setText(text.get(position));
//        switch (position) {
//            case 0:
//                viewHolder.img.setImageDrawable(convertView.getResources().getDrawable(R.drawable.list));
//                break;
//            case 1:
//                viewHolder.img.setImageDrawable(convertView.getResources().getDrawable(R.drawable.account));
//                break;
//            case 2:
//                viewHolder.img.setImageDrawable(convertView.getResources().getDrawable(R.drawable.cloud));
//                break;
//            case 3:
//                viewHolder.img.setImageDrawable(convertView.getResources().getDrawable(R.drawable.logout));
//                break;
//            default:
//                break;
//        }
        return  convertView;
    }

    public class ViewHolder{
        ImageView img;
        TextView text;
    }
}
