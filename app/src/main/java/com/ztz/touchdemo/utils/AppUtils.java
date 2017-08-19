package com.ztz.touchdemo.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by wqewqe on 2017/6/2.
 */

public class AppUtils {
    /**
     * 描述：获取可用内存
     * @param context
     * @return
     */
    public static long getAvailMemory(Context context){
        //获取android当前可用内存大小
        ActivityManager activityManager= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo=new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        //当前系统可用内存,将获得的内存大小规格化
        return memoryInfo.availMem;
    }

    /**
     * 总内存
     * @param context
     * @return
     */
    public static long getTotalMemory(Context context){
        //系统内存信息
        String file="/proc/meminfo";
        String memInfo;
        String[] str;
        long memory=0;
        try{
            FileReader fileReader=new FileReader(file);
            BufferedReader bufferedReader=new BufferedReader(fileReader,8192);
            //读取menInfo第一行,系统内存大小
            memInfo=bufferedReader.readLine();
            str=memInfo.split("\\s+");
            //获得系统总内存,单位kb
            memory=Integer.valueOf(str[1]).intValue();
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Byte转为KB或MB
        return memory*1024;
    }
}
