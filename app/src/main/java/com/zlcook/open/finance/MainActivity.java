package com.zlcook.open.finance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

/**
 * 登录后的主界面
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /**
     * 查看收支按钮
     * @param view
     */
    public void search(View view){
        startActivity(new Intent(MainActivity.this, ListActivity.class));

        finish();
    }

    /**
     * 添加收支按钮
     * @param view
     */
    public void add(View view){
        startActivity(new Intent(MainActivity.this, AddActivity.class));

        finish();
    }

    /**
     * 收支报表按钮
     * @param view
     */
    public void biao(View view){
        startActivity(new Intent(MainActivity.this, BiaoActivity.class));

        finish();
    }
    /**
     * 消费报表按钮
     * @param view
     */
    public void yuan(View view){
        startActivity(new Intent(MainActivity.this, YuanActivity.class));

        finish();
    }
    /**
     * 设置按钮
     * @param view
     */
    public void set(View view){
        startActivity(new Intent(MainActivity.this, SetActivity.class));

        finish();
    }

    //监听返回键退出事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出吗");
            // 添加选择按钮并注册监听
            isExit.setButton("确定", listener);
            isExit.setButton2("取消", listener);
            // 显示对话框
            isExit.show();

        }
        return false;
    }

    private DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序

                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

}
