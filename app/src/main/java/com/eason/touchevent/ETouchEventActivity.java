package com.eason.touchevent;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;

import com.eason.BaseActivity;
import com.eason.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import base.util.Logger;

public class ETouchEventActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private static final String key = "log";

    public static final String ETouchEventActivity = "领    导:";
    public static final String ERootView = "部    长:";
    public static final String FirstViewGroup = "组    长:";
    public static final String LastView = "员    工:";
    private List<String> datas = new ArrayList<>();
    public boolean[][][] switchs = new boolean[5][5][5];
    public boolean openMoveLog;
    private RecyclerView logList;
    private LogAdapter logAdapter;

    @Override
    protected void init() {
        logList = findViewById(R.id.log_list);
        logList.setLayoutManager(new LinearLayoutManager(this));
        logAdapter = new LogAdapter(this, datas);
        logList.setAdapter(logAdapter);

        ((Switch) findViewById(R.id.control_11)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.control_12)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.control_11_su)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.control_12_su)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.control_11_su)).setChecked(true);
        ((Switch) findViewById(R.id.control_12_su)).setChecked(true);

        ((Switch) findViewById(R.id.control_21)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.control_22)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.control_23)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.control_21_su)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.control_22_su)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.control_23_su)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.control_21_su)).setChecked(true);
        ((Switch) findViewById(R.id.control_22_su)).setChecked(true);
        ((Switch) findViewById(R.id.control_23_su)).setChecked(true);


        ((Switch) findViewById(R.id.control_31)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.control_32)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.control_33)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.control_31_su)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.control_32_su)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.control_33_su)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.control_31_su)).setChecked(true);
        ((Switch) findViewById(R.id.control_32_su)).setChecked(true);
        ((Switch) findViewById(R.id.control_33_su)).setChecked(true);


        ((Switch) findViewById(R.id.control_41)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.control_42)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.control_41_su)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.control_42_su)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.control_41_su)).setChecked(true);
        ((Switch) findViewById(R.id.control_42_su)).setChecked(true);

        ((Switch) findViewById(R.id.control_move_log)).setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        Logger.d(ETouchEventActivity, "switch回调：" + buttonView.getId() + "  isChecked:" + isChecked);
        switch (buttonView.getId()) {
            case R.id.control_11:
                switchs[1][1][1] = isChecked;
                break;
            case R.id.control_11_su:
                switchs[1][1][0] = isChecked;
                break;
            case R.id.control_12:
                switchs[1][2][1] = isChecked;
                break;
            case R.id.control_12_su:
                switchs[1][2][0] = isChecked;
                break;
            case R.id.control_21:
                switchs[2][1][1] = isChecked;
                break;
            case R.id.control_21_su:
                switchs[2][1][0] = isChecked;
                break;
            case R.id.control_22:
                switchs[2][2][1] = isChecked;
                break;
            case R.id.control_22_su:
                switchs[2][2][0] = isChecked;
                break;
            case R.id.control_23:
                switchs[2][3][1] = isChecked;
                break;
            case R.id.control_23_su:
                switchs[2][3][0] = isChecked;
                break;
            case R.id.control_31:
                switchs[3][1][1] = isChecked;
                break;
            case R.id.control_31_su:
                switchs[3][1][0] = isChecked;
                break;
            case R.id.control_32:
                switchs[3][2][1] = isChecked;
                break;
            case R.id.control_32_su:
                switchs[3][2][0] = isChecked;
                break;
            case R.id.control_33:
                switchs[3][3][1] = isChecked;
                break;
            case R.id.control_33_su:
                switchs[3][3][0] = isChecked;
                break;
            case R.id.control_41:
                switchs[4][1][1] = isChecked;
                break;
            case R.id.control_41_su:
                switchs[4][1][0] = isChecked;
                break;
            case R.id.control_42:
                switchs[4][2][1] = isChecked;
                break;
            case R.id.control_42_su:
                switchs[4][2][0] = isChecked;
                break;
            case R.id.control_move_log:
                openMoveLog = isChecked;
                break;
        }
    }

    //子控件添加事件监听日志
    public void addLog(final String content) {
        datas.add(content);
        logAdapter.notifyDataSetChanged();
        logList.smoothScrollToPosition(logAdapter.getItemCount() - 1);
    }

    public void clearLog(View view) {
        datas.clear();
        logAdapter.notifyDataSetChanged();
    }

    @Override
    public int getLayoutId() {
        return R.layout.touchevent_act_main;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                addLog(ETouchEventActivity + "dispatchTouchEvent：ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                if (openMoveLog)
                    addLog(ETouchEventActivity + "dispatchTouchEvent：ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                addLog(ETouchEventActivity + "dispatchTouchEvent：ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                addLog(ETouchEventActivity + "dispatchTouchEvent：ACTION_CANCEL");
                break;
        }
        return switchs[1][1][0] ? super.dispatchTouchEvent(ev) : switchs[1][1][1];
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                addLog(ETouchEventActivity + "onTouchEvent：ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                if (openMoveLog)
                    addLog(ETouchEventActivity + "onTouchEvent：ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                addLog(ETouchEventActivity + "onTouchEvent：ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                addLog(ETouchEventActivity + "onTouchEvent：ACTION_CANCEL");
                break;
        }
        return switchs[1][2][0] ? super.onTouchEvent(event) : switchs[1][2][1];
    }


}
