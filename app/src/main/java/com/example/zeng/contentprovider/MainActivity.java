package com.example.zeng.contentprovider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 获取 手机通信录demo
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.get_contact_btn)
    Button getContactBtn;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.db_btn)
    Button dbBtn;

    private Unbinder bind;

    private MainActivity mActivity;

    private static final int READ_CONCACTS_CODE = 1;

    private Uri contractUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

    private ArrayAdapter adapter;

    List<String> contcacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        mActivity = this;
    }

    @OnClick(R.id.get_contact_btn)
    public void onViewClicked() {
        //申请权限
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONCACTS_CODE);
        } else {
            readContacts();
        }

//        mActivity.openOrCreateDatabase();
//        SQLiteDatabase.openOrCreateDatabase()

    }

    /**
     * 获取联系人数据
     */
    private void readContacts() {
        Cursor cursor = null;
        try {
            //1.获取contentresolver
            ContentResolver contentResolver = mActivity.getContentResolver();
            //查询通信录的数据
            cursor = contentResolver.query(contractUri, null, null, null, null);
            //遍历数据获取具体字段
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contcacts.add(name + "\n" + number);
                }
                adapter = new ArrayAdapter(mActivity, android.R.layout.simple_list_item_activated_1, contcacts);
                lv.setAdapter(adapter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_CONCACTS_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts();
                } else {
                    Toast.makeText(mActivity, "您拒绝了权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }


    @OnClick({R.id.get_contact_btn, R.id.db_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.get_contact_btn:
                //申请权限
                if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONCACTS_CODE);
                } else {
                    readContacts();
                }
                break;
            case R.id.db_btn:
                //跳转数据库页面
                Intent intent = new Intent(mActivity,DbActivity.class);
                mActivity.startActivity(intent);

                break;
        }
    }
}
