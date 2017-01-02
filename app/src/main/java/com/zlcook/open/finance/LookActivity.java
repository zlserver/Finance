package com.zlcook.open.finance;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zlcook.open.finance.bean.Consume;
import com.zlcook.open.finance.presenter.ConsumePresenter;

import java.text.SimpleDateFormat;
import java.util.Date;


public class LookActivity extends AppCompatActivity {
    private TextView tv_money;
    private TextView tv_comment;
    private TextView tv_time;
    private TextView tv_flage;



    private int flage=0;//收支类型，1：收入；0：支出
    private Float money ;
    private String comment = "";
    private Date time ;
    private static int id;
    private ConsumePresenter presenter;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);
        tv_money = (TextView) findViewById(R.id.money);
        tv_time = (TextView) findViewById(R.id.time);
        tv_comment = (TextView) findViewById(R.id.comment);
        tv_flage =  (TextView) findViewById(R.id.flage);
        presenter = new ConsumePresenter(this);
    }

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
    }

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
                finish();
            }
        });
        builder.setPositiveButton("取消", null);
        builder.show();
    }

    public void back(View v) {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
        finish();
    }

    public void edit(View v) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }


}
