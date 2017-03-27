package com.zlcook.open.finance.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.zlcook.open.finance.bean.Consume;
import com.zlcook.open.finance.bean.User;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 数据库操作类
 * Created by Administrator on 2016/7/15.
 */
public class DBAdopter implements Serializable {
   //用户表
    public  static User USER;
    //数据库基本信息
    private static final String DB_NAME="myfinance.db";//数据库名
    private static final String USER_TABLE ="userinfo";//用户信息表名
    private static final String CONSUME_TABLE ="consumeinfo";//消费表表名
    private static final int DB_VERSION=3;//版本号

    //用户表基本信息
    private static final String USER_ID ="id";//学生表的所有列名
    private static final String USER_USERNAME ="username";
    private static final String USER_PASSWORD ="password";

    //收支表基本信息
    private static final String CONSUME_ID ="id";//图书表的所有列名
    private static final String CONSUME_USER_ID ="user_id"; //消费者id
    private static final String CONSUME_MOENY ="money"; //消费额
    private static final String CONSUME_COMMENT="comment";//备注
    private static final String CONSUME_FLAGE="flage";//收支标志
    private static final String CONSUME_TYPE="type";//收支类型
    private static final String CONSUME_TIME="time"; //时间
    private static final String CONSUME_TIME_LONG="longmills"; //时间

    private SQLiteDatabase db;//
    private final Context context;//上下文对象，通常是this
    private  DBOpenHelper dbOpenHelper;//内部类类型

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        public DBAdopter(Context _context) {
            context = _context;
        }//构造函数

        public void open() throws SQLiteException {//打开数据库
            dbOpenHelper = new DBOpenHelper(context,DB_NAME, null, DB_VERSION);
            try {
                db = dbOpenHelper.getWritableDatabase();//以写的方式打开或创建数据库

            } catch (SQLiteException e) {
                db = dbOpenHelper.getReadableDatabase();//以读的方式打开或创建数据库
            }
        }

        public void close(){//关闭数据库
            if(db!=null)
            {
                db.close();
                db=null;
            }
        }
//--------------------------数据库用户表操作----------------------------------
        public long insert(User s1){//增加一个用户
            ContentValues newValues= new ContentValues();
            newValues.put(USER_USERNAME,s1.getUsername());
            newValues.put(USER_PASSWORD,s1.getPassword());
            return db.insert(USER_TABLE,null,newValues);
        }

        public long delete(String id){//删除一个用户
            return db.delete(USER_TABLE,USER_ID+" like ? ",new String[]{id});
        }

        public long deleteByName(String name){//删除一个用户
            return db.delete(USER_TABLE, USER_USERNAME +" like ? ",new String[]{name});
        }
        public long deleteall(){//删除所有人
            return db.delete(USER_TABLE,null,null);
        }

    public long Update(String id,User user){//修改用户信息
        ContentValues updateValues=new ContentValues();
        updateValues.put(USER_ID,user.getId());
        updateValues.put(USER_USERNAME,user.getUsername());
        updateValues.put(USER_PASSWORD,user.getPassword());

        return db.update(USER_TABLE,updateValues,USER_ID+" like ? ",new String[]{id});
    }

    private ArrayList<User> ConvertToStudent(Cursor cursor){//将指针类型转换为User数组
        int resultConts=cursor.getCount();
        ArrayList<User> peoples=new ArrayList<User>();
        if(resultConts==0||!cursor.moveToFirst())return null;
        else
        {
            for(int i=0;i<resultConts;i++)
            {
                String s1=cursor.getString(cursor.getColumnIndex(USER_ID));
                String s2=cursor.getString(cursor.getColumnIndex(USER_USERNAME));
                String s3=cursor.getString(cursor.getColumnIndex(USER_PASSWORD));
                User s=new User(Integer.parseInt(s1),s2,s3);
                peoples.add(s);
                cursor.moveToNext();
            }
            return peoples;
        }
    }

    public User getUser(String username){//查找，按用户名精确查找
        Cursor cursor=db.query(USER_TABLE,new String[]{USER_ID, USER_USERNAME, USER_PASSWORD},USER_USERNAME+" = ? ",new String[]{username} ,null,null,null,null);
        if(cursor.getCount()==0||!cursor.moveToFirst())
            return null;
        ArrayList<User> list= ConvertToStudent(cursor);
        if(list !=null && list.size()==1)
            return list.get(0);
        return null;
    }
    public User getUser(String username,String password){//查找，按学号精确查找
        Cursor cursor=db.query(USER_TABLE,new String[]{USER_ID, USER_USERNAME, USER_PASSWORD},USER_USERNAME+" = ? and "+USER_PASSWORD +"= ?",new String[]{username,password} ,null,null,null,null);

        if(cursor.getCount()==0||!cursor.moveToFirst())
            return null;
        ArrayList<User> list= ConvertToStudent(cursor);
        if(list !=null && list.size()==1)
            return list.get(0);
        return null;
    }
//-------------------------数据库收支表操作----------------------------------

    public long consume_insert(Consume consume)//插入一笔收支
    {
        ContentValues newValues=new ContentValues();
        newValues.put(CONSUME_COMMENT,consume.getComment());
        newValues.put(CONSUME_USER_ID,consume.getUser_id());
        newValues.put(CONSUME_MOENY,consume.getMoney());
        newValues.put(CONSUME_FLAGE,consume.getFlage());
        newValues.put(CONSUME_TYPE,consume.getType());
        newValues.put(CONSUME_TIME,sdf.format(consume.getTime()));
        long longmills = consume.getTime().getTime();
        newValues.put(CONSUME_TIME_LONG,longmills);
        return db.insert(CONSUME_TABLE,null,newValues);
    }

    public long consume_delete(String no)//删除一笔收支
    {
        return db.delete(CONSUME_TABLE, CONSUME_ID +" like ? ",new String[]{no});
    }

    public long consume_Update(String id,Consume consume){//修改消费信息
        ContentValues newValues=new ContentValues();
        newValues.put(CONSUME_ID,consume.getId());
        newValues.put(CONSUME_USER_ID,consume.getUser_id());
        newValues.put(CONSUME_COMMENT,consume.getComment());
        newValues.put(CONSUME_MOENY,consume.getMoney());
        newValues.put(CONSUME_FLAGE,consume.getFlage());
        newValues.put(CONSUME_TYPE,consume.getType());
        newValues.put(CONSUME_TIME,sdf.format(consume.getTime()));

        return db.update(CONSUME_TABLE,newValues, CONSUME_ID +" = ? ",new String[]{id});
    }

    private ArrayList<Consume> ConvertToConsume(Cursor cursor){//将指针类型转换为Book数组
        int resultConts=cursor.getCount();
        ArrayList<Consume> consumes=new ArrayList<Consume>();
        if(resultConts==0||!cursor.moveToFirst())return null;
        else
        {
            for(int i=0;i<resultConts;i++)
            {
                String s1=cursor.getString(cursor.getColumnIndex(CONSUME_ID));
                String s2=cursor.getString(cursor.getColumnIndex(CONSUME_MOENY));
                String s3=cursor.getString(cursor.getColumnIndex(CONSUME_COMMENT));
                String s4=cursor.getString(cursor.getColumnIndex(CONSUME_TIME));
                String s5=cursor.getString(cursor.getColumnIndex(CONSUME_FLAGE));
                String type=cursor.getString(cursor.getColumnIndex(CONSUME_TYPE));
                String user_id=cursor.getString(cursor.getColumnIndex(CONSUME_USER_ID));
                int id=Integer.parseInt(s1);
                int flage = Integer.parseInt(s5);
                float money=Float.parseFloat(s2);
                int userid = Integer.parseInt(user_id);
                try{
                    Date time =sdf.parse(s4);
                    Consume consume=new Consume(id,userid,money,s3,flage,time);
                    consume.setType(type);
                    consumes.add(consume);
                }catch (Exception e){
                    e.printStackTrace();
                }
                cursor.moveToNext();
            }
            return consumes;
        }
    }

    public Consume getConsume(String id){//查找，按id模糊查找
        Cursor cursor=db.query(CONSUME_TABLE,new String[]{CONSUME_ID,CONSUME_USER_ID,CONSUME_MOENY,CONSUME_COMMENT,CONSUME_FLAGE,CONSUME_TYPE,CONSUME_TIME}, CONSUME_ID +" like ? ",new String[]{id} ,null,null,null,null);
        if(cursor.getCount()==0||!cursor.moveToFirst())
            return null;
        ArrayList<Consume> list = ConvertToConsume(cursor);
        if(list !=null && list.size()==1)
            return list.get(0);
        return null;
    }

    /**
     * 根据用户id和年月查询当月收入数据
     * @param userid  用户id
     * @param year_month  年月信息
     * @param flage  1收入；0支出
     * @return
     */
    public float consume_getMoneyForMonth(String userid,String year_month,String flage){
        Cursor cursor=db.query(CONSUME_TABLE,new String[]{CONSUME_ID,CONSUME_USER_ID,CONSUME_MOENY,CONSUME_COMMENT,CONSUME_FLAGE,CONSUME_TYPE,CONSUME_TIME},CONSUME_USER_ID +" like ? and "+CONSUME_FLAGE+" like ?  and "+CONSUME_TIME+" like ?",new String[]{userid,flage,year_month+"%"} ,null,null,null,null);
        if(cursor.getCount()==0||!cursor.moveToFirst())
            return 0;
        ArrayList<Consume> list = ConvertToConsume(cursor);
        float money =0;
        for( Consume c : list){
            money+=c.getMoney();
        }
        return money;
    }
    public ArrayList<Consume> getConsumes(String userid){//查找
        Cursor cursor=db.query(CONSUME_TABLE,new String[]{CONSUME_ID,CONSUME_USER_ID,CONSUME_MOENY,CONSUME_COMMENT,CONSUME_FLAGE,CONSUME_TYPE,CONSUME_TIME},CONSUME_USER_ID +" like ? ",new String[]{userid} ,null,null,null,null);
        if(cursor.getCount()==0||!cursor.moveToFirst())
            return null;
        return ConvertToConsume(cursor);
    }

    public ArrayList<Consume> getConsumes(String userid,String startTime,String endTime){//查找
      //  Cursor cursor=db.query(CONSUME_TABLE,new String[]{CONSUME_ID,CONSUME_USER_ID,CONSUME_MOENY,CONSUME_COMMENT,CONSUME_FLAGE,CONSUME_TYPE,CONSUME_TIME},CONSUME_USER_ID +" like ? and datetime("+CONSUME_TIME+") >= datetime(?) and datetime("+CONSUME_TIME+") <= datetime(?) "  ,new String[]{userid,startTime,endTime} ,null,null,null,null);
        long startLong =0L;
        long endLong =0L;
        try {
            SimpleDateFormat sddd = new SimpleDateFormat("yyyy-MM-dd");
            startLong= sddd.parse(startTime).getTime();
            endLong= sddd.parse(endTime).getTime();
            endLong+=24*60*60*1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Cursor cursor=db.query(CONSUME_TABLE,new String[]{CONSUME_ID,CONSUME_USER_ID,CONSUME_MOENY,CONSUME_COMMENT,CONSUME_FLAGE,CONSUME_TYPE,CONSUME_TIME},CONSUME_USER_ID +" like ? and  "+CONSUME_TIME_LONG + " >= ? and  "+CONSUME_TIME_LONG + " <= ? "  ,new String[]{userid,""+startLong,endLong+""} ,null,null,null,null);

        if(cursor.getCount()==0||!cursor.moveToFirst()) {
            System.out.print("查询为null...........");
            return null;
        }
        return ConvertToConsume(cursor);
    }
    private static class DBOpenHelper extends SQLiteOpenHelper {//内部类，辅助类
        //方便管理数据库的创建，版本升级，打开等
        private static final String USER_CREATE = "create table " +
                USER_TABLE + " ( " + USER_ID + " integer primary key autoincrement," + USER_USERNAME + " varchar(20),"+ USER_PASSWORD +" varchar(20))";

        private static final String CONSUME_CREATE="create table " +
                CONSUME_TABLE + " ( " + CONSUME_ID + " integer primary key autoincrement,"+ CONSUME_USER_ID + " integer," + CONSUME_MOENY + " varchar(20)," + CONSUME_COMMENT + " varchar(20),"+CONSUME_FLAGE+" integer ,"+ CONSUME_TYPE + " varchar(20),"+CONSUME_TIME+" DATETIME ,"+CONSUME_TIME_LONG+" long )";

        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            //context:上下文对象，name:数据库文件名（不包含路径），factory:可选游标工场（一般为null）,version:版本号
        }

        @Override
        public void onCreate(SQLiteDatabase _db){
            _db.execSQL(USER_CREATE);
            _db.execSQL(CONSUME_CREATE);
        }//创建表
        //execSQL(String)创建表的方法，DB_CREATE创建表的语句
        @Override
        public void onUpgrade(SQLiteDatabase _db,int _oldVersion,int _newVersion){//升级，数据库升级时自动调用
            _db.execSQL("DROP TABLE IF EXISTS "+ USER_TABLE);
            _db.execSQL("DROP TABLE IF EXISTS "+ CONSUME_TABLE);
            onCreate(_db);
        }
    }
}