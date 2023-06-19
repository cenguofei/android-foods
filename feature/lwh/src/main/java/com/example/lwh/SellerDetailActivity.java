package com.example.lwh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.model.remoteModel.Food;
import com.example.model.remoteModel.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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


        RecyclerView leftRecyclerView = findViewById(R.id.leftRecyclerView);
        RecyclerView rightRecyclerView = findViewById(R.id.rightRecyclerView);

        leftRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rightRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));


        leftRecyclerView.setAdapter(new MyAdapter(5));
        rightRecyclerView.setAdapter(new MyAdapter(250));


    }


    private List<String> generateData(int num) {
        List<String> res = new ArrayList<>();
        for (int i = 0;i < num;i++) {
            res.add("item -> "+i);
        }
        return res;
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    static class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        int num;
        public MyAdapter(int num) {
            this.num = num;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.textView.setText("item "+num+"  pos="+position);
        }

        @Override
        public int getItemCount() {
            return num;
        }
    }
}