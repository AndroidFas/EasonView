package eason.rxsomthing.http;

/**
 * Created by 83642 on 2017/5/25.
 */

public abstract class HttpHandler<T> {

    public abstract void DefaultNext(T rsp);

    public void DefaultError(MyThrowable throwable){

    }

    public void DefaultEmpty(){

    }

    public void DefaultSsion(){
    }


}
