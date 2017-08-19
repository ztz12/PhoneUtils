package com.ztz.touchdemo.programmanager;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ztz.touchdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wqewqe on 2017/6/5.
 */

public class UserSoftManager extends Fragment {
    List<AppInfo> appInfoList=new ArrayList<>();
    private static final String TAG = "UserSoftManager";
    UserSoftAdapter adapter;
    RecyclerView reSoft;
    LinearLayout ll_load;
    TextView tv_number;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reSoft=(RecyclerView)view.findViewById(R.id.recycler_user);
        ll_load=(LinearLayout)view.findViewById(R.id.l_load);
        tv_number=(TextView)view.findViewById(R.id.tv_number);
        reSoft.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new UserSoftAdapter();
        reSoft.setAdapter(adapter);

    }
    class UserSoftAdapter extends RecyclerView.Adapter<UserSoftAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app,parent,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.icon.setImageDrawable(appInfoList.get(position).getIcon());
            holder.appName.setText(appInfoList.get(position).getName());
            holder.packName.setText(appInfoList.get(position).getPackName());
            holder.btn_uninstall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.setAction("android.intent.action.DELETE");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse("package:"+appInfoList.get(position).getPackName()));//必须加"package:" Android约定
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return appInfoList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView icon;
            TextView appName;
            TextView packName;
            Button btn_uninstall;
            public ViewHolder(View view) {
                super(view);
                icon=(ImageView) view.findViewById(R.id.iv_icon);
                appName=(TextView)view.findViewById(R.id.tv_appname);
                packName=(TextView)view.findViewById(R.id.tv_packname);
                btn_uninstall=(Button)view.findViewById(R.id.bt_uninstall);
            }
        }
    }

    /**
     * Fragment 可见时调用生命周期方法
     */
    @Override
    public void onResume() {
        super.onResume();
        MyAsyncTask task=new MyAsyncTask();
        task.execute();
    }

    class MyAsyncTask extends AsyncTask<Void,Integer,List<AppInfo>>{


        @Override
        protected List<AppInfo> doInBackground(Void... params) {
            //将数据存储到集合中
            ArrayList<AppInfo> tempInfo=new ArrayList<>();
            PackageManager manager=getActivity().getPackageManager();
            List<PackageInfo> packageInfos=manager.getInstalledPackages(0);
            for(int i=0;i<packageInfos.size();i++){
                PackageInfo info=packageInfos.get(i);
                String name=info.applicationInfo.loadLabel(manager).toString();
                String packName=info.applicationInfo.packageName;
                Drawable icon=info.applicationInfo.loadIcon(manager);
                int flags=info.applicationInfo.flags;
                try{
                    //模拟卡顿
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //更新UI
                publishProgress(i,packageInfos.size());//调用该方法后，系统会自动调用onProgressUpdate方法，并且将i的值传入改方法中
                if((flags&ApplicationInfo.FLAG_SYSTEM)!=0){
                    //系统应用
                }else {
                    AppInfo appInfo=new AppInfo(name,packName,icon);
                    tempInfo.add(appInfo);

                }
            }

            return tempInfo;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            tv_number.setText("正在扫描第"+values[0].intValue()+"/"+values[1]);

        }

        @Override
        protected void onPostExecute(List<AppInfo> appInfos) {
            UserSoftManager.this.appInfoList.clear();
            UserSoftManager.this.appInfoList.addAll(appInfos);
            adapter.notifyDataSetChanged();
            ll_load.setVisibility(View.INVISIBLE);
        }
    }
}
