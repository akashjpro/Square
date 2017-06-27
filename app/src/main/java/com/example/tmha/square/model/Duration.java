package com.example.tmha.square.model;

/**
 * Created by Aka on 6/27/2017.
 */

public class Duration {
    private String mText;
    private String mValue;

    public Duration(String mText, String mValue) {
        this.mText = mText;
        this.mValue = mValue;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public String getmValue() {
        return mValue;
    }

    public void setmValue(String mValue) {
        this.mValue = mValue;
    }
}
