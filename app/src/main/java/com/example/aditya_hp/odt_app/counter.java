package com.example.aditya_hp.odt_app;

import android.app.Application;

/**
 * Created by aditya-hp on 1/6/17.
 */

public class counter extends Application{
    private int count = 0;

    public int getcount() {
        return count;
    }

    public void setcount(int c) {
        this.count = c;
    }
}
