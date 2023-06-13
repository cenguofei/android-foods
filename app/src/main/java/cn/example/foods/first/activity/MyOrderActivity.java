package cn.example.foods.first.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.example.foods.R;
import cn.example.foods.first.adapter.MyOrderAdapter;
import cn.example.foods.first.entity.OrderEntity;
import cn.example.foods.first.utils.ItFxqConstants;
import cn.example.foods.first.utils.CommonUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MyOrderActivity extends AppCompatActivity {


    RecyclerView myOrderRv;
    MyOrderAdapter myOrderAdapter;
    //存储歌曲数据
    List<OrderEntity> myOrderDatas;


    private MyOrderHandler mMyOrderHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        mMyOrderHandler = new MyOrderHandler();
        initView();
        initBaseData();


    }

    private void initView(){
        myOrderRv = findViewById(R.id.myorder_rv);
    }
    //加载初始数据
    private void initBaseData(){
        myOrderDatas = new ArrayList<>();

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", CommonUtils.getLoginUser(MyOrderActivity.this).getUsername())
                .build();

        Request request = new Request.Builder()
                .url(ItFxqConstants.MYORDER_URL)
                .post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        // 开启异步线程访问网络
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string(); //获取店铺数据
                Message msg = new Message();
                msg.what = ItFxqConstants.OK_STATUS;
                msg.obj = res;
                mMyOrderHandler.sendMessage(msg);
            }
            @Override
            public void onFailure(Call call, IOException e) {

            }
        });


    }

    class MyOrderHandler extends Handler {
        @SuppressLint("NewApi")
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case ItFxqConstants.OK_STATUS:
                    //解析获取的JSON数据
                    Gson gson = new Gson();
                    //通过反射得到type对象
                    Type listType = new TypeToken<Map>() {
                    }.getType();
                    Map resultMap = gson.fromJson((String)msg.obj, listType);
                    Boolean isSuccess =  (Boolean)resultMap.get("isSuccess");
                    if(isSuccess){
                        //存储登录用户信息
                       List<LinkedTreeMap> myOrders = (ArrayList) resultMap.get("myorders");
                       List<OrderEntity> orderEntities = new ArrayList();
                       myOrders.forEach(myOrder->{
                           OrderEntity orderEntity = new OrderEntity();
                           orderEntity.setOrdernum((String)myOrder.get("ordernum"));
                           orderEntity.setPrice((Double)myOrder.get("price"));
                           orderEntity.setAddress((String)myOrder.get("address"));
                           orderEntity.setUsername((String)myOrder.get("username"));
                           orderEntity.setTel((String)myOrder.get("tel"));
                           orderEntities.add(orderEntity);

                       });
                        myOrderDatas = orderEntities;
                        //创建适配器对象
                        myOrderAdapter = new MyOrderAdapter(MyOrderActivity.this, myOrderDatas);
                        myOrderRv.setAdapter(myOrderAdapter);
                        //数据源变化，提示适配器更新
                        myOrderAdapter.notifyDataSetChanged();
                        //设置布局管理器
                        LinearLayoutManager layoutManager = new LinearLayoutManager(MyOrderActivity.this, LinearLayoutManager.VERTICAL, false);
                        myOrderRv.setLayoutManager(layoutManager);
                    }else{

                    }
                    break;
                case ItFxqConstants.ERROR_STATUS:
                    break;
            }
        }
    }


    public List getMyOrderList() {
        return this.myOrderDatas;
    }
}