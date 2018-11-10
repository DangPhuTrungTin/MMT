package com.example.dangphutrungtin.clientapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;

public class InvitationDialog extends AppCompatDialogFragment {
    EditText friendname;
    EditText message;
    InvitationDialog.InvitationDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.signin,null);
        builder.setView(view)
                .setTitle("Invitation")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        String name=friendname.getText().toString().trim();
                        String mess=message.getText().toString().trim();
                        try {
                            listener.applyTexts(name,mess);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        friendname=view.findViewById(R.id.username);
        message=view.findViewById(R.id.password);
        message.setHint("Message");
        friendname.setHint("Friend Name");
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener=(InvitationDialog.InvitationDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
    public interface InvitationDialogListener{
        void applyTexts(String friendname,String message) throws JSONException;
    }
}

