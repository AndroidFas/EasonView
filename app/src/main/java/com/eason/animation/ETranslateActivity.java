package com.eason.animation;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.eason.BaseActivity;
import com.eason.R;

import base.util.Logger;
import base.util.StringUtil;

/**
 * 测试平移
 */
public class ETranslateActivity extends BaseActivity {
    private static final String TAG = "ETranslateActivity==";

    private TextView tv_getX;
    private TextView tv_getRawX;
    private TextView tv_getY;
    private TextView tv_getRawY;
    private TextView tv_x;
    private TextView tv_y;
    private TextView tv_scrollX;
    private TextView tv_scrollY;
    private TextView tv_transLateX;
    private TextView tv_transLateY;

    private float downX, downY;
    private View transView;

    private float hasTransX, hasTransY;
    private EditText etX;
    private EditText etY;
    private Switch switchView;

    private boolean isCheck, isScrollBy;
    private View rootView;

    @Override
    public int getLayoutId() {
        return R.layout.animation_act_main;
    }

    @Override
    protected void init() {
        rootView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);

        transView = findViewById(R.id.iv_trans_view);
        transView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Logger.d(TAG, "OnTouchListener:ACTION_DOWN");
                        downX = event.getRawX();
                        downY = event.getRawY();
                        hasTransX = transView.getTranslationX();
                        hasTransY = transView.getTranslationY();

                        tv_getX.setText("getX:" + event.getX());
                        tv_getY.setText("getY:" + event.getY());
                        tv_getRawX.setText("getRawX:" + event.getRawX());
                        tv_getRawY.setText("getRawY:" + event.getRawY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Logger.d(TAG, "OnTouchListener:ACTION_MOVE");
                        tv_getX.setText("getX:" + event.getX());
                        tv_getY.setText("getY:" + event.getY());
                        tv_getRawX.setText("getRawX:" + event.getRawX());
                        tv_getRawY.setText("getRawY:" + event.getRawY());

                        //移动
                        transView.setTranslationX(hasTransX + event.getRawX() - downX);
                        transView.setTranslationY(hasTransY + event.getRawY() - downY);
                        getTransViewParams();
                        break;
                    case MotionEvent.ACTION_UP:
                        Logger.d(TAG, "OnTouchListener:ACTION_UP");
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        Logger.d(TAG, "OnTouchListener:ACTION_CANCEL");
                        break;
                }
                //拦截了才能收到move指令【全盘接受事件】
                return true;
            }
        });

        tv_getX = findViewById(R.id.tv_getX);
        tv_getRawX = findViewById(R.id.tv_getRawX);
        tv_getY = findViewById(R.id.tv_getY);
        tv_getRawY = findViewById(R.id.tv_getRawY);
        tv_x = findViewById(R.id.tv_x);
        tv_y = findViewById(R.id.tv_y);
        tv_scrollX = findViewById(R.id.tv_scrollX);
        tv_scrollY = findViewById(R.id.tv_scrollY);
        tv_transLateX = findViewById(R.id.tv_transLateX);
        tv_transLateY = findViewById(R.id.tv_transLateY);
        //滚动相关
        etX = findViewById(R.id.et_x);
        etY = findViewById(R.id.et_y);
        switchView = findViewById(R.id.switch_view);
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheck = isChecked;
            }
        });
        //打开scrollBy
        ((Switch) findViewById(R.id.switch_scrollBy)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isScrollBy = isChecked;
            }
        });
        rootView.post(new Runnable() {
            @Override
            public void run() {
                getTransViewParams();
            }
        });
    }


    public void scroll(View view) {
        String scrollX = etX.getText().toString().trim();
        String scrollY = etY.getText().toString().trim();
        if (StringUtil.isEmpty(scrollX) && StringUtil.isEmpty(scrollY)) {
            return;
        }
        int x = StringUtil.isEmpty(scrollX) ? 0 : Integer.parseInt(scrollX);
        int y = StringUtil.isEmpty(scrollY) ? 0 : Integer.parseInt(scrollY);
        if (isCheck) {
            //调用根view的scroll
            if (isScrollBy)
                rootView.scrollBy(x, y);
            else
                rootView.scrollTo(x, y);
        } else {
            if (isScrollBy)
                transView.scrollBy(x, y);
            else
                transView.scrollTo(x, y);
        }
        getTransViewParams();
    }

    public void reset(View view) {
        rootView.scrollTo(0, 0);
        transView.scrollTo(0, 0);
        transView.setTranslationX(0);
        transView.setTranslationY(0);
        getTransViewParams();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Logger.d(TAG, "onTouchEvent");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                downX = event.getRawX();
//                downY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
//                //移动
//                transView.setTranslationX(event.getRawX() - downX);
//                transView.setTranslationY(event.getRawY() - downY);
//                //相关参数
//                tv_x.setText("X:" + transView.getX());
//                tv_y.setText("Y:" + transView.getY());
//
//                tv_scrollX.setText("scrollX:" + transView.getScrollX());
//                tv_scrollY.setText("scrollY:" + transView.getScrollY());
//
//                tv_transLateX.setText("tranLateX:" + transView.getTranslationX());
//                tv_transLateY.setText("tranLateY:" + transView.getTranslationY());
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }

    //展示蓝色区域的相关参数
    private void getTransViewParams() {
        //相关参数
        tv_x.setText("getX:" + transView.getX());
        tv_y.setText("getY:" + transView.getY());

        tv_scrollX.setText("scrollX:" + transView.getScrollX());
        tv_scrollY.setText("scrollY:" + transView.getScrollY());

        tv_transLateX.setText("transLateX:" + transView.getTranslationX());
        tv_transLateY.setText("transLateY:" + transView.getTranslationY());
    }

}
