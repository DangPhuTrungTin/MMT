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

public class AddQuesSetDialog extends AppCompatDialogFragment {
    EditText editTextTitle;
    AddQuesSetDialog.AddQuesSetDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.layout_addquesset,null);
        builder.setView(view)
                .setTitle("Add new question set")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        String title=editTextTitle.getText().toString();
                        try {
                            listener.applyTexts(title);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        editTextTitle=view.findViewById(R.id.Title);
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener=(AddQuesSetDialog.AddQuesSetDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
    public interface AddQuesSetDialogListener{
        void applyTexts(String title) throws JSONException;
    }
}

