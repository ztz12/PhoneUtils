package com.ztz.touchdemo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MyService extends Service {
    MyPhoneListener listener;
    private static final String TAG = "MyService";
    private CallReceiver receiver;
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private View toastView;
    private TelephonyManager telephonyManager;
    private String area;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: 我被创建");
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener=new MyPhoneListener();
        telephonyManager.listen(listener,PhoneStateListener.LISTEN_CALL_STATE);
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        receiver = new CallReceiver();
        registerReceiver(receiver,intentFilter);

    }
    class CallReceiver extends BroadcastReceiver{



        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.i(TAG, "onReceive: 拨打电话啦"+getResultData());
//            if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
//                //拨打电话
//                showToast();
//            }else {
//                //来电啦
//                Log.i(TAG, "onReceive: 来电啦");
//                showToast();
//            }
            String phoneNumber=getResultData();
            copy("/location.db");
            switch (phoneNumber.length()){
                case 3:
                    break;
                case 11:
                    phoneNumber=phoneNumber.substring(0,7);
                    String path=getFilesDir().getPath()+"/location.db";
                    SQLiteDatabase db=SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);
                    Cursor cursor=db.query("phone_location",new String[]{"_id","area"},"_id=?",new String[]{phoneNumber},null,null,null);
                    while (cursor.moveToNext()){
                        String id=cursor.getString(cursor.getColumnIndex("_id"));
                        area = cursor.getString(cursor.getColumnIndex("area"));
                        showToast(area);
                    }
            }

        }
    }
    public void copy(String dbName){
        File files=getFilesDir();
        File file=new File(files,dbName);
        if(file.exists()){
            return;
        }
        int temp=0;
        try{
            InputStream inputStream=getResources().getAssets().open("location.db");
            FileOutputStream fileOutputStream=new FileOutputStream(file);
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
    class MyPhoneListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                    //等待
                    Log.i(TAG, "onCallStateChanged: 电话等待 --- CALL_STATE_IDLE");
                    if(windowManager != null){
                        windowManager.removeView(toastView);
                    }

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i(TAG, "onCallStateChanged: 通话状态  CALL_STATE_OFFHOOK");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i(TAG, "onCallStateChanged: 来电啦  CALL_STATE_RINGING");
                    showToast(area);
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }

    private void showToast(String area) {
        params = new WindowManager.LayoutParams();
        params.height=WindowManager.LayoutParams.WRAP_CONTENT;
        params.width=WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        params.format= PixelFormat.TRANSLUCENT;
        //获取屏幕宽高
        final int screenHeight = windowManager.getDefaultDisplay().getHeight();
        final int screenWidth = windowManager.getDefaultDisplay().getWidth();
        params.gravity = Gravity.LEFT|Gravity.TOP;
        //设置当前View为电话窗口
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        toastView = View.inflate(this, R.layout.toast_view,null);
        TextView tv = (TextView) toastView.findViewById(R.id.tv);
        tv.setText(area);

        //将View添加到Window窗口，并且设置属性
        windowManager.addView(toastView, params);
        toastView.setOnTouchListener(new View.OnTouchListener() {
            int startX = 0;
            int startY = 0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();

                        int disX = moveX - startX;
                        int disY = moveY - startY;

                        params.x = params.x + disX;
                        params.y = params.y + disY;

                        //判断边界
                        if(params.x < 0){
                            params.x = 0;
                        }

                        if(params.y < 0){
                            params.y = 0;
                        }

                        if(params.x > screenWidth - toastView.getWidth()){
                            params.x = screenWidth - toastView.getWidth();
                        }

                        if(params.y > screenHeight - toastView.getHeight()){
                            params.y = screenHeight - toastView.getHeight();
                        }
                        //更新View的位置
                        windowManager.updateViewLayout(toastView, params);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;

                }
                return true;
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(telephonyManager!=null){
            telephonyManager.listen(listener,PhoneStateListener.LISTEN_NONE);
        }
        if(receiver!=null){
            unregisterReceiver(receiver);
        }
    }
}
