package com.huowei.heartbeatstudy.service;

import android.app.Service;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Author: huowei
 * @CreateDate: 2021/8/18 11:25
 * @Describe: 模拟虚拟心跳包  五秒调用一次
 * @Version: 1.1.2
 */
public class HeartBeatService extends Service {
    public static String TAG = "HeartBeatService";
    private Handler handler;
    private int number = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        initHandler();
    }

    private void initHandler() {
        //创建Handler
        handler = new Handler(getMainLooper()) {
            @Override
            public void dispatchMessage(@NonNull Message msg) {
                super.dispatchMessage(msg);
                //TODO 实现心跳业务 如：网络请求，列表刷新等
                Log.d(TAG, "" + number++);
                //设置每秒调用一次
                handler.sendEmptyMessageDelayed(1, 5000);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //判断Handler
        if (handler == null) {
            initHandler();
        }
        //设置每秒调用一次
        handler.sendEmptyMessageDelayed(1, 1000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭Hanlder
        handler.removeCallbacksAndMessages(null);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
