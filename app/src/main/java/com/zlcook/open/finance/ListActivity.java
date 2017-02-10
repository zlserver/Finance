package com.zlcook.open.finance;

import android.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.zlcook.open.finance.bean.Consume;
import com.zlcook.open.finance.db.DBAdopter;
import com.zlcook.open.finance.presenter.ConsumePresenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 查看收支界面
 */
public class ListActivity extends AppCompatActivity  {
    private ListView lv;

    private ConsumePresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        presenter = new ConsumePresenter(this);
    }

    /**
     * 将适配器显示在onStart方法中是为了让每次显示此界面时都刷新列表
     */
    @Override
    public void onStart() {
        super.onStart();
        lv = (ListView) findViewById(R.id.listView);
        lv.setTextFilterEnabled(true);//设置lv可以被过虑

        final List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat hmsdf =new SimpleDateFormat("HH:mm");
        ArrayList<Consume> consumes =presenter.getConsumes(DBAdopter.USER.getId());
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


    //监听返回菜单键退出事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        return false;
    }

}
