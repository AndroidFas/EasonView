package base.appUpdate;

/**
 * Created by 83642 on 2017/10/11.
 */

public class Build {

    String downloadUrl;// 下载链接地址
    int threadNum;// 开启的线程数
    String filePath;// 保存文件路径地址
    String md5;    //文件MD5值
    String versionCode;   //需要更新的apk版本
    OnFileDownLoadListener onFileDownLoadListener;
    boolean neadSpeed;

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public Build setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
        return this;
    }

    public int getThreadNum() {
        return threadNum;
    }

    public Build setThreadNum(int threadNum) {
        this.threadNum = threadNum;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public Build setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getMd5() {
        return md5;
    }

    public Build setMd5(String md5) {
        this.md5 = md5;
        return this;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public Build setVersionCode(String versionCode) {
        this.versionCode = versionCode;
        return this;
    }

    public Build setOnFileDownLoadListener(OnFileDownLoadListener onFileDownLoadListener) {
        this.onFileDownLoadListener = onFileDownLoadListener;
        return this;
    }

    public boolean isNeadSpeed() {
        return neadSpeed;
    }

    public Build setNeadSpeed(boolean neadSpeed) {
        this.neadSpeed = neadSpeed;
        return this;
    }

    public FileDownloadTask start(){
        FileDownloadTask fileDownloadTask = new FileDownloadTask(this);
        fileDownloadTask.start();
        return fileDownloadTask;
    }
}
