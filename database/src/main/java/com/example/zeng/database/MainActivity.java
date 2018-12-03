package com.example.zeng.database;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import greendao.MyUserDao;
import greendao.User;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.green_add)
    Button greenAdd;
    @BindView(R.id.green_delete)
    Button greenDelete;
    @BindView(R.id.green_update)
    Button greenUpdate;
    @BindView(R.id.green_query)
    Button greenQuery;

    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
    }

    @OnClick({R.id.green_add, R.id.green_delete, R.id.green_update, R.id.green_query})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.green_add:
                Random random = new Random();
                long start = System.currentTimeMillis();
                for (int i = 0; i < 200; i++) {
                    User user = new User();
                    user.setEgpoint(10.0+i);
                    user.setName("hello"+i);
                    user.setGameId(1000L+i);
                    user.setLevel(random.nextInt(20));
                    user.setRoleId(""+10000+i);
                    user.setServerId("霸业"+i);
                    MyUserDao.insertUser(user);
                }
                long end = System.currentTimeMillis();
                Log.e(TAG, "onViewClicked: addTIme  "+(end-start));
                break;
            case R.id.green_delete:
                long start1 = System.currentTimeMillis();
                for (int i = 0; i < 5; i++) {
                    MyUserDao.deleteUser(i);
                }
                long end1 = System.currentTimeMillis();
                Log.e(TAG, "onViewClicked: deleteTIme  "+(end1-start1));
                break;
            case R.id.green_update:
                List<User> users1 = MyUserDao.queryById();
                long start2 = System.currentTimeMillis();
                for (int i = 0; i < users1.size(); i++) {
                    User user = users1.get(i);
                    user.setName("zmy"+i);
                    MyUserDao.updateUser(user);
                }
                long end2 = System.currentTimeMillis();
                Log.e(TAG, "onViewClicked: updateTIme  "+(end2-start2));
                break;
            case R.id.green_query:
                long start3 = System.currentTimeMillis();
                List<User> users = MyUserDao.queryAll();
                long end3 = System.currentTimeMillis();
                Log.e(TAG, "onViewClicked: queryTIme  "+(end3-start3));
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    Log.e(TAG, "show: "+ user.getName()+" level:"+user.getLevel());
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind!=null){
            bind.unbind();
        }
    }
}
