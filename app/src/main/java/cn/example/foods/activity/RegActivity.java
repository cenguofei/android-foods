package cn.example.foods.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import cn.example.foods.R;
import cn.example.foods.utils.ItFxqConstants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 *   描述:RegActivity 注册Activity
 *   联系方式:QQ 2579692606
 *   @copyright:项目分享圈 违者必究
 */
public class RegActivity extends AppCompatActivity {
    //点击去登录组件
    private TextView goLoginTv ;
    //用户名编辑框
    private EditText usernameEt ;
    //密码编辑框
    private EditText pwdEt ;
    //邮件编辑框
    private EditText emailEt ;
    //电话号码编辑框
    private EditText telEt ;
    //注册成功
    private RegHandler mRegHandler;

    private RadioButton female,male;
    private RadioGroup rg;
    private boolean sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置注册布局
        setContentView(R.layout.activity_reg);
        mRegHandler=new RegHandler();
        //获取组件
        usernameEt = findViewById(R.id.username);
        pwdEt = findViewById(R.id.pwd);
        emailEt = findViewById(R.id.email);
        telEt = findViewById(R.id.tel);
        rg = findViewById(R.id.rg);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                if(checkedId==R.id.male){
                    sex = true;
                }else {
                    sex =false;
                }
            }
        });

        //绑定事件
        goLoginTv = findViewById(R.id.goLoginTv);
        goLoginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    void log(String msg) {
        Log.v("cgf",msg);
    }
    //注册保存用户信息
    public void saveUser(View view){
        log("开始saveUser");
        //获取存入的内容-用户名
        String username = usernameEt.getText().toString().trim();
        //获取存入的内容-密码
        String pwd = pwdEt.getText().toString().trim();
        //获取存入的内容-电话
        String tel = telEt.getText().toString().trim();
        //获取存入的内容-邮件
        String email = emailEt.getText().toString().trim();
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd)){
            log("用户名和密码不能为空");
            Toast.makeText(this,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
        }else{
            log("开始请求注册用户");
            int sexes = sex ? 0 : 1;
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("username", username)
                    .add("password",pwd)
                    .add("email",email)
                    .add("tel",tel)
                    .add("sex",sex+"")
                    .build();

            Request request = new Request.Builder()
                    .url(ItFxqConstants.REG_URL)
                    .post(requestBody).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                Message msg = new Message();
                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    //获取返回数据
                    String res = response.body().string();
                    log("onResponse:body="+res);

                    msg.what = ItFxqConstants.OK_STATUS;
                    msg.obj = res;
                    mRegHandler.sendMessage(msg);
                }
                @Override
                public void onFailure(Call call, IOException e) {
                    log("onFailure:msg="+e.getMessage());
                    msg.what = ItFxqConstants.ERROR_STATUS;
                    mRegHandler.sendMessage(msg);
                }
            });
        }
    }

    class RegHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case ItFxqConstants.OK_STATUS:
                    log("OK_STATUS");
                    //解析获取的JSON数据
                    Gson gson = new Gson();
                    //通过反射得到type对象
                    Type listType = new TypeToken<Map>() {
                    }.getType();
                    Map resultMap = gson.fromJson((String)msg.obj, listType);
                    Boolean isSuccess =  (Boolean)resultMap.get("isSuccess");
                    String resultMsg =  (String)resultMap.get("msg");
                    //注册成功
                    if(isSuccess){
                        log("OK_STATUS-》注册成功");
                        AlertDialog.Builder dlog = new AlertDialog.Builder(RegActivity.this);
                        dlog.setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dlg,int arg1) {
                                Intent intent = new Intent(RegActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                        dlog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dlg,int arg1) {
                                dlg.dismiss();;
                            }
                        });
                        dlog.setMessage("保存成功,返回登录。");
                        dlog.setTitle("温馨提示");
                        dlog.show();
                    }else{
                        log("OK_STATUS-》注册失败"+resultMsg);
                        Toast.makeText(RegActivity.this,resultMsg,Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ItFxqConstants.ERROR_STATUS:
                    log("注册失败 ERROR_STATUS");
                    Toast.makeText(RegActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


}