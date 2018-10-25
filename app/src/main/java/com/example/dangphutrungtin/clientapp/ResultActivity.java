package com.example.dangphutrungtin.clientapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ResultActivity extends AppCompatActivity {
    Socket mSocket=MainClient.mSocket;
    Button NextorEndbutt;
    String PIN;
    String myname;
    String isUser;
    ListView ranklist;
    GraphView graph;
    int CorrectAns;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        NextorEndbutt=(Button)findViewById(R.id.NextorEnd);
        ranklist=(ListView)findViewById(R.id.ranklist);


        graph=(GraphView)findViewById(R.id.graph);
        graph.getGridLabelRenderer().setGridStyle( GridLabelRenderer.GridStyle.NONE );
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(5);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(10);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);


        Intent lastintent=getIntent();
        isUser=lastintent.getStringExtra("isUser");
        PIN=lastintent.getStringExtra("PIN");
        myname=lastintent.getStringExtra("myName");
        if(Boolean.valueOf(isUser)){
            //mSocket.on("next question",on_next_question);
            NextorEndbutt.setVisibility(View.VISIBLE);
        }else
            mSocket.on("listen to next question",on_listen);
        mSocket.on("game finish",on_finish);

        mSocket.on("receive result",on_receive);
        mSocket.emit("give me result",PIN,mProperty.countQues);

        CorrectAns=mProperty.quesfullcontent.get(mProperty.countQues).getRightans();
        mProperty.countQues++;
        if(mProperty.countQues>=mProperty.size) {
            NextorEndbutt.setText("FINISH");
            NextorEndbutt.setVisibility(View.VISIBLE);
        }


    }
    @Override
    public void finish(){
        mSocket.off("game finish",on_finish);
        mSocket.off("listen to next question",on_listen);
        mSocket.off("receive result",on_receive);
        ((Activity)mProperty.context).finish();
        super.finish();

    }
    public void finish1(){
        mSocket.off("game finish",on_finish);
        mSocket.off("listen to next question",on_listen);
        mSocket.off("receive result",on_receive);
        super.finish();

    }
    private Emitter.Listener on_listen = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent gotoIngame=new Intent(getApplicationContext(),InGameActivity.class);
                    gotoIngame.putExtra("PIN",PIN);
                    gotoIngame.putExtra("isUser",isUser);
                    gotoIngame.putExtra("myName",myname);
                    startActivity(gotoIngame);
                    //mSocket.off("listen to next question",on_listen);
                    finish1();
                }
            });
        }
    };
    private Emitter.Listener on_finish = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
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
                    JSONArray chart = (JSONArray) args[0];
                    JSONObject Scorelist = (JSONObject) args[1];
                    JSONArray namelist=(JSONArray) args[2];
                    ArrayList<Rank> rank=new ArrayList<Rank>();
                    try {
                        for(int i=0;i< namelist.length();i++){
                            String name =namelist.get(i).toString();
                            String score =Scorelist.getString(name);
                            rank.add(new Rank(name,score));
                        }
                        Collections.sort(rank,new RankSort());
                        RankAdapter adapter=new RankAdapter(getApplicationContext(),R.layout.layout_resultlist,rank);
                        ranklist.setAdapter(adapter);


                        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                                new DataPoint(1, Integer.valueOf(chart.get(0).toString())),
                                new DataPoint(2, Integer.valueOf(chart.get(1).toString())),
                                new DataPoint(3, Integer.valueOf(chart.get(2).toString())),
                                new DataPoint(4, Integer.valueOf(chart.get(3).toString()))
                        });
                        graph.removeAllSeries();
                        graph.addSeries(series);
                        series.setSpacing(50);
                        series.setDrawValuesOnTop(true);
                        series.setValuesOnTopColor(Color.RED);
                        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                            @Override
                            public int get(DataPoint data) {
                                if ((data.getX()-1-CorrectAns)==0)
                                    return Color.GREEN;
                                else return Color.rgb(141,143,138);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    public void NextorEnd(View v){
        if(mProperty.countQues>=mProperty.quesfullcontent.size())
            finishallclient();
        else{
            mSocket.emit("next question",PIN);
            Intent gotoIngame=new Intent(getApplicationContext(),InGameActivity.class);
            gotoIngame.putExtra("PIN",PIN);
            gotoIngame.putExtra("isUser",isUser);
            gotoIngame.putExtra("myName",myname);
            startActivity(gotoIngame);
            finish();
        }
    }
    public void finishallclient(){
        //Toast.makeText(getApplicationContext(),isUser,Toast.LENGTH_LONG).show();
       if(Boolean.valueOf(isUser)) {mSocket.emit("finish",PIN);}
       else {
           finish();
       }
    }
}
class RankSort implements Comparator<Rank>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(Rank a, Rank b)
    {
        return -(Integer.valueOf(a.getScore()) - Integer.valueOf(b.getScore()));
    }
}
