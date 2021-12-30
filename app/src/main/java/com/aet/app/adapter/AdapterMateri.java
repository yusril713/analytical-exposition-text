package com.aet.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aet.app.R;
import com.aet.app.pdf_viewer.PdfViewerActivity;
import com.aet.app.settings.JenisSoalActivity;
import com.aet.app.settings.SoalActivity;

import java.util.ArrayList;

public class AdapterMateri extends ArrayAdapter {
    Context context;
    ViewHolder viewHolder;
    ArrayList<String> allPdf;
    ArrayList<String> pdfUrls;
    ArrayList<String> id;

    public AdapterMateri(@NonNull Context context,
                         ArrayList<String> allPdf,
                         ArrayList<String> pdfUrls,
                         ArrayList<String> id) {
        super(context, R.layout.adapter_materi, allPdf);
        this.context = context;
        this.id = id;
        this.allPdf = allPdf;
        this.pdfUrls = pdfUrls;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_materi, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvFileName = (TextView) convertView.findViewById(R.id.tvMateri);
            viewHolder.btnPreview = (Button) convertView.findViewById(R.id.btnPreview);
//            viewHolder.btnSoal = (Button) convertView.findViewById(R.id.btnLatihan);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvFileName.setText(allPdf.get(position));

        viewHolder.btnPreview.setOnClickListener(v -> {
            Intent intent = new Intent(context, PdfViewerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("pdf_url", pdfUrls.get(position));
            intent.putExtra("title", allPdf.get(position));
            context.startActivity(intent);
        });

//        viewHolder.btnSoal.setOnClickListener(v -> {
//            Intent intent = new Intent(context, JenisSoalActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra("id", id.get(position));
//            intent.putExtra("materi", allPdf.get(position));
//            context.startActivity(intent);
//        });
        return  convertView;
    }

    public class ViewHolder {
        TextView tvFileName;
        Button btnPreview;
    }
}
