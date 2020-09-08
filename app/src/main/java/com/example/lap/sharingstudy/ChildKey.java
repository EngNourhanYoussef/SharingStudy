package com.example.lap.sharingstudy;

import java.io.Serializable;


public class ChildKey implements Serializable {
    private String mKey;

    public ChildKey(String key) {
        mKey = key;
    }

    public String getKey() {
        return mKey;
    }
}
