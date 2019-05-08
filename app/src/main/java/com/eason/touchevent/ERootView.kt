package com.eason.touchevent

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

/**
 * 事件研究的起点
 */
class ERootView : FrameLayout {
    private var activity: ETouchEventActivity? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        activity = context as ETouchEventActivity
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> activity!!.addLog(ETouchEventActivity.ERootView + "dispatchTouchEvent：ACTION_DOWN")
            MotionEvent.ACTION_MOVE -> if (activity!!.openMoveLog)
                activity!!.addLog(ETouchEventActivity.ERootView + "dispatchTouchEvent：ACTION_MOVE")
            MotionEvent.ACTION_UP -> activity!!.addLog(ETouchEventActivity.ERootView + "dispatchTouchEvent：ACTION_UP")
            MotionEvent.ACTION_CANCEL -> activity!!.addLog(ETouchEventActivity.ERootView + "dispatchTouchEvent：ACTION_CANCEL")
        }
        return if (activity!!.switchs[2][1][0]) super.dispatchTouchEvent(ev) else activity!!.switchs[2][1][1]
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> activity!!.addLog(ETouchEventActivity.ERootView + "onInterceptTouchEvent：ACTION_DOWN")
            MotionEvent.ACTION_MOVE -> if (activity!!.openMoveLog)
                activity!!.addLog(ETouchEventActivity.ERootView + "onInterceptTouchEvent：ACTION_MOVE")
            MotionEvent.ACTION_UP -> activity!!.addLog(ETouchEventActivity.ERootView + "onInterceptTouchEvent：ACTION_UP")
            MotionEvent.ACTION_CANCEL -> activity!!.addLog(ETouchEventActivity.ERootView + "onInterceptTouchEvent：ACTION_CANCEL")
        }
        return if (activity!!.switchs[2][2][0]) super.onInterceptTouchEvent(ev) else activity!!.switchs[2][2][1]
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> activity!!.addLog(ETouchEventActivity.ERootView + "onTouchEvent：ACTION_DOWN")
            MotionEvent.ACTION_MOVE -> if (activity!!.openMoveLog) {
                activity!!.addLog(ETouchEventActivity.ERootView + "onTouchEvent：ACTION_MOVE")
            }
            MotionEvent.ACTION_UP -> activity!!.addLog(ETouchEventActivity.ERootView + "onTouchEvent：ACTION_UP")
            MotionEvent.ACTION_CANCEL -> activity!!.addLog(ETouchEventActivity.ERootView + "onTouchEvent：ACTION_CANCEL")
        }
        return if (activity!!.switchs[2][3][0]) super.onTouchEvent(event) else activity!!.switchs[2][3][1]
    }
}
