package com.ztz.touchdemo;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by wqewqe on 2017/6/1.
 */

public class MoveLayout extends LinearLayout {
    private ViewDragHelper helper;
    public MoveLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        helper=ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            //决定view是否可以拖动
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }
            //水平
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }
            //垂直
            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }
        });
    }
    //拦截事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return helper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        helper.processTouchEvent(event);
        return true;
    }
}
