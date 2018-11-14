package com.example.dangphutrungtin.clientapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONException;

public class QuesFreeDiaLog extends AppCompatDialogFragment {
    TextView editTextContent;
//    TextView editTextAnsA;
//    TextView editTextAnsB;
//    TextView editTextAnsC;
//    TextView editTextAnsD;
    TextView[] textlist=new TextView[4];
    Question question=null;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.questionfree,null);
        builder.setView(view)
                .setTitle("Question Detail:")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                    }
                });
        editTextContent=view.findViewById(R.id.content2);
        textlist[0]=view.findViewById(R.id.ansA);
        textlist[1]=view.findViewById(R.id.ansB);
        textlist[2]=view.findViewById(R.id.ansC);
        textlist[3]=view.findViewById(R.id.ansD);


        if(question!=null) {
            editTextContent.setText("Question: "+question.getContent());
            textlist[0].setText(question.getAns()[0]);
            textlist[1].setText(question.getAns()[1]);
            textlist[2].setText(question.getAns()[2]);
            textlist[3].setText(question.getAns()[3]);
//            ((RadioButton)radioGroup.getChildAt(question.getRightans())).setChecked(true);
            textlist[question.getRightans()].setBackgroundColor(Color.GREEN);
        }
        return builder.create();
    }
}
