package com.example.dangphutrungtin.clientapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InGameActivity extends AppCompatActivity {
    Socket msocket=MainClient.mSocket;
    //ArrayList<Question> quesfullcontent=mProperty.quesfullcontent;
    //int countQues=0;
    String isUser;
    Button A;
    Button B;
    Button C;
    Button D;
    TextView Question;
    ProgressBar PBar;
    int myAns=-1;
    String myname;
    String PIN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playin);
        Intent lastintent=getIntent();
        PIN=lastintent.getStringExtra("PIN");
        isUser=lastintent.getStringExtra("isUser");
        myname=lastintent.getStringExtra("myname");
        //Toast.makeText(this,"con ddm"+myname,Toast.LENGTH_LONG).show();
        Question=(TextView)findViewById(R.id.question);
        PBar=(ProgressBar)findViewById(R.id.progressBar);
        A=(Button) findViewById(R.id.A);
        B=(Button) findViewById(R.id.B);
        C=(Button) findViewById(R.id.C);
        D=(Button) findViewById(R.id.D);
        Button[] butt=new Button[]{A,B,C,D};

        //Button effect
        if(!Boolean.valueOf(isUser)) {
            A.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    A.setBackgroundResource(R.drawable.bu2);
                    B.setBackgroundResource(R.drawable.bu1);
                    C.setBackgroundResource(R.drawable.bu1);
                    D.setBackgroundResource(R.drawable.bu1);
//                    A.setBackgroundColor(Color.GREEN);
//                    B.setBackgroundColor(Color.BLUE);
//                    C.setBackgroundColor(Color.BLUE);
//                    D.setBackgroundColor(Color.BLUE);
                    myAns = 0;
                }
            });
            B.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    A.setBackgroundResource(R.drawable.bu1);
                    B.setBackgroundResource(R.drawable.bu2);
                    C.setBackgroundResource(R.drawable.bu1);
                    D.setBackgroundResource(R.drawable.bu1);
                    myAns = 1;
                }
            });
            C.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    A.setBackgroundResource(R.drawable.bu1);
                    B.setBackgroundResource(R.drawable.bu1);
                    C.setBackgroundResource(R.drawable.bu2);
                    D.setBackgroundResource(R.drawable.bu1);
                    myAns = 2;
                }
            });
            D.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    A.setBackgroundResource(R.drawable.bu1);
                    B.setBackgroundResource(R.drawable.bu1);
                    C.setBackgroundResource(R.drawable.bu1);
                    D.setBackgroundResource(R.drawable.bu2);
                    myAns = 3;
                }
            });
        }else {
            Question ques= mProperty.quesfullcontent.get(mProperty.countQues);
            butt[ques.getRightans()].setBackgroundResource(R.drawable.bu2);
        }
        //
        runQues(mProperty.quesfullcontent);
        //if(mProperty.countQues>=mProperty.size) finish();
    }
    public void runQues(ArrayList<Question> lst){
        final Question ques= lst.get(mProperty.countQues);
        Question.setText(ques.getContent());
        A.setText(ques.getAns()[0]);
        B.setText(ques.getAns()[1]);
        C.setText(ques.getAns()[2]);
        D.setText(ques.getAns()[3]);
        PBar.setProgress(100);
        final CountDownTimer CDTimer=new CountDownTimer(10000,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                int current=PBar.getProgress();
                PBar.setProgress((int)millisUntilFinished*10/1000);
            }
            @Override
            public void onFinish() {
                Intent gotoResult=new Intent(getApplicationContext(),ResultActivity.class);
                if(!Boolean.valueOf(isUser)){
                    Toast.makeText(getApplicationContext(),"het gio",Toast.LENGTH_LONG).show();
                    int mark;
                    if(myAns==ques.getRightans()) mark=1;
                    else mark=0;
                    msocket.emit("individual result",mProperty.countQues,myname,myAns,mark,PIN);
                    gotoResult.putExtra("mark",mark);
                }
                else gotoResult.putExtra("isUser",isUser);
                gotoResult.putExtra("PIN",PIN);
                gotoResult.putExtra("myname",myname);
                //gotoResult.putExtra("index",WaitRoomActivity.countQues);
                startActivity(gotoResult);//go to Result Activity
                finish();
            }
        };
        CDTimer.start();
    }
}
