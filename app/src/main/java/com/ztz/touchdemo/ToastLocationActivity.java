package com.ztz.touchdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ToastLocationActivity extends AppCompatActivity {
    private long lastTime=0;
    private long curTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast_location);
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //安卓版本5.0以上getHeight方法无法使用;无法通过此方法获取窗口高度;
        final int screenHeight = windowManager.getDefaultDisplay().getHeight();
        final int screenWidth=windowManager.getDefaultDisplay().getWidth();
        final LinearLayout ll_toast=(LinearLayout)findViewById(R.id.ll_toast);
        ll_toast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //两次点击时间间隔小于300,就被认为双击
                lastTime=curTime;
                curTime = System.currentTimeMillis();
                if(curTime-lastTime<300){
                    curTime=0;
                    lastTime=0;
                    Toast.makeText(ToastLocationActivity.this, "双击啦", Toast.LENGTH_SHORT).show();
                    int left=(screenWidth/2)-(ll_toast.getWidth()/2);
                    int top=(screenHeight/2)-(ll_toast.getHeight()/2);
                    int bottom=(screenHeight/2)+(ll_toast.getHeight()/2);
                    int right=(screenWidth/2)+(ll_toast.getWidth()/2);
                    ll_toast.layout(left,top,right,bottom);
                }
            }
        });

    }
}
