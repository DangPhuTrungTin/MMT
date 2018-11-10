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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONException;

public class AddQuesDialog extends AppCompatDialogFragment {
    EditText editTextContent;
    EditText editTextAnsA;
    EditText editTextAnsB;
    EditText editTextAnsC;
    EditText editTextAnsD;
    Question question=null;
    RadioGroup radioGroup;
    AddQuesDialog.AddQuesDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.questioncustom,null);
        builder.setView(view)
                .setTitle("Add question")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        String Content=editTextContent.getText().toString().trim();
                        String ansA=editTextAnsA.getText().toString().trim();
                        String ansB=editTextAnsB.getText().toString().trim();
                        String ansC=editTextAnsC.getText().toString().trim();
                        String ansD=editTextAnsD.getText().toString().trim();
                        int checkedRadio=radioGroup.getCheckedRadioButtonId();
                        int rightAns=radioGroup.indexOfChild(radioGroup.findViewById(checkedRadio));
                        try {
                            listener.applyTextsAddQues(Content,ansA,ansB,ansC,ansD,rightAns);
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
        radioGroup=view.findViewById(R.id.RG);
//        if(question!=null) {
//            editTextContent.setText(question.getContent());
//            editTextAnsA.setText(question.getAns()[0]);
//            editTextAnsB.setText(question.getAns()[1]);
//            editTextAnsC.setText(question.getAns()[2]);
//            editTextAnsD.setText(question.getAns()[3]);
//            ((RadioButton)radioGroup.getChildAt(question.getRightans())).setChecked(true);
//        }
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener=(AddQuesDialog.AddQuesDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
    public interface AddQuesDialogListener{
        void applyTextsAddQues(String content,String ansA,String ansB,String ansC,String ansD,int rightAns) throws JSONException;
    }
}

