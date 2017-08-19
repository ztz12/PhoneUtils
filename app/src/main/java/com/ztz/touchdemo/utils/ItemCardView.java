package com.ztz.touchdemo.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ztz.touchdemo.R;


/**
 * View
 * Created by weidong on 2017/5/23.
 */

public class ItemCardView extends RelativeLayout{

    private static final String TAG = "ItemCardView";
    private TextView tv_cardname;
    private ImageView iv_cardimage;

    public ItemCardView(Context context) {
        super(context);
        Log.i(TAG, "ItemCardView: 一个参数的构造方法");
    }

    public ItemCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG, "ItemCardView: 两个参数的构造方法");
        //加载item_cardview.xml布局
        initView(context);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.ItemCardView);
        Drawable cardimage = t.getDrawable(R.styleable.ItemCardView_card_image);
        String cardname = t.getString(R.styleable.ItemCardView_card_name);
        t.recycle();

        Log.i(TAG, "ItemCardView: " + cardimage + "  " + cardname);
        iv_cardimage.setImageDrawable(cardimage);
        tv_cardname.setText(cardname);

    }

    public void initView(Context context){
        //参数一：上下文
        //参数二：布局文件
        View view=View.inflate(context,R.layout.item_cardview,this);
        iv_cardimage = (ImageView) view.findViewById(R.id.iv_cardimage);
        tv_cardname = (TextView) view.findViewById(R.id.tv_cardname);
    }

}
