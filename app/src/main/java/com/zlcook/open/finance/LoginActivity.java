package com.zlcook.open.finance;

import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.EditText;
import android.widget.Toast;

import com.zlcook.open.finance.bean.User;
import com.zlcook.open.finance.db.DBAdopter;
import com.zlcook.open.finance.presenter.UserPresenter;


public class LoginActivity extends AppCompatActivity {

    private User user;
    private UserPresenter userPresenter;
    private EditText etUserName,etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        etUserName = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        user = new User();
        userPresenter = new UserPresenter(this);
        //回显用户名和密码
        readAccount();
    }


    public void login(View v) {

        String name = etUserName.getText().toString();
        String password = etPassword.getText().toString();
        user.setUsername(name);
        user.setPassword(password);

        //基础校验
        boolean falge = userPresenter.checkUser(user);
        if( falge){ //弹出对话框提示
            boolean res =userPresenter.login(user);
            if( res)
            {
                //登录成功
                Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();

                saveAccount(user);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }else{

                Toast.makeText(LoginActivity.this,"账号或密码有误",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(LoginActivity.this,"请填写账号密码",Toast.LENGTH_SHORT).show();
        }
    }

    public void readAccount() {
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        String name = sp.getString("username", "");
        String pass = sp.getString("password", "");
        etUserName.setText(name);
        etPassword.setText(pass);
    }

    private void saveAccount(User user) {
        //把用户名和密码记在本地，用于密码回显
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);//存储路径在data/data/cn.cnu.smartbook/share_prefs
        //拿到sp的编辑器
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("userid", user.getId()+"");
        ed.putString("username", user.getUsername());
        ed.putString("password", user.getPassword());
        DBAdopter.USER = user;
        //提交
        ed.commit();
    }

    public void register(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
