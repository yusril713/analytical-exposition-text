package com.aet.app.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aet.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdapterAnalyse extends ArrayAdapter<String> {
    private Context context;
    private List<String> detail_id, teks, no;
    private ViewHolder holder;
    private HashMap<String, String> textValues = new HashMap<String, String>();
    private List<String> listKeterangan = new ArrayList<>();

    public AdapterAnalyse(@NonNull Context context, List<String> no, List<String> detail_id, List<String> teks) {
        super(context, R.layout.adapter_analyse, detail_id);
        this.context = context;
        this.detail_id = detail_id;
        this.teks = teks;
        this.no = no;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (detail_id.size() > 0) {
            return detail_id.size();
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        boolean convertViewWasNull = false;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.adapter_analyse, parent, false);
            holder = new ViewHolder();
            holder.tvTeks = convertView.findViewById(R.id.tvTeks);
            holder.txtKeterangan = convertView.findViewById(R.id.txtKeterangan);
            holder.txtKeterangan.setVisibility(View.INVISIBLE);
            holder.tvNo = convertView.findViewById(R.id.tvNo);
            holder.radio = convertView.findViewById(R.id.radioGroupAnalyze);
            convertViewWasNull = true;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvNo.setText(no.get(position) + " ");
        holder.tvTeks.setText(teks.get(position));

        View finalConvertView = convertView;
        holder.radio.setOnCheckedChangeListener((group, checkedId) -> {
            holder.radioButton = finalConvertView.findViewById(checkedId);
            if (indexExists(listKeterangan, position))
                listKeterangan.set(position, holder.radioButton.getText().toString());
            else
                listKeterangan.add(holder.radioButton.getText().toString());
//            holder.txtKeterangan.setText(holder.radioButton.getText());
        });
//        if (convertViewWasNull) {
//            holder.txtKeterangan.addTextChangedListener(new GenericTextWatcher(holder.txtKeterangan));
//        }
//        holder.txtKeterangan.setTag("theFirstEditTextAtPos:"+position);
        return convertView;
    }

    public boolean indexExists(final List list, final int index) {
        return index >= 0 && index < list.size();
    }

    private class GenericTextWatcher implements TextWatcher {

        private View view;
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {

            String text = editable.toString();
            //save the value for the given tag :
            AdapterAnalyse.this.textValues.put((String) view.getTag(), editable.toString());
        }
    }

    public String GetKeterangan(int position) {
        String res = listKeterangan.get(position);
        if (res == "") {
            res = "";
        }
        return res;
    }

    //you can implement a method like this one for each EditText with the list position as parameter :
    public String GetValueKeterangan(int position){
        //here you need to recreate the id for the first editText
        String result = textValues.get("theFirstEditTextAtPos:"+position);
        if(result ==null)
            result = "";

        return result;
    }

    public class ViewHolder {
        TextView tvNo, tvTeks;
        EditText txtKeterangan;
        RadioGroup radio;
        RadioButton radioButton;
    }
}
