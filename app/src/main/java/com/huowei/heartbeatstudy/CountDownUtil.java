package com.huowei.heartbeatstudy;

import android.os.Handler;
import android.os.Message;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: huowei
 * @CreateDate: 2021/8/18 14:27
 * @Describe: 倒计时工具类
 * @Version: 1.1.2
 */
public class CountDownUtil {
    private int countDownTime;
    private CountDownCallback callback;
    private Handler handler;
    private ScheduledExecutorService service;
    private static final int DEFAULT_TIME = 60;//默认倒计时

    public CountDownUtil() {
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (callback != null) {
                    callback.countDown(msg.what);
                }
            }
        };
    }

    /**
     * 开始倒计时
     * @param callback
     */
    public void start(CountDownCallback callback) {
        start(DEFAULT_TIME, callback);
    }

    /**
     * 开始倒计时
     * @param callback
     */
    public void start(int countDownTime, CountDownCallback callback) {
        if (callback == null || countDownTime <= 0 || service != null) {
            return;
        }
        service = new ScheduledThreadPoolExecutor(1);
        this.countDownTime = countDownTime;
        this.callback = callback;
        handler.sendEmptyMessage(countDownTime);
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                CountDownUtil.this.countDownTime--;
                handler.sendEmptyMessage(CountDownUtil.this.countDownTime);
                if (CountDownUtil.this.countDownTime <= 0) {
                    stop();
                }
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * 结束倒计时, 释放资源
     */
    public void stop() {
        if (service != null && !service.isShutdown()) {
            service.shutdownNow();
        }
        service = null;
    }

    public void release() {
        stop();
    }

    public interface CountDownCallback {
        void countDown(int time);
    }
}
