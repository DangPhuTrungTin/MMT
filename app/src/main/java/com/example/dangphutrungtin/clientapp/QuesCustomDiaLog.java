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

public class QuesCustomDiaLog extends AppCompatDialogFragment {
    EditText editTextContent;
    EditText editTextAnsA;
    EditText editTextAnsB;
    EditText editTextAnsC;
    EditText editTextAnsD;
    EditText editTextRightAns;
    Question question=null;
    QuesCustomDiaLog.QuesCustomDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.questioncustom,null);
        builder.setView(view)
                .setTitle("Setting question")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        String Content=editTextContent.getText().toString();
                        String ansA=editTextAnsA.getText().toString();
                        String ansB=editTextAnsB.getText().toString();
                        String ansC=editTextAnsC.getText().toString();
                        String ansD=editTextAnsD.getText().toString();
                        String rightAns=editTextRightAns.getText().toString();
                        try {
                            listener.applyTexts(Content,ansA,ansB,ansC,ansD,rightAns);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        editTextContent=view.findViewById(R.id.content2);
        editTextAnsA=view.findViewById(R.id.ansA);
        editTextAnsB=view.findViewById(R.id.ansB);
        editTextAnsC=view.findViewById(R.id.ansC);
        editTextAnsD=view.findViewById(R.id.ansD);
        editTextRightAns=view.findViewById(R.id.rightans);
        if(question!=null) {
            editTextContent.setText(question.getContent());
            editTextAnsA.setText(question.getAns()[0]);
            editTextAnsB.setText(question.getAns()[1]);
            editTextAnsC.setText(question.getAns()[2]);
            editTextAnsD.setText(question.getAns()[3]);
            editTextRightAns.setText(String.valueOf(question.getRightans()));
        }
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener=(QuesCustomDiaLog.QuesCustomDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
    public interface QuesCustomDialogListener{
        void applyTexts(String content,String ansA,String ansB,String ansC,String ansD,String rightAns) throws JSONException;
    }
}
