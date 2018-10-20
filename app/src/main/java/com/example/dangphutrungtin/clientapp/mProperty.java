package com.example.dangphutrungtin.clientapp;

import android.content.Context;

import java.util.ArrayList;

public class mProperty {
    static ArrayList<Question> quesfullcontent=new ArrayList<Question>();
    static int countQues=0;
    static int size=0;
    static Context context;

    public static void setQuesfullcontent(ArrayList<Question> quesfullcontent) {
        mProperty.quesfullcontent = quesfullcontent;
    }
}
