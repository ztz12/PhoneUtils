package com.ztz.touchdemo;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.ztz.touchdemo.flashlight.FlashLightActivity;
import com.ztz.touchdemo.programmanager.SoftManagerActivity;
import com.ztz.touchdemo.sms.SMSHelperActivity;
import com.ztz.touchdemo.utils.AppUtils;
import com.ztz.touchdemo.utils.ItemCardView;
import com.ztz.touchdemo.utils.StorageUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//    private LinearLayout ll;
//    private static final String TAG = "MainActivity";
//    private int disX;
//    private int disY;
//    private ArcProgress arcProgress;
//    private ArcProgress arcProgress1;
//    private Button open;

    private ItemCardView icv_card1;
    private ItemCardView icv_card2;
    private ItemCardView icv_card3;
    private ItemCardView icv_card4;
    private ItemCardView icv_card5;
    private ArcProgress arcprogress1;
    private ArcProgress arcprogress2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        setListener();
    }
    public void findView() {
        icv_card1 = (ItemCardView) findViewById(R.id.icv_card1);
        icv_card2 = (ItemCardView) findViewById(R.id.icv_card2);
        icv_card3 = (ItemCardView) findViewById(R.id.icv_card3);
        icv_card4 = (ItemCardView) findViewById(R.id.icv_card4);
        icv_card5 = (ItemCardView) findViewById(R.id.icv_card5);
        arcprogress1 = (ArcProgress) findViewById(R.id.arcprogress1);
        arcprogress2 = (ArcProgress) findViewById(R.id.arcprogress2);
    }

    public void setListener() {
        icv_card1.setOnClickListener(this);
        icv_card2.setOnClickListener(this);
        icv_card3.setOnClickListener(this);
        icv_card4.setOnClickListener(this);
        icv_card5.setOnClickListener(this);
        memory();
    }

    private void memory() {
        long l = AppUtils.getAvailMemory(this);
        long y = AppUtils.getTotalMemory(this);
        final double x = ((y - l) / (double)y) * 100;

        arcprogress2.setProgress(0);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(arcprogress2.getProgress() >= (int) x){
                            timer.cancel();
                        }else {
                            arcprogress2.setProgress(arcprogress2.getProgress()+1);
                        }
                    }
                });

            }
        }, 100,50);//100 第一次执行延迟的时间，  50 第二次之后延迟的时间

        StorageUtils.SDCardInfo info = StorageUtils.getSDCardInfo();
        StorageUtils.SDCardInfo sysinfo = StorageUtils.getSystemSpaceinfo(this);
        int availaBlock  = 0;
        int totalBlock  = 0;

        if(info != null) {
            availaBlock = (int) (info.free + sysinfo.free);
            totalBlock = (int) (info.total + sysinfo.total);
        }else{
            availaBlock = (int) sysinfo.free;
            totalBlock = (int) sysinfo.total;
        }

        final double size = ((totalBlock - availaBlock) / (double)totalBlock) * 100;

        arcprogress1.setProgress((int) size);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icv_card1:
                Intent intent = new Intent(this,SoftManagerActivity.class);
                startActivity(intent);

                break;
            case R.id.icv_card2:

                Intent intent2 = new Intent(this, FlashLightActivity.class);
                startActivity(intent2);

                break;

            case R.id.icv_card3:
                Intent intent3 = new Intent(this, SMSHelperActivity.class);
                startActivity(intent3);
                break;

            case R.id.icv_card4:
                Intent intent4 = new Intent(this, CallAssistantActivity.class);
                startActivity(intent4);
                break;

            case R.id.icv_card5:
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                break;
        }
    }
    long lastTime = 0;
    @Override
    public void onBackPressed() {

        long curTime = System.currentTimeMillis();
        //curTime = 400
        //lastTime = 0;

        //curTime = 500
        //lastTime = 400;

        if((curTime - lastTime) > 300){
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            lastTime = curTime;
            return;
        }else {
            finish();
        }
        super.onBackPressed();
    }
    /**
     * 判断无障碍是否开启
     * @return
     */
    private boolean isAccessibleEnable(){
        AccessibilityManager manager= (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> serviceInfos=manager.getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK);
        for (AccessibilityServiceInfo info : serviceInfos) {
            if(info.getId().equals(getPackageName()+"/.RedPacketService")){
                return true;
            }
        }
        return false;
    }
}
