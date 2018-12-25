package com.spm_8.goodgoodstudy_client;


import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.support.v7.app.AppCompatActivity;


import com.R;
import com.otherclass.Camera2BasicFragment;
import com.otherclass.Course;


public class CheckActivity extends AppCompatActivity {
    private String courseID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_check);
        Course course = (Course) getIntent().getSerializableExtra("course");
        courseID = course.getCourseID();
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance(courseID))
                    .commit();
        }

    }


}
