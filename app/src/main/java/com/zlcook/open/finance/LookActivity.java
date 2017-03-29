package com.zlcook.open.finance;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zlcook.open.finance.bean.Consume;
import com.zlcook.open.finance.presenter.ConsumePresenter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 某个收支条目的详细界面，可以返回、编辑、删除
 */
public class LookActivity extends AppCompatActivity {
    private TextView tv_money;
    private TextView tv_comment;
    private TextView tv_time;
    private TextView tv_flage;
    private TextView tv_type;
    private LinearLayout line_type;



    private int flage=0;//收支类型，1：收入；0：支出
    private Float money ;
    private String comment = "";
    private Date time ;
    private static int id;
    private ConsumePresenter presenter;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);
        tv_money = (TextView) findViewById(R.id.money);
        tv_time = (TextView) findViewById(R.id.time);
        tv_comment = (TextView) findViewById(R.id.comment);
        tv_flage =  (TextView) findViewById(R.id.flage);
        line_type = (LinearLayout) findViewById(R.id.line_type);
        tv_type = (TextView) findViewById(R.id.tv_type);
        presenter = new ConsumePresenter(this);
    }

    /**
     * 显示条目详细内容
     */
    public void onStart() {
        super.onStart();
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        Consume consume =presenter.getConsume(id);
        comment = consume.getComment();
        money = consume.getMoney();
        flage =consume.getFlage();
        time =consume.getTime();
        tv_comment.setText(comment);
        tv_money.setText(money+"");
        tv_flage.setText(flage==0?"支出":"收入");
        tv_time.setText(sdf.format(time));
        if( flage == 0){
            line_type.setVisibility(View.VISIBLE);
            tv_type.setText(consume.getType());
        }
    }

    /**
     * 点击删除记录按钮
     * @param v
     */
    public void delete(View v) {
        buildDialog();
    }

    //删除时弹出的提示对话框
    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LookActivity.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle("将要删除记录");
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                presenter.delete(id);
                Toast.makeText(LookActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LookActivity.this, ListActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setPositiveButton("取消", null);
        builder.show();
    }

    /**
     * 点击返回
     * @param v
     */
    public void back(View v) {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
        finish();
    }

    //监听返回菜单退出事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return false;
        }
        return true;
    }
    /**
     * 点击编辑按钮
     * @param v
     */
    public void edit(View v) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
        finish();
    }


}
