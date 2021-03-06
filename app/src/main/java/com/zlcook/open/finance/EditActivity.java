package com.zlcook.open.finance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.zlcook.open.finance.bean.Consume;
import com.zlcook.open.finance.presenter.ConsumePresenter;

import java.util.Date;


/**
 * 编辑收支条目
 */
public class EditActivity extends AppCompatActivity {
    private EditText et_money;
    private EditText et_comment;
    private ConsumePresenter presenter;
    private RadioButton rb_flage_0,rb_flage_1;

    private Spinner spinner;
    private LinearLayout ll_type;
    private int flage = 0;//默认支出
    private int id;
    private Consume consume;
    private String type="收入" ;//类型
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        et_money = (EditText) findViewById(R.id.et_money);
        et_comment = (EditText) findViewById(R.id.et_comment);
        rb_flage_0 =(RadioButton) findViewById(R.id.rb_flage_0);
        rb_flage_1 =(RadioButton) findViewById(R.id.rb_flage_1);
        ll_type = (LinearLayout) findViewById(R.id.ll_type);

        spinner = (Spinner)findViewById(R.id.sp_type);
        presenter = new ConsumePresenter(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //拿到被选择项的值
                type = (String) spinner.getSelectedItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });
    }
    public void onStart() {
        super.onStart();
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        consume =presenter.getConsume(id);
        flage =consume.getFlage();
        et_comment.setText(consume.getComment());
        et_money.setText(consume.getMoney()+"");
        if( flage ==0) {
            ll_type.setVisibility(View.VISIBLE);
            rb_flage_0.setChecked(true);
        } else {
            ll_type.setVisibility(View.GONE);
            rb_flage_1.setChecked(true);
        }
    }
    public  void zhichu(View v){
        flage = 0;
        ll_type.setVisibility(View.VISIBLE);
    }
    public  void shouru(View v){
        flage = 1;
        ll_type.setVisibility(View.GONE);
    }

    public void sure(View v) {
        String money = et_money.getText().toString().trim();
        String comment = et_comment.getText().toString().trim();
        try{
            if (money.equals("") || comment.equals(""))
                Toast.makeText(EditActivity.this, "请填写完整", Toast.LENGTH_SHORT).show();
            else {
                Float f_money= Float.parseFloat(money);
                consume.setComment(comment);
                consume.setMoney(f_money);
                consume.setFlage(flage);
                consume.setType(type);
                if(presenter.updateConsume(consume)){
                    Toast.makeText(EditActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, ListActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(EditActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(EditActivity.this, "请填写正确金额", Toast.LENGTH_SHORT).show();
        }

    }
}
