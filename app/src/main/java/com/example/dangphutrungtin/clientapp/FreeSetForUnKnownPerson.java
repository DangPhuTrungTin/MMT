package com.example.dangphutrungtin.clientapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FreeSetForUnKnownPerson extends AppCompatActivity {
    Socket msocket=MainClient.mSocket;
    ListView list;
    public void clear(){
        sets.clear();
    }
    ListFreeSetAdapter adapter;
    ArrayList<FreeSet> sets;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.freeset);
        msocket.emit("give free set");
        msocket.on("nhanpin",on_nhanpin);
        msocket.on("receive free set",on_receive);
        list=(ListView)findViewById(R.id.quesset);
        sets=new ArrayList<FreeSet>();
        adapter= new ListFreeSetAdapter(this,R.layout.layout_questionset,sets);
        list.setAdapter(adapter);
    }
    public void LogoutFreeSet(View v){
        clear();
        msocket.off("nhanpin",on_nhanpin);
        msocket.off("receive free set",on_receive);
        finish();
    }
    private Emitter.Listener on_nhanpin = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String PIN = args[0].toString();
                    ListFreeSetAdapter.gotoWaitroom.putExtra("PIN",PIN);
                    ListFreeSetAdapter.gotoWaitroom.putExtra("myname","Anonymous");
                    startActivity(ListFreeSetAdapter.gotoWaitroom);
                }
            });
        }
    };
    private Emitter.Listener on_receive = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray data = (JSONArray)args[0];
                    for ( int i=0;i<data.length();i++){
                        try {
                            JSONObject a=(JSONObject) data.get(i);
                            sets.add(new FreeSet(a.getString("IDowner"),
                                    a.getString("username"),
                                    a.getString("IDset"),
                                    a.getString("title"))
                            );
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    };
}
