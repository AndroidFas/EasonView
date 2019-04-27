package com.eason.touchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * 事件研究的第一个ViewGroup
 */
public class FirstViewGroup extends FrameLayout {
    private ETouchEventActivity activity;
    public FirstViewGroup(Context context) {
        super(context);
    }

    public FirstViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (ETouchEventActivity) context;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                activity.addLog(ETouchEventActivity.FirstViewGroup + "dispatchTouchEvent：ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
//                activity.addLog(ETouchEventActivity.FirstViewGroup + "dispatchTouchEvent：ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                activity.addLog(ETouchEventActivity.FirstViewGroup + "dispatchTouchEvent：ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                activity.addLog(ETouchEventActivity.FirstViewGroup + "dispatchTouchEvent：ACTION_CANCEL");
                break;
        }
        return activity.switchs[3][1][0] ? super.dispatchTouchEvent(ev) : activity.switchs[3][1][1];
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                activity.addLog(ETouchEventActivity.FirstViewGroup + "onInterceptTouchEvent：ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
//                activity.addLog(ETouchEventActivity.FirstViewGroup + "onInterceptTouchEvent：ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                activity.addLog(ETouchEventActivity.FirstViewGroup + "onInterceptTouchEvent：ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                activity.addLog(ETouchEventActivity.FirstViewGroup + "onInterceptTouchEvent：ACTION_CANCEL");
                break;
        }
        return activity.switchs[3][2][0] ? super.dispatchTouchEvent(ev) : activity.switchs[3][2][1];
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                activity.addLog(ETouchEventActivity.FirstViewGroup + "onTouchEvent：ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
//                activity.addLog(ETouchEventActivity.FirstViewGroup + "onTouchEvent：ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                activity.addLog(ETouchEventActivity.FirstViewGroup + "onTouchEvent：ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                activity.addLog(ETouchEventActivity.FirstViewGroup + "onTouchEvent：ACTION_CANCEL");
                break;
        }
        return activity.switchs[3][3][0] ? super.dispatchTouchEvent(event) : activity.switchs[3][3][1];
    }
}
