package com.example.zeng.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

/**
 * 数据库创建及版本管理类
 */

public class MySqlHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    public static final String DBNAME = "user.db";   //存放在默认的目录下

    public static final String TABLE_NAME = "user";

    //如果有需求可以自定义存放的路径
//    private static final String FILE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()+"/sqlitetest/";


    public MySqlHelper(Context context, String name) {
        super(context, name, null, VERSION);
    }

    /**
     * 如果没有创建数据库时才调用
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建路径：如有需要
//        File file = new File(FILE_DIR);
//        if (!file.exists()){
//            file.mkdir();
//        }

        //使用sql语句创建表
        db.execSQL("create table if not exists "+TABLE_NAME+"(uid integer primary key autoincrement ," +
                "nickname text," +
                "egpoint real," +
                "gender bit default 1 )");

    }

    /**
     * 只有当版本号不一致时才会调用，在这里执行更新操作
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //更新：先删除旧表在创建新表
        if (newVersion>oldVersion){
            String sql = "DROP TABLE IF EXISTS "+TABLE_NAME;
            db.execSQL(sql);
            onCreate(db);
        }
    }
}
