package com.spm_8.goodgoodstudy_client;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class SigninActivity extends AppCompatActivity {

    public static final int TAKE_POTHO=1;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        String course=getIntent().getStringExtra("course");
        TextView coursename=findViewById(R.id.courseName);
        coursename.setText("课程"+course);

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
            Log.d("signin","没有相机权限");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},0);}

        imageView=(ImageView)findViewById(R.id.picture);
        ImageButton button=findViewById(R.id.startPhoto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");

                    startActivityForResult(intent,TAKE_POTHO);
            }
        });  }



            protected void onActivityResult(int requestCode,int resultCode,Intent data)
            {
                Log.d("signin","返回数据");
                switch (requestCode)
                {
                    case TAKE_POTHO:
                        if(resultCode==RESULT_OK)
                        {
                            try{

                                Bitmap photo = (Bitmap) data.getExtras().get("data");
                                imageView.setImageBitmap(photo);

                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        break;

                    default:
                            break;
                }    }

}
