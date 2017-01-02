package com.zlcook.open.finance.net;

import com.zlcook.open.finance.bean.ResponseBody;
import com.zlcook.open.finance.presenter.Ioperation;


/**
 * Created by 李欢 on 2016/8/29.
 * 客户端
 */
public class ClientThread extends Thread {
    private Ioperation operation;
    private ResponseBody requestBody ;
    public ClientThread(Ioperation operation , ResponseBody requestBody)
    {
        this.operation =operation ;
        this.requestBody=requestBody;
    }


    @Override
    public void run()
    {

    }
}
