package com.zlcook.open.finance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.zlcook.open.finance.bean.Consume;
import com.zlcook.open.finance.db.DBAdopter;
import com.zlcook.open.finance.presenter.ConsumePresenter;


public class AddActivity extends AppCompatActivity {
    private EditText et_money;
    private EditText et_comment;
    private ConsumePresenter presenter;
    private int flage = 0;//默认支出

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        et_money = (EditText) findViewById(R.id.et_money);
        et_comment = (EditText) findViewById(R.id.et_comment);

        presenter = new ConsumePresenter(this);
    }

    public  void zhichu(View v){
        flage = 0;
    }

    public  void shouru(View v){
        flage = 1;
    }

    public void sure(View v) {
        String money = et_money.getText().toString().trim();
        String comment = et_comment.getText().toString().trim();
        try{
            if (money.equals("") || comment.equals(""))
                Toast.makeText(AddActivity.this, "请填写完整", Toast.LENGTH_SHORT).show();
            else {
                Float f_money= Float.parseFloat(money);
                Consume consume = new Consume(DBAdopter.USER.getId(),f_money,comment,flage);
                if(presenter.add(consume)){
                    Toast.makeText(AddActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(AddActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(AddActivity.this, "请填写正确金额", Toast.LENGTH_SHORT).show();
        }

    }

    //监听返回键退出事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        return false;
    }
}
