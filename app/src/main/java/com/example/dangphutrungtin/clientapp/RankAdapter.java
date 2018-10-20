package com.example.dangphutrungtin.clientapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class RankAdapter extends ArrayAdapter<Rank> {
    public RankAdapter(Context context, int resource, List<Rank> items) {
        super(context, resource, items);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view =  inflater.inflate(R.layout.layout_resultlist, null);
        }
        Rank p = getItem(position);
        if (p != null) {
            // Anh xa + Gan gia tri
            TextView rankname = (TextView) view.findViewById(R.id.RankName);
            TextView score=(TextView) view.findViewById(R.id.Score);
            rankname.setText(p.getName());
            score.setText(p.getScore());
        }
        return view;
    }
}
