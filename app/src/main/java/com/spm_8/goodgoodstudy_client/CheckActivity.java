package com.spm_8.goodgoodstudy_client;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;


import com.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.otherclass.Camera2BasicFragment;
import com.otherclass.Check_Student;
import com.otherclass.Course;
import com.otherclass.ListViewAdapter;
import com.otherclass.ListViewAdapter_cs;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;


public class CheckActivity extends AppCompatActivity implements Camera2BasicFragment.CallBackValue {
    private String courseID;
    private String coursename;
    private ListViewAdapter_cs csadapter;
    private ListView listView;
    private TextView tvcoursename;
    private PieChart pieChart;
    //private boolean changeflag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_check);
        listView = findViewById(R.id.checkstus);
        tvcoursename = findViewById(R.id.textView_show);
        pieChart = findViewById(R.id.pic_chart);
        //changeflag = false;
        Course course = (Course) getIntent().getSerializableExtra("course");
        courseID = course.getCourseID();
        coursename = course.getCourseName();
        tvcoursename.setText(coursename);
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance(courseID))
                    .commit();
        }
//        List<Check_Student> csdatas=new ArrayList<>();
//        for(int i=0;i<20;i++){
//
//            csdatas.add(new Check_Student("学生",valueOf(i),"听课"));
//        }
//        listView.setAdapter(new ListViewAdapter_cs(this,csdatas));
    }

    public void SendMessageValue(List<Check_Student> checkstulist){
        List<Check_Student> csdatas=new ArrayList<>();
        if(null == checkstulist){
            for(int i=0;i<20;i++){
                if(i%4==0){
                    csdatas.add(new Check_Student("学生",valueOf(i),"ALIVE"));
                }else if(i%4==1){
                    csdatas.add(new Check_Student("学生",valueOf(i),"SLEEP"));
                }else if(i%4==2){
                    csdatas.add(new Check_Student("学生",valueOf(i),"DOWN"));
                }else{
                    csdatas.add(new Check_Student("学生",valueOf(i),"ABSENT"));
                }

          }
            listView.setAdapter(new ListViewAdapter_cs(this,csdatas));
            Log.i("shownulornot","传过来的是空是空");
        }else{
            Log.i("shownulornot","传过来的不是空不是空");
            listView.setAdapter(new ListViewAdapter_cs(this,checkstulist));
        }

        float[] piedataf = new float[4];
        piedataf[0] = 0.0f;
        piedataf[1] = 0.0f;
        piedataf[2] = 0.0f;
        piedataf[3] = 0.0f;
        float peoplecount=0.0f;
        if(null!=checkstulist){
            for(Check_Student cs:checkstulist){
                peoplecount++;
                if(cs.getCs_state().equals("ALIVE")){
                    piedataf[0]+=1.0f;
                }else if(cs.getCs_state().equals("SLEEP")){
                    piedataf[1]+=1.0f;
                }else if(cs.getCs_state().equals("DOWN")){
                    piedataf[2]+=1.0f;
                }else{
                    piedataf[3]+=1.0f;
                }
            }
        }else{
           // changeflag = true;
            for(Check_Student cs:csdatas){
                peoplecount++;
                if(cs.getCs_state().equals("ALIVE")){
                    piedataf[0]+=1.0f;
                }else if(cs.getCs_state().equals("SLEEP")){
                    piedataf[1]+=1.0f;
                }else if(cs.getCs_state().equals("DOWN")){
                    piedataf[2]+=1.0f;
                }else{
                    piedataf[3]+=1.0f;
                }
            }
        }

        pieChart.setDrawHoleEnabled(true);//是否绘制饼状图中间的圆
        pieChart.setHoleColor(Color.WHITE);//饼状图中间的圆的绘制颜色
        pieChart.setTransparentCircleColor(Color.WHITE);//设置圆环的颜色
        pieChart.setTransparentCircleAlpha(110);//设置圆环的透明度[0,255]
        pieChart.setHoleRadius(20f);//饼状图中间的圆的半径大小
        pieChart.setTransparentCircleRadius(61f);//设置圆环的半径值
        pieChart.setDrawCenterText(true);//是否绘制中间的文字
        pieChart.setRotationAngle(0);//设置饼状图旋转的角度
// 触摸旋转
        pieChart.setRotationEnabled(true);;//设置饼状图是否可以旋转(默认为true)
        pieChart.setCenterTextSize(6f);
        Legend l = pieChart.getLegend();
        l.setEnabled(true);
        l.setTextSize(6f);

        piedataf[0] = piedataf[0]*100.0f/peoplecount;
        piedataf[1] = piedataf[1]*100.0f/peoplecount;
        piedataf[2] = piedataf[2]*100.0f/peoplecount;
        piedataf[3] = 100.0f-piedataf[0]-piedataf[1]-piedataf[2];

        Log.i("sesedata",piedataf[0]+" " +piedataf[1] + " " + piedataf[2]);

        List<PieEntry> strings = new ArrayList<>();
        strings.add(new PieEntry(piedataf[0],"ALIVE"));
        strings.add(new PieEntry(piedataf[1],"SLEEP"));
        strings.add(new PieEntry(piedataf[2],"DOWN"));
        strings.add(new PieEntry(piedataf[3],"ABSENT"));

        PieDataSet dataSet = new PieDataSet(strings,"Label");
        dataSet.setValueTextSize(12f);


        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.rgb(100, 100, 0));
        colors.add(Color.rgb(0, 100, 100));
        colors.add(Color.rgb(100, 0, 100));
        colors.add(Color.rgb(100, 100, 100));
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);

        pieChart.setData(pieData);
        pieChart.invalidate();


    }

}
