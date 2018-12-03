package com.example.zeng.sevicetest;

import java.util.Observable;

/**
 * 被观察这
 */
public class DownChager extends Observable {

    private static DownChager mDownChager;

    private DownChager(){}

    private static final class DownChagrHelper{
        private static final DownChager mDownChager = new DownChager();
    }

    public static DownChager getInstance(){
        return DownChagrHelper.mDownChager;
    }


    public void setPostChage(int progress){
        setChanged();
        notifyObservers(progress);
    }

}
