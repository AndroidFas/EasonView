package base;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

import eason.rxsomthing.http.Config;

public class BaseApplication extends Application {
    public static boolean isDebug = Config.isDebug;
    public static boolean isApkUpdate = true;

    @Override
    public void onCreate() {
        super.onCreate();
        if (isDebug) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ARouter.getInstance().destroy();
    }
}
