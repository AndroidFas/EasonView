package com.eason.touchevent

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import com.eason.BaseActivity
import com.eason.R
import java.util.*

class ETouchEventActivity : BaseActivity(), CompoundButton.OnCheckedChangeListener {
    private val datas = ArrayList<String>()
    var switchs = Array(5) { Array(5) { BooleanArray(5) } }
    var openMoveLog: Boolean = false
    private var logList: RecyclerView? = null
    private var logAdapter: LogAdapter? = null

    override val layoutId: Int
        get() = R.layout.touchevent_act_main

    override fun init() {
        logList = findViewById(R.id.log_list)
        logList!!.layoutManager = LinearLayoutManager(this)
        logAdapter = LogAdapter(this, datas)
        logList!!.adapter = logAdapter

        (findViewById<View>(R.id.control_11) as Switch).setOnCheckedChangeListener(this)
        (findViewById<View>(R.id.control_12) as Switch).setOnCheckedChangeListener(this)
        (findViewById<View>(R.id.control_11_su) as Switch).setOnCheckedChangeListener(this)
        (findViewById<View>(R.id.control_12_su) as Switch).setOnCheckedChangeListener(this)
        (findViewById<View>(R.id.control_11_su) as Switch).isChecked = true
        (findViewById<View>(R.id.control_12_su) as Switch).isChecked = true

        (findViewById<View>(R.id.control_21) as Switch).setOnCheckedChangeListener(this)
        (findViewById<View>(R.id.control_22) as Switch).setOnCheckedChangeListener(this)
        (findViewById<View>(R.id.control_23) as Switch).setOnCheckedChangeListener(this)
        (findViewById<View>(R.id.control_21_su) as Switch).setOnCheckedChangeListener(this)
        (findViewById<View>(R.id.control_22_su) as Switch).setOnCheckedChangeListener(this)
        (findViewById<View>(R.id.control_23_su) as Switch).setOnCheckedChangeListener(this)
        (findViewById<View>(R.id.control_21_su) as Switch).isChecked = true
        (findViewById<View>(R.id.control_22_su) as Switch).isChecked = true
        (findViewById<View>(R.id.control_23_su) as Switch).isChecked = true


        (findViewById<View>(R.id.control_31) as Switch).setOnCheckedChangeListener(this)
        (findViewById<View>(R.id.control_32) as Switch).setOnCheckedChangeListener(this)
        (findViewById<View>(R.id.control_33) as Switch).setOnCheckedChangeListener(this)
        (findViewById<View>(R.id.control_31_su) as Switch).setOnCheckedChangeListener(this)
        (findViewById<View>(R.id.control_32_su) as Switch).setOnCheckedChangeListener(this)
        (findViewById<View>(R.id.control_33_su) as Switch).setOnCheckedChangeListener(this)
        (findViewById<View>(R.id.control_31_su) as Switch).isChecked = true
        (findViewById<View>(R.id.control_32_su) as Switch).isChecked = true
        (findViewById<View>(R.id.control_33_su) as Switch).isChecked = true


        (findViewById<View>(R.id.control_41) as Switch).setOnCheckedChangeListener(this)
        (findViewById<View>(R.id.control_42) as Switch).setOnCheckedChangeListener(this)
        (findViewById<View>(R.id.control_41_su) as Switch).setOnCheckedChangeListener(this)
        (findViewById<View>(R.id.control_42_su) as Switch).setOnCheckedChangeListener(this)
        (findViewById<View>(R.id.control_41_su) as Switch).isChecked = true
        (findViewById<View>(R.id.control_42_su) as Switch).isChecked = true

        (findViewById<View>(R.id.control_move_log) as Switch).setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        //        Logger.d(ETouchEventActivity, "switch回调：" + buttonView.getId() + "  isChecked:" + isChecked);
        when (buttonView.id) {
            R.id.control_11 -> switchs[1][1][1] = isChecked
            R.id.control_11_su -> switchs[1][1][0] = isChecked
            R.id.control_12 -> switchs[1][2][1] = isChecked
            R.id.control_12_su -> switchs[1][2][0] = isChecked
            R.id.control_21 -> switchs[2][1][1] = isChecked
            R.id.control_21_su -> switchs[2][1][0] = isChecked
            R.id.control_22 -> switchs[2][2][1] = isChecked
            R.id.control_22_su -> switchs[2][2][0] = isChecked
            R.id.control_23 -> switchs[2][3][1] = isChecked
            R.id.control_23_su -> switchs[2][3][0] = isChecked
            R.id.control_31 -> switchs[3][1][1] = isChecked
            R.id.control_31_su -> switchs[3][1][0] = isChecked
            R.id.control_32 -> switchs[3][2][1] = isChecked
            R.id.control_32_su -> switchs[3][2][0] = isChecked
            R.id.control_33 -> switchs[3][3][1] = isChecked
            R.id.control_33_su -> switchs[3][3][0] = isChecked
            R.id.control_41 -> switchs[4][1][1] = isChecked
            R.id.control_41_su -> switchs[4][1][0] = isChecked
            R.id.control_42 -> switchs[4][2][1] = isChecked
            R.id.control_42_su -> switchs[4][2][0] = isChecked
            R.id.control_move_log -> openMoveLog = isChecked
        }
    }

    //子控件添加事件监听日志
    fun addLog(content: String) {
        datas.add(content)
        logAdapter!!.notifyDataSetChanged()
        logList!!.smoothScrollToPosition(logAdapter!!.itemCount - 1)
    }

    fun clearLog(view: View) {
        datas.clear()
        logAdapter!!.notifyDataSetChanged()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> addLog(ETouchEventActivity + "dispatchTouchEvent：ACTION_DOWN")
            MotionEvent.ACTION_MOVE -> if (openMoveLog)
                addLog(ETouchEventActivity + "dispatchTouchEvent：ACTION_MOVE")
            MotionEvent.ACTION_UP -> addLog(ETouchEventActivity + "dispatchTouchEvent：ACTION_UP")
            MotionEvent.ACTION_CANCEL -> addLog(ETouchEventActivity + "dispatchTouchEvent：ACTION_CANCEL")
        }
        return if (switchs[1][1][0]) super.dispatchTouchEvent(ev) else switchs[1][1][1]
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> addLog(ETouchEventActivity + "onTouchEvent：ACTION_DOWN")
            MotionEvent.ACTION_MOVE -> if (openMoveLog)
                addLog(ETouchEventActivity + "onTouchEvent：ACTION_MOVE")
            MotionEvent.ACTION_UP -> addLog(ETouchEventActivity + "onTouchEvent：ACTION_UP")
            MotionEvent.ACTION_CANCEL -> addLog(ETouchEventActivity + "onTouchEvent：ACTION_CANCEL")
        }
        return if (switchs[1][2][0]) super.onTouchEvent(event) else switchs[1][2][1]
    }

    companion object {
        private val key = "log"

        val ETouchEventActivity = "领    导:"
        val ERootView = "部    长:"
        val FirstViewGroup = "组    长:"
        val LastView = "员    工:"
    }


}
