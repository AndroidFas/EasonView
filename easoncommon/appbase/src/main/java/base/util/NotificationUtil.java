package base.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * 通知管理器
 */
public class NotificationUtil {
    //    private  NotificationUtil instance;
//    public static void getInstance(){
//        if (null==ins)
//    }
    private static String channel = "zgono";
    private static String channelName = "小包智工";
    private static String description = "小包智工通知";

    //通知参数封装
    public static class NotificationWrapper {
        public int smallIcon;
        public String title;//通知标题
        public String content;//内容
        public boolean onGoing;//是否常驻
        public boolean autoCancel;//点击后消失
        public PendingIntent pendingIntent;

        public boolean[] feel;//sound, vibration,light  -- 声音  震动  闪灯

        public NotificationWrapper(int smallIcon, String title, String content, boolean onGoing, boolean autoCancel, PendingIntent pendingIntent, boolean[] feel) {
            this.smallIcon = smallIcon;
            this.title = title;
            this.content = content;
            this.onGoing = onGoing;
            this.autoCancel = autoCancel;
            this.pendingIntent = pendingIntent;
            this.feel = feel;
        }
    }


    public static Notification.Builder buildNotification(Context context, int notificationId, NotificationWrapper wrapper) {
        NotificationManager manager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

        Notification.Builder builder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            //创建通知渠道
            NotificationChannel notificationChannel = new NotificationChannel(channel, channelName, importance);
            // 配置通知渠道的属性
            notificationChannel.setDescription(description);
            // 设置通知出现时声音，默认通知是有声音的
//            notificationChannel.setSound(null, null);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            notificationChannel.enableLights(wrapper.feel[2]);
//            notificationChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            notificationChannel.enableVibration(wrapper.feel[1]);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            manager.createNotificationChannel(notificationChannel);
            builder = new Notification.Builder(context, channel);
        } else {
            builder = new Notification.Builder(context);
        }
        builder.setSmallIcon(wrapper.smallIcon)
                /*设置title*/
                .setContentTitle(wrapper.title)
                /*设置详细文本*/
                .setContentText(wrapper.content)
                /*设置发出通知的时间为发出通知时的系统时间*/
                .setWhen(System.currentTimeMillis())
                /*设置发出通知时在status bar进行提醒*/
                .setTicker(wrapper.title)
                /*设为true,notification将无法通过左右滑动的方式清除可用于添加常驻通知，必须调用cancle方法来清除
                 * 他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)*/
                .setOngoing(wrapper.onGoing)
                /*设置点击后通知消失*/
                .setAutoCancel(wrapper.autoCancel)
//                .setContentIntent()//设置点击跳转
                /*设置通知数量的显示类似于QQ那种，用于同志的合并*/
//        .setNumber(2)
                .setPriority(Notification.PRIORITY_DEFAULT); //设置该通知优先级
        if (wrapper.feel[0]) {//开启声音
            builder.setDefaults(Notification.DEFAULT_SOUND);
        }
        if (wrapper.feel[1]) {//开启震动
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
        }
        if (wrapper.feel[0] && wrapper.feel[1]) {
            builder.setDefaults(Notification.DEFAULT_ALL);
        } else {
            builder.setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE);
        }

//                .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE);//只显示内容，无声音无振动
//                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
        //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
        /*点击跳转*/
        if (null != wrapper.pendingIntent)
            builder.setContentIntent(wrapper.pendingIntent);

        Notification notification = builder.build();
        manager.notify(notificationId, notification);
        return builder;
    }

    /**
     * 进度提示的通知
     *
     * @param context
     * @param notificationId
     * @param builder
     * @param max
     * @param current
     */
    public static void progressNotification(Context context, int notificationId, Notification.Builder builder, int max, int current, NFCallback callback) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //去掉震动和声音
        builder.setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.getNotificationChannel(channel).enableVibration(false);//去除震动
        }

        builder.setContentTitle(callback.beginTitle());
        builder.setContentText(callback.beginContent());
        //其中max为进度最大值，progress为当前进度，indeterminate为不确定的（设置为true，则为不确定的，反之则确定）
        builder.setProgress(max, current, max == current);
        if (max == current) {
            builder.setContentTitle(callback.endTitle());
            builder.setContentText(callback.endContent());
        }
        Notification notification = builder.build();
        manager.notify(notificationId, notification);
    }

    public static void cancel(Context context, int notificationId) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }

    public static void cancelAll(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
    }

    public interface NFCallback {
        String beginTitle();

        String endTitle();

        String beginContent();

        String endContent();

    }

    /*=====================用法======================*/
    private void test() {
        final Context context = null;
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, new Intent(context, null), PendingIntent.FLAG_UPDATE_CURRENT);//替换成自己的class
        final NotificationUtil.NotificationWrapper wrapper = new NotificationUtil.NotificationWrapper(0,//替换成自己的icon
                "测试通知标题",
                "测试通知内容",
                true, false, pendingIntent, new boolean[]{true, false, true});
        final Notification.Builder builder = NotificationUtil.buildNotification(context, 1, wrapper);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(50);//演示休眠50毫秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    final int finalI = i;
                    NotificationUtil.progressNotification(context, 1, builder, 100, i + 1, new NotificationUtil.NFCallback() {
                        @Override
                        public String beginTitle() {
                            return "下载";
                        }

                        @Override
                        public String endTitle() {
                            return "安装";
                        }

                        @Override
                        public String beginContent() {
                            return "下载" + finalI + "%";
                        }

                        @Override
                        public String endContent() {
                            return "安装中...";
                        }
                    });
                    if (i == 99)
                        NotificationUtil.cancel(context, 1);
                }
            }
        }).start();

    }
}
