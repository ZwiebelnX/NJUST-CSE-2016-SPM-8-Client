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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.R;
import com.otherclass.Course;
import com.otherclass.ReSigninAdapter;
import com.otherclass.SigninAdapter;
import com.otherclass.Student;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private List<Student> students;//所有学生列表
    private List<Student>noStudents;//未签到的学生列表
    private List<Student>reSignStudents;//要重新签到的学生列表
    String times;//签到次数

    boolean isReSigning=false;//是否处于签到状态
    private String resignResult;//重签返回结果
    ProgressDialog waitingDialog;//等待框Loading...
    SigninAdapter signinAdapter;//学生信息框的adapter
    ListView listView;//listview框



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        intent=new Intent(this,CheckActivity.class);




        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)//设置连接超时时间
                 .readTimeout(30, TimeUnit.SECONDS)//设置读取超时时间
               .build();

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
        //设置学生列表ListView
        students=new ArrayList();
        noStudents=new ArrayList();
        reSignStudents=new ArrayList();
        listView=findViewById(R.id.signinList);
        signinAdapter=new SigninAdapter(this,students);
        listView.setAdapter(signinAdapter);

        //设置每个item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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


                       Request request=new Request.Builder()
                               .url("http://111.230.31.228:8080/SPM/sign.sign")
                               .post(requestBody)
                                .build();
                       Log.d("requestBuilder",request.toString());

                       final Call call = client.newCall(request);


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


           Log.w("signinFail",e.toString());
       }

       @Override
       public void onResponse(Call call, Response response) throws IOException {
           try{

               String str = new String(response.body().bytes(), "utf-8");

               Log.w("signinResponse", str);

               Message msg = signHander.obtainMessage();
               msg.obj = str;
               msg.sendToTarget();

           }catch (Exception ex){
               Log.i("signinResponse", ex.toString());

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

                students.clear();
                noStudents.clear();
                for(int i=0;i<array.length();i++){
                    JSONObject obj = (JSONObject)array.get(i);


                    Student student=new Student(obj.getString("studentName")
                            ,obj.getString("studentID"),obj.getString("signResult"));
                    if(obj.getString("signResult").equals("NO")){
                        noStudents.add(student);
                    }

                    students.add(student);
                }

                signinAdapter.notifyDataSetChanged();






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

                //更新没到名单
                for(int i=0;i<reSignStudents.size();i++){

                    noStudents.remove(reSignStudents.get(i));
                    for(int j=0;j<students.size();j++){
                        if(students.get(j).getStudentID().equals(reSignStudents.get(i).getStudentID())){
                            students.get(j).setSignResult("YES");
                        }
                    }
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

        listView.setAdapter(signinAdapter);
        signinAdapter.notifyDataSetChanged();

    }
    private void setReSign(){


        final ReSigninAdapter reSigninAdapter=new ReSigninAdapter(this,noStudents);
        listView.setAdapter(reSigninAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                reSignStudents.add(noStudents.get(i));
                if(noStudents.get(i).isIschecked()==false){
                    noStudents.get(i).setIschecked(true);
                }
                else{
                    noStudents.get(i).setIschecked(false);
                }
                reSigninAdapter.notifyDataSetChanged();


            }
        });


    }


}
