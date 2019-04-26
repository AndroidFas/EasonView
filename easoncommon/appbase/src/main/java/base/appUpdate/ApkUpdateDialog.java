package base.appUpdate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import base.BaseActivity;
import base.BaseApplication;
import zgono.zhigonghulian.com.base.R;


/**
 * cyj
 */

public class ApkUpdateDialog extends Dialog {


    private TextView tv_desc;

    private MyProgressBar mProgressBar;

    private TextView tv_version;

    private View layout_menu;

    private TextView tv_internet_speed;
    private Button bt_update;
    private TextView tv_update_errer;
    private Button bt_cancel;
    private File tempFile;

    private NotificationManager manager;
    private NotificationCompat.Builder notifyBuilder;
    private Activity context;
    private int iconRes;
    private String appName;
    private boolean isForceUpdate;

    public ApkUpdateDialog(Activity context, int iconRes, String appName) {
        super(context);
        setContentView(R.layout.dialog_updateapk);
//        Window window = getWindow();
//        WindowManager.LayoutParams params1 = window.getAttributes();
//        int width = context.getResources().getDisplayMetrics().widthPixels;
//        params1.width = width*2/3;
//        window.setAttributes(params1);

        this.context = context;
        this.iconRes = iconRes;
        this.appName = appName;
        manager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        tempFile = FileUpdateUtils.getApkupdateFile();

        tv_desc = (TextView) findViewById(R.id.tv_desc);
        mProgressBar = (MyProgressBar) findViewById(R.id.up_progressbar);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_internet_speed = (TextView) findViewById(R.id.tv_internet_speed);
        tv_update_errer = (TextView) findViewById(R.id.tv_update_errer);
        tv_update_errer.setVisibility(View.GONE);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        bt_update = (Button) findViewById(R.id.bt_update);
        layout_menu = findViewById(R.id.layout_menu);

        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.equals(bt_update.getText(), "立即安装")) {
                    Log.d("======apkDir:", tempFile.getAbsolutePath());
                    checkPermision();
//                    FileUpdateUtils.startActionFile(context, tempFile, "application/vnd.android.package-archive");
                } else {
                    UpdateApkBean apkBean = (UpdateApkBean) v.getTag();
                    tv_update_errer.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.VISIBLE);
                    if (isForceUpdate) {
                        mProgressBar.setColor(Color.parseColor("#0387d1"), Color.BLACK, Color.parseColor("#DBDBDB"));
                        downLoadeFile(apkBean);
                    } else {
                        Toast.makeText(ApkUpdateDialog.this.context, "后台下载中...", Toast.LENGTH_SHORT).show();
                        dismiss();
                        //后台通知显示下载
                        showNotification(apkBean);
                    }
                    layout_menu.setVisibility(View.GONE);
                }
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                UpdateApkBean apkBean = (UpdateApkBean) v.getTag();
                if (isForceUpdate) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                }
            }
        });


    }

    private void checkPermision() {
        //检查允许未知来源的权限
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            checkInstallPermission();
        } else {
            install();
        }
    }

    /**
     * 检查8.0权限
     */
    @RequiresApi(api = android.os.Build.VERSION_CODES.O)
    private void checkInstallPermission() {
        boolean b = context.getPackageManager().canRequestPackageInstalls();
        if (b) {
            install();
        } else {
            getPermissionDialog();
        }
    }

    /**
     * 申请权限的对话框
     */
    private void getPermissionDialog() {
        String message = "为了正常升级APP，请点击设置-高级设置-允许安装未知来源应用，本功能只限用于APP版本升级";
        AlertDialog dialog = new AlertDialog.Builder(context).
                setCancelable(false).
                setTitle("权限提醒").
                setMessage(message).
                setPositiveButton("权限设置", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {//第6步骤，下载

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            //注意这个是8.0新API,直接跳转到允许安装位置来源的页面
                            intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, uri);
                        } else {
                            intent.setData(uri);
                        }
                        context.startActivityForResult(intent, BaseActivity.REQUEST_INSTALL_PACKAGES);
                    }
                }).
                setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //这里涉及到下载的强制更新，是不是强制更新   强制更新，点取消按钮，退出程序
                        if (isForceUpdate) {
                            Toast.makeText(context, "此版本需要更新，程序即将退出", Toast.LENGTH_SHORT).show();
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(0);
                        } else {
                            dialog.dismiss();
                        }
                    }
                }).
                create();
        dialog.show();
    }


    public void show(UpdateApkBean apkBean) {
        bt_cancel.setTag(apkBean);
        bt_update.setTag(apkBean);
        tv_desc.setText(apkBean.appDesc);
        tv_version.setText(apkBean.appVersionName);
        if (tempFile.exists()) {
            String file_md5 = FileMd5Util.getFileMD5String(tempFile);
            Log.d("UpdateApk", "MD5:   " + file_md5);
            if (TextUtils.equals(file_md5, apkBean.appMd5)) {
                bt_update.setText("立即安装");
            } else {
                tempFile.delete();
            }
        }
        isForceUpdate = apkBean.appForceUpdate;
        if (isForceUpdate) {
            setCancelable(false);
            setCanceledOnTouchOutside(false);
        } else {
            setCancelable(true);
            setCanceledOnTouchOutside(true);
        }
        if (BaseApplication.isApkUpdate && isForceUpdate) {
            bt_update.setEnabled(false);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            bt_update.setEnabled(true);
            mProgressBar.setVisibility(View.GONE);
        }
        show();
    }

    //android 8.0 通知适配
    private boolean checkNotifyPermision() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channel = "update";
            String channelName = "软件更新";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            //创建通知渠道
            NotificationChannel notificationChannel = new NotificationChannel(channel, channelName, importance);
            manager.createNotificationChannel(notificationChannel);
            return true;
        }
        return false;
    }

    private void showNotification(UpdateApkBean apkBean) {
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R1.drawable.push);
        NotificationCompat.Builder notifyBuilder = null;
        if (checkNotifyPermision()) {
            notifyBuilder = new NotificationCompat.Builder(context, "update");
        } else {
            notifyBuilder = new NotificationCompat.Builder(context);
        }
        /*设置large icon*/
//        .setLargeIcon(bitmap)
        /*设置small icon*/
        notifyBuilder.setSmallIcon(iconRes)
                /*设置title*/
                .setContentTitle(appName)
                /*设置详细文本*/
                .setContentText("当前进度：" + 0 + "% ")
                /*设置发出通知的时间为发出通知时的系统时间*/
                .setWhen(System.currentTimeMillis())
                /*设置发出通知时在status bar进行提醒*/
                .setTicker(appName)
                /*设为true,notification将无法通过左右滑动的方式清除可用于添加常驻通知，必须调用cancle方法来清除
                 * 他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)*/
                .setOngoing(false)
                /*设置点击后通知消失*/
                .setAutoCancel(false)
//                .setContentIntent()//设置点击跳转
                /*设置通知数量的显示类似于QQ那种，用于同志的合并*/
//        .setNumber(2)
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE);//只显示内容，无声音无振动
//                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
        //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
        /*点击跳转*/
//                .setContentIntent(getPendingIntent(false, holder));
//				.setProgress(100, 0, false);
        manager.notify(1, notifyBuilder.build());
        this.notifyBuilder = notifyBuilder;
        downLoadeFile(apkBean);
    }

    private void downLoadeFile(UpdateApkBean apkBean) {
        new Build()
                .setFilePath(tempFile.getAbsolutePath())
                .setDownloadUrl(apkBean.appUrl)
                .setMd5(apkBean.appMd5)
                .setNeadSpeed(true)
                .setThreadNum(1)
                .setVersionCode(apkBean.appVersionCode)
                .setOnFileDownLoadListener(new OnFileDownLoadListener() {
                    @Override
                    public void onStart(int max) {
                        BaseApplication.isApkUpdate = true;
                    }

                    @Override
                    public void updateSpeed(String speed) {
                        if (null != tv_internet_speed)
                            tv_internet_speed.setText(speed);
                    }

                    @Override
                    public void onDownLoad(float ratio, int schedule) {
                        //刷新通知
                        reFreshNotification(ratio);
                        if (mProgressBar != null) {
                            mProgressBar.setValue2(ratio);
                        }
                    }

                    @Override
                    public void onEnd(File file, Build build) {
                        layout_menu.setVisibility(View.VISIBLE);
                        BaseApplication.isApkUpdate = false;
                        bt_update.setText("立即安装");
                        bt_update.setEnabled(true);
                        checkPermision();
//                        FileUpdateUtils.startActionFile(context, file, "application/vnd.android.package-archive");
                    }

                    @Override
                    public void onErre(String err) {
                        BaseApplication.isApkUpdate = false;
                        bt_update.setEnabled(true);
                        layout_menu.setVisibility(View.VISIBLE);
                        dismiss();
                        //取消通知
                        manager.cancel(1);
                    }
                }).start();
    }

    private void reFreshNotification(float progress) {
        if (null == notifyBuilder) {
            return;
        }
        if (progress == 1) {
            notifyBuilder.setProgress(100, (int) (progress * 100), false);
            notifyBuilder.setAutoCancel(true);
            notifyBuilder.setContentText("下载完成");
        } else if (progress == -1) {
            notifyBuilder.setAutoCancel(true);
            notifyBuilder.setContentText("下载失败!");
        } else {
            notifyBuilder.setProgress(100, (int) (progress * 100), false);
            notifyBuilder.setContentText("当前进度：" + (int) (progress * 100) + "% ");
        }
        manager.notify(1, notifyBuilder.build());
    }

    public void install() {
        FileUpdateUtils.startActionFile(context, tempFile, "application/vnd.android.package-archive");
    }
}
