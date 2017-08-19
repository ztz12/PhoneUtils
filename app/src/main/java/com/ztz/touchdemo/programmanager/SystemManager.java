package com.ztz.touchdemo.programmanager;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ztz.touchdemo.R;

/**
 * Created by wqewqe on 2017/6/6.
 */

public class SystemManager extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_system,container,false);
        return view;
    }
}
