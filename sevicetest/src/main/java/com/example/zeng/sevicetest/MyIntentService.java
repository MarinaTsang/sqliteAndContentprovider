package com.example.zeng.sevicetest;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {
    private static final String TAG = "MyIntentService";

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.zeng.sevicetest.action.FOO";
    private static final String ACTION_BAZ = "com.example.zeng.sevicetest.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "download_url_key";
    private static final String EXTRA_PARAM2 = "download_url_key_2";


    private File testFile = null;
    private long fileLenth;
    private long downLoadLength;


    private  Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            DownChager.getInstance().setPostChage(msg.what);
            return true;
        }
    });

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        //设置文件的保存路径
        testFile = new File(getCacheDir() + File.separator + "test.png");
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart: ");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionDownloadService(Context context, String param1) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: ");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                downLoadFile(param1);
            } else if (ACTION_BAZ.equals(action)) {
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param2);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void downLoadFile(String param1) {
        // TODO: Handle action Foo
        //开启线程下载URL
        HttpURLConnection connection = null;
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            connection = (HttpURLConnection) new URL(param1).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15 * 1000);
            connection.setReadTimeout(30 * 1000);
            int responseCode = connection.getResponseCode();
            fileLenth = connection.getContentLength();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                fos = new FileOutputStream(testFile);
                //获取图片
                is = connection.getInputStream();

                //写入文件
                byte[] bytes = new byte[2048];
                int len = -1;
                while ((len = is.read(bytes)) != -1) {
                    fos.write(bytes, 0, len);
                    downLoadLength += len;
                    Log.d(TAG, "downLoadFile: fileLenth:" + fileLenth + "  downloadLength:" + downLoadLength);
                    handler.sendEmptyMessage((int) (downLoadLength*100/fileLenth));
                }

                fos.flush();
                fos.close();
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1) {
        Log.d(TAG, "handleActionBaz: 2");
        // TODO: Handle action Baz
        downLoadFile(param1);
    }
}
