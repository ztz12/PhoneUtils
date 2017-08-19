package com.ztz.touchdemo.flashlight;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.ztz.touchdemo.R;

public class ScreenLightActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout rl_screenBg;
    private RelativeLayout ll_screen_mode;
    private VerticalSeekBar vs_screen_time;
    private VerticalSeekBar vs_screen_light;
    private ImageButton iv_flick;
    private ImageButton iv_touch;
    private ImageButton iv_sos;
    private int mColor;
    SOSTask sosTask;
    FlickTask flickTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_light);
        mColor=getIntent().getIntExtra("color",0);
        findViews();
    }
    private View.OnTouchListener onTouchListener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(iv_touch.isSelected()){
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        openFlash();
                        break;
                    case MotionEvent.ACTION_UP:
                        closeFlash();
                        break;
                }
                //必须返回true
                return true;
            }
            return false;
        }
    };


    private void findViews() {
        rl_screenBg=(RelativeLayout)findViewById(R.id.rl_screenBg);
        rl_screenBg.setOnClickListener(this);
        rl_screenBg.setOnTouchListener(onTouchListener);
        ll_screen_mode=(RelativeLayout)findViewById(R.id.ll_screen_mode);
        vs_screen_time = (VerticalSeekBar) findViewById(R.id.vs_screen_time);
        vs_screen_light = (VerticalSeekBar) findViewById(R.id.vs_screen_light);
        iv_flick = (ImageButton) findViewById(R.id.iv_flick);
        iv_touch = (ImageButton) findViewById(R.id.iv_touch);
        iv_sos = (ImageButton) findViewById(R.id.iv_sos);
        ll_screen_mode.setVisibility(View.INVISIBLE);
        vs_screen_time.setVisibility(View.INVISIBLE);
        openFlash();
        setListener();
    }
    public int getScreenTime(){
        int progress=vs_screen_time.getProgress();
        int max=vs_screen_time.getMax();
        return ((max-progress)*400/max)+100;
    }
    public float getLight(){
        return vs_screen_light.getProgress()/vs_screen_light.getMax()*0.7f+0.3f;
    }
    private void setListener() {
        iv_flick.setOnClickListener(this);
        iv_touch.setOnClickListener(this);
        iv_sos.setOnClickListener(this);
        vs_screen_light.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setScreenLight(getLight());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    public void iv_close(View view){
        finish();
    }
    /**
     *设置屏幕亮度
     * @param size
     */
    public void setScreenLight(float size){
       WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.screenBrightness=size;
        getWindow().setAttributes(lp);
    }
    /**
     * 打开手电筒
     */
    public void openFlash() {
        rl_screenBg.setBackgroundColor(mColor);
    }

    /**
     * 关闭手电筒
     */
    public void closeFlash(){
        rl_screenBg.setBackgroundColor(0xff000000);

    }
    public void openSos(){
        if(sosTask.getmCount()<6){
            rl_screenBg.setBackgroundColor(0xfffd0000);
        }else {
            //改变颜色
            rl_screenBg.setBackgroundColor(0xff0000fd);
        }

    }
    //新建一个任务类，执行闪屏任务
    class FlickTask{
        private static final String TAG = "FlickTask";
        //屏幕开关(亮暗)状态，true为亮  false 暗
        private boolean mState=false;
        //记录运行状态 如果为false 那么停止Handler 中的消息
        public boolean mRunning=false;
        public void start(){
            mRunning=true;
            Message message=new Message();
            handler.sendMessage(message);
        }
        public void stop(){
            mRunning=false;
            closeFlash();
        }
        private Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(mRunning){
                    if(mState){
                        openFlash();
                    }else {
                        closeFlash();
                    }

                    mState=!mState;
                    //再次发送消息
                    Message message=new Message();
                    //再次发现延迟消息给handle
                    sendMessageDelayed(message,getScreenTime());
                }
                super.handleMessage(msg);
            }
        };
    }
    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.rl_screenBg:
             if(iv_sos.isSelected()){
                 iv_sos.setSelected(false);
                 sosTask.stop();
             }
             int visible=ll_screen_mode.getVisibility();
             if(visible==View.INVISIBLE){
                 ll_screen_mode.setVisibility(View.VISIBLE);
                 vs_screen_time.setVisibility(View.INVISIBLE);
             }else {
                 ll_screen_mode.setVisibility(View.INVISIBLE);
                 vs_screen_time.setVisibility(View.INVISIBLE);
             }
             break;
         case R.id.iv_flick:
             iv_flick.setSelected(!iv_flick.isSelected());
             if(iv_touch.isSelected()){
                 iv_touch.setSelected(false);
             }
             if(iv_sos.isSelected()){
                 iv_sos.setSelected(false);
                 sosTask.stop();
             }
             if(flickTask==null) {
                 flickTask = new FlickTask();
             }
             if(iv_flick.isSelected()){
                 flickTask.start();
                 vs_screen_light.setVisibility(View.INVISIBLE);
                 vs_screen_time.setVisibility(View.VISIBLE);
             }else {
                 flickTask.stop();
                 vs_screen_time.setVisibility(View.INVISIBLE);
                 vs_screen_light.setVisibility(View.VISIBLE);
             }
             break;
         case R.id.iv_touch:
             if(iv_flick.isSelected()){
                 iv_flick.setSelected(false);
                 flickTask.stop();
             }
             if(iv_sos.isSelected()){
                 iv_sos.setSelected(false);
                 sosTask.stop();
             }
             iv_touch.setSelected(!iv_touch.isSelected());
             //手指按压屏幕会一直亮，抬起就暗
             if(iv_touch.isSelected()){
                 vs_screen_light.setVisibility(View.INVISIBLE);
                 vs_screen_time.setVisibility(View.INVISIBLE);
             }else {
                 vs_screen_light.setVisibility(View.VISIBLE);
                 vs_screen_time.setVisibility(View.VISIBLE);
             }
             break;
         case R.id.iv_sos:
             iv_sos.setSelected(!iv_sos.isSelected());
             if(iv_flick.isSelected()){
                 iv_flick.setSelected(false);
                 flickTask.stop();
             }
             if(iv_touch.isSelected()){
                 iv_touch.setSelected(false);
             }
             if(sosTask==null){
                 sosTask=new SOSTask(this);
             }
            if(iv_sos.isSelected()){
                sosTask.start();
                ll_screen_mode.setVisibility(View.INVISIBLE);
                vs_screen_time.setVisibility(View.INVISIBLE);
            }else {
                sosTask.stop();
                ll_screen_mode.setVisibility(View.VISIBLE);
                vs_screen_time.setVisibility(View.VISIBLE);
                vs_screen_light.setVisibility(View.VISIBLE);
            }
            break;

     }
    }

}
