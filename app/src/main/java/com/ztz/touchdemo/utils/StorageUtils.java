package com.ztz.touchdemo.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * Created by wqewqe on 2017/6/2.
 */

public class StorageUtils {
    public static class SDCardInfo{
        public long total;
        public long free;
    }
    public static SDCardInfo getSDCardInfo(){
//        1）Environment 是一个提供访问环境变量的类，常用的方法有：
//        A，getRootDirectory() ，返回File，获取android 的根目录。
//        B，getDataDirectory() ，返回File ，获取Android 数据目录。
//        C，getExternalStorageDirectory() ，返回File ，获取外部存储目录即SDCard。
//        D，getExternalStorageState() ，返回String，获取外部存储设备的当前状态字串。
//        E，getDownloadCacheDirectory()，返回File ，获取Android 下载/缓存内容目录。
        if(Environment.isExternalStorageRemovable()){
            String sDcString=Environment.getExternalStorageState();
            if(sDcString.equals(Environment.MEDIA_MOUNTED)){
                File pathFile=Environment.getExternalStorageDirectory();
                try{
                    StatFs statFs=new StatFs(pathFile.getPath());
                    // 获取SDCard上BLOCK总数
                    long nTotalBlocks=statFs.getBlockCount();
                    //获取SDCard上每个block的SIZE
                    long nBlockSize=statFs.getBlockSize();
                    //获取可供程序使用的Block的数量
                    long nAvailBlock=statFs.getAvailableBlocks();
                    //获取剩下的所有Block的数量(包括预留的一般程序无法使用的块
                    long nFreeBlock=statFs.getFreeBlocks();
                    SDCardInfo info=new SDCardInfo();
                    //计算SDCard 总容量大小MB
                    info.total=nTotalBlocks*nBlockSize;
                    //计算 SDCard 剩余大小MB
                    info.free=nAvailBlock*nBlockSize;
                    return info;
                }catch (IllegalArgumentException e){

                }
            }
        }
        return null;
    }
    public static SDCardInfo getSystemSpaceinfo(Context context){
        File path=Environment.getDataDirectory();
        StatFs statFs=new StatFs(path.getPath());
        long blockSize=statFs.getBlockSize();
        long totalBlocks=statFs.getBlockCount();
        long availableBlocks=statFs.getAvailableBlocks();
        long totalSize=totalBlocks*blockSize;
        long availSize=availableBlocks*blockSize;
        SDCardInfo info=new SDCardInfo();
        info.total=totalSize;
        info.free=availSize;
        return info;
    }

}
