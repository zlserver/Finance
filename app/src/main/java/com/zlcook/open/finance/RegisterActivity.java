package com.zlcook.open.finance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zlcook.open.finance.bean.User;
import com.zlcook.open.finance.presenter.UserPresenter;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_username;
    private EditText et_password;

    private User user;
    private UserPresenter userPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        user = new User();
        userPresenter = new UserPresenter(this);
    }

    public void register(View v) {

        String name = et_username.getText().toString();
        String password = et_password.getText().toString();
        user.setUsername(name);
        user.setPassword(password);
        //基础校验
        boolean falge = userPresenter.checkUser(user);
        if( falge){ //弹出对话框提示
            if( !userPresenter.existUser(user.getUsername())){
                boolean res =userPresenter.register(user);
                if( res)
                {
                    //注册成功
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(RegisterActivity.this,"用户名已经存在",Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(RegisterActivity.this,"请填写账号密码",Toast.LENGTH_SHORT).show();
        }

    }
}
