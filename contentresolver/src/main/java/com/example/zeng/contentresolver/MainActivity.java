package com.example.zeng.contentresolver;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.add)
    Button add;
    @BindView(R.id.update)
    Button update;
    @BindView(R.id.delete)
    Button delete;
    @BindView(R.id.query)
    Button query;
    @BindView(R.id.lv)
    ListView lv;

    private ArrayAdapter adapter;

    private static final String URI = "content://com.example.zeng.contentprovider.provider/user";

    private Unbinder bind;

    private MainActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        mActivity =this;
    }

    @OnClick({R.id.add, R.id.update, R.id.delete, R.id.query})
    public void onViewClicked(View view) {

        ContentResolver resolver = mActivity.getContentResolver();
        Uri parse = Uri.parse(URI);
        switch (view.getId()) {
            case R.id.add:
                ContentValues values = new ContentValues();
                values.put("nickName","zengmanyan");
                values.put("egpoint",999.9);
                values.put("gender",1);
                resolver.insert(parse,values);
                values.clear();
                values.put("nickName","huge");
                values.put("egpoint",900.9);
                values.put("gender",0);
                resolver.insert(parse,values);
                break;
            case R.id.update:
//                String s = parse.getPathSegments().get(1);
                ContentValues values1 = new ContentValues();
                values1.put("nickName","zmy");
                int update = resolver.update(parse, values1, "uid=?", new String[]{"1"});
                break;
            case R.id.delete:
                int delete = resolver.delete(parse, "uid = ?", new String[]{"2"});

                break;
            case R.id.query:
                Cursor cursor = null;
                try{
                    cursor = resolver.query(parse, null, null, null, null);
                    if (cursor!=null){
                        while (cursor.moveToNext()){
                            String nickName = cursor.getString(cursor.getColumnIndex("nickName"));
                            Log.e(TAG, "onViewClicked: "+nickName);
                        }
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (cursor!=null){
                        cursor.close();
                    }
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( bind!=null){
            bind.unbind();
        }
    }
}
