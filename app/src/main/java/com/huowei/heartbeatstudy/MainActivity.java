package com.huowei.heartbeatstudy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.huowei.heartbeatstudy.databinding.ActivityMainBinding;
import com.huowei.heartbeatstudy.service.HeartBeatService;
/**
 * @Author: huowei
 * @CreateDate: 2021/8/18 14:01
 * @Describe:  在使用的地方注册Service，同时通过生命周期控制相应的操作
 * @Version: 1.2.2
 */

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    private ActivityMainBinding detailBinding;
    private CountDownUtil mCountDownUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_main, null, false);
        setContentView(detailBinding.getRoot());
        //初始化倒计时
        if (mCountDownUtil == null) {
            mCountDownUtil = new CountDownUtil();
        }
        detailBinding.startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailBinding.startTime.setEnabled(false);
                mCountDownUtil.start(60, new CountDownUtil.CountDownCallback() {
                    @Override
                    public void countDown(int time) {
                        String text = (time > 0) ? String.format("%ss", String.valueOf(time)) : "再次发送";
                        detailBinding.startTime.setText(text);
                        if (time <= 0) {
                            detailBinding.startTime.setEnabled(true);
                            stopCountDown();
                        }
                    }
                });
            }
        });

        //注册服务
        intent = new Intent(this, HeartBeatService.class);

    }
    //倒计时执行完毕
    private void stopCountDown() {
        if (mCountDownUtil != null) {
            mCountDownUtil.stop();
        }

        detailBinding.startTime.setEnabled(true);
        detailBinding.startTime.setText("重新发送");
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
    }
}