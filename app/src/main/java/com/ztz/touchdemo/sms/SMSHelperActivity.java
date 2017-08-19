package com.ztz.touchdemo.sms;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ztz.touchdemo.R;

import java.util.ArrayList;

public class SMSHelperActivity extends AppCompatActivity {
    private static final String TAG = "SMSHelperActivity";
    ArrayList<Message> messages;
    RecyclerView rl;
    Switch st;
    private SMSAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smshelper);
        messages=new ArrayList<>();
        rl=(RecyclerView)findViewById(R.id.recycler);
        rl.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SMSAdapter();
        rl.setAdapter(adapter);
        st=(Switch)findViewById(R.id.st);
        st.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.setShowType(!isChecked);
                adapter.notifyDataSetChanged();
                Log.i(TAG, "onCheckedChanged: "+isChecked);
            }
        });


        readSMS();

    }



    class SMSAdapter extends RecyclerView.Adapter<SMSAdapter.ViewHolder>{
        boolean showType=true;//true 完整的， false 简化的
        public void setShowType(boolean showType){
            this.showType=showType;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sms,parent,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            if(showType){
                holder.body.setText(messages.get(position).getBody());
            }else {
                holder.body.setText(messages.get(position).getResultData());
            }
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(SMSHelperActivity.this)
                            .setTitle(messages.get(position).getAddress())
                            .setMessage(messages.get(position).getBody())
                            .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(SMSHelperActivity.this, "取消了删除", Toast.LENGTH_SHORT).show();
                                }
                            });
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            messages.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.show();

                }
            });

        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView address;
            TextView body;
            TextView date;
            LinearLayout item;
            public ViewHolder(View view) {
                super(view);
                item= (LinearLayout) view;
                address=(TextView)view.findViewById(R.id.address);
                body=(TextView)view.findViewById(R.id.body);
                date=(TextView)view.findViewById(R.id.date);
            }
        }
    }
    private static final String[] ALL_THREADS_PROJECTION = {
            "_id", "address", "person", "body",
            "date", "type", "thread_id"};
    private void readSMS() {
        ContentResolver resolver=getContentResolver();
        //读取收件箱内容
        Uri uri=Uri.parse("content://sms/inbox").buildUpon().appendQueryParameter("simple","true").build();
        Cursor cursor=resolver.query(uri,ALL_THREADS_PROJECTION,null,null,"date desc");
        ArrayList<Message> tempMessage=new ArrayList<>();
        while (cursor.moveToNext()){
            String body=cursor.getString(cursor.getColumnIndex("body"));
            String address=cursor.getString(cursor.getColumnIndex("address"));
            String thread_id=cursor.getString(cursor.getColumnIndex("thread_id"));
            Log.i(TAG, "readSMS: "+body+" "+address+" "+thread_id);
            Message message=new Message(body,address,thread_id);
            tempMessage.add(message);
        }
        for (Message message1 : tempMessage) {
            String body=message1.getBody();
            boolean il=StringUtils.isCaptchMessage(body);
            String code=StringUtils.tryToGetCap(body);
            String resultDate="当前验证码为: "+code;
            message1.setResultData(resultDate);
            if(il&&code!=null&&!code.equals("")){
                messages.add(message1);
            }

        }
    }
}
