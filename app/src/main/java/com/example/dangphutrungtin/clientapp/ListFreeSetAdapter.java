package com.example.dangphutrungtin.clientapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.Socket;

import java.util.List;

public class ListFreeSetAdapter extends ArrayAdapter<FreeSet> {
    private Context context;
    Socket msocket=MainClient.mSocket;
    public static Intent gotoWaitroom;
    public ListFreeSetAdapter(Context context, int resource, List<FreeSet> items) {
        super(context, resource, items);
        this.context = context;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view =  inflater.inflate(R.layout.layout_freeset, null);
        }
        final FreeSet p = getItem(position);
        if (p != null) {
            TextView name = (TextView) view.findViewById(R.id.ownername);
            TextView title=(TextView) view.findViewById(R.id.title);
            Button PlayButton=(Button)view.findViewById(R.id.PlayButton);

            name.setText("Author: "+p.getOwnername());
            title.setText(p.getTitle());
            PlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    msocket.emit("createroom",p.getIDowner(),p.getIDset());
                    gotoWaitroom=new Intent(getContext(),WaitRoomActivity.class);
                    gotoWaitroom.putExtra("IDset",p.getIDset());
                    gotoWaitroom.putExtra("IDowner",p.getIDowner());
                    gotoWaitroom.putExtra("isUser","true");
                }
            });

        }
        return view;
    }
}