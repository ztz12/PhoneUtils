package com.ztz.touchdemo.flashlight;

import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ztz.touchdemo.R;

public class FlashLightActivity extends AppCompatActivity implements View.OnClickListener{
    //所有View的颜色
    private int[] mColorGroup=new int[]{
            0xfff5f5f5,
            0xfff46543,
            0xfffebb22,
            0xffecee21,
            0xffbae534,
            0xff65ccdb,
            0xff75ddba,
            0xff9834cc
    };
    //所有View的id
    private int[] mImaIds={
            R.id.iv_top,
            R.id.iv_top_right,
            R.id.iv_right,
            R.id.iv_right_bottom,
            R.id.iv_bottom,
            R.id.iv_left_bottom,
            R.id.iv_left,
            R.id.iv_top_left  
    };
    //选中View的id;
    int mSelectedId=0;
    int mSelectedColor=0;
    //Ctrl + E 打开之前关闭的文件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_light);
        setListener();
        //设置默认选中状态
        mSelectedId=mImaIds[0];
        ImageView def= (ImageView) findViewById(mSelectedId);
        def.setSelected(true);
    }

    private void setListener() {
        for(int i=0;i<mImaIds.length;i++){
            ImageView iv= (ImageView) findViewById(mImaIds[i]);
            iv.setOnClickListener(this);
            ShapeDrawable shapeDrawable=new ShapeDrawable(new OvalShape());
            shapeDrawable.getPaint().setColor(mColorGroup[i]);
            iv.setBackground(shapeDrawable);
        }

    }

    @Override
    public void onClick(View v) {
        //获取上一次选中的View
        ImageView beforeIv= (ImageView) findViewById(mSelectedId);
        //当前选中的View
        ImageView selected= (ImageView) v;
        //当前选中的View不处于选中状态
       if(!selected.isSelected()) {
           if(beforeIv!=null){
               beforeIv.setSelected(false);
           }
           //设置本次选中的View状态
           selected.setSelected(true);
           //记录本次选中View的ID,变更为上一次选中的View ID
           mSelectedId=v.getId();
           //需要拿到本次选中View对应的颜色值
           int selectedID=v.getId();
           //根据本次选的View的id，去查找mImaIds数组里面的id，找到匹配的id值的下标，
           //再根据下标去找颜色数组里面的值
           int index=0;
           for(int i=0;i<mImaIds.length;i++){
               if(mImaIds[i]==selectedID){
                   index=i;
               }
           }
           mSelectedColor=mColorGroup[index];
       }
    }
    public void startScreen(View view){
        Intent intent=new Intent(this,ScreenLightActivity.class);
        intent.putExtra("color",mSelectedColor);
        startActivity(intent);
    }
    public void startLight(View view){
        startActivity(new Intent(this,LightActivity.class));
    }
}
