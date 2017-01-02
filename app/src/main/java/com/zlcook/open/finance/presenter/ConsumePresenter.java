package com.zlcook.open.finance.presenter;

import android.content.Context;

import com.zlcook.open.finance.bean.Consume;
import com.zlcook.open.finance.bean.User;
import com.zlcook.open.finance.db.DBAdopter;

import java.util.ArrayList;

/**
 * Created by dell on 2017/1/2.
 */
public class ConsumePresenter {
    private Context context;
    public  ConsumePresenter(Context context){
        this.context= context;
    }

    public boolean add(Consume consume){
        DBAdopter dbAdopter = new DBAdopter(this.context);
        dbAdopter.open();
        long result = dbAdopter.consume_insert(consume);
        dbAdopter.close();

        if( result !=-1)
            return true;
        else
           return false;
    }

    public  ArrayList<Consume> getConsumes(int userid){
        DBAdopter dbAdopter = new DBAdopter(this.context);
        dbAdopter.open();
        ArrayList<Consume> list =dbAdopter.getConsumes(userid+"");
        dbAdopter.close();
        return list;
    }

    public boolean updateConsume(Consume consume){
        DBAdopter dbAdopter = new DBAdopter(this.context);
        dbAdopter.open();
        long result =dbAdopter.consume_Update(consume.getId()+"",consume);
        dbAdopter.close();
        return (result ==1)?true:false;
    }
    public Consume getConsume(int id){
        DBAdopter dbAdopter = new DBAdopter(this.context);
        dbAdopter.open();
        Consume consume =dbAdopter.getConsume(id+"");
        dbAdopter.close();
        return consume;
    }
    public  boolean delete(int id){
        DBAdopter dbAdopter = new DBAdopter(this.context);
        dbAdopter.open();
        long result =dbAdopter.consume_delete(id+"");
        dbAdopter.close();
        if( result ==1)
            return true;
        return false;
    }

}
