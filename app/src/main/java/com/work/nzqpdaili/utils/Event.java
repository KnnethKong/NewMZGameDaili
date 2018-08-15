package com.work.nzqpdaili.utils;

/**
 * Created by Range on 2016/8/12.
 */
public class Event {
    private String mMsg;
    private String mOrder;
    private String mType;
    public Event(String msg) {
        // TODO Auto-generated constructor stub
        mMsg = msg;
    }
    public Event(String msg, String order){
        mMsg = msg;
        mOrder=order;
    }
    public String getMsg(){
        return mMsg;
    }
    public String getmOrder(){
        return mOrder;
    }
    public String getmType(){
        return mType;
    }
    public Event(String mMsg, String mOrder, String mType) {
        this.mMsg = mMsg;
        this.mOrder = mOrder;
        this.mType = mType;
    }

}
