package com.example.dangphutrungtin.clientapp;

public class Question {
    private String IDquestion;

    public String getIDquestion() {
        return IDquestion;
    }

    public String getContent() {
        return content;
    }

    public String[] getAns() {
        return Ans;
    }

    public int getRightans() {
        return rightans;
    }

    private String content;
    private String[] Ans;
    private int rightans;
    public Question(String ID,String content,String AnsA,String AnsB,String AnsC,String AnsD,int RightAns) {
        IDquestion=ID;
        this.content=content;
        Ans=new String[]{AnsA,AnsB,AnsC,AnsD};
        rightans=RightAns;
    }

}
