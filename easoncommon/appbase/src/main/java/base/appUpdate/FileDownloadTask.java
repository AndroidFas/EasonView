package base.appUpdate;

import android.net.TrafficStats;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author 邓治民
 * date 2017/8/17 11:42
 * 文件下载
 */
public class FileDownloadTask extends Thread {

    private final int count = 1;
    private long total_data = TrafficStats.getTotalRxBytes();



    private Build build;
    private FileDownloadThread[] threads;

    private Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                BundData bundData = (BundData) msg.obj;
                if (null != build.onFileDownLoadListener) {
                    build.onFileDownLoadListener.onDownLoad(bundData.ratio, bundData.all_size);
                }
                if (bundData.ratio >= 1) {
                    if (null != build.onFileDownLoadListener) {
                        File file = new File(build.filePath);
                        if(TextUtils.isEmpty(build.getMd5())){
                            build.onFileDownLoadListener.onEnd(file,build);
                        }else if(TextUtils.equals(FileMd5Util.getFileMD5String(file), build.getMd5())){
                            build.onFileDownLoadListener.onEnd(file,build);
                        }else {
                            build.onFileDownLoadListener.onErre("MD5 不匹配");
                        }
                    }

                    handler.removeCallbacks(speedRunnable);
                }
            }else if(msg.what == 3){
                if (null != build.onFileDownLoadListener) {
                    build.onFileDownLoadListener.onErre("文件下载失败");
                }
            }
        }
    };

    FileDownloadTask(Build build) {
        this.build = build;
        this.build.threadNum = 5;
    }

    private Runnable speedRunnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, count * 500);
            if (null != build.onFileDownLoadListener) {
                build.onFileDownLoadListener.updateSpeed(getNetSpeed() / 1024 + "kb/s");
            }
        }
    };

    /**
     * 核心方法，得到当前网速
     */
    private int getNetSpeed() {
        long traffic_data = TrafficStats.getTotalRxBytes() - total_data;
        total_data = TrafficStats.getTotalRxBytes();
        return (int) traffic_data / count;
    }

    @Override
    public void run() {
        if(build.neadSpeed){
            handler.removeCallbacks(speedRunnable);
            handler.postDelayed(speedRunnable,0);
        }
        threads = new FileDownloadThread[build.threadNum];
        int index;
        int des = 0;
        boolean isDestroy = false;
        while (!isDestroy) {
            index = 5;
            try {
                URL url = new URL(build.downloadUrl);
                URLConnection conn = url.openConnection();
                // 读取下载文件总大小
                int fileSize = conn.getContentLength();
                if (fileSize <= 0) {
                    System.out.println("读取文件失败");
                } else {
                    if (null != build.onFileDownLoadListener) {
                        build.onFileDownLoadListener.onStart(fileSize);
                    }
                    // 计算每条线程下载的数据长度
                    int blockSize = (fileSize % build.threadNum) == 0 ? fileSize / build.threadNum : fileSize / build.threadNum + 1;
                    File file = new File(build.filePath);
                    for (int i = 0; i < threads.length; i++) {
                        // 启动线程，分别下载每个线程需要下载的部分
                        threads[i] = new FileDownloadThread(url, file, blockSize, (i + 1));
                        threads[i].setName("Thread:" + i);
                        threads[i].start();
                    }

                    boolean isfinished = false;
                    boolean isChilcErrer;
                    int downloadedAllSize;
                    while (!isfinished) {
                        isfinished = true;
                        isChilcErrer = false;
                        // 当前所有线程下载总量
                        downloadedAllSize = 0;
                        for (FileDownloadThread thread : threads) {
                            downloadedAllSize += thread.getDownloadLength();
                            if (!thread.isCompleted()) {
                                isfinished = false;
                            }
                            isChilcErrer = thread.isOnErre();
                        }
                        // 通知handler去更新视图组件
                        Message msg = handler.obtainMessage();
                        msg.what = 2;
                        BundData bundData = new BundData();
                        bundData.all_size = downloadedAllSize;
                        bundData.ratio = (float) downloadedAllSize / fileSize;
                        msg.obj = bundData;
                        handler.sendMessage(msg);
                        if (isChilcErrer) {
                            Thread.sleep(1000);// 休息1秒后再读取下载进度
                        } else {
                            Thread.sleep(10);// 休息1秒后再读取下载进度
                        }
                    }
                    isDestroy = true;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            try {
                while (index > 0) {
                    sleep(1000);
                    index--;
                    Log.d("update_thread", "connect filed" + index);
                    des++;
                }
                if(des > 60 && !isDestroy){
                    handler.sendEmptyMessage(3);
                    for (FileDownloadThread thread : threads) {
                        // 启动线程，分别下载每个线程需要下载的部分
                        if(null != thread)
                            thread.setCompleted(true);
                    }
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class BundData {
        int all_size;
        float ratio;
    }

}
