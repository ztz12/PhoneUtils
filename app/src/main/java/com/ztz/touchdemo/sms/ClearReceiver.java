package com.ztz.touchdemo.sms;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by wqewqe on 2017/6/3.
 */

public class ClearReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String code=intent.getStringExtra("smscode");
        NotificationManager manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(111);
        ClipboardManager clipboardManager= (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null,code));
        Toast.makeText(context, "验证码"+code+"已拷贝到剪切板", Toast.LENGTH_SHORT).show();
    }
}
