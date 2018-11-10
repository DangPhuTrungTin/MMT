package com.example.dangphutrungtin.clientapp;

public class QuestionSet {
    private String IDset;
    private String title;
    private int ispublic=0;
    private String IDowner="";

    public QuestionSet(String IDset, String title,int ispublic,String IDowner) {
        this.IDset = IDset;
        this.title = title;
        this.ispublic=ispublic;
        this.IDowner=IDowner;
    }
    public QuestionSet(String IDset, String title,String IDowner) {
        this.IDset = IDset;
        this.title = title;
        this.IDowner=IDowner;
    }

    public String getIDset() {
        return IDset;
    }

    public String getTitle() {
        return title;
    }
    public int getIspublic() {
        return ispublic;
    }
    public String getIdowner() {
        return IDowner;
    }
    public void setIspublic(int ispublic){this.ispublic=ispublic;}
}
