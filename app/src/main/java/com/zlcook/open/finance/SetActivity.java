package com.zlcook.open.finance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zlcook.open.finance.bean.Consume;
import com.zlcook.open.finance.bean.User;
import com.zlcook.open.finance.db.DBAdopter;
import com.zlcook.open.finance.presenter.UserPresenter;

/**
 * 系统设置界面
 */
public class SetActivity extends AppCompatActivity {

    private EditText et_password,et_password2;
    private UserPresenter userPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        et_password =(EditText) findViewById(R.id.et_password);
        et_password2 = (EditText) findViewById(R.id.et_password2);
        userPresenter = new UserPresenter(this);
    }

    /**
     * 确定按钮
     * @param v
     */
    public void sure(View v) {
        String p1 = et_password.getText().toString().trim();
        String p2 = et_password2.getText().toString().trim();
        try{
            if (p1.equals("") || p2.equals(""))
                Toast.makeText(SetActivity.this, "请填写完整", Toast.LENGTH_SHORT).show();
            else {
               if( !p1.equals(p2) ){
                   Toast.makeText(SetActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
               }else{
                   User u = DBAdopter.USER;
                   u.setPassword(p1);
                   if(userPresenter.editPassword(u)){
                       Toast.makeText(SetActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(SetActivity.this, MainActivity.class));
                       finish();
                   }else{
                       Toast.makeText(SetActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                   }
               }
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(SetActivity.this, "请填写正确金额", Toast.LENGTH_SHORT).show();
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
