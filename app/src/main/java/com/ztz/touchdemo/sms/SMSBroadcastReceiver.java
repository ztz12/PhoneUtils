package com.ztz.touchdemo.sms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.ztz.touchdemo.R;

/**
 * Created by wqewqe on 2017/6/3.
 */

public class SMSBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "SMSBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "接收到短信", Toast.LENGTH_SHORT).show();
        Object[] objects= (Object[]) intent.getExtras().get("pdus");
        for (Object o : objects) {
            byte[] sms= (byte[]) o;
            SmsMessage message=SmsMessage.createFromPdu(sms);
            String body=message.getMessageBody();
            //【百度】164789(动态验证码)请在30分支内填写。
            //登陆QQ，验证码是: 444555，如有疑问请联系：95598

            //判断短信是否是验证码的短信
            boolean ll=StringUtils.isCaptchMessage(body);
            String code=StringUtils.tryToGetCap(body);
            Log.i(TAG, "onReceive: 是否是验证码"+ll+" "+code);
            //将获取的验证码拷贝到剪切板
//            ClipboardManager manager= (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//            manager.setText(code);
//            Toast.makeText(context, "验证码"+code+"已拷贝到剪切板", Toast.LENGTH_SHORT).show();
            showNotificationBar(context,code);
        }

    }

    private void showNotificationBar(Context context, String code) {
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context);
        builder.setAutoCancel(true);//点击自动消失
        RemoteViews remoteViews=new RemoteViews(context.getPackageName(),R.layout.layout_notificationbar);
        remoteViews.setTextViewText(R.id.tv_notification,"当前验证码"+code+"(点击可复制)");
        builder.setContent(remoteViews);
        Intent intent=new Intent("com.ztz.touchdemo.NotificationClick");
        intent.putExtra("smscode",code);
        PendingIntent pi=PendingIntent.getBroadcast(context,111,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ll_content,pi);
        Notification notification=builder.build();
        NotificationManager manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);//获取通知栏管理器
        manager.notify(111,notification);
    }
}
