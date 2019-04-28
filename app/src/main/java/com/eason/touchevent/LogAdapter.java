package com.eason.touchevent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eason.R;

import java.util.ArrayList;
import java.util.List;

public class LogAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<String> datas;

    public LogAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.touchevent_item_log, viewGroup, false)) {
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        String content = datas.get(i);
        String color = "";
        if (content.contains(ETouchEventActivity.ETouchEventActivity)) {
            color = "#cc0000";
        }
        if (content.contains(ETouchEventActivity.ERootView)) {
            color = "#ff8800";
        }
        if (content.contains(ETouchEventActivity.FirstViewGroup)) {
            color = "#FEE600";
        }
        if (content.contains(ETouchEventActivity.LastView)) {
            color = "#0099cc";
        }
        String replace = "<font color=\"" + color + "\">" + content + "</font>";
        ((TextView) viewHolder.itemView).setText(Html.fromHtml(replace));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
