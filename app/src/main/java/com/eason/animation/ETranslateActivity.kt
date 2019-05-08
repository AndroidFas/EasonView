package com.eason.animation

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import base.util.Logger
import base.util.StringUtil
import com.eason.BaseActivity
import com.eason.R

/**
 * 测试平移
 */
class ETranslateActivity : BaseActivity() {

    private var tv_getX: TextView? = null
    private var tv_getRawX: TextView? = null
    private var tv_getY: TextView? = null
    private var tv_getRawY: TextView? = null
    private var tv_x: TextView? = null
    private var tv_y: TextView? = null
    private var tv_scrollX: TextView? = null
    private var tv_scrollY: TextView? = null
    private var tv_transLateX: TextView? = null
    private var tv_transLateY: TextView? = null

    private var downX: Float = 0.toFloat()
    private var downY: Float = 0.toFloat()
    private var transView: View? = null

    private var hasTransX: Float = 0.toFloat()
    private var hasTransY: Float = 0.toFloat()
    private var etX: EditText? = null
    private var etY: EditText? = null
    private var switchView: Switch? = null

    private var isCheck: Boolean = false
    private var isScrollBy: Boolean = false
    private var rootView: View? = null

    override val layoutId: Int
        get() = R.layout.animation_act_main

    override fun init() {
        rootView = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)

        transView = findViewById(R.id.iv_trans_view)
        transView!!.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Logger.d(TAG, "OnTouchListener:ACTION_DOWN")
                    downX = event.rawX
                    downY = event.rawY
                    hasTransX = transView!!.translationX
                    hasTransY = transView!!.translationY

                    tv_getX!!.text = "getX:" + event.x
                    tv_getY!!.text = "getY:" + event.y
                    tv_getRawX!!.text = "getRawX:" + event.rawX
                    tv_getRawY!!.text = "getRawY:" + event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    Logger.d(TAG, "OnTouchListener:ACTION_MOVE")
                    tv_getX!!.text = "getX:" + event.x
                    tv_getY!!.text = "getY:" + event.y
                    tv_getRawX!!.text = "getRawX:" + event.rawX
                    tv_getRawY!!.text = "getRawY:" + event.rawY

                    //移动
                    transView!!.translationX = hasTransX + event.rawX - downX
                    transView!!.translationY = hasTransY + event.rawY - downY
                    getTransViewParams()
                }
                MotionEvent.ACTION_UP -> Logger.d(TAG, "OnTouchListener:ACTION_UP")
                MotionEvent.ACTION_CANCEL -> Logger.d(TAG, "OnTouchListener:ACTION_CANCEL")
            }
            //拦截了才能收到move指令【全盘接受事件】
            true
        }

        tv_getX = findViewById(R.id.tv_getX)
        tv_getRawX = findViewById(R.id.tv_getRawX)
        tv_getY = findViewById(R.id.tv_getY)
        tv_getRawY = findViewById(R.id.tv_getRawY)
        tv_x = findViewById(R.id.tv_x)
        tv_y = findViewById(R.id.tv_y)
        tv_scrollX = findViewById(R.id.tv_scrollX)
        tv_scrollY = findViewById(R.id.tv_scrollY)
        tv_transLateX = findViewById(R.id.tv_transLateX)
        tv_transLateY = findViewById(R.id.tv_transLateY)
        //滚动相关
        etX = findViewById(R.id.et_x)
        etY = findViewById(R.id.et_y)
        switchView = findViewById(R.id.switch_view)
        switchView!!.setOnCheckedChangeListener { buttonView, isChecked -> isCheck = isChecked }
        //打开scrollBy
        (findViewById<View>(R.id.switch_scrollBy) as Switch).setOnCheckedChangeListener { buttonView, isChecked -> isScrollBy = isChecked }
        rootView!!.post { getTransViewParams() }
    }


    fun scroll(view: View) {
        val scrollX = etX!!.text.toString().trim { it <= ' ' }
        val scrollY = etY!!.text.toString().trim { it <= ' ' }
        if (StringUtil.isEmpty(scrollX) && StringUtil.isEmpty(scrollY)) {
            return
        }
        val x = if (StringUtil.isEmpty(scrollX)) 0 else Integer.parseInt(scrollX)
        val y = if (StringUtil.isEmpty(scrollY)) 0 else Integer.parseInt(scrollY)
        if (isCheck) {
            //调用根view的scroll
            if (isScrollBy)
                rootView!!.scrollBy(x, y)
            else
                rootView!!.scrollTo(x, y)
        } else {
            if (isScrollBy)
                transView!!.scrollBy(x, y)
            else
                transView!!.scrollTo(x, y)
        }
        getTransViewParams()
    }

    fun reset(view: View) {
        rootView!!.scrollTo(0, 0)
        transView!!.scrollTo(0, 0)
        transView!!.translationX = 0f
        transView!!.translationY = 0f
        getTransViewParams()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Logger.d(TAG, "onTouchEvent")
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
            }
        }//                downX = event.getRawX();
        //                downY = event.getRawY();
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
        return super.onTouchEvent(event)
    }

    //展示蓝色区域的相关参数
    private fun getTransViewParams() {
        //相关参数
        tv_x!!.text = "getX:" + transView!!.x
        tv_y!!.text = "getY:" + transView!!.y

        tv_scrollX!!.text = "scrollX:" + transView!!.scrollX
        tv_scrollY!!.text = "scrollY:" + transView!!.scrollY

        tv_transLateX!!.text = "transLateX:" + transView!!.translationX
        tv_transLateY!!.text = "transLateY:" + transView!!.translationY
    }

    companion object {
        private val TAG = "ETranslateActivity=="
    }

}
