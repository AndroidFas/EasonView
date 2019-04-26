package base.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;

import java.io.IOException;

/**
 * 媒体管理-语音播放
 * VideoView是包装过的MediaPlayer,所以使用起来很相似。
 * Android的MediaPlayer包含了Audio和Video的播放功能，在Android的界面上，Music和Video两个应用程序都是调用MediaPlaer来实现的。
 */
public class MediaPlayUtil implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private MediaPlayer mPlayer;
    private boolean hasPrepared;

    /*-------------------监听回调-------------------*/
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        hasPrepared = false;
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (null != mPlayListener)
            mPlayListener.onComplete();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        hasPrepared = true; // 准备完成后回调到这里
        //回调总时间
        if (null != mPlayListener)
            mPlayListener.hasPrepared(Math.round(mPlayer.getDuration() / 1000));
    }

    private static final class INSTANCE {
        private static final MediaPlayUtil instance = new MediaPlayUtil();
    }

    private MediaPlayUtil() {
    }

    private void init() {
        if (null == mPlayer) {
            mPlayer = new MediaPlayer();
            mPlayer.setOnErrorListener(this);
            mPlayer.setOnCompletionListener(this);
            mPlayer.setOnPreparedListener(this);
        }
    }

    public static MediaPlayUtil getInstance() {
        return INSTANCE.instance;
    }

    /*-------------------工具方法-------------------*/

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (hasPrepared && isPlaying()) {
                mHandler.postDelayed(this, 1000);
            }
            int currentTime = Math
                    .round(mPlayer.getCurrentPosition() / 1000);
            if (null != mPlayListener)
                mPlayListener.currentTime(currentTime, Math.round(mPlayer.getDuration() / 1000));
        }
    };
    private Handler mHandler;
    private PlayListener mPlayListener;

    /**
     * 播放的入口方法
     *
     * @param context      上下文
     * @param dataSource   音频源
     * @param handler      handler 让回调进入主线程
     * @param playListener 播放进度回调
     */
    public void prepare(Context context, Uri dataSource, Handler handler, PlayListener playListener) {
        hasPrepared = false; // 开始播放前讲Flag置为不可操作
        init();
        this.mHandler = handler;
        this.mPlayListener = playListener;
        try {
            mPlayer.reset();
            mPlayer.setDataSource(context, dataSource); // 设置曲目资源
            mPlayer.prepareAsync(); // 异步的准备方法
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface PlayListener {
        void onStart();

        void onStop();

        void onComplete();

        //一秒回调一次
        void currentTime(int currentTime, int totalTime);//当前播放到哪了

        //仅回调一次
        void hasPrepared(int totalTime);
    }

    public void start() {
        // release()会释放player、将player置空，所以这里需要判断一下
        if (null != mPlayer && hasPrepared) {
            mPlayer.start();
            //开始计时
            mHandler.postDelayed(mRunnable, 1000);
            if (null != mPlayListener)
                mPlayListener.onStart();
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        if (!isPlaying()) return;
        if (null != mPlayer && hasPrepared) {
            mPlayer.pause();
            if (null != mPlayListener)
                mPlayListener.onStop();
        }
    }

    public void stop() {
        if (isPlaying()) {
            mPlayer.stop();
            if (null != mPlayListener)
                mPlayListener.onStop();
        }
    }

    /**
     * 拖拽
     *
     * @param position
     */
    public void seekTo(int position) {
        if (null != mPlayer && hasPrepared) {
            mPlayer.seekTo(position);
        }
    }

    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    public void destroy() {
        if (mPlayer != null) {
            hasPrepared = false;
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            if (null != mHandler)
                mHandler.removeCallbacks(mRunnable);
        }
    }

    /*-------------------录音-------------------*/
}
