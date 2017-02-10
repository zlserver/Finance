package com.zlcook.open.finance.bean;


import java.util.Date;

/**
 * 收支类
 * Created by dell on 2016/12/31.
 */
public class Consume {
    private int id;
    private int user_id;//用户id
    private float money;  //钱
    private String comment;  //备注
    private int flage;//1收入；0支出
    private  Date time;  //时间

    public Consume(int user_id,float money, String comment, int flage) {
        this.user_id = user_id;
        this.money = money;
        this.comment = comment;
        this.flage = flage;
        this.time =new Date();
    }

    public Consume(int id, int user_id,float money, String comment, int flage, Date time) {
        this(user_id,money,comment,flage);
        this.id = id;
        this.time = time;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getFlage() {
        return flage;
    }

    public void setFlage(int flage) {
        this.flage = flage;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
