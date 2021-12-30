package com.aet.app.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aet.app.R;
import com.aet.app.config.JSONParser;
import com.aet.app.utils.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdapterLatihan extends ArrayAdapter<String> {
    private Context context;
    private String latihan_id;
    private List<String> id, soal, pilA, pilB, pilC, pilD, jenis_soal, nomor;
    private String userId, materi;
    private ViewHolder holder;

    public AdapterLatihan(@NonNull Context context, String userId, String materi,
                          List id, List soal, List pilA,
                          List pilB, List pilC, List pilD, String latihan_id, List jenis_soal, List nomor) {
        super(context, R.layout.adapter_latihan, id);
        this.context = context;
        this.userId = userId;
        this.materi = materi;
        this.id = id;
        this.soal = soal;
        this.pilA = pilA;
        this.pilB = pilB;
        this.pilC = pilC;
        this.pilD = pilD;
        this.latihan_id = latihan_id;
        this.jenis_soal = jenis_soal;
        this.nomor = nomor;
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_latihan, parent, false);
            holder = new ViewHolder();
            holder.tvSoal = convertView.findViewById(R.id.tvSoal);
            holder.radA = convertView.findViewById(R.id.radioA);
            holder.radB = convertView.findViewById(R.id.radioB);
            holder.radC = convertView.findViewById(R.id.radioC);
            holder.radD = convertView.findViewById(R.id.radioD);
            holder.radioGroup = convertView.findViewById(R.id.radioGroup);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (jenis_soal.get(position).equals(Constants.MULTIPLE_CHOICE)) {
            if (nomor.get(position).equals("1")) {
                holder.tvSoal.setText("Untuk lebih memahami " +
                        "konsep Analytical Exposition text, jawablah " +
                        "pertanyaan-pertanyaan berikut ini dengan benar. \n\n" +
                        "Pilihan Ganda\n\n" +
                        nomor.get(position) + ". " + soal.get(position));
            } else
                holder.tvSoal.setText( nomor.get(position) + ". " + soal.get(position));
            holder.radA.setText(pilA.get(position));
            holder.radB.setText(pilB.get(position));
            holder.radC.setText(pilC.get(position));
            holder.radD.setText(pilD.get(position));

            holder.tvSoal.bringToFront();
            holder.radA.bringToFront();
            holder.radB.bringToFront();
            holder.radC.bringToFront();
            holder.radD.bringToFront();


        } else {
            if (nomor.get(position).equals("1")) {
                holder.tvSoal.setText("Tentukan apakah pernyataan berikut ini " +
                        "Benar (True) or Salah (False)\n\n" +
                        nomor.get(position) + ". " + soal.get(position));
            } else
                holder.tvSoal.setText(nomor.get(position) + ". " + soal.get(position));
            holder.radA.setText("True");
            holder.radB.setText("False");
            holder.radC.setVisibility(View.INVISIBLE);
            holder.radD.setVisibility(View.INVISIBLE);
        }
        View finalConvertView = convertView;
        holder.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            holder.radioButton = finalConvertView.findViewById(checkedId);
            //            Log.i("RADIOBUTTON", "" + holder.radioButton.getText().toString());
            new PostJawaban().execute(latihan_id, id.get(position), holder.radioButton.getText().toString());
        });
        return  convertView;
    }


    public class ViewHolder {
        TextView tvSoal;
        RadioGroup radioGroup;
        RadioButton radA, radB, radC, radD, radioButton;
    }

    class PostJawaban extends AsyncTask<String, String, String> {
        private String URL_POST_JAWABAN = Constants.BASE_URL + "api/latihan/post-jawaban";
        private JSONParser jsonParser = new JSONParser();
        @Override
        protected String doInBackground(String... strings) {
            String latihan_id = strings[0];
            String soal_id = strings[1];
            String jawaban = strings[2];

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("latihan_id", latihan_id));
            params.add(new BasicNameValuePair("soal_id", soal_id));
            params.add(new BasicNameValuePair("jawaban", jawaban));
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_POST_JAWABAN, "POST", params);
            return null;
        }
    }

    class GetHistory extends AsyncTask<String, String, String> {
        private String URL_GET_HIST = Constants.BASE_URL + "api/latihan/history/";
        private JSONParser jsonParser = new JSONParser();
        @Override
        protected String doInBackground(String... strings) {
            String latihan_id = strings[0];
            String soal_id = strings[1];
            URL_GET_HIST += latihan_id + "/" + soal_id;
            List<NameValuePair> params = new ArrayList<>();
            JSONObject jsonObject = jsonParser.makeHttpRequest(URL_GET_HIST, "GET", params);
            try {
                String jawaban = jsonObject.getString("jawaban");
                Log.d("jawaban", jawaban);
                holder.radA.setChecked(false);
                holder.radB.setChecked(false);
                holder.radC.setChecked(false);
                holder.radD.setChecked(false);

                if (jawaban.equals(holder.radA.getText().toString()))
                    holder.radA.setChecked(true);
                else if (jawaban.equals(holder.radB.getText().toString()))
                    holder.radB.setChecked(true);
                else if (jawaban.equals(holder.radC.getText().toString()))
                    holder.radC.setChecked(true);
                else if (jawaban.equals(holder.radD.getText().toString()))
                    holder.radD.setChecked(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
