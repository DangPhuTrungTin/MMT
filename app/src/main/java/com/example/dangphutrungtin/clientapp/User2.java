package com.example.dangphutrungtin.clientapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User2 extends AppCompatActivity implements QuesCustomDiaLog.QuesCustomDialogListener,AddQuesDialog.AddQuesDialogListener {
    ListView list;
    ArrayList<Question> quesfullcontent;
    ArrayList<String> ques;
    int choice;
    Socket msocket=MainClient.mSocket;
    ArrayAdapter<String> adapter;
    String IDset;
    String IDowner;
    public void clear(){
        quesfullcontent.clear();
        ques.clear();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions);

        msocket.on("on_update_ques",on_update_ques);
        msocket.on("on_list_questions",on_list_questions);
        msocket.on("on_add_question",on_add_question);
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
                QuesCustomDiaLog quesCustomDiaLog=new QuesCustomDiaLog();
                choice=position;
                quesCustomDiaLog.question=quesfullcontent.get(position);
                quesCustomDiaLog.show(getSupportFragmentManager(),"quesCustomDiaLog");
            }
        });
    }
    public void endsetting(View v){
        clear();
        msocket.off("on_update_ques",on_update_ques);
        msocket.off("on_list_questions",on_list_questions);
        msocket.off("on_add_question",on_add_question);
        super.finish();
    }

    @Override
    public void applyTextsAddQues(String content, String ansA, String ansB, String ansC, String ansD, String rightAns) throws JSONException {
        if(content.isEmpty()||ansA.isEmpty()||ansB.isEmpty()||ansC.isEmpty()||ansD.isEmpty()||rightAns.isEmpty()){
            Toast.makeText(getApplicationContext(),"Khong the co khoang trong",Toast.LENGTH_LONG).show();
            return;
        }
        try {
            if(Integer.valueOf(rightAns)<1 || Integer.valueOf(rightAns)>4) throw new NumberFormatException();
            quesfullcontent.add(new Question(String.valueOf(choice), content, ansA, ansB, ansC, ansD, Integer.valueOf(rightAns)-1));
            ques.add(content);
            adapter.notifyDataSetChanged();
            String[] a = {content, ansA, ansB, ansC, ansD};
            String[] b = {IDset, IDowner};
            String where = "IDset='" + IDset + "' and IDowner='" + IDowner + "'";
            String c1 = "'" + TextUtils.join("','", a) + "',";
            String c3 = ",'" + TextUtils.join("','", b) + "'";
            String right=String.valueOf(Integer.valueOf(rightAns)-1);
            msocket.emit("add question", c1 + right + c3, where);
        }catch (NumberFormatException e){
            Toast.makeText(getApplicationContext(),"Sai Corrected Answer",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void applyTexts(String content, String ansA, String ansB, String ansC, String ansD, String rightAns) throws JSONException {
        if(content.isEmpty()||ansA.isEmpty()||ansB.isEmpty()||ansC.isEmpty()||ansD.isEmpty()||rightAns.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Khong the co khoang trong", Toast.LENGTH_LONG).show();
            return;
        }
        try{
            if(Integer.valueOf(rightAns)<1 || Integer.valueOf(rightAns)>4) throw new NumberFormatException();
            quesfullcontent.set(choice,new Question(String.valueOf(choice),content,ansA,ansB,ansC,ansD,Integer.valueOf(rightAns)-1));
            ques.set(choice,content);
            adapter.notifyDataSetChanged();
            String[] a={"IDquestion='"+choice,"IDset='"+IDset,"IDowner='"+IDowner};
            String[] b={"Content='"+content,"AnsA='"+ansA,"AnsB='"+ansB,"AnsC='"+ansC,"AnsD='"+ansD,"RightAns="};
            String where= TextUtils.join("' and ",a)+"'";
            int right=Integer.valueOf(rightAns)-1;
            String set=TextUtils.join("',",b)+String.valueOf(right);
            msocket.emit("update question",set,where);
        }
        catch (NumberFormatException e){
            Toast.makeText(getApplicationContext(),"Sai Corrected Answer",Toast.LENGTH_LONG).show();
        }
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
    private Emitter.Listener on_update_ques = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Boolean isOK = Boolean.valueOf(args[0].toString());
                    if(isOK)
                        Toast.makeText(getApplicationContext(),"OK",Toast.LENGTH_LONG).show();
                }
            });
        }
    };
    private Emitter.Listener on_add_question = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Boolean isOK = Boolean.valueOf(args[0].toString());
                    if(isOK)
                        Toast.makeText(getApplicationContext(),"OK",Toast.LENGTH_LONG).show();
                }
            });
        }
    };
    public void addQues(View v){
        AddQuesDialog dialog=new AddQuesDialog();
        dialog.show(getSupportFragmentManager(),"add question");
    }
}
