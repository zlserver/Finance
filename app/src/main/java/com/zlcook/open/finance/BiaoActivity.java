package com.zlcook.open.finance;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zlcook.open.finance.db.DBAdopter;
import com.zlcook.open.finance.presenter.ConsumePresenter;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

public class BiaoActivity extends AppCompatActivity {


    private ConsumePresenter presenter;
    private EditText et_year;

    private LineChartView lineChart;
    String[] months = {"1","2","3","4","5","6","7","8","9","10","11","12"};//X轴的标注

    //图表的数据
    //12个月收入钱集合 ，每次更新年份都会从新从数据库获取
    float[] shou_money;
    //12个月支出钱集合
    float[] zhi_money;

    /**
     * 收入坐标点
     */
    private List<PointValue> shouPointValues;

    /**
     * 支出坐标点
     */
    private List<PointValue> zhiPointValues;

    private List<AxisValue> mAxisValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biao);

        //初始化
        init();
    }
    //初始化
    public void init(){
        et_year = (EditText) findViewById(R.id.year);
        lineChart = (LineChartView)findViewById(R.id.chart);
        presenter = new ConsumePresenter(this);
        String default_year ="2017";
        et_year.setText(default_year);
        //更新这一年每个月的收支总数
        updateMonthMoney(DBAdopter.USER.getId(),default_year);
        //图表显示
        display();
    }

    /**
     * 点击查看按钮
     * @param v
     */
    public void look(View v) {
        String year =et_year.getText().toString().trim();
        //更新收入和支出数据
        updateMonthMoney(DBAdopter.USER.getId(),year);
        //图表显示
        display();
    }
    /**
     *  更新年份的12个月收入和支出数据
     * @param userid
     * @param year
     */
    public void updateMonthMoney(int userid ,String year){
        shou_money= presenter.getShouMoney(userid,year);
        zhi_money= presenter.getZhiMoney(userid,year);
    }
    //图表显示
    public void display()
    {
        getAxisLables();//获取x轴的标注
        getShouAxisPoints();//获取收入坐标点
        getZhiAxisPoints();//获取支出坐标点
        setLineChart();//设置曲线图的数据
    }

    /**
     * 设置LineChart的一些设置
     */
    private void setLineChart(){
        //--------收入线---------
        Line line = new Line(shouPointValues).setColor(Color.BLUE).setCubic(false);  //折线的颜色

        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(true);//曲线是否平滑
        line.setFilled(false);//是否填充曲线的面积
        //line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示



        //-------支出线----------
        Line zhiline = new Line(zhiPointValues).setColor(Color.RED).setCubic(false);  //折线的颜色
        zhiline.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        zhiline.setCubic(true);//曲线是否平滑
        zhiline.setFilled(false);//是否填充曲线的面积
        //line.setHasLabels(true);//曲线的数据坐标是否加上备注
        zhiline.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        zhiline.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        zhiline.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示


        List<Line> lines = new ArrayList<Line>();
        lines.add(line);
        lines.add(zhiline);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setName("月份  蓝:收入，红:支出");  //表格名称
        axisX.setTextSize(7);//设置字体大小
       // axisX.setMaxLabelChars(7);  //最多几个X轴坐标
        axisX.setValues(mAxisValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
//      data.setAxisXTop(axisX);  //x 轴在顶部

        Axis axisY = new Axis();  //Y轴
        axisY.setMaxLabelChars(7); //默认是3，只能看最后三个数字
        axisY.setName("金额  单位：元");//y轴标注
        axisY.setTextSize(7);//设置字体大小
        axisY.setTextColor(Color.BLACK);

        data.setAxisYLeft(axisY);  //Y轴设置在左边
//      data.setAxisYRight(axisY);  //y轴设置在右边

        //设置行为属性，支持缩放、滑动以及平移

        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
    }


    /**
     * X 轴的显示
     */
    private void getAxisLables(){
        mAxisValues = new ArrayList<AxisValue>();
        for (int i = 0; i < months.length; i++) {
            mAxisValues.add(new AxisValue(i).setLabel(months[i]));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getShouAxisPoints(){
        shouPointValues = new ArrayList<PointValue>();
        for (int i = 0; i < shou_money.length; i++) {
            shouPointValues.add(new PointValue(i, shou_money[i]));
        }
    }

    /**
     * 图表支出每个点的显示
     */
    private void getZhiAxisPoints(){
        zhiPointValues = new ArrayList<PointValue>();
        for (int i = 0; i < zhi_money.length; i++) {
            zhiPointValues.add(new PointValue(i, zhi_money[i]));
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
