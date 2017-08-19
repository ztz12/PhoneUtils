package com.ztz.touchdemo.flashlight;

import android.os.Handler;
import android.os.Message;

/**
 * 屏幕sos模式任务器
 * Created by wqewqe on 2017/6/6.
 */

public class SOSTask {
    private int mCount=0;
    public boolean mRunning=false;
    ScreenLightActivity activity;
    public SOSTask(ScreenLightActivity activity){
        this.activity=activity;
    }
    public int getmCount(){
        return mCount;
    }
    //开始模式
    public void start(){
        mRunning=true;
        Message message=new Message();
        handler.sendMessage(message);
    }
    //结束模式
    public void stop(){
       mRunning=false;
        mCount=0;
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(mRunning){
                int time=0;
                mCount=mCount%12;
                if(mCount==0||mCount==6){
                    //这里需要使用ScreenLightActivity 的方法
                    activity.closeFlash();
                    time=1000;
                }else if(mCount%2==1){
                    activity.openSos();
                    time=100;
                }else {
                    activity.closeFlash();
                    time=100;
                }
                Message message=Message.obtain();
                sendMessageDelayed(message,time);
                mCount++;
            }else {
                activity.closeFlash();
            }
            super.handleMessage(msg);
        }
    };
}
