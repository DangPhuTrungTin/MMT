package com.example.dangphutrungtin.clientapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import java.util.List;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
public class ListQuesSetAdapter  extends ArrayAdapter<QuestionSet> {
    private Context context;
    Socket msocket=MainClient.mSocket;
    String IDuser;
    public static Intent gotoWaitroom;
    public ListQuesSetAdapter(Context context, int resource, List<QuestionSet> items,String ID) {
        super(context, resource, items);
        this.context = context;
        IDuser=ID;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view =  inflater.inflate(R.layout.layout_questionset, null);
        }
        QuestionSet p = getItem(position);
        if (p != null) {
            TextView idset = (TextView) view.findViewById(R.id.id);
            TextView title=(TextView) view.findViewById(R.id.title);
            Button button7=(Button)view.findViewById(R.id.button7);
            Button PlayButton=(Button)view.findViewById(R.id.PlayButton);
            idset.setText(p.getIDset());
            title.setText(p.getTitle());
            button7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent goToQuestion=new Intent(getContext(),User2.class);
                    goToQuestion.putExtra("IDset",String.valueOf(position));
                    goToQuestion.putExtra("IDowner",IDuser);
                    context.startActivity(goToQuestion); // bam vao question

                }
            });
            PlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    msocket.emit("createroom",IDuser,String.valueOf(position));
                    gotoWaitroom=new Intent(getContext(),WaitRoomActivity.class);
                    gotoWaitroom.putExtra("IDset",String.valueOf(position));
                    gotoWaitroom.putExtra("IDowner",IDuser);
                    gotoWaitroom.putExtra("isUser","true");
                    //context.startActivity(gotoWaitroom);

                }
            });
        }
        return view;
    }

}
