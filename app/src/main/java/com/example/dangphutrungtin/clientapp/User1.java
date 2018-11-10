package com.example.dangphutrungtin.clientapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

public class User1 extends AppCompatActivity implements AddQuesSetDialog.AddQuesSetDialogListener{
    Socket msocket=MainClient.mSocket;
    ListView list;
    //static Intent goToQuestion;
    ArrayList<QuestionSet> sets;
    String IDuser;
    String myname;
    public void clear(){
        sets.clear();
    }
    ListQuesSetAdapter adapter;
    String mtitle;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listquestionset);
        Intent intent=getIntent();
        IDuser =intent.getStringExtra("ID");
        myname =intent.getStringExtra("myname");

        //goToQuestion.putExtra("IDowner",IDuser);
        msocket.on("nhanpin",on_nhanpin);
        msocket.on("on_list_ques_set",on_list_ques_set);
        msocket.on("on_add_set",on_add_set);
        msocket.emit("list_ques_set",IDuser);
        list=(ListView)findViewById(R.id.quesset);
        sets=new ArrayList<QuestionSet>();
        adapter= new ListQuesSetAdapter(this,R.layout.layout_questionset,sets,IDuser);
        list.setAdapter(adapter);
    }

    public void Logout(View v){
        msocket.emit("logout",IDuser);
        finish1();
    }
    public void finish1(){
        msocket.off("nhanpin",on_nhanpin);
        msocket.off("on_list_ques_set",on_list_ques_set);
        msocket.off("on_add_set",on_add_set);
        clear();
        super.finish();
    }

    private Emitter.Listener on_nhanpin = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String PIN = args[0].toString();
                    ListQuesSetAdapter.gotoWaitroom.putExtra("PIN",PIN);
                    ListQuesSetAdapter.gotoWaitroom.putExtra("myname",myname);
                    startActivity(ListQuesSetAdapter.gotoWaitroom);
                }
            });
        }
    };
    private Emitter.Listener on_add_set = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Boolean isOK = Boolean.valueOf(args[0].toString());
                    String ID=args[1].toString();
                    if(isOK){
                        Toast.makeText(getApplicationContext(),"Thanh cong",Toast.LENGTH_LONG).show();
                        sets.add(new QuestionSet(ID,mtitle,IDuser));
                        adapter.notifyDataSetChanged();
                    }else
                        Toast.makeText(getApplicationContext(),"Vui long nhap lai",Toast.LENGTH_LONG).show();
                }
            });
        }
    };
    private Emitter.Listener on_list_ques_set = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray data = (JSONArray)args[0];
                    for ( int i=0;i<data.length();i++){
                        try {
                            JSONObject a=(JSONObject) data.get(i);
                            sets.add(new QuestionSet(a.getString("IDset"),a.getString("title"),a.getInt("public"),IDuser));
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    };
    public void AddQuesSet(View v){
        AddQuesSetDialog dialog=new AddQuesSetDialog();
        dialog.show(getSupportFragmentManager(),"add ques set");
    }

    @Override
    public void applyTexts(String title) throws JSONException {
        if(title.isEmpty()){
            Toast.makeText(getApplicationContext(),"Khong the co khoang trong",Toast.LENGTH_LONG).show();
            return;
        }
        mtitle=title;
        String[] a={title,IDuser};
        String mpackage= TextUtils.join("','",a);
        msocket.emit("add set",mpackage,IDuser);
    }
}
