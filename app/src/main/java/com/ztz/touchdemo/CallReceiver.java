package com.ztz.touchdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by wqewqe on 2017/6/1.
 */

public class CallReceiver extends BroadcastReceiver {
    private static final String TAG = "CallReceiver";
    private WindowManager.LayoutParams params;
    private WindowManager windowManager;
    private View toastView;

    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context,"接收到电话",Toast.LENGTH_SHORT).show();
        String phoneNumber=getResultData();
        copy(context,"/location.db");
        switch (phoneNumber.length()){
            case 3:
                break;
            case 11:
                phoneNumber=phoneNumber.substring(0,7);
                String path=context.getFilesDir().getPath()+"/location.db";
                SQLiteDatabase db=SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
                Cursor cursor=db.query("phone_location",new String[]{"_id","area"},"_id=?",new String[]{phoneNumber},null,null,null);
                while (cursor.moveToNext()){
                    String id=cursor.getString(cursor.getColumnIndex("_id"));
                    String area=cursor.getString(cursor.getColumnIndex("area"));
                    Log.d(TAG, "onReceive: "+id+" "+area);
                    Toast.makeText(context,area,Toast.LENGTH_SHORT).show();
                    showView(context,area);
                    setPhoneStateListener(context);
                }

        }
    }
    public void setPhoneStateListener(Context context){
        TelephonyManager telephonyManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new MyPhoneStateListener(),PhoneStateListener.LISTEN_CALL_STATE);
    }
    class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.i(TAG, "onCallStateChanged: 电话等待--CALL_STATE_IDLE");
                    windowManager.removeView(toastView);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i(TAG, "onCallStateChanged: 通话状态--CALL_STATE_OFFHOOK");
                    windowManager.addView(toastView,params);
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i(TAG, "onCallStateChanged:来电状态--CALL_STATE_RINGING");
                    windowManager.addView(toastView,params);
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }
    public  void showView(Context context,String area){
        params = new WindowManager.LayoutParams();
        params.height=WindowManager.LayoutParams.WRAP_CONTENT;
        params.width=WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        params.format= PixelFormat.TRANSLUCENT;
        //安卓版本5.0以上getHeight方法无法使用;无法通过此方法获取窗口高度;
        final int screenHeight=windowManager.getDefaultDisplay().getHeight();
        final int screenWidth=windowManager.getDefaultDisplay().getWidth();
        params.gravity= Gravity.LEFT|Gravity.TOP;
        //设置当前view为电话窗口
        toastView = View.inflate(context, R.layout.toast_view,null);
        TextView tv=(TextView) toastView.findViewById(R.id.tv);
        tv.setText(area);
        toastView.setOnTouchListener(new View.OnTouchListener() {
            int startX=0;
            int startY=0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX= (int) event.getRawX();
                        startY= (int) event.getRawX();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX= (int) event.getRawX();
                        int moveY= (int) event.getRawX();
                        int disX=moveX-startX;
                        int disY=moveY-startY;
                        params.x=params.x+disX;
                        params.y=params.y+disY;
                        if(params.x<0){
                            params.x=0;
                        }
                        if(params.y<0){
                            params.y=0;
                        }
                        if(params.x>screenWidth- toastView.getWidth()){
                            params.x=screenWidth- toastView.getWidth();
                        }
                        if(params.y>screenHeight- toastView.getHeight()){
                            params.y=screenHeight- toastView.getHeight();
                        }
                        //更新view位置
                        windowManager.updateViewLayout(toastView,params);
                        startX= (int) event.getRawX();
                        startY= (int) event.getRawY();

                }
                return true;
            }
        });
    }
    public void copy(Context context,String dbName){
        File files=context.getFilesDir();
        File file=new File(files,dbName);
        if(file.exists()){
            return;
        }
        try{
            InputStream inputStream=context.getResources().getAssets().open("location.db");
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            int temp=0;
            byte[] bytes=new byte[1024];
            while ((temp=inputStream.read(bytes))!=-1){
                fileOutputStream.write(bytes,0,temp);
            }
            inputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
