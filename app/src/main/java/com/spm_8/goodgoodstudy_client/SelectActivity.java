package com.spm_8.goodgoodstudy_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.otherclass.Course;
import com.otherclass.ListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends AppCompatActivity {


    private List<Course> courses;
    private ListView listView;
    private ListViewAdapter adapter;
    Intent intent;

    //private ListViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        listView=findViewById(R.id.selectList);
//        initCourseList();
        courses=(List<Course>) getIntent().getSerializableExtra("courses");
        intent=new Intent(this,SigninActivity.class);
        listView.setAdapter(new ListViewAdapter(this,courses));

        //设置每个item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent.putExtra("course",courses.get(i));
                startActivity(intent);
            }
        });




    }
//    private void initCourseList(){
//        //需要从服务器获取课程list
//        datas=new ArrayList<>();
//        for(int i=0;i<20;i++){
//            datas.add("课程"+i);
//        }
//    }

}
