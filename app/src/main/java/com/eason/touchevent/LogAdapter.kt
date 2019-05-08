package com.eason.touchevent

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.eason.R

class LogAdapter(private val context: Context, private val datas: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val content = datas[p1]
        var color = ""
        if (content.contains(ETouchEventActivity.ETouchEventActivity)) {
            color = "#cc0000"
        }
        if (content.contains(ETouchEventActivity.ERootView)) {
            color = "#ff8800"
        }
        if (content.contains(ETouchEventActivity.FirstViewGroup)) {
            color = "#FEE600"
        }
        if (content.contains(ETouchEventActivity.LastView)) {
            color = "#0099cc"
        }
        val replace = "<font color=\"$color\">$content</font>"
        (p0.itemView as TextView).text = Html.fromHtml(replace)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.touchevent_item_log, viewGroup, false)) {

        }
    }

    override fun getItemCount(): Int {
        return datas.size
    }
}
