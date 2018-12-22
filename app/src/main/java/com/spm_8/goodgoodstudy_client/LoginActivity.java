package com.spm_8.goodgoodstudy_client;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.*;

import com.R;
import com.otherclass.Course;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {
    private List<Course> courses;
    String id;
    String psw;
    String result;
    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");

    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button button=findViewById(R.id.button);
        button.setOnClickListener(listener);
        intent=new Intent(this,SelectActivity.class);



    }
    private View.OnClickListener listener=new View.OnClickListener(){
        @Override
        public void onClick(View view) {



            EditText editTextid=findViewById(R.id.userid);
            EditText editTextpsw=findViewById(R.id.password);
            id=editTextid.getText().toString();
            psw=editTextpsw.getText().toString();

            login();

        }
    };
    private void login(){

        String path="http://111.230.31.228:8080/SPM/login.login";
        JSONObject jsonObject=new JSONObject();
        try{
            jsonObject.put("userName",id);
            jsonObject.put("password",psw);

        }catch (Exception e){
            e.printStackTrace();
        }
        String json=jsonObject.toString();


        RequestBody requestBody=RequestBody.create(JSON,json);

        Request.Builder builder=new Request.Builder().url(path).post(requestBody);
        OkHttpClient client=new OkHttpClient();
        Call call=client.newCall(builder.build());
        call.enqueue(callback);



    }

    private Callback callback=new Callback(){
        @Override
        public void onFailure(Call call, IOException e) {

            Log.w("LoginFalse:",e.toString());
        }

        @Override
        public void onResponse(Call call, Response response) {
           try{
                String str = new String(response.body().bytes(), "utf-8");

                Log.w("LoginResponse:", str);
                Message msg = handler.obtainMessage();
                msg.obj = str;
                msg.sendToTarget();

           }catch (Exception ex){
               Log.i("LoginResponse:", ex.toString());
           }
        }


    };
    //UI操作放在了handler里
    private  Handler handler=new Handler(){
        public void handleMessage(Message msg){
            try{
                courses=new ArrayList<>();
                String str=(String)msg.obj;
                JSONObject json = new JSONObject(str);

                result=json.getString("msg");
                JSONArray array=(JSONArray) json.get("courseList");

            for(int i=0;i<array.length();i++){
                JSONObject obj = (JSONObject)array.get(i);


                Course course=new Course(obj.getString("courseID")
                                        ,obj.getString("courseName")
                                        ,obj.getString("courseClassroom"));

                courses.add(course);
            }


            }catch (Exception ex){
                Log.d("LoginResponse:", ex.toString());
            }
            if(result.equals("SUCCESS_TEACHER")||result.equals("SUCCESS_ADMIN")){

                Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                intent.putExtra("courses", (Serializable)courses);
                startActivity(intent);


            }
            else{
                Map<String,String> m=new HashMap<>();
                m.put("PASSWORD_ERROR","密码错误");
                m.put("ACCOUNT_ERROR","无此用户");
                m.put("SERVER_ERROR","服务器错误");
                m.put("INPUT_DATA_ERROR","输入数据错误");
                Toast.makeText(LoginActivity.this,m.get(result)+"，请重新登录",Toast.LENGTH_LONG).show();

            }
        }
    };


}
