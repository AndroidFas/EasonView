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
                activity.addLog(ETouchEventActivity.Companion.getFirstViewGroup() + "dispatchTouchEvent：ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                if (activity.getOpenMoveLog())
                    activity.addLog(ETouchEventActivity.Companion.getFirstViewGroup() + "dispatchTouchEvent：ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                activity.addLog(ETouchEventActivity.Companion.getFirstViewGroup() + "dispatchTouchEvent：ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                activity.addLog(ETouchEventActivity.Companion.getFirstViewGroup() + "dispatchTouchEvent：ACTION_CANCEL");
                break;
        }
        return activity.getSwitchs()[3][1][0] ? super.dispatchTouchEvent(ev) : activity.getSwitchs()[3][1][1];
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                activity.addLog(ETouchEventActivity.Companion.getFirstViewGroup() + "onInterceptTouchEvent：ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                if (activity.getOpenMoveLog())
                    activity.addLog(ETouchEventActivity.Companion.getFirstViewGroup() + "onInterceptTouchEvent：ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                activity.addLog(ETouchEventActivity.Companion.getFirstViewGroup() + "onInterceptTouchEvent：ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                activity.addLog(ETouchEventActivity.Companion.getFirstViewGroup() + "onInterceptTouchEvent：ACTION_CANCEL");
                break;
        }
        return activity.getSwitchs()[3][2][0] ? super.onInterceptTouchEvent(ev) : activity.getSwitchs()[3][2][1];
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                activity.addLog(ETouchEventActivity.Companion.getFirstViewGroup() + "onTouchEvent：ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                if (activity.getOpenMoveLog())
                    activity.addLog(ETouchEventActivity.Companion.getFirstViewGroup() + "onTouchEvent：ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                activity.addLog(ETouchEventActivity.Companion.getFirstViewGroup() + "onTouchEvent：ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                activity.addLog(ETouchEventActivity.Companion.getFirstViewGroup() + "onTouchEvent：ACTION_CANCEL");
                break;
        }
        return activity.getSwitchs()[3][3][0] ? super.onTouchEvent(event) : activity.getSwitchs()[3][3][1];
    }
}
