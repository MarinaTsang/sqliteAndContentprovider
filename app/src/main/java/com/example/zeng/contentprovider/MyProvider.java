package com.example.zeng.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.zeng.db.MySqlHelper;
import com.example.zeng.db.UserDao;
import com.example.zeng.db.UserInfo;

import java.util.List;


/**
 * 实现跨进程数据共享
 */

public class MyProvider extends ContentProvider {

    //自定义的urimatcher 中的自定义代码
    private static final int TABLE1_DIR = 0;
    private static final int TABLE1_ITEM = 1;

    //自定义uri规则
    private static final String AUTHORITY = "com.example.zeng.contentprovider.provider";

    private static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, MySqlHelper.TABLE_NAME,TABLE1_DIR);
        uriMatcher.addURI(AUTHORITY,MySqlHelper.TABLE_NAME+"/#",TABLE1_ITEM);
    }


    private UserDao userDao;
    /**
     * 初始化 内容提供器  ，此方法内一般进行数据库的创建和升级操作。  运行在content
     * provider的主线程中，故不能做耗时操作。
     * @return   true ---初始化成功   false---初始化失败
     */
    @Override
    public boolean onCreate() {
        userDao = new UserDao(getContext());
        return true;
    }

    /**
     * 查询
     * @param uri   The URI, using the content:// scheme, for the content to
     *                      retrieve.   uri，固定格式，用于标识读取哪里的数据
     * @param projection  A list of which columns to return. Passing null will
     *                      return all columns, which is inefficient.  需要查询的列的数组，null表示所有列
     * @param selection  A filter declaring which rows to return, formatted as an
     *                      SQL WHERE clause (excluding the WHERE itself). Passing null will
     *                      return all rows for the given URI.     需要过滤的条件 where语句，null返回所有行
     * @param selectionArgs  You may include ?s in selection, which will be
     *                      replaced by the values from selectionArgs, in the order that they
     *                      appear in the selection. The values will be bound as Strings.   where的参数
     * @param sortOrder   How to order the rows, formatted as an SQL ORDER BY
     *                      clause (excluding the ORDER BY itself). Passing null will use the
     *                      default sort order, which may be unordered.     对结果进行排序
     * @return
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor= null;
        switch (uriMatcher.match(uri)){
            case TABLE1_DIR:
                //查询表1 所有数据
                cursor = userDao.query(projection, selection, selectionArgs, sortOrder);
                break;
            case TABLE1_ITEM:
                //查询表1 单条数据
                String queryId = uri.getPathSegments().get(1);
                cursor = userDao.query(projection,"uid= ?",new String[]{queryId},sortOrder);
                break;

        }

        return cursor;
    }

    /**
     * uri是内容解析者传递过来的：contentresolver
     * 根据传入的URI来返回对应的 MIME类型，  两种类型：vnd.android.cursor.item/  单条记录，  uri 以ID结尾         vnd.android.cursor.dir/   多条记录
     * @return
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case TABLE1_DIR:
                return "vnd.android.cursor.dir/vnd."+AUTHORITY+"."+MySqlHelper.TABLE_NAME;
            case TABLE1_ITEM:
                return "vnd.android.cursor.item/vnd."+AUTHORITY+"."+MySqlHelper.TABLE_NAME;
        }

        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri uriRetrun = null;
        switch (uriMatcher.match(uri)){
            case TABLE1_DIR:
            case TABLE1_ITEM:
                long l = userDao.addDatasWithValues(values);
                uriRetrun = Uri.parse("content://"+AUTHORITY+"/"+MySqlHelper.TABLE_NAME+"/"+l);
        }
        return uriRetrun;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return userDao.deleteData(selection,selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return userDao.updateDataWithVaules(values,selection,selectionArgs);
    }
}
