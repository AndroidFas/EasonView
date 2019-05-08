package com.eason

import android.os.Bundle

abstract class BaseActivity : base.BaseActivity() {

    abstract val layoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        init()
    }

    protected abstract fun init()
}
