package eason.rxsomthing.http;

/**
 * Created by jyt on 2016/7/6.
 *
 */
public interface RequestUI {
    void onStart(String info);

    void onSuscess();

    void onError(MyThrowable e);

    boolean isNeedShow();

    void empty();
}
