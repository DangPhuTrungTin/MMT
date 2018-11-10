package com.example.dangphutrungtin.clientapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpDialog extends AppCompatDialogFragment {
    EditText editTextUsername;
    EditText editTextPassword;
    SignUpDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.signin,null);
        builder.setView(view)
                .setTitle("Sign up")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        String username=editTextUsername.getText().toString();
                        String password=editTextPassword.getText().toString();
                        try {
                            listener.applyTextsSignUp(username,password);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        editTextUsername=view.findViewById(R.id.username);
        editTextPassword=view.findViewById(R.id.password);
        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener=(SignUpDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
    public interface SignUpDialogListener{
        void applyTextsSignUp(String username,String password) throws JSONException;
    }
}