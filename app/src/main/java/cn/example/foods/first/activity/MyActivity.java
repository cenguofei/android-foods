package cn.example.foods.first.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import cn.example.foods.R;
import cn.example.foods.first.entity.UserEntity;
import cn.example.foods.first.utils.CommonUtils;


public class MyActivity extends AppCompatActivity {

    //退出文本组件
    TextView logout_tv;
    //我的喜好
    TextView myOrder_tv;
    TextView my_name_tv,my_email_tv,my_tel_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);



        initView();
        initData();
        initEvent();
    }

    private void initView(){
        logout_tv = findViewById(R.id.logout);
        myOrder_tv = findViewById(R.id.myOrder);
        my_name_tv =  findViewById(R.id.my_name);
        my_email_tv =  findViewById(R.id.my_email);
        my_tel_tv =  findViewById(R.id.my_tel);


        logout_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MyActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        myOrder_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myOrderIntent=new Intent();
                myOrderIntent.setClass(MyActivity.this,MyOrderActivity.class);
                startActivity(myOrderIntent);
            }
        });
    }

    private void initData(){
        UserEntity loginUser = CommonUtils.getLoginUser(MyActivity.this);
        my_name_tv.setText(loginUser.getUsername());
        my_email_tv.setText(loginUser.getEmail());
        my_tel_tv.setText(loginUser.getTel());

    }

    private void initEvent(){

    }


}