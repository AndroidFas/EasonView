package eason.rxsomthing.http;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import eason.rxsomthing.R;


public class AlertDialogUtil implements RequestUI {

    private Context context;
    private TextView lodingTxt;
    public Dialog dialog;

    private boolean needShow = true;

    //加载窗
    public AlertDialogUtil(Context context) {
        if (context == null) {
            return;
        }
        init(context, R.string.loading_txt);
    }

    public AlertDialogUtil(Context context, int textId) {
        if (context == null) {
            return;
        }
        init(context, textId);
    }

    private void init(Context context, int textId) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(
                R.layout.loading_layout, null);
        lodingTxt = (TextView) view
                .findViewById(R.id.loading_tv);
        lodingTxt.setText(context.getString(textId));
        dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params1 = window.getAttributes();
        int width = context.getResources().getDisplayMetrics().widthPixels;
        float margin = 30 * context.getResources().getDisplayMetrics().density * 2;
        params1.width = Math.round(width - margin);
        window.setAttributes(params1);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
    }


    public void showAlertDialog(String... args) {
        if (args.length == 1) {
            lodingTxt.setText(args[0]);
        }
        if (dialog != null && !dialog.isShowing())
            dialog.show();
    }


    public void cancelAlertDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    @Override
    public void onStart(String info) {
        showAlertDialog(info);
    }

    @Override
    public void onSuscess() {
        cancelAlertDialog();
    }

    @Override
    public void onError(MyThrowable e) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show();
        cancelAlertDialog();
    }

    public void setNeedShow(boolean needShow) {
        this.needShow = needShow;
    }

    @Override
    public boolean isNeedShow() {
        return needShow;
    }


    @Override
    public void empty() {
        onSuscess();
    }
}
