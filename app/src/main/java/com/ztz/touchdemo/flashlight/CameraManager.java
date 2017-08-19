package com.ztz.touchdemo.flashlight;

import android.hardware.Camera;

/**
 * 相机管理类
 * Created by wqewqe on 2017/6/6.
 */

public class CameraManager {
    static Camera camera=Camera.open();
    public static void openFlash(){
        if(camera==null){
            camera=Camera.open();
        }

        Camera.Parameters parameters=camera.getParameters();
        //设置闪光灯模式为“火炬”状态
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(parameters);
        camera.startPreview();
    }
    public static void closeFlash(){


        if(camera==null){
            return;
        }
        Camera.Parameters parameters=camera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(parameters);
    }
    public static void release(){
        if(camera==null){
            return;
        }
        camera.release();
        camera=null;
    }
}
