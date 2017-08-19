package com.ztz.touchdemo.flashlight;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.ztz.touchdemo.R;

public class LightActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageButton iv_flicker;
    private ImageButton iv_touch;
    private ImageButton iv_sos;
    LinearLayout light_container;
    SosTask sosTask;
    LightTask lightTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_light);
        findViews();
        openLightFlash();
    }

    public void openLightFlash() {
        CameraManager.openFlash();
    }

    private void findViews() {
        iv_flicker=(ImageButton)findViewById(R.id.iv_flick);
        iv_touch=(ImageButton) findViewById(R.id.iv_touch);
        iv_sos=(ImageButton) findViewById(R.id.iv_sos);
        light_container=(LinearLayout)findViewById(R.id.light_container);
        setListener();
    }

    private void setListener() {
        iv_flicker.setOnClickListener(this);
        iv_touch.setOnClickListener(this);
        iv_sos.setOnClickListener(this);
        light_container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (iv_touch.isSelected()) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            openLightFlash();
                            break;
                        case MotionEvent.ACTION_UP:
                            closeFlash();
                            break;
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void closeFlash() {
        CameraManager.closeFlash();
    }
    public void release(){
        CameraManager.release();
    }

    @Override
    protected void onDestroy() {
        closeFlash();
        release();
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_flick:
                if(iv_touch.isSelected()){
                    iv_touch.setSelected(false);
                }
                if (iv_sos.isSelected()){
                    iv_sos.setSelected(false);
                    sosTask.stop();
                }
                iv_flicker.setSelected(!iv_flicker.isSelected());
                if(lightTask==null){
                    lightTask=new LightTask();
                }
                if(iv_flicker.isSelected()){
                    lightTask.start();

                }else {
                    lightTask.stop();

                }

                break;
            case R.id.iv_touch:
                if(iv_flicker.isSelected()){
                    iv_flicker.setSelected(false);
                    lightTask.stop();
                }
                if(iv_sos.isSelected()){
                    iv_sos.setSelected(false);
                    sosTask.stop();
                }
                iv_touch.setSelected(!iv_touch.isSelected());

                break;
            case R.id.iv_sos:
                if(iv_flicker.isSelected()){
                    iv_flicker.setSelected(false);
                    lightTask.stop();
                }
                if(iv_touch.isSelected()){
                    iv_touch.setSelected(false);
                }
                iv_sos.setSelected(!iv_sos.isSelected());
                if(sosTask==null){
                    sosTask=new SosTask();
                }
                if(iv_sos.isSelected()){
                    sosTask.start();
                }else {
                    sosTask.stop();
                }
        }

    }
    //屏幕开关(亮暗)状态，true为亮  false 暗
    boolean state=false;
    // 灭1300、亮200、灭200、亮200、灭200、亮200、灭500、亮400、灭200、亮400、灭200、亮400、灭500、亮200、灭200、亮200、灭200、亮200、 （MS）循环
    int[] time={1300, 200, 200, 200, 200, 200, 500, 400, 200, 400, 200, 400, 500, 200, 200, 200, 200, 200};
    class SosTask{
        private boolean mRunning=false;
        public void start(){
            mRunning=true;
            Message message=new Message();
            handle.sendMessage(message);
        }
        public void stop(){
            mRunning=false;
            closeFlash();
        }
        private Handler handle=new Handler(){
            int index=0;
            @Override
            public void handleMessage(Message msg) {

                if(mRunning){
                    if(state){
                        openLightFlash();
                    }else {
                        closeFlash();
                    }
                    state=!state;
                    if(index==time.length){
                        index=0;
                    }
                    int tempTime=time[index];
                    sendMessageDelayed(new Message(),tempTime);
                    index++;
                }
            }
        };

   }
   class LightTask{
       private boolean mRunning=false;
       public void start(){
           mRunning=true;
           Message message=new Message();
           handle.sendMessage(message);
       }
       public void stop(){
           mRunning=false;
           closeFlash();
       }
       private Handler handle=new Handler(){
           @Override
           public void handleMessage(Message msg) {

               if(mRunning){
                   if(state){
                       openLightFlash();
                   }else {
                       closeFlash();
                   }
                   state=!state;
                   sendMessageDelayed(new Message(),500);
               }
           }
       };
   }
}
