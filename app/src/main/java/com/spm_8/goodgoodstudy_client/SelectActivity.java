package com.spm_8.goodgoodstudy_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends AppCompatActivity {


    private List<String> datas;
    private ListView listView;
    private ListViewAdapter adapter;
    Intent intent;

    //private ListViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        listView=findViewById(R.id.selectList);
        initCourseList();
        intent=new Intent(this,SigninActivity.class);
        listView.setAdapter(new ListViewAdapter(this,datas));

        //设置每个item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent.putExtra("course",i+"");
                startActivity(intent);
            }
        });




    }
    private void initCourseList(){
        //需要从服务器获取课程list
        datas=new ArrayList<>();
        for(int i=0;i<20;i++){
            datas.add("课程"+i);
        }
    }

}
