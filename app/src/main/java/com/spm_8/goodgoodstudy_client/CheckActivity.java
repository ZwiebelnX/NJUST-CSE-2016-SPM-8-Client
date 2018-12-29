package com.spm_8.goodgoodstudy_client;


import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;


import com.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_check);
        listView = findViewById(R.id.checkstus);
        tvcoursename = findViewById(R.id.textView_show);
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

            csdatas.add(new Check_Student("学生",valueOf(i),"听课"));
          }
            listView.setAdapter(new ListViewAdapter_cs(this,csdatas));
            Log.i("shownulornot","传过来的是空是空");
        }else{
            Log.i("shownulornot","传过来的不是空不是空");
            listView.setAdapter(new ListViewAdapter_cs(this,checkstulist));
        }

    }

}
