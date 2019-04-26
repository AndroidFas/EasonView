package base.appUpdate;

import java.io.File;

/**
 * Created by 83642 on 2017/6/16.
 */

public abstract class OnFileDownLoadListener {

    public abstract void updateSpeed(String speed);

    public void onStart(int max){}

    public abstract void onDownLoad(float ratio,int schedule);

    public abstract void onEnd(File file, Build build);

    public abstract void onErre(String err);
}
