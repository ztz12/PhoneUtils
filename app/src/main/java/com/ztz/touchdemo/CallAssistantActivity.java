package com.ztz.touchdemo;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

/**
 * 使用服务来完成来电助手开启和关闭功能
 * 在服务里面监听电话状态
 */
public class CallAssistantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_assistant);
        CheckBox cb_state= (CheckBox) findViewById(R.id.check_state);
        //判断服务是否开启
        cb_state.setChecked(isRunning(getPackageName()+".MyService"));
        cb_state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent=new Intent(CallAssistantActivity.this,MyService.class);
                if(isChecked){
                    //开启服务
                    startService(intent);
                }else {
                    stopService(intent);
                }
            }
        });
        findViewById(R.id.ll_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CallAssistantActivity.this,ToastLocationActivity.class));
            }
        });
    }

    /**
     * 获取服务是否在运行
     * @param serviceName
     * @return
     */
    public boolean isRunning(String serviceName){
        ActivityManager activityManager= (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos=activityManager.getRunningServices(1000);
        for (ActivityManager.RunningServiceInfo info : runningServiceInfos) {
            if(serviceName.equals(info.service.getClassName())){
                return true;
            }
        }
        return false;
    }
}
