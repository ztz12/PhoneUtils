package com.ztz.touchdemo.sms;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static com.ztz.touchdemo.sms.StaticObjectInterface.CPATCHAS_KEYWORD;

/**
 * Created by wqewqe on 2017/6/3.
 */

public class StringUtils {
    private static final String TAG = "StringUtils";
    /**
     * 判断是否是验证码短信
     * @param content
     * @return
     */
    public static boolean isCaptchMessage(String content){
        for(int i=0;i<CPATCHAS_KEYWORD.length;i++){
            if(content.contains(CPATCHAS_KEYWORD[i])){
                return true;
            }
        }
        return false;
    }
    public static String tryToGetCap(String content){
        Pattern pattern=Pattern.compile("[0-9]+");
        Matcher m=pattern.matcher(content);
        while (m.find()){
            if(m.group().length()>3&&m.group().length()<7){
                int startIndex=0;
                int endIndex=content.length()-1;
                if(content.indexOf(m.group())>12){
                    startIndex=content.indexOf(m.group())-12;
                }
                if(content.indexOf(m.group())+m.group().length()+12<content.length()-1){
                    endIndex=content.indexOf(m.group())+m.group().length()+12;
                }
                for(int i=0;i<CPATCHAS_KEYWORD.length;i++){
                    if(content.substring(startIndex,endIndex).contains(CPATCHAS_KEYWORD[i])){
                        Log.i(TAG, "tryToGetCap: 提取到的验证码"+m.group());
                        return m.group();
                    }
                }
            }
        }
        return "";
    }
}
