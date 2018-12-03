package com.example.zeng.database;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.greendao.DaoMaster;
import com.example.greendao.DaoSession;


public class MyApp extends Application {

    private static DaoSession daoSession;

    private static final String DBNAME = "user.db";

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化greendao
        initGreenDao();
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(this, DBNAME);
        SQLiteDatabase readableDatabase = openHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(readableDatabase);
        daoSession = daoMaster.newSession();
    }


    /**
     * 获取 daosession
     * @return
     */
    public static DaoSession getDaoSession(){
        return daoSession;
    }

}
