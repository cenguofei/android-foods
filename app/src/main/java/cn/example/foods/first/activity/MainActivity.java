package cn.example.foods.first.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import cn.example.foods.R;
import cn.example.foods.first.utils.CommonUtils;


public class MainActivity extends AppCompatActivity {

    private TextView rcId, lcId, tpId, ggId, ylId, tangId, riceId;
    PopupWindow popupWindow;//定义popupWindow
    Button myMenuBtn;//我的菜单

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    public void initView() {
        rcId = findViewById(R.id.rcId);
        lcId = findViewById(R.id.lcId);
        tpId = findViewById(R.id.tpId);
        ggId = findViewById(R.id.ggId);
        ylId = findViewById(R.id.ylId);
        tangId = findViewById(R.id.tangId);
        riceId = findViewById(R.id.riceId);
    }

    public void initEvent() {
        rcId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,FoodsActivity.class);

                intent.putExtra("foodType", 1);
                startActivity(intent);
            }
        });
        lcId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,FoodsActivity.class);
                intent.putExtra("foodType", 2);
                startActivity(intent);
            }
        });
        tpId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,FoodsActivity.class);
                intent.putExtra("foodType", 3);
                startActivity(intent);
            }
        });
        ggId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,FoodsActivity.class);
                intent.putExtra("foodType", 4);
                startActivity(intent);
            }
        });
        ylId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,FoodsActivity.class);
                intent.putExtra("foodType", 6);
                startActivity(intent);
            }
        });
        tangId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,FoodsActivity.class);
                intent.putExtra("foodType", 5);
                startActivity(intent);
            }
        });
        riceId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,FoodsActivity.class);
                intent.putExtra("foodType", 7);
                startActivity(intent);
            }
        });


    }

    //菜单按钮的单机事件
    public void OnMenu(View v){
        //获取自定义菜单的布局文件
        View popupWindow_view=getLayoutInflater().inflate(R.layout.menu,null,false);
        //创建popupWindow实例，设置菜单的宽度和高度
        popupWindow=new PopupWindow(popupWindow_view, ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT,true);
        //设置菜单显示在按钮的下面
        popupWindow.showAsDropDown(findViewById(R.id.btn_menu),0,0);
        myMenuBtn= popupWindow_view.findViewById(R.id.myMenuBtn);

        //单机其它位置隐藏菜单
        popupWindow_view.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                //如果菜单存在并且处于显示状态
                if (popupWindow!=null&&popupWindow.isShowing()){
                    popupWindow.dismiss();//关闭菜单
                    popupWindow=null;
                }
                return false;
            }
        });
        myMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CommonUtils.navigateTo(MainActivity.this,MyActivity.class);
            }
        });
    }

}