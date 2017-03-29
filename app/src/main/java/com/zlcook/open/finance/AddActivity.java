package com.zlcook.open.finance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.zlcook.open.finance.bean.Consume;
import com.zlcook.open.finance.db.DBAdopter;
import com.zlcook.open.finance.presenter.ConsumePresenter;

/**
 * 添加收支界面
 */
public class AddActivity extends AppCompatActivity {
    private EditText et_money;
    private EditText et_comment;
    private ConsumePresenter presenter;
    private LinearLayout ll_type;
    private int flage = 0;//默认支出
    private Spinner spinner;
    private String type="收入" ;//类型
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        et_money = (EditText) findViewById(R.id.et_money);
        et_comment = (EditText) findViewById(R.id.et_comment);
        spinner = (Spinner)findViewById(R.id.sp_type);
        ll_type = (LinearLayout) findViewById(R.id.ll_type);
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

    /**
     * 点击支出选项
     * @param v
     */
    public  void zhichu(View v){
        flage = 0;
        ll_type.setVisibility(View.VISIBLE);
    }

    /**
     * 点击收入选项
     * @param v
     */
    public  void shouru(View v){
        flage = 1;
        ll_type.setVisibility(View.GONE);
    }

    /**
     * 点击确定按钮
     * @param v
     */
    public void sure(View v) {
        //获取填写的钱和备注
        String money = et_money.getText().toString().trim();
        String comment = et_comment.getText().toString().trim();
        try{
            if (money.equals("") )//填写错误则提示
                Toast.makeText(AddActivity.this, "请填写完整", Toast.LENGTH_SHORT).show();
            else {
                // 将收支信息保存到数据库
                Float f_money= Float.parseFloat(money);
                if( flage == 0)
                   type = (String) spinner.getSelectedItem();
                Consume consume = new Consume(DBAdopter.USER.getId(),f_money,comment,flage,type);
                if(presenter.add(consume)){
                    Toast.makeText(AddActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(AddActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(AddActivity.this, "请填写正确金额", Toast.LENGTH_SHORT).show();
        }

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

}
