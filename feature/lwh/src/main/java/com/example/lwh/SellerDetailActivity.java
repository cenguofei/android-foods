package com.example.lwh;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.example.model.remoteModel.Food;
import com.example.model.remoteModel.User;

public class SellerDetailActivity extends AppCompatActivity {
    private User seller;
    private Food food;

    Application application;


    public SellerDetailActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_detail);

        Bundle extras = getIntent().getExtras();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            User currentUser = extras.getParcelable("currentUser", User.class);
            Log.v("lwh_test","当前登录用户："+currentUser);
            User seller = extras.getParcelable("seller",User.class);
            Log.v("lwh_test","商家："+seller);

            Food food = extras.getParcelable("food",Food.class);
            Log.v("lwh_test","food："+food);

            String str = extras.getString("str");
            Log.v("lwh_test","str："+str);
        }
    }
}