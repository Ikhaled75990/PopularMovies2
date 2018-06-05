package com.example.ikki.popularmovies;

public class Trailer {

    private String mName;
    private String mKey;

    public Trailer(String name, String key) {
        this.mName = name;
        this.mKey = key;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String name) {
        this.mName = name;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String key) {
        this.mKey = key;
    }
}
