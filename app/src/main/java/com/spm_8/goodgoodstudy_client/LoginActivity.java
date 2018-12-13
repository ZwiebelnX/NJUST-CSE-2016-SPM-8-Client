package com.spm_8.goodgoodstudy_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity {

    Intent intent;
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
            //这里还需要向服务器验证用户名密码
            EditText editTextid=findViewById(R.id.userid);
            EditText editTextpsw=findViewById(R.id.password);
            String id=editTextid.getText().toString();
            String psw=editTextpsw.getText().toString();
            startActivity(intent);

        }
    };

}
