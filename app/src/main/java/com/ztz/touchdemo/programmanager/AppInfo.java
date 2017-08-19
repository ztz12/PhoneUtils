package com.ztz.touchdemo.programmanager;

import android.graphics.drawable.Drawable;

/**
 * App信息类 封装App信息
 * Created by wqewqe on 2017/6/5.
 */

public class AppInfo {
    private String name;
    private String packName;
    private Drawable icon;

    public AppInfo(String name, String packName, Drawable icon) {
        this.name = name;
        this.packName = packName;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "AppInfo{"+"name='"+name+'\''+",packName='"+packName+'\''+",icon="
                +icon+'}';
    }
}
