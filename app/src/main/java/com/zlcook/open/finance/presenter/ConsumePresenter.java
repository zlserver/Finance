package com.zlcook.open.finance.presenter;

import android.content.Context;

import com.zlcook.open.finance.bean.Consume;
import com.zlcook.open.finance.db.DBAdopter;

import java.util.ArrayList;

/**
 * 收支数据操作类
 * Created by dell on 2017/1/2.
 */

public class ConsumePresenter {
    private Context context;
    public  ConsumePresenter(Context context){
        this.context= context;
    }

    /**
     * 添加收支
     * @param consume
     * @return
     */
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

    /**
     * 获取数据库中所有收支
     * @param userid
     * @return
     */
    public  ArrayList<Consume> getConsumes(int userid){
        DBAdopter dbAdopter = new DBAdopter(this.context);
        dbAdopter.open();
        ArrayList<Consume> list =dbAdopter.getConsumes(userid+"");
        dbAdopter.close();
        return list;
    }

    /**
     * 更新收支记录
     * @param consume
     * @return
     */
    public boolean updateConsume(Consume consume){
        DBAdopter dbAdopter = new DBAdopter(this.context);
        dbAdopter.open();
        long result =dbAdopter.consume_Update(consume.getId()+"",consume);
        dbAdopter.close();
        return (result ==1)?true:false;
    }

    /**
     * 根据id查询收支记录
     * @param id
     * @return
     */
    public Consume getConsume(int id){
        DBAdopter dbAdopter = new DBAdopter(this.context);
        dbAdopter.open();
        Consume consume =dbAdopter.getConsume(id+"");
        dbAdopter.close();
        return consume;
    }

    /**
     * 根据id删除收支记录
     * @param id
     * @return
     */
    public  boolean delete(int id){
        DBAdopter dbAdopter = new DBAdopter(this.context);
        dbAdopter.open();
        long result =dbAdopter.consume_delete(id+"");
        dbAdopter.close();
        if( result ==1)
            return true;
        return false;
    }

    /**
     * 根据年份信息获取12个月中每个月的收入总数
     * @param year 年份
     * @return 数组一共包含12个数据
     */
    public float[] getShouMoney(int userid,String year){
        DBAdopter dbAdopter = new DBAdopter(this.context);
        dbAdopter.open();
        float[] month_money= new float[12];
        for( int i =1;i<=12;i++){
            String year_month = year_month(year,i);
            float  totalmoney =dbAdopter.consume_getMoneyForMonth(userid+"",year_month,1+"");
            month_money[i-1]=totalmoney;
        }
        dbAdopter.close();
        return month_money;
    }
    /**
     * 根据年份信息获取12个月中每个月的支出总数
     * @param year 年份
     * @return 数组一共包含12个数据
     */
    public float[] getZhiMoney(int userid,String year){
        DBAdopter dbAdopter = new DBAdopter(this.context);
        dbAdopter.open();
        float[] month_money= new float[12];
        for( int i =1;i<=12;i++){
            String year_month = year_month(year,i);
            float  totalmoney =dbAdopter.consume_getMoneyForMonth(userid+"",year_month,0+"");
            month_money[i-1]=totalmoney;
        }
        dbAdopter.close();
        return month_money;
    }
    /**
     * 将年月拼成yyyy-MM格式
     * @param year
     * @param month
     * @return
     */
    public String year_month(String year,int month){
        if(month<=9 && month >=1)
            return year+"-"+"0"+month;
        else
            return year+"-"+month;
    }

}
