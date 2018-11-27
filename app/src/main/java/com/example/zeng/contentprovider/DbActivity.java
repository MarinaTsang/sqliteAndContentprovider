package com.example.zeng.contentprovider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.zeng.db.UserDao;
import com.example.zeng.db.UserInfo;

import java.util.List;

public class DbActivity extends AppCompatActivity {

    private static final String TAG = "DbActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);

        UserDao userDao = new UserDao(this);

        //添加数据
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName("hello001");
        userInfo.setEgpoint(99.99);
        userInfo.setGender(1);
        userDao.addDatas(userInfo);

        UserInfo userInfo1 = new UserInfo();
        userInfo1.setNickName("hello002");
        userInfo1.setEgpoint(99);
        userInfo1.setGender(0);
        userDao.addDatas(userInfo1);

        //查询数据
        showDatas(userDao);


        //删除数据
        int i = userDao.deleteData("uid = ?", new String[]{"2"});

        showDatas(userDao);

        //更新数据
        userInfo.setNickName("dalao001");

        userDao.updateData(userInfo,"uid=?",new String[]{"1"});

        showDatas(userDao);
    }

    private void showDatas(UserDao userDao) {
        List<UserInfo> userInfos = userDao.queryDataByWhere(null, null);
        for (int i = 0; i < userInfos.size(); i++) {
            UserInfo userInfo2 = userInfos.get(i);
            Log.e(TAG, "name: "+userInfo2.getNickName()+" uid:"+userInfo2.getUid()+" egpoint:"+userInfo2.getEgpoint()+" gender:"+userInfo2.getGender());
        }
    }
}
