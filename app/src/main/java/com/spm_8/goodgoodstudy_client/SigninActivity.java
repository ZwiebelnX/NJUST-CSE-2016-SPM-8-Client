package com.spm_8.goodgoodstudy_client;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.otherclass.Course;
import com.otherclass.FixGridLayout;
import com.otherclass.Student;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Callback;
import okhttp3.Response;


public class SigninActivity extends AppCompatActivity {

    public static final int TAKE_POTHO=1;
    private ImageView imageView;//图片显示
    private String courseID;//课程id
    private OkHttpClient client;
    private List<Student> students;//没到的学生列表
    static int times;//签到次数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        //获取课程信息
        Course course=(Course) getIntent().getSerializableExtra("course");
        TextView coursename=findViewById(R.id.courseName);
        courseID=course.getCourseID();
        coursename.setText(course.getCourseName()+"("+course.getCourseClassroom()+")");

        //申请相机权限
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
            Log.d("signin","没有相机权限");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},0);
        }
        //摄像头开启的点击事件
        imageView=(ImageView)findViewById(R.id.picture);
        ImageButton button=findViewById(R.id.startPhoto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");

                    startActivityForResult(intent,TAKE_POTHO);
            }
        });
        initStudents();
        setReSign();
    }



   protected void onActivityResult(int requestCode,int resultCode,Intent data)
   {

       switch (requestCode)
       {
           case TAKE_POTHO:
               if(resultCode==RESULT_OK)
               {

                   try{


                       Bitmap photo = (Bitmap) data.getExtras().get("data");
                       imageView.setImageBitmap(photo);
                       ByteArrayOutputStream baos = new ByteArrayOutputStream();
                       photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                       byte[]bytes=null;
                       bytes=baos.toByteArray();
                       baos.close();
                       String dataString=new String(bytes,"ISO-8859-1");
                       MediaType type=MediaType.parse("application/octet-stream");
                       RequestBody fileBody=RequestBody.create(type,bytes);
                       Log.d("图片",dataString);

                       MultipartBody.Builder builder=new MultipartBody.Builder();
                       builder.addFormDataPart("img","signin.jpg",fileBody);
                       builder.addFormDataPart("courseID",courseID);
                       builder.addFormDataPart("signType","SIGN");
                       RequestBody requestBody=builder.build();
                       Log.d("requestBody",requestBody.toString());
                       Request.Builder requestBuilder=new Request.Builder();
                       requestBuilder.url("http://111.230.31.228:8080/SPM/sign.sign");
                       requestBuilder.post(requestBody);
                       Call call=client.newCall(requestBuilder.build());
                       call.enqueue(callback);






                       }catch (Exception e) {

                       Log.d("SigninERROR",e.toString());
                   }
               }
               break;
           default:
               break;
       }
   }

   private Callback callback=new Callback() {
       @Override
       public void onFailure(Call call, IOException e) {
           Log.w("LoginFalse:",e.toString());
       }

       @Override
       public void onResponse(Call call, Response response) throws IOException {
           try{
               String str = new String(response.body().bytes(), "utf-8");

               Log.w("signinResponse:", str);
//               Message msg = handler.obtainMessage();
//               msg.obj = str;
//               msg.sendToTarget();

           }catch (Exception ex){
               Log.i("LoginResponse:", ex.toString());
           }

       }
   };
    private void initStudents(){
        students=new ArrayList<>();
        for (int i=0;i<10;i++){
            Student student=new Student("小"+i,i+"");
            students.add(student);
        }
    }
    private void setReSign(){
        FixGridLayout view=findViewById(R.id.resignList);
        view.setmCellHeight(80);
        view.setmCellWidth(135);
        for(int i=0;i<students.size();i++){
            CheckBox checkBox=new CheckBox(this);
            checkBox.setText(students.get(i).getStudentName());

            view.addView(checkBox);

        }

    }

}
