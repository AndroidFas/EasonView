package com.eason

import android.content.Intent
import android.view.View
import com.eason.animation.ETranslateActivity
import com.eason.touchevent.ETouchEventActivity

class MainActivity : BaseActivity() {

    override val layoutId: Int
        get() = R.layout.activity_main

    override fun init() {

    }

    /**
     * 动画
     *
     * @param view
     */
    fun animation(view: View) {
        startActivity(Intent(this, ETranslateActivity::class.java))
    }

    fun touchEvent(view: View) {
        startActivity(Intent(this, ETouchEventActivity::class.java))
    }
}
