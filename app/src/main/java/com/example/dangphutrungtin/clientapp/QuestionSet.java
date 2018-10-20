package com.example.dangphutrungtin.clientapp;

public class QuestionSet {
    private String IDset;
    private String title;

    public QuestionSet(String IDset, String title) {
        this.IDset = IDset;
        this.title = title;
    }

    public String getIDset() {
        return IDset;
    }

    public String getTitle() {
        return title;
    }
}
