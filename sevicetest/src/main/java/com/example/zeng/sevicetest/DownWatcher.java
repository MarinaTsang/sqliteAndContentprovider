package com.example.zeng.sevicetest;

import java.util.Observable;
import java.util.Observer;

public abstract class DownWatcher implements Observer{
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Integer){
            int i = Integer.parseInt(arg.toString());
            notifyUpData(i);
        }
    }


    public abstract void notifyUpData(int progress
    );
}
