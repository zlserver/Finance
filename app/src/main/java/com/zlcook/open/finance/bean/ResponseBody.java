package com.zlcook.open.finance.bean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by dell on 2016/12/31.
 */
public class ResponseBody<T> {
    private String operateion;//操作
    private boolean status; //状态,操作成功或失败
    private int count; //result集合中包含元素个数
    private ArrayList<T> result;


    public ResponseBody(boolean status,  String operateion) {
        this.status = status;
        this.operateion = operateion;

    }

    public boolean getStatus() {
        return status;
    }

    public int getCount() {
        return count;
    }

    public ArrayList<T> getResult() {
        return result;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setResult(ArrayList<T> result) {
        this.result = result;
        if( this.result!=null)
        this.count =result.size();
    }

    public void setOperateion(String operateion) {
        this.operateion = operateion;
    }

    public String getOperateion() {
        return operateion;
    }
}
