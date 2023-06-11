package cn.example.foods.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.example.foods.R;
import cn.example.foods.adapter.OrderAdapter;
import cn.example.foods.entity.FoodEntity;
import cn.example.foods.entity.OrderDetailEntity;
import cn.example.foods.entity.OrderEntity;
import cn.example.foods.utils.CommonUtils;
import cn.example.foods.utils.ItFxqConstants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderActivity extends AppCompatActivity {

    private ListView lv_order;
    private OrderAdapter adapter;
    private List<FoodEntity> carFoodList;
    private TextView tv_distribution_cost,tv_total_cost,
            tv_cost,tv_payment;
    private EditText et_orderNumId,et_orderUserId,et_addressId,et_telId;
    private Double money;
    private OrderHandler mOrderHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        mOrderHandler = new OrderHandler();
        //获取购物车中的数据
        carFoodList= (List<FoodEntity>) getIntent().getSerializableExtra("carFoodList");
        //获取购物车中菜的总价格
        money=new Double(getIntent().getStringExtra("totalMoney"));

        initView();
        setData();
    }
    /**
     * 初始化界面控件
     */
    private void initView(){
        //设置订单号
        et_orderNumId =  findViewById(R.id.orderNumId);
        et_orderNumId.setText(CommonUtils.getOrderNum());
        et_orderNumId.setKeyListener(null);
        //设置当前用户名
        et_orderUserId = findViewById(R.id.orderUserId);
        et_orderUserId.setText(CommonUtils.getLoginUser(OrderActivity.this).getUsername());
        et_orderUserId.setKeyListener(null);
        //获取地址Id
        et_addressId = findViewById(R.id.addressId);
        et_telId = findViewById(R.id.telId);

        lv_order=  findViewById(R.id.lv_order);
        tv_distribution_cost = findViewById(R.id.tv_distribution_cost);
        tv_total_cost = findViewById(R.id.tv_total_cost);
        tv_cost =findViewById(R.id.tv_cost);
        tv_payment = findViewById(R.id.tv_payment);

        tv_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //支付订单按钮
                String orderNum = et_orderNumId.getText().toString();
                String username = et_orderUserId.getText().toString();
                String address = et_addressId.getText().toString();
                String tel = et_telId.getText().toString();
                Double totalPrice = money+3;
                if(TextUtils.isEmpty(address) ){
                    Toast.makeText(OrderActivity.this,"配送地址不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(tel) ){
                    Toast.makeText(OrderActivity.this,"联系电话不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }

                //封装参数
                OrderEntity orderEntity = new OrderEntity();
                orderEntity.setAddress(address);
                orderEntity.setIsPay("1");
                orderEntity.setOrdernum(orderNum);
                orderEntity.setTel(tel);
                orderEntity.setUsername(username);
                orderEntity.setPrice(totalPrice);
                List<OrderDetailEntity> orderDetailEntityList = new ArrayList<>();
                //循环购物车食物
                carFoodList.forEach(carFood->{
                    OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
                    orderDetailEntity.setFoodName(carFood.getFoodName());
                    orderDetailEntity.setNum(carFood.getCount());
                    orderDetailEntity.setPrice(carFood.getPrice());
                    orderDetailEntity.setOrdernum(orderNum);
                    orderDetailEntityList.add(orderDetailEntity);
                });
                orderEntity.setOrderDetailList(orderDetailEntityList);


                //发送请求 存入订单表
                OkHttpClient okHttpClient = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
                RequestBody requestBody = RequestBody.create(mediaType,new Gson().toJson(orderEntity));

                Request request = new Request.Builder()
                        .url(ItFxqConstants.ORDER_URL)
                        .post(requestBody)
                        .addHeader("Content-Type", "application/json")
                        .build();
                Call call = okHttpClient.newCall(request);
                // 开启异步线程访问网络
                call.enqueue(new Callback() {
                    Message msg = new Message();
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String res = response.body().string();
                        msg.what = ItFxqConstants.OK_STATUS;
                        msg.obj = res;
                        mOrderHandler.sendMessage(msg);
                    }
                    @Override
                    public void onFailure(Call call, IOException e) {
                        msg.what = ItFxqConstants.ERROR_STATUS;

                    }
                });




            }
        });
    }
    /**
     * 设置界面数据
     */
    private void setData() {
        adapter=new OrderAdapter(this);
        lv_order.setAdapter(adapter);
        adapter.setData(carFoodList);
        tv_cost.setText("￥"+money);
        tv_distribution_cost.setText("￥3");
        tv_total_cost.setText("￥"+(money+3));
    }


    class OrderHandler extends Handler {
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
                        AlertDialog.Builder dlog = new AlertDialog.Builder(OrderActivity.this);
                        dlog.setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dlg,int arg1) {
                                Intent intent = new Intent(OrderActivity.this,FoodsActivity.class);
                                startActivity(intent);
                            }
                        });
                        dlog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dlg,int arg1) {
                                dlg.dismiss();;
                            }
                        });
                        dlog.setMessage("恭喜您,您的订单支付成功。");
                        dlog.setTitle("温馨提示");
                        dlog.show();
                    }
                    break;
                case ItFxqConstants.ERROR_STATUS:
                    Toast.makeText(OrderActivity.this,"操作失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}