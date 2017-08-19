package com.ztz.touchdemo.programmanager;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ztz.touchdemo.R;

import java.util.ArrayList;
import java.util.List;

public class SoftManagerActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tab;
    ViewPager vp;
    MyPageAdapter adapter;
    List<Fragment> fragmentList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_manager);
        toolbar=(Toolbar)findViewById(R.id.toolBar);
        toolbar.setTitle("软件管理");
        setSupportActionBar(toolbar);
        tab=(TabLayout)findViewById(R.id.tab);
        vp=(ViewPager)findViewById(R.id.soft_vp);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);
        }
        fragmentList.add(new UserSoftManager());
        fragmentList.add(new SystemManager());
        adapter=new MyPageAdapter(getSupportFragmentManager(),fragmentList);
        vp.setAdapter(adapter);
        tab.setupWithViewPager(vp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
    class MyPageAdapter extends FragmentPagerAdapter{

        List<Fragment> fragmentList;
        public MyPageAdapter(FragmentManager fm,List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList=fragmentList;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "用户软件";
                case 1:
                    return "系统应用";

            }
            return super.getPageTitle(position);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
