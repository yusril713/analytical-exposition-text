package com.aet.app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aet.app.R;
import com.aet.app.config.JSONParser;
import com.aet.app.settings.AddSoalActivity;
import com.aet.app.utils.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdapterSoal extends ArrayAdapter<String> {
    Context context;
    AdapterSoal adapter;
    ViewHolder viewHolder;
    ArrayList<String> id, soal, pilA, pilB, pilC, pilD, jawaban, jenis_soal;
    JSONParser jsonParser = new JSONParser();
    private String URL_DESTROY = Constants.BASE_URL + "api/soal/";
    public static boolean essay = false;

    public AdapterSoal(@NonNull Context context, ArrayList<String>  id,
                       ArrayList<String>  soal, ArrayList<String>  pilA,
                       ArrayList<String>  pilB, ArrayList<String>  pilC,
                       ArrayList<String>  pilD, ArrayList<String> jawaban, ArrayList<String> jenis_soal) {
        super(context, R.layout.adapter_soal, id);
        this.context = context;
        this.id = id;
        this.soal = soal;
        this.pilA = pilA;
        this.pilB = pilB;
        this.pilC = pilC;
        this.pilD = pilD;
        this.jawaban = jawaban;
        this.jenis_soal = jenis_soal;
        this.adapter = this;
    }

    public AdapterSoal(@NonNull Context context, ArrayList<String> id, ArrayList<String> soal, ArrayList<String> jawaban, ArrayList<String> jenis_soal) {
        super(context, R.layout.adapter_soal, id);
        this.context = context;
        this.id = id;
        this.soal = soal;
        this.jenis_soal = jenis_soal;
        this.jawaban = jawaban;
    }

    public AdapterSoal(@NonNull Context context, ArrayList<String> id, ArrayList<String> soal) {
        super(context, R.layout.adapter_soal, id);
        this.context = context;
        this.id = id;
        this.soal = soal;
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

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_soal, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvNo = convertView.findViewById(R.id.tvNo);
            viewHolder.tvSoal = convertView.findViewById(R.id.tvSoal);
            viewHolder.tvA = convertView.findViewById(R.id.tvA);
            viewHolder.tvB = convertView.findViewById(R.id.tvB);
            viewHolder.tvC = convertView.findViewById(R.id.tvC);
            viewHolder.tvD = convertView.findViewById(R.id.tvD);
            viewHolder.btnEdit = convertView.findViewById(R.id.btnEdit);
            viewHolder.btnHapus = convertView.findViewById(R.id.btnRemove);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvNo.setText("" + (position + 1) + ". ");
        viewHolder.tvSoal.setText(soal.get(position));

        if (!essay) {
            if (jenis_soal.get(position).equals(Constants.MULTIPLE_CHOICE)) {
                viewHolder.tvA.setText("a. " + pilA.get(position));
                viewHolder.tvB.setText("b. " + pilB.get(position));
                viewHolder.tvC.setText("c. " + pilC.get(position));
                viewHolder.tvD.setText("d. " + pilD.get(position));

                if (jawaban.get(position).equals(pilA.get(position)))
                    viewHolder.tvA.setTextColor(context.getResources().getColor(R.color.red));
                else if (jawaban.get(position).equals(pilB.get(position)))
                    viewHolder.tvB.setTextColor(context.getResources().getColor(R.color.red));
                else if (jawaban.get(position).equals(pilC.get(position)))
                    viewHolder.tvC.setTextColor(context.getResources().getColor(R.color.red));
                else if (jawaban.get(position).equals(pilD.get(position)))
                    viewHolder.tvD.setTextColor(context.getResources().getColor(R.color.red));
            } else if (jenis_soal.get(position).equals(Constants.TRUE_FALSE)) {
                viewHolder.tvSoal.setText(soal.get(position) + " - " + jawaban.get(position));
            }

        } else {
            viewHolder.tvA.setVisibility(View.INVISIBLE);
        }
        viewHolder.btnEdit.setOnClickListener(v -> {
            EditSoal(context, position);
        });

        View finalConvertView = convertView;
        viewHolder.btnHapus.setOnClickListener(v -> {
            new AlertDialog.Builder(finalConvertView.getRootView().getContext())
                    .setIcon(R.drawable.delete)
                    .setTitle("Are you sure?")
                    .setMessage("Do you want to remove this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        new DestroySoal().execute(id.get(position));
                        id.remove(position);
                        soal.remove(position);
                        pilA.remove(position);
                        pilB.remove(position);
                        pilC.remove(position);
                        pilD.remove(position);
                        jawaban.remove(position);
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
        return convertView;
    }

    public void EditSoal(Context context, int position) {
        AddSoalActivity.edit = true;
        Intent intent = new Intent(context, AddSoalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id", id.get(position));
        intent.putExtra("soal", soal.get(position));
        intent.putExtra("a", pilA.get(position));
        intent.putExtra("b", pilB.get(position));
        intent.putExtra("c", pilC.get(position));
        intent.putExtra("d", pilD.get(position));
        intent.putExtra("jawaban", jawaban.get(position));
        context.startActivity(intent);
    }

    public class ViewHolder {
        TextView tvSoal, tvA, tvB, tvC, tvD, tvNo;

        ImageButton btnEdit, btnHapus;
    }

    class DestroySoal extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... args0) {
            String id = args0[0];
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("_method", "DELETE"));
            JSONObject json = jsonParser.makeHttpRequest(URL_DESTROY + id + "/destroy", "POST", params);
            try {
                String message = json.getString("message");
                return message;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
