package com.ztz.touchdemo.sms;

/**
 * Created by wqewqe on 2017/6/3.
 */

public class Message {
    String body;
    String address;
    String thread_id;

    String resultData; //简化后的数据

    public Message(String body, String address, String thread_id) {
        this.body = body;
        this.address = address;
        this.thread_id = thread_id;
    }

    public Message(String body, String address, String thread_id,String resultData) {
        this.body = body;
        this.address = address;
        this.thread_id = thread_id;
        this.resultData = resultData;
    }
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getThread_id() {
        return thread_id;
    }

    public String getResultData() {
        return resultData;
    }

    public void setResultData(String resultData) {
        this.resultData = resultData;
    }

    public void setThread_id(String thread_id) {


        this.thread_id = thread_id;
    }
}
