package com.aet.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aet.app.R;
import com.snov.timeagolibrary.PrettyTimeAgo;

import java.text.SimpleDateFormat;
import java.util.List;

public class AdapterNotification extends ArrayAdapter<String> {
    private Context context;
    private List<String> id;
    private List<String> title;
    private List<String> body;
    private List<String> time;
    private  ViewHolder holder;

    public AdapterNotification(@NonNull Context context, List<String> id,
                               List<String> title, List<String> body,
                               List<String> time) {
        super(context, R.layout.adapter_notification, id);
        this.id = id;
        this.context = context;
        this.body = body;
        this.time = time;
        this.title = title;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_notification, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = convertView.findViewById(R.id.tvTitle);
            holder.tvBody = convertView.findViewById(R.id.tvBody);
            holder.tvTime = convertView.findViewById(R.id.tvTime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTitle.setText(title.get(position));
        holder.tvBody.setText(body.get(position));

        String str = time.get(position).substring(time.get(position).indexOf("T") + 1, time.get(position).indexOf(".0"));
        try {
            long date = PrettyTimeAgo.timestampToMilli(time.get(position), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
            String TimeAgo = PrettyTimeAgo.getTimeAgo(date);
            holder.tvTime.setText(TimeAgo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  convertView;
    }


    public class ViewHolder {
        TextView tvTitle, tvBody, tvTime;
    }
}
