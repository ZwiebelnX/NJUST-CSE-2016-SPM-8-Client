package com.spm_8.goodgoodstudy_client;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.R;
import com.otherclass.Course;
import com.otherclass.FixGridLayout;
import com.otherclass.Student;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Callback;
import okhttp3.Response;

import static com.spm_8.goodgoodstudy_client.LoginActivity.JSON;


public class SigninActivity extends AppCompatActivity {

    public static final int TAKE_POTHO=1;
    private ImageView imageView;//图片显示
    private String courseID;//课程id
    private OkHttpClient client;
    Intent intent;//状态识别
    private List<Student> students;//没到的学生列表
    private List<Student>reSignStudents;//要重新签到的学生列表
    String times;//签到次数
    FixGridLayout fixGridLayout;//签到框
    boolean isReSigning=false;//是否处于签到状态
    private String resignResult;//重签返回结果
    ProgressDialog waitingDialog;//等待框Loading...


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        intent=new Intent(this,CheckActivity.class);

        fixGridLayout=findViewById(R.id.resignList);
        fixGridLayout.setmCellHeight(80);
        fixGridLayout.setmCellWidth(150);



        client=new OkHttpClient();
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
        //补签的点击事件
        Button replenishSign=findViewById(R.id.replenishSign);
        replenishSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isReSigning=true;
                //fixGridLayout.removeAllViews();
                setReSign();
            }
        });
        //确认的点击事件
        Button affirm=findViewById(R.id.affirm);
        affirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isReSigning){
                //向服务器发请求
                    isReSigning=false;
                    Resign();



                }
                else{

                    intent.putExtra("course",(Course) getIntent().getSerializableExtra("course"));
                    startActivity(intent);
                }
            }
        });




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


                       waitingDialog=new ProgressDialog(SigninActivity.this);
                       waitingDialog.setTitle("正在上传图片");
                       waitingDialog.setMessage("等待中...");
                       waitingDialog.setIndeterminate(true);
                       waitingDialog.setCancelable(false);
                       waitingDialog.show();

                       MultipartBody.Builder builder=new MultipartBody.Builder();
                       builder.setType(MultipartBody.FORM);
                       builder.addFormDataPart("img","signin.jpg",fileBody);
                       builder.addFormDataPart("courseID",courseID);

                       builder.addFormDataPart("signType","SIGN");

                       RequestBody requestBody=builder.build();

                       final Request request = new Request.Builder().url("http://111.230.31.228:8080/SPM/sign.sign")
                                                                            .post(requestBody).build();
                       //单独设置参数 比如读取超时时间
                       final Call call = client.newBuilder().writeTimeout(10, TimeUnit.SECONDS).build().newCall(request);


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

               Message msg = signHander.obtainMessage();
               msg.obj = str;
               msg.sendToTarget();

           }catch (Exception ex){
               Log.i("LoginResponse:", ex.toString());

           }

       }
   };
    private Handler signHander=new Handler(){
        public void handleMessage(Message msg){
            try{
                waitingDialog.cancel();


                String str=(String)msg.obj;
                JSONObject json = new JSONObject(str);

                resignResult=json.getString("msg");
                times=json.getString("signCnt");
                JSONArray array=(JSONArray) json.get("signFailedList");

                students=new ArrayList<>();
                for(int i=0;i<array.length();i++){
                    JSONObject obj = (JSONObject)array.get(i);


                    Student student=new Student(obj.getString("studentName")
                            ,obj.getString("studentID"));

                    students.add(student);
                }
                Log.w("signinResponse:", students.toString());
                setSign();





            }catch (Exception ex){
                Log.d("signResponse:", ex.toString());
            }

            if(resignResult!=null&&resignResult.equals("SUCCESS")){

                Toast.makeText(SigninActivity.this,"签到成功",Toast.LENGTH_LONG).show();




            }
            else{

                Toast.makeText(SigninActivity.this,resignResult+"，签到失败",Toast.LENGTH_LONG).show();

            }
        }
    };
    private void Resign(){

        String path="http://111.230.31.228:8080/SPM/resign.sign";
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("signCnt",times);
            jsonObject.put("courseID",courseID);

            JSONArray jsonArray=new JSONArray();
            for(int i=0;i<reSignStudents.size();i++){
                JSONObject studentObject=new JSONObject();
                studentObject.put("studentName",reSignStudents.get(i).getStudentName());
                studentObject.put("studentID",reSignStudents.get(i).getStudentID());
                jsonArray.put(studentObject);
            }
            jsonObject.put("resignList",jsonArray);
            Log.d("resignResponse:", jsonObject.toString());

        }catch (Exception e){
            Log.d("resignResponse:", e.toString());
        }
        String json=jsonObject.toString();


        RequestBody requestBody=RequestBody.create(JSON,json);

        Request.Builder builder=new Request.Builder().url(path).post(requestBody);
        OkHttpClient client=new OkHttpClient();
        Call call=client.newCall(builder.build());
        call.enqueue(reSigncallback);



    }

    private Callback reSigncallback=new Callback(){
        @Override
        public void onFailure(Call call, IOException e) {

            Log.w("ResignFalse:",e.toString());
        }

        @Override
        public void onResponse(Call call, Response response) {
            try{
                String str = new String(response.body().bytes(), "utf-8");

                Log.w("ResignResponse:", str);
                Message msg = reSignHandler.obtainMessage();
                msg.obj = str;
                 msg.sendToTarget();

            }catch (Exception ex){
                Log.i("ResignResponse:", ex.toString());
            }
        }


    };

    private  Handler reSignHandler=new Handler(){
        public void handleMessage(Message msg){
            try{

                String str=(String)msg.obj;
                JSONObject json = new JSONObject(str);
                resignResult=new String();

                resignResult=json.getString("msg");
                Log.d("resignResponse:", json.toString());
                Log.d("resignResult", resignResult);




            if(resignResult!=null&&resignResult.equals("SUCCESS")){

                Toast.makeText(SigninActivity.this,"补签成功",Toast.LENGTH_LONG).show();

                //更新没到名单，清除重新签到名单
                for(int i=0;i<reSignStudents.size();i++){
                    students.remove(reSignStudents.get(i));
                }
                reSignStudents.clear();
                setSign();





            }
            else{

                Toast.makeText(SigninActivity.this,resignResult+"，补签失败",Toast.LENGTH_LONG).show();

                setSign();
            }
         }catch (Exception ex){
            Log.d("LoginResponseEX:", ex.toString());
                Toast.makeText(SigninActivity.this,"补签失败\n"+ex.toString(),Toast.LENGTH_LONG).show();
        }
        }
    };

    private void setSign(){

        fixGridLayout.removeAllViews();

        for(int i=0;i<students.size();i++){
            TextView textView=new TextView(this);
            textView.setText(students.get(i).getStudentName());

            fixGridLayout.addView(textView);

        }
    }
    private void setReSign(){
        reSignStudents=new ArrayList<>();

        fixGridLayout.removeAllViews();
        for(int i=0;i<students.size();i++){
            CheckBox checkBox=new CheckBox(this);
            checkBox.setText(students.get(i).getStudentName());

            checkBox.setOnCheckedChangeListener(checkBoxListener);
            fixGridLayout.addView(checkBox);

        }

    }
    private CompoundButton.OnCheckedChangeListener checkBoxListener=new CompoundButton.OnCheckedChangeListener(){

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                String student=(String)buttonView.getText();
                Log.w("reSignStudent",student);
                for(int i=0;i<students.size();i++){
                    if(students.get(i).getStudentName().equals(student)){
                        reSignStudents.add(students.get(i));

                    }
                }
            }




        }
    };

}
