package com.example.dangphutrungtin.clientapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ListQuesAdapter extends ArrayAdapter<Question> {
    public ListQuesAdapter(Context context, int resource, List<Question> items) {
        super(context, resource, items);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view =  inflater.inflate(R.layout.layout_questions, null);
        }
        Question p = getItem(position);
        if (p != null) {
            // Anh xa + Gan gia tri
            TextView content = (TextView) view.findViewById(R.id.contentques);
            Button settingbutt=(Button) view.findViewById(R.id.settingquestion);
            content.setText(p.getContent());
            settingbutt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
        return view;
    }
}
