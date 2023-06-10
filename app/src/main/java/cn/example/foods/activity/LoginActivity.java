package cn.example.foods.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.namespace.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import cn.example.foods.utils.CommonUtils;
import cn.example.foods.utils.ItFxqConstants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 *   描述:登录Activity
 *   联系方式:QQ 2579692606
 *   @copyright:项目分享圈 违者必究
 */
public class LoginActivity extends AppCompatActivity {
    //注册组件
    private TextView regTv;
    //登录组件
    private Button loginBtn;
    //用户名称
    private EditText loginNameEt ;
    //密码
    private EditText loginPwdEt ;
    private LoginHandler mLoginsHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局文件
        setContentView(R.layout.activity_login);
        mLoginsHandler = new LoginHandler();
        //注册
        regTv = findViewById(R.id.regTv);
        loginBtn=findViewById(R.id.loginBtn);
        loginNameEt = findViewById(R.id.log_name);
        loginPwdEt = findViewById(R.id.log_pwd);
        //登录监听事件
        regTv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(LoginActivity.this, RegActivity.class);
                //启动
                startActivity(intent);
            }
        });

    }

    void log(String msg) {
        Log.v("_login",msg);
    }
    //登录系统
    public void loginSys(View view){
        Log.v("cgf","loginSys");
        String username = loginNameEt.getText().toString().trim();
        String pwd = loginPwdEt.getText().toString().trim();
        ContentValues values = new ContentValues();
        values.put("username",username);
        values.put("password",pwd);

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd)){
            log("用户名和密码不能为空");
            Toast.makeText(this,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
        }else {
            log("开始发送登录请求");
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("username", username)
                    .add("password",pwd)
                    .build();

            Request request = new Request.Builder()
                    .url(ItFxqConstants.LOGIN_URL)
                    .post(requestBody)
                    .build();
            Call call = okHttpClient.newCall(request);
            // 开启异步线程访问网络
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String res = response.body().string();
                    log("login onResponse:"+res);

                    Message msg = new Message();
                    msg.what = ItFxqConstants.OK_STATUS;
                    msg.obj = res;
                    mLoginsHandler.sendMessage(msg);
                }
                @Override
                public void onFailure(Call call, IOException e) {
                    log("login onFailure:"+e.getMessage());
                }
            });


        }

    }

    class LoginHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case ItFxqConstants.OK_STATUS:
                    log("解析json");
                    //解析获取的JSON数据
                    Gson gson = new Gson();
                    //通过反射得到type对象
                    Type listType = new TypeToken<Map>() {
                    }.getType();
                    Map resultMap = gson.fromJson((String)msg.obj, listType);
                    Boolean isSuccess =  (Boolean)resultMap.get("isSuccess");
                    if(isSuccess){
                        //存储登录用户信息
                        Map userMap =  (Map)resultMap.get("user");
                        log("登录成功："+userMap.toString());

                        CommonUtils.storeLoginUser(userMap,LoginActivity.this);
                        CommonUtils.navigateTo(LoginActivity.this,MainActivity.class);
                    }else{
                        String message = (String)resultMap.get("msg");
                        log("登录失败"+message);
                        Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ItFxqConstants.ERROR_STATUS:
                    log("操作失败");
                    Toast.makeText(LoginActivity.this,"操作失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


}