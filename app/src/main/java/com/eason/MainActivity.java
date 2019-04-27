package com.eason;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.eason.animation.ETranslateActivity;
import com.eason.touchevent.ETouchEventActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void init() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 动画
     *
     * @param view
     */
    public void animation(View view) {
        startActivity(new Intent(this, ETranslateActivity.class));
    }

    public void touchEvent(View view) {
        startActivity(new Intent(this, ETouchEventActivity.class));
    }
}
