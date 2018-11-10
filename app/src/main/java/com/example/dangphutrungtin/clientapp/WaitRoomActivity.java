package com.example.dangphutrungtin.clientapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Property;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WaitRoomActivity extends AppCompatActivity implements InvitationDialog.InvitationDialogListener{
    Button start;
    Socket msocket=MainClient.mSocket;
    ListView listmems;
    String[] quespackage;
    String myname;
    String PIN;
    ArrayList<Question> quesfullcontent;
    ArrayList<String> name=new ArrayList<String>();


    TextView pin;
    TextView gamepin;
    ImageView pinimage;
    public void finish1(){
        msocket.off("question list for me",on_quesforme);
        msocket.off("updatedlistmems",on_updatedlistmems);
        msocket.off("goto Ingame",on_ingame);
        super.finish();
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wait);
        msocket.on("question list for me",on_quesforme);
        msocket.on("updatedlistmems",on_updatedlistmems);
        pin=(TextView)findViewById(R.id.pin);
        gamepin=(TextView)findViewById(R.id.gamepin);
        pinimage=(ImageView)findViewById(R.id.pinimage);

        Intent lastintent=getIntent();
        myname=lastintent.getStringExtra("myname");
        PIN=lastintent.getStringExtra("PIN");
        quesfullcontent=new ArrayList<Question>();

        start=(Button)findViewById(R.id.Start);
        listmems=(ListView)findViewById(R.id.listmemswaiting);

        msocket.emit("startupdate",PIN);
        msocket.emit("give client questions",PIN);
        String isUser=lastintent.getStringExtra("isUser");
        //user va client
        if (Boolean.valueOf(isUser)){//user
            //Toast.makeText(this,lastintent.getStringExtra("PIN"),Toast.LENGTH_LONG).show();
            pin.setVisibility(View.VISIBLE);
            pin.setTypeface(null, Typeface.BOLD);
            gamepin.setTypeface(null, Typeface.BOLD);
            gamepin.setVisibility(View.VISIBLE);
            pinimage.setVisibility(View.VISIBLE);
            pin.setText(lastintent.getStringExtra("PIN"));
            start.setVisibility(View.VISIBLE);
            quespackage=new String[]{lastintent.getStringExtra("IDset"),
                    lastintent.getStringExtra("IDowner"),
                    lastintent.getStringExtra("PIN")};
        }
        else{
            msocket.on("goto Ingame",on_ingame);
        }

    }
    private Emitter.Listener on_updatedlistmems = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        //Toast.makeText(getApplicationContext(),"ello",Toast.LENGTH_LONG).show();
                        JSONArray list = data.getJSONArray("noidung");
                        name=new ArrayList<String>();
                        for ( int i=0;i<list.length();i++)
                            name.add(list.get(i).toString());
                        ArrayAdapter<String> arrayadapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,name);
                        listmems.setAdapter(arrayadapter);
                    } catch (JSONException e) {
                        return;
                    }
                }
            });
        }
    };
    public void goToInGame(View v){//Ingame for user
        if(name.size()==0) return;
        msocket.emit("chuyen canh", quespackage[2]);
        Intent ingame= new Intent(getApplicationContext(),InGameActivity.class);
        //ingame.putExtra("package",quespackage);
        ingame.putExtra("isUser","true");
        ingame.putExtra("PIN",PIN);
        ingame.putExtra("mymame",myname);
        mProperty.quesfullcontent=(ArrayList<Question>)quesfullcontent.clone();
        mProperty.countQues=0;
        mProperty.size=quesfullcontent.size();
        if(quesfullcontent.size()==0) finish1();
        else {startActivity(ingame);finish1();}
    }
    private Emitter.Listener on_ingame = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {//Ingame for client
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent ingameclient=new Intent(getApplicationContext(),InGameActivity.class);
                    ingameclient.putExtra("myname",myname);
                    ingameclient.putExtra("PIN",PIN);
                    mProperty.quesfullcontent=(ArrayList<Question>)quesfullcontent.clone();
                    mProperty.countQues=0;
                    mProperty.size=quesfullcontent.size();
                    if(mProperty.quesfullcontent.size()==0) finish1();
                    else {startActivity(ingameclient);finish1();}
                }
            });
        }
    };
    private Emitter.Listener on_quesforme = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONArray data = (JSONArray)args[0];
                    for ( int i=0;i<data.length();i++) {
                        try {
                            JSONObject a = (JSONObject) data.get(i);
                            quesfullcontent.add(new Question(a.getString("IDquestion"),
                                    a.getString("Content"),
                                    a.getString("AnsA"),
                                    a.getString("AnsB"),
                                    a.getString("AnsC"),
                                    a.getString("AnsD"),
                                    Integer.valueOf(a.getString("RightAns"))));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    };
    public void Invitionclick(View v){
        InvitationDialog dialog=new InvitationDialog();
        dialog.show(getSupportFragmentManager(),"Invitation");
    }
    @Override
    public void applyTexts(String friendname, String message) throws JSONException {
        if(myname.equals(friendname))
            Toast.makeText(getApplicationContext(),"Khong duoc gui cho chinh minh",Toast.LENGTH_LONG).show();
        else msocket.emit("invitation",myname,PIN,friendname,message);
    }
}
