package com.example.lwh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class MyNestedScrollView extends NestedScrollView {
    private int mParentScrollHeight = 140;


    public MyNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public MyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        View view = findViewById(R.id.topHeightImageView);
//        mParentScrollHeight = dp2px(140);
//        Log.v("scroll_test","top height="+view.getMeasuredHeight());
        Log.v("scroll_test","mParentScrollHeight height="+mParentScrollHeight);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
//        View view = findViewById(R.id.topHeightImageView);
//        Log.v("scroll_test","3 top height="+view.getMeasuredHeight());
        Log.v("scroll_test","3 mParentScrollHeight height="+dp2px(mParentScrollHeight));

//        if (getScrollY() < view.getMeasuredHeight()) {
////            super.onNestedPreScroll(target,dx,dy,consumed,type);
//
//            Log.v("scroll_test","3 getScrollY="+getScrollY()+", dx="+dx+", dy="+dy);
//            consumed[0] = dx;
//            consumed[1] = dy;
//            scrollBy(0, dy);
//        } else {
//            Log.v("scroll_test","3 else getScrollY="+getScrollY()+", dx="+dx+", dy="+dy);
//            consumed[0] = 0;
//            consumed[1] = 0;
//            super.onNestedPreScroll(target,dx,dy,consumed,type);
//        }
    }

    private int dp2px(int dp) {
        return (int) (getResources().getDisplayMetrics().density+0.5) * dp;
    }
}


