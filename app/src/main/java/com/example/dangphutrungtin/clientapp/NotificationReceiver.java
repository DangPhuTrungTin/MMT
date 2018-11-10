package com.example.dangphutrungtin.clientapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class NotificationReceiver extends BroadcastReceiver {
    Socket mSocket=MainClient.mSocket;
    @Override
    public void onReceive(Context context, Intent intent) {
        JSONObject obj = new JSONObject();
        try {
            //Toast.makeText(context,"hello"+intent.getStringExtra("PIN")+intent.getStringExtra("myname"),Toast.LENGTH_LONG).show();
            obj.put("pin",intent.getStringExtra("pin"));
            obj.put("name",intent.getStringExtra("name"));
            mSocket.emit("pinconfirm",obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
