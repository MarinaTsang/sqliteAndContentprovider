package com.example.zeng.sevicetest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    @BindView(R.id.start)
    Button start;
    @BindView(R.id.stop)
    Button stop;
    @BindView(R.id.bind)
    Button bind;
    @BindView(R.id.unbind)
    Button unbind;

    @BindView(R.id.start_intent)
    Button startIntent;
    @BindView(R.id.progress)
    TextView progress;


    private Unbinder binder;

//    private MyService.MyBinder myBinder;  //自定义Binder

    private IMyAidlInterface iMyAidlInterface;  //是IBinder的子类 ,AIDL ,mainactivity 和myservice 是不同的进程，此时实现了跨进程通信
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
//           myBinder = (MyService.MyBinder) service;
//           myBinder.startDownload();
            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            try {
                String hello_world = iMyAidlInterface.toUppercase("hello world");

                Log.d(TAG, "onServiceConnected: " + hello_world);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    private DownWatcher downWatcher = new DownWatcher() {
        @Override
        public void notifyUpData(int progress1) {
            progress.setText("进度："+progress1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binder = ButterKnife.bind(this);

        Log.d(TAG, "onCreate: " + Process.myPid());

        DownChager.getInstance().addObserver(downWatcher);

    }

    @OnClick({R.id.start, R.id.stop, R.id.bind, R.id.unbind, R.id.start_intent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.start:
                Intent intent = new Intent(MainActivity.this, MyService.class);
                startService(intent);
                break;
            case R.id.stop:
                stopService(new Intent(MainActivity.this, MyService.class));
                break;
            case R.id.bind:
                bindService(new Intent(MainActivity.this, MyService.class), connection, BIND_AUTO_CREATE);
                break;
            case R.id.unbind:
                unbindService(connection);
                break;
            case R.id.start_intent:
                MyIntentService.startActionDownloadService(MainActivity.this,
                        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543747271685&di=1aa5dea333a52385d6932df837fb2631&imgtype=0&src=http%3A%2F%2Fp1.gexing.com%2Fshaitu%2F20130129%2F2002%2F5107ba5f90dbb.jpg");
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binder != null) {
            binder.unbind();
        }

        DownChager.getInstance().deleteObservers();
    }
}
