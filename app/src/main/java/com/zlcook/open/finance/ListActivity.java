package com.zlcook.open.finance;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zlcook.open.finance.bean.Consume;
import com.zlcook.open.finance.db.DBAdopter;
import com.zlcook.open.finance.presenter.ConsumePresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 查看收支界面
 */
public class ListActivity extends AppCompatActivity  {
    private ListView lv;
    int mYear, mMonth, mDay;
    TextView tv_start,tv_end;
    final int DATE_DIALOG = 1;

    boolean time_flage = true;//true:设置开始时间，false:设置结束时间
    private ConsumePresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        tv_start = (TextView) findViewById(R.id.ed_start);
        tv_end = (TextView) findViewById(R.id.ed_end);
        presenter = new ConsumePresenter(this);
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 将适配器显示在onStart方法中是为了让每次显示此界面时都刷新列表
     */
    @Override
    public void onStart() {
        super.onStart();

        ArrayList<Consume> consumes =presenter.getConsumes(DBAdopter.USER.getId());
        refresh(consumes);
    }

    public void refresh(ArrayList<Consume> consumes){
        lv = (ListView) findViewById(R.id.listView);
        lv.setTextFilterEnabled(true);//设置lv可以被过虑

        final List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hmsdf =new SimpleDateFormat("HH:mm");
        if (consumes != null)
            for (int i = 0; i < consumes.size(); i++) {
                HashMap<String, Object> item = new HashMap<String, Object>();
                int flage = consumes.get(i).getFlage();
                String flage_Str = (flage ==0)?"-":"+" ;
                item.put("id", consumes.get(i).getId());
                item.put("year_month",sdf.format( consumes.get(i).getTime()));
                item.put("hour_mil", hmsdf.format( consumes.get(i).getTime()));
                item.put("money",flage_Str+consumes.get(i).getMoney()+" 元");
                item.put("comment", "备注："+consumes.get(i).getComment());
                data.add(item);
            }
        //创建SimpleAdapter适配器将数据绑定到item显示控件上
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.listview, new String[]{"year_month","hour_mil","money","comment"}, new int[]{R.id.year_month,R.id.hour_mil,R.id.money,R.id.comment});

        /**
         * 点击某个条目，跳转到详细界面
         */
        if (lv != null) {
            lv.setAdapter(adapter);
            //listView点击事件
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override //设置点击事件要判断这个position是对应原来的list，还是搜索后的list，parent.getAdapter().getItem(position)。
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    System.out.println("--------点击的是-----" + parent.getAdapter().getItem(position).toString());
                    List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

                    data.add((HashMap<String, Object>) parent.getAdapter().getItem(position));
                    Set<String> keySet = data.get(0).keySet();//用Set的keySet方法取出key的集合
                    Iterator<String> it = keySet.iterator();
                    while (it.hasNext()) {
                        String key = it.next();
                        if (key.equals("id")) {
                            int value = (int) data.get(0).get(key);//拿到key对应的value
                            Intent intent = new Intent(ListActivity.this, LookActivity.class);
                            intent.putExtra("id", value);//把id传递到下一个界面
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            });
        }
    }
    /**
     * 开始时间
     * @param v
     */
    public void startTime(View v) {
        time_flage=true;
        showDialog(DATE_DIALOG);
    }

    /**
     * 结束时间
     * @param v
     */
    public void endTime(View v) {
        time_flage=false;
        showDialog(DATE_DIALOG);
    }
    /**
     * 搜索
     * @param v
     */
    public void sousu(View v) {

        String startTime = tv_start.getText().toString();
        String endTime = tv_end.getText().toString();
       // Toast.makeText(this,startTime+" : "+endTime,Toast.LENGTH_LONG).show();
       // startTime+=" 00:00:00";
       // endTime+=" 00:00:00";
        ArrayList<Consume> consumes =presenter.getConsumes(DBAdopter.USER.getId(),startTime,endTime);


        refresh(consumes);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            setTextValue();
        }
    };
    /**
     * 设置日期 利用StringBuffer追加
     */
    public void setTextValue() {

        String time= dataStr(mYear,mMonth,mDay);
        if( time_flage )
            tv_start.setText(time);
        else
            tv_end.setText(time);
    }

    public String dataStr(int year,int month,int day){
        StringBuffer sb = new StringBuffer().append(year).append("-").append(month + 1).append("-").append(day);
        String time= sb.toString();
        return time;
    }
    //监听返回菜单键退出事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        return false;
    }

}
