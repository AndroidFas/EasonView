package com.eason.touchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * 事件研究的起点
 */
public class ERootView extends FrameLayout {
    private ETouchEventActivity activity;

    public ERootView(Context context) {
        super(context);
    }

    public ERootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (ETouchEventActivity) context;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                activity.addLog(ETouchEventActivity.ERootView + "dispatchTouchEvent：ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
//                activity.addLog(ETouchEventActivity.ERootView + "dispatchTouchEvent：ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                activity.addLog(ETouchEventActivity.ERootView + "dispatchTouchEvent：ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                activity.addLog(ETouchEventActivity.ERootView + "dispatchTouchEvent：ACTION_CANCEL");
                break;
        }
        return activity.switchs[2][1][0] ? super.dispatchTouchEvent(ev) : activity.switchs[2][1][1];
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                activity.addLog(ETouchEventActivity.ERootView + "onInterceptTouchEvent：ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
//                activity.addLog(ETouchEventActivity.ERootView + "onInterceptTouchEvent：ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                activity.addLog(ETouchEventActivity.ERootView + "onInterceptTouchEvent：ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                activity.addLog(ETouchEventActivity.ERootView + "onInterceptTouchEvent：ACTION_CANCEL");
                break;
        }
        return activity.switchs[2][2][0] ? super.dispatchTouchEvent(ev) : activity.switchs[2][2][1];
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                activity.addLog(ETouchEventActivity.ERootView + "onTouchEvent：ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
//                activity.addLog(ETouchEventActivity.ERootView + "onTouchEvent：ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                activity.addLog(ETouchEventActivity.ERootView + "onTouchEvent：ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                activity.addLog(ETouchEventActivity.ERootView + "onTouchEvent：ACTION_CANCEL");
                break;
        }
        return activity.switchs[2][3][0] ? super.dispatchTouchEvent(event) : activity.switchs[2][3][1];
    }
}
