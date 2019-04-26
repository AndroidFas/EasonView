package base.util;

import android.media.MediaRecorder;
import android.os.Handler;

import java.io.File;

/**
 * 媒体管理-语音录制，分贝处理
 */
public class MediaRecordUtil {
    private MediaRecorder mRecorder;
    private boolean hasPrepared;
    private boolean isReocording;
    private File file;

    private static final class INSTANCE {
        private static final MediaRecordUtil instance = new MediaRecordUtil();
    }

    private MediaRecordUtil() {

    }

    private void init() {
        if (null == mRecorder) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setAudioChannels(1); // MONO
            mRecorder.setAudioSamplingRate(8000); // 8000Hz
            mRecorder.setAudioEncodingBitRate(64); // seems if change this to
            // 128, still got same file
            // size.

        }
    }

    public static MediaRecordUtil getInstance() {
        return INSTANCE.instance;
    }

    /*-------------------工具方法-------------------*/

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (isReocording) {
                int bei = mRecorder.getMaxAmplitude() * 13 / 0x7FFF;
                if (null != mRecordListener)
                    mRecordListener.onRecording(bei);
                mHandler.postDelayed(this, 100);
            }
        }
    };
    private Handler mHandler;
    private RecordListener mRecordListener;

    /**
     * 播放的入口方法
     *
     * @param file           文件
     * @param handler        handler 让回调进入主线程
     * @param recordListener 录音回调
     */
    public void prepare(File file, Handler handler, RecordListener recordListener) {
        hasPrepared = false; // 开始播放前讲Flag置为不可操作

        this.mHandler = handler;
        this.mRecordListener = recordListener;
        this.file = file;
    }

    public interface RecordListener {
        void onStart();

        //音频声音大小
        void onRecording(int bei);

        //录音结束
        void onComplete();

        //停止  废弃录音
        void onStop();
    }

    public void start() {
        init();
        try {
            mRecorder.setOutputFile(file.getAbsolutePath());
            mRecorder.prepare();
            hasPrepared = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != mRecorder && hasPrepared) {
            mRecorder.start();
            isReocording = true;
            //开始计时
            mHandler.postDelayed(mRunnable, 100);
            if (null != mRecordListener)
                mRecordListener.onStart();
        }
    }

    /**
     * 废弃录制
     */
    public void discardRecord() {
        release();
        if (file != null && file.exists() && !file.isDirectory()) {
            file.delete();
        }
        if (null != mRecordListener)
            mRecordListener.onStop();
    }

    /**
     * 录制完毕
     */
    public void stop() {
        if (null != mRecorder) {
            release();
        }
        if (null != mRecordListener)
            mRecordListener.onComplete();
    }

    private void release() {
        if (null != mRecorder) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            isReocording = false;
        }
    }


    public void destroy() {
        release();
        if (null != mHandler)
            mHandler.removeCallbacks(mRunnable);
    }

    /*-------------------录音-------------------*/
}
