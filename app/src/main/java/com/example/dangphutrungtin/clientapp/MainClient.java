package com.example.dangphutrungtin.clientapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URISyntaxException;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class MainClient extends AppCompatActivity implements SignInDialog.SigninDialogListener,SignUpDialog.SignUpDialogListener{
    EditText mInputMessageView;
    EditText nickname;
    TextView text;
    //ListView listView;
    Intent gotowaitroom;
    Intent listquestionset;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSocket.connect();
        // xac nhan ma pin co dung khong
        mSocket.on("on_pinconfirmreturn", on_pinconfirmreturn);
        mSocket.on("on_signin",on_signin);
        mSocket.on("on_signup",on_signup);
        gotowaitroom= new Intent(this,WaitRoomActivity.class);

        ///////////////////////////////////
        //dang nhap
        listquestionset=new Intent(this,User1.class);
        setContentView(R.layout.activity_main_client);
        mInputMessageView=(EditText)findViewById(R.id.mInputMessageView);
        text=(TextView)findViewById(R.id.text);
        nickname=(EditText)findViewById(R.id.nickname);
        //listView= (ListView)findViewById(R.id.dynamic);
        mSocket.emit("requestcode","tinyeucau");
    }
    public static Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.126.2:3000"); //coi chung doi mang
            //mSocket = IO.socket("http://192.168.40.182:3000");
        } catch (URISyntaxException e) {}
    }
    ////////////////////////////////////////////////////////
    public void pinconfirm(View v) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("pin",mInputMessageView.getText().toString());
        obj.put("name",nickname.getText().toString().trim());
        gotowaitroom.putExtra("PIN",mInputMessageView.getText().toString());
        mSocket.emit("pinconfirm",obj);
    }
    private Emitter.Listener on_pinconfirmreturn = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Boolean flag =  Boolean.valueOf(args[0].toString());
                    if(flag) {
                        gotowaitroom.putExtra("myName", nickname.getText().toString().trim());
                        clearEditTExt();
                        startActivity(gotowaitroom);
                    }
                        //Toast.makeText(getApplicationContext(),"dung PIN r",Toast.LENGTH_LONG).show();}
                    else{
                        Toast.makeText(getApplicationContext(),args[1].toString(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    };
    //////////////////////////////////////////////////////////////////////
    //dang nhap
    public void Signin(View v){
        //startActivity(signinintent);
        SignInDialog signindialog=new SignInDialog();
        signindialog.show(getSupportFragmentManager(),"signin");
    }
    @Override
    public void applyTexts(String username, String password) throws JSONException {
            signinpacket(username,password);
    }

    @Override
    public void applyTextsSignUp(String username, String password) throws JSONException {
        mSocket.emit("sign up",username,password);
    }

    public void signinpacket(String username, String password) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("username",username);
        obj.put("pass",password);
        mSocket.emit("signin",obj);
    }
    private Emitter.Listener on_signin = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data =  args[0].toString();
                    if(Boolean.valueOf(data)){
                        listquestionset.putExtra("ID",args[1].toString());
                        startActivity(listquestionset);
                        //startActivityForResult(listquestionset,GO_TO_USERQUESTIONSET);
                    }else{
                        Toast.makeText(getApplicationContext(),"dang nhap that bai",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    };
    private Emitter.Listener on_signup = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data =  args[0].toString();
                    if(Boolean.valueOf(data)){
                        Toast.makeText(getApplicationContext(),"Dang ky thanh cong",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Ten dang nhap da ton tai",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    };
    public void clearEditTExt(){
        mInputMessageView.setText("");
        nickname.setText("");
    }
    public void SignUp(View v){
        SignUpDialog signupdialog=new SignUpDialog();
        signupdialog.show(getSupportFragmentManager(),"signup");
    }
}