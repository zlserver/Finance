package com.zlcook.open.finance;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.zlcook.open.finance.bean.Consume;
import com.zlcook.open.finance.db.DBAdopter;
import com.zlcook.open.finance.presenter.ConsumePresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class YuanActivity extends AppCompatActivity {
    private PieChartView pieChart;
    private PieChartData pieChardata;
    List<SliceValue> values = new ArrayList<SliceValue>();
    private int[] data = {21,20,9,2,8,33,14,12};
    //不同类型消费数据,对应衣、食、住、行、其它
    private float[] typeDatas ;
    int mYear, mMonth, mDay;
    TextView tv_start,tv_end;
    final int DATE_DIALOG = 1;

    boolean time_flage = true;//true:设置开始时间，false:设置结束时间
    private ConsumePresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuan);
        pieChart = (PieChartView) findViewById(R.id.piechart);
        pieChart.setOnValueTouchListener(selectListener);//设置点击事件监听

        tv_start = (TextView) findViewById(R.id.ed_start);
        tv_end = (TextView) findViewById(R.id.ed_end);
        presenter = new ConsumePresenter(this);
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

        //获取数据
        String endtime= dataStr(mYear,mMonth,mDay);
        String starttime="";
        if( mMonth-1 <=0 ) {
            mYear -= 1;
            mMonth = 11;
        }else {
            mMonth -=1;
        }
        starttime = dataStr(mYear, mMonth, mDay);
        typeDatas=  listTypeData(starttime,endtime);

        tv_start.setText(starttime);
        tv_end.setText(endtime);

        setPieChartData();
        initPieChart();
    }


    public float[] listTypeData(String startTime,String endTime){
        ArrayList<Consume> consumes =presenter.getConsumes(DBAdopter.USER.getId(),startTime,endTime);
        float[] typeDatas = new float[5];
        if( consumes!=null){
            for( Consume con : consumes){
                String type =con.getType();
                if( type.equalsIgnoreCase("衣")){
                    if(con.getFlage()==0)//支出
                        typeDatas[0]-=con.getMoney();
                    else //收入
                        typeDatas[0]+=con.getMoney();

                }else if(type.equalsIgnoreCase("食")){
                    if(con.getFlage()==0)//支出
                        typeDatas[1]-=con.getMoney();
                    else //收入
                        typeDatas[1]+=con.getMoney();
                }else if(type.equalsIgnoreCase("住")){
                    if(con.getFlage()==0)//支出
                        typeDatas[2]-=con.getMoney();
                    else //收入
                        typeDatas[2]+=con.getMoney();
                }else if(type.equalsIgnoreCase("行")){
                    if(con.getFlage()==0)//支出
                        typeDatas[3]-=con.getMoney();
                    else //收入
                        typeDatas[3]+=con.getMoney();
                }else if(type.equalsIgnoreCase("其它")){
                    if(con.getFlage()==0)//支出
                        typeDatas[4]-=con.getMoney();
                    else //收入
                        typeDatas[4]+=con.getMoney();
                }
            }
        }
        return typeDatas;
    }
    /**
     * 获取数据
     */
    private void setPieChartData(){
        values.clear();
        for (int i = 0; i < typeDatas.length; ++i) {
            SliceValue sliceValue = new SliceValue( typeDatas[i],ChartUtils.COLORS[i]);
            float money = sliceValue.getValue();
            if (i == 0) {
                sliceValue.setLabel("衣："+money);//设置label}
            }else if (i == 1) {
                sliceValue.setLabel("食："+money);//设置label}
            }else if (i == 2) {
                sliceValue.setLabel("住："+money);//设置label}
            }else if (i == 3) {
                sliceValue.setLabel("行："+money);//设置label}
            }else if (i == 4) {
                sliceValue.setLabel("其它："+money);//设置label}
            }
                values.add(sliceValue);
        }
    }


    /**
     * 初始化
     */
    private void initPieChart() {
        pieChardata = new PieChartData();
        pieChardata.setHasLabels(true);//显示表情
        pieChardata.setHasLabelsOnlyForSelected(false);//不用点击显示占的百分比
        pieChardata.setHasLabelsOutside(false);//占的百分比是否显示在饼图外面
        pieChardata.setHasCenterCircle(true);//是否是环形显示
        pieChardata.setValues(values);//填充数据
        pieChardata.setCenterCircleColor(Color.WHITE);//设置环形中间的颜色
        pieChardata.setCenterCircleScale(0.5f);//设置环形的大小级别
       // pieChardata.setCenterText1("饼图测试");//环形中间的文字1
        pieChardata.setCenterText1Color(Color.BLACK);//文字颜色
        pieChardata.setCenterText1FontSize(14);//文字大小

        pieChardata.setCenterText2("消费分类");
        pieChardata.setCenterText2Color(Color.BLACK);
        pieChardata.setCenterText2FontSize(18);
        /**这里也可以自定义你的字体   Roboto-Italic.ttf这个就是你的字体库*/
//      Typeface tf = Typeface.createFromAsset(this.getAssets(), "Roboto-Italic.ttf");
//      data.setCenterText1Typeface(tf);

        pieChart.setPieChartData(pieChardata);
        pieChart.setValueSelectionEnabled(true);//选择饼图某一块变大
        pieChart.setAlpha(0.9f);//设置透明度
        pieChart.setCircleFillRatio(1f);//设置饼图大小

    }


    /**
     * 监听事件
     */
    private PieChartOnValueSelectListener selectListener = new PieChartOnValueSelectListener() {
        @Override
        public void onValueDeselected() {        }
        @Override
        public void onValueSelected(int arg0, SliceValue value) {
            // TODO Auto-generated method stub
            Toast.makeText(YuanActivity.this, value.getValue()+"元", Toast.LENGTH_SHORT).show();
        }
    };
    /**
     * 开始时间
     * @param v
     */
    public void startTime(View v) {
        time_flage=true;
        showDialog(DATE_DIALOG);
    }

    /**
     * 结束时间
     * @param v
     */
    public void endTime(View v) {
        time_flage=false;
        showDialog(DATE_DIALOG);
    }
    /**
     * 搜索
     * @param v
     */
    public void sousu(View v) {

        String startTime = tv_start.getText().toString();
        String endTime = tv_end.getText().toString();

        typeDatas=  listTypeData(startTime,endTime);
        setPieChartData();
        initPieChart();

        // Toast.makeText(this,startTime+" : "+endTime,Toast.LENGTH_LONG).show();
        // startTime+=" 00:00:00";
        // endTime+=" 00:00:00";
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            setTextValue();
        }
    };
    /**
     * 设置日期 利用StringBuffer追加
     */
    public void setTextValue() {

      //  StringBuffer sb = new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay);
        String time= dataStr(mYear,mMonth,mDay);
        if( time_flage )
            tv_start.setText(time);
        else
            tv_end.setText(time);
    }
    public String dataStr(int year,int month,int day){
        StringBuffer sb = new StringBuffer().append(year).append("-").append(month + 1).append("-").append(day);
        String time= sb.toString();
        return time;
    }
    //监听返回菜单退出事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        return false;
    }

}
