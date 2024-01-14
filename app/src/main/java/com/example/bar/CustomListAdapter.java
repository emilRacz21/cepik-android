package com.example.bar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
    String[][] valueTab;
    CustomListAdapter(Context context, String[][] valueTab){
        this.context = context;
        this.valueTab = valueTab;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return valueTab.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) convertView = inflater.inflate(R.layout.list_view, parent, false);
        TextView marka = convertView.findViewById(R.id.marka);
        marka.setText(valueTab[position][0]);
        TextView model = convertView.findViewById(R.id.model);
        model.setText(valueTab[position][1]);
        TextView sposobProdukcji = convertView.findViewById(R.id.sposobProdukcji);
        sposobProdukcji.setText(valueTab[position][2]);
        TextView rodzPaliw = convertView.findViewById(R.id.rodzPaliw);
        rodzPaliw.setText(valueTab[position][3]);
        TextView powiat = convertView.findViewById(R.id.powiat);
        powiat.setText(valueTab[position][4]);
        return convertView;
    }
}
