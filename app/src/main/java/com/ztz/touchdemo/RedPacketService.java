package com.ztz.touchdemo;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;
//AccessibilityService 使用这个类可以开发用于给用户提供替换或者是增强反馈的辅助功能服务。
public class RedPacketService extends AccessibilityService {
    private static final String TAG = "RedPacketService";
    /**
     * 微信几个页面的包名+地址。用于判断在哪个页面
     * LAUCHER-微信聊天界面
     * LUCKEY_MONEY_RECEIVER-点击红包弹出的界面
     * LUCKEY_MONEY_DETAIL-红包领取后的详情界面
     */
    private String LAUCHER = "com.tencent.mm.ui.LauncherUI";
    private String LUCKEY_MONEY_DETAIL = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";
    private String LUCKEY_MONEY_RECEIVER = "com.tencent.mm.plugin.luckymoney.ui.En_fba4b94f";

    /**
     * 必须重写：接受系统发来的event。在你注册的event发生时被调用。会被调用多次。
     * @param event
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType=event.getEventType();//获取动作类型
        switch (eventType){
            //判断辅助服务触发的事件是否是通知栏改变事件
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                List<CharSequence> texts=event.getText();
                for (CharSequence text : texts) {
                    String content=text.toString();
                    if (!TextUtils.isEmpty(content)) {
                            if(content.contains("[微信红包]")){
                                //微信红包来了 打开聊天界面
                                Log.i(TAG, "onAccessibilityEvent: 微信红包来啦");
                                openWeChatPage(event);
                            }
                         }
                    }
                    break;
            //判断辅助服务触发的事件是否是窗体改变事件
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED://界面跳转
                String className=event.getClassName().toString();//获取当前界面类名
                //判断是否聊天界面
                if(LAUCHER.equals(className)){
                    Log.e(TAG, "onAccessibilityEvent: 我倒微信聊天界面啦" );
                    //获取当前聊天界面跟布局
                    AccessibilityNodeInfo rootNode=getRootInActiveWindow();
                    //开始查找红包i
                    findRedPacket(rootNode);
                }
                if(LUCKEY_MONEY_RECEIVER.equals(className)){
                    Log.e(TAG, "onAccessibilityEvent:点击红包弹出界面 ");
                    AccessibilityNodeInfo rootNode=getRootInActiveWindow();
                    openRedPacket(rootNode);
                }
                if(LUCKEY_MONEY_DETAIL.equals(className)){
                    Log.e(TAG, "onAccessibilityEvent:红包领取界面 ");
                    backHome();
                }
                break;
                }
        }

    private void backHome() {
        Intent home=new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }

    /**
     * 打开红包
     * @param rootNode
     */
    private void openRedPacket(AccessibilityNodeInfo rootNode) {
        for(int i=0;i<rootNode.getChildCount();i++){
            AccessibilityNodeInfo childInf=rootNode.getChild(i);
            if("android.widget.Button".equals(childInf.getClassName())){
                childInf.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
            openRedPacket(childInf);
        }
    }

    private void findRedPacket(AccessibilityNodeInfo rootNode) {
        if(rootNode!=null){
            for(int i=rootNode.getChildCount()-1;i>=0;i--){
                AccessibilityNodeInfo chileNote=rootNode.getChild(i);
                if(chileNote==null){
                    continue;
                }
                CharSequence text=chileNote.getText();
                if(text!=null&&text.toString().equals("领取红包")){
                    Log.e(TAG, "findRedPacket: "+text.toString() );
                    AccessibilityNodeInfo parent=chileNote.getParent();
                    while (parent!=null){
                        if(parent.isClickable()){
                            //模拟点击
                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            break;
                        }
                        parent=parent.getParent();
                    }
                }
                findRedPacket(chileNote);
            }
        }
    }

    /**
     * 打开通知栏
     * @param event
     */

    private void openWeChatPage(AccessibilityEvent event) {
        if(event.getParcelableData()!=null&&event.getParcelableData() instanceof Notification){
            Notification notification= (Notification) event.getParcelableData();
            PendingIntent pi=notification.contentIntent;
            try {
                pi.send();
        } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }

        }

}

    /**
     * 必须重写：系统要中断此service时会调用。会被调用多次。
     */
    @Override
    public void onInterrupt() {
        Toast.makeText(this, "我即将被终结", Toast.LENGTH_SHORT).show();

    }

    /**
     * 服务已连接
     */
    @Override
    protected void onServiceConnected() {
        Toast.makeText(this, "抢红包服务开启", Toast.LENGTH_SHORT).show();
        super.onServiceConnected();
    }

    /**
     * 服务断开
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(this, "抢红包服务已经关闭", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }
}
