package com.eason.touchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 事件研究的最后一个子View
 */
public class LastView extends View {
    private ETouchEventActivity activity;

    public LastView(Context context) {
        super(context);
    }

    public LastView(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (ETouchEventActivity) context;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                activity.addLog(ETouchEventActivity.LastView + "dispatchTouchEvent：ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
//                activity.addLog(ETouchEventActivity.LastView + "dispatchTouchEvent：ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                activity.addLog(ETouchEventActivity.LastView + "dispatchTouchEvent：ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                activity.addLog(ETouchEventActivity.LastView + "dispatchTouchEvent：ACTION_CANCEL");
                break;
        }
        return activity.switchs[4][1][0] ? super.dispatchTouchEvent(ev) : activity.switchs[4][1][1];
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                activity.addLog(ETouchEventActivity.LastView + "onTouchEvent：ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
//                activity.addLog(ETouchEventActivity.LastView + "onTouchEvent：ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                activity.addLog(ETouchEventActivity.LastView + "onTouchEvent：ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                activity.addLog(ETouchEventActivity.LastView + "onTouchEvent：ACTION_CANCEL");
                break;
        }
        return activity.switchs[4][2][0] ? super.onTouchEvent(event) : activity.switchs[4][2][1];
    }
}
