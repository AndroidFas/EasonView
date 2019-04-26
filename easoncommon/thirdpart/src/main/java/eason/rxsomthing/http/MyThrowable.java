package eason.rxsomthing.http;

/**
 * Created by 83642 on 2017/5/25.
 */

public class MyThrowable extends Throwable {
    public Throwable preThrowable;
    public String code;
    public String message;

    public MyThrowable(Throwable preThrowable, String code, String message) {
        this.preThrowable = preThrowable;
        this.code = code;
        this.message = message;
    }


}
