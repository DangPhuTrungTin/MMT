package com.example.dangphutrungtin.clientapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Anonymous extends AppCompatActivity {
    ListView list;
    ArrayList<Question> quesfullcontent;
    ArrayList<String> ques;
    int choice;
    Socket msocket=MainClient.mSocket;
    ArrayAdapter<String> adapter;
    String IDset;
    String IDowner;
    Button See;
    public void clear(){
        quesfullcontent.clear();
        ques.clear();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionsfree);
        msocket.on("on_list_questions",on_list_questions);
        Intent lastIntent=getIntent();
        IDset=lastIntent.getStringExtra("IDset");
        IDowner=lastIntent.getStringExtra("IDowner");
        msocket.emit("list_questions",IDset,IDowner);
        list=(ListView)findViewById(R.id.listques);
        quesfullcontent=new ArrayList<Question>();
        ques=new ArrayList<String>();
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ques);
        //adapter=new ListQuesAdapter(this,R.layout.layout_questions,ques);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuesFreeDiaLog quesFreeDiaLog=new QuesFreeDiaLog();
                choice=position;
                quesFreeDiaLog.question=quesfullcontent.get(position);
                quesFreeDiaLog.show(getSupportFragmentManager(),"quesFreeDiaLog");
            }
        });
    }
    private Emitter.Listener on_list_questions = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray data = (JSONArray)args[0];
                    for ( int i=0;i<data.length();i++){
                        try {
                            JSONObject a=(JSONObject) data.get(i);
                            quesfullcontent.add(new Question(a.getString("IDquestion"),
                                    a.getString("Content"),
                                    a.getString("AnsA"),
                                    a.getString("AnsB"),
                                    a.getString("AnsC"),
                                    a.getString("AnsD"),
                                    Integer.valueOf(a.getString("RightAns"))));
                            ques.add(new String(a.getString("Content")));
                            adapter.notifyDataSetChanged();
                            //Toast.makeText(getApplicationContext(),a.getString("Content"),Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    };
    public void endsetting(View v){
        clear();
        msocket.off("on_list_questions",on_list_questions);
        super.finish();
    }
}