package com.example.dangphutrungtin.clientapp;

public class FreeSet {
    private String IDowner;
    private String ownername;
    private String IDset;
    private String title;

    public FreeSet(String IDowner, String ownername, String IDset, String title) {
        this.IDowner = IDowner;
        this.ownername = ownername;
        this.IDset = IDset;
        this.title = title;
    }

    public String getIDowner() {
        return IDowner;
    }

    public String getOwnername() {
        return ownername;
    }

    public String getIDset() {
        return IDset;
    }

    public String getTitle() {
        return title;
    }
}
