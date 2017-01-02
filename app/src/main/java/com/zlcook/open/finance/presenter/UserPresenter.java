package com.zlcook.open.finance.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.zlcook.open.finance.bean.User;
import com.zlcook.open.finance.db.DBAdopter;

import java.util.ArrayList;


/**
 * Created by dell on 2016/12/31.
 */
public class UserPresenter {

    private Context context;
    public  UserPresenter(Context context){
        this.context= context;
    }

    public boolean checkUser(User user){
        if( user ==null || TextUtils.isEmpty(user.getUsername())|| TextUtils.isEmpty(user.getPassword()))
            return false;
        else
            return true;
    }
    public boolean editPassword(User user){
        DBAdopter dbAdopter = new DBAdopter(this.context);
        dbAdopter.open();
        long result =dbAdopter.Update(user.getId()+"",user);
        dbAdopter.close();
        return (result ==1)?true:false;
    }
    public boolean existUser(String name){
        DBAdopter dbAdopter = new DBAdopter(this.context);
        dbAdopter.open();
        User us =dbAdopter.getUser(name);

        dbAdopter.close();
        if( us ==null)
            return false;
        else
            return true;
    }
    public boolean  login(User user){
        DBAdopter dbAdopter = new DBAdopter(this.context);
        dbAdopter.open();
        User us =dbAdopter.getUser(user.getUsername(),user.getPassword());
        dbAdopter.close();

        if(us ==null)
           return false;
        else{
            user.setId(us.getId());
            ArrayList<User> list =new ArrayList<User>();
            list.add(us);
            return true;
        }

    }
    public boolean register(User user){
        DBAdopter dbAdopter = new DBAdopter(this.context);
        dbAdopter.open();
        long res =dbAdopter.insert(user);
        dbAdopter.close();
        if(res ==-1)
            return false;
        else
            return true;
    }

}
