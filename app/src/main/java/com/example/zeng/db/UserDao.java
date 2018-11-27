package com.example.zeng.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作类：增删改查操作
 */
public class UserDao {

    private MySqlHelper helper;

    public UserDao(Context context) {
        helper = new MySqlHelper(context, MySqlHelper.DBNAME);
    }

    /**
     * 插入数据
     *
     * @param userInfo
     * @return 返回受影响行号
     */
    public long addDatas(UserInfo userInfo) {
        //1。获取可操作的数据库对象
        SQLiteDatabase readableDatabase = helper.getReadableDatabase();
        //构建需要添加的数据
        ContentValues values = new ContentValues();
        values.clear();
        values.put("nickname", userInfo.getNickName());
        values.put("egpoint", userInfo.getEgpoint());
        values.put("gender", userInfo.getGender());

        return readableDatabase.insert(MySqlHelper.TABLE_NAME, null, values);
    }

    /**
     * 插入数据
     *
     * @return 返回受影响行号
     */
    public long addDatasWithValues(ContentValues values) {
        //1。获取可操作的数据库对象
        SQLiteDatabase readableDatabase = helper.getReadableDatabase();
        return readableDatabase.insert(MySqlHelper.TABLE_NAME, null, values);
    }


    /**
     * 更新的数据，
     *
     * @param userInfo 更新后的数据
     * @param where  需要更新的条件
     * @return
     */
    public int updateData(UserInfo userInfo, String where, String[] whereAgrs) {
        SQLiteDatabase database = helper.getReadableDatabase();
        ContentValues values1= new ContentValues();
        values1.put("nickName",userInfo.getNickName());
        values1.put("egpoint", userInfo.getEgpoint());
        values1.put("gender", userInfo.getGender());
        return database.update(MySqlHelper.TABLE_NAME, values1, where, whereAgrs);
    }

    public int updateDataWithVaules(ContentValues values, String where, String[] whereAgrs) {
        SQLiteDatabase database = helper.getReadableDatabase();
        return database.update(MySqlHelper.TABLE_NAME, values, where, whereAgrs);
    }

    /**
     * 删除数据
     * @param where
     * @param whereAgrs
     * @return
     */
    public int deleteData(String where, String[] whereAgrs) {
        SQLiteDatabase readableDatabase = helper.getReadableDatabase();
        return readableDatabase.delete(MySqlHelper.TABLE_NAME, where, whereAgrs);
    }


    /**
     * 查询数据
     * @param where
     * @param whereAgrs
     * @return
     */
    public List<UserInfo> queryDataByWhere(String where, String[] whereAgrs){
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor query = null;
        List<UserInfo> userInfos = null;
        try {
            query = database.query(MySqlHelper.TABLE_NAME, null, where, whereAgrs, null,null,null);
            if (query!=null){
                while (query.moveToNext()){
                    int uid = query.getInt(query.getColumnIndex("uid"));
                    String nickname = query.getString(query.getColumnIndex("nickname"));
                    double egpoint = query.getDouble(query.getColumnIndex("egpoint"));
                    int genders = query.getInt(query.getColumnIndex("gender"));

                    UserInfo userInfo = new UserInfo();
                    userInfo.setUid(uid);
                    userInfo.setNickName(nickname);
                    userInfo.setEgpoint(egpoint);
                    userInfo.setGender(genders);

                    if (userInfos==null){
                        userInfos = new ArrayList<>();
                    }

                    userInfos.add(userInfo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (query!=null){
                query.close();
            }
        }
        return userInfos;
    }


    public Cursor query(String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder){
        SQLiteDatabase database = helper.getReadableDatabase();
        return database.query(MySqlHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public Cursor queryItem(String[] projection,@Nullable String[] selectionArgs,@Nullable String sortOrder){
        SQLiteDatabase database = helper.getReadableDatabase();
        return database.query(MySqlHelper.TABLE_NAME,projection,"id=?",selectionArgs,null,null,null,sortOrder );
    }


    public void closeDb(){
        if (helper!=null){
            SQLiteDatabase database = helper.getReadableDatabase();
            if (database!=null){
                database = null;
            }

            SQLiteDatabase writableDatabase = helper.getWritableDatabase();
            if (writableDatabase!=null){
                writableDatabase = null;
            }
        }

        helper = null;
    }

}
