package com.example.zeng.sevicetest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

public class MyService extends Service {

    private static final String TAG = "MyService";

//    private MyBinder myBinder = new MyBinder();

    public MyService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: "+ Process.myPid());
//        //测试ANR---会出现ANR
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 返回的三种类型：描述在系统杀死服务后怎么运行
     * @param intent1
     * @param flags
     * @param startId
     * @return   START_STICK :重新创建服务，但不会传入上次未传送完的intent
     *           START_NOT_STICK：不会重新创建服务，除非上次有未传送完的intent
     *           START_REDELIVE_INTENT：重新创建服务且任何未传入的intent都会依次传入
     */

    @Override
    public int onStartCommand(Intent intent1, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        Notification notification = null;
        //前台服务
        //8.0 适配通知栏
//        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//            NotificationChannel channel = new NotificationChannel("service","test", NotificationManager.IMPORTANCE_DEFAULT);
//            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            assert manager != null;
//            manager.createNotificationChannel(channel);
//            NotificationCompat.Builder service = new NotificationCompat.Builder(this, "service");
//            service.setContentTitle("执行前台服务的通知");
//            service.setContentText("执行前台服务的内容");
//            service.setSmallIcon(R.mipmap.ic_launcher);
//            notification = service.getNotification();
//        }else {
//            Intent intent = new Intent(this,MainActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
//            Notification.Builder builder = new Notification.Builder(this);
//            builder.setContentTitle("执行前台服务的通知");
//            builder.setContentText("执行前台服务的内容");
//            builder.setSmallIcon(R.mipmap.ic_launcher);
//            builder.setContentIntent(pendingIntent);
//            notification = builder.getNotification();
//        }
//
//        startForeground(1,notification);

        return super.onStartCommand(intent1, flags, startId);
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        // TODO: Return the communication channel to the service.

        return mBinder;
    }


    IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub() {
        @Override
        public String toUppercase(String aString) throws RemoteException {
            if (!TextUtils.isEmpty(aString)){
                return aString.toUpperCase();
            }
            return null;
        }
    };

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }


     class MyBinder extends Binder{
        public void startDownload(){
            Log.d(TAG, "startDownload: ");
        }
    }
}
