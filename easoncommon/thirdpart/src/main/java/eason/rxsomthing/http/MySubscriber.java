package eason.rxsomthing.http;


import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;

import eason.rxsomthing.R;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author 邓治民
 * date 2017/5/27 11:07
 * http 回调
 */

class MySubscriber<T> implements Observer<T> {


    private final Context context;
    private RequestUI requestUI;

    private Tips tip;

    private HttpHandler<T> httpHandler;

    MySubscriber(Context context, RequestUI requestUI, Tips tip, HttpHandler httpHandler) {
        this.context = context;
        this.requestUI = requestUI;
        this.tip = tip;
        this.httpHandler = httpHandler;
    }

    private void onStart() {
        if (requestUI != null) {
            if (requestUI.isNeedShow())
                requestUI.onStart(tip == null ? Tips.DEFAULT.getMsg() : tip.getMsg());
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.d("cyj-http", e.toString());
        MyThrowable throwable;
        String errMessage = context.getResources().getString(R.string.request_fail);
        String errCode = "-2";
        if (e.getMessage() != null) {
            if (e.getMessage().contains("ailed to connect")) {
                if (e.getMessage().contains("after")) {
                    errMessage = context.getResources().getString(R.string.networt_unsteadiness);
                    errCode = "-1";
                } else {
                    errMessage = context.getResources().getString(R.string.networt_unavailable_pls_check);
                    errCode = "-1";
                }
            } else if (e.getMessage().contains("404") || e.getMessage().contains("5")) {//504网关超时
                errMessage = context.getResources().getString(R.string.server_anomaly_try_later);
                errCode = "0";
            } else if (e.getMessage().contains("timeout")) {
                errMessage = context.getResources().getString(R.string.networt_unsteadiness);
                errCode = "-1";
            }

            throwable = new MyThrowable(e, errCode, errMessage);

            if (null != requestUI && requestUI.isNeedShow()) {
                requestUI.onError(throwable);
            }

            if (null != httpHandler) {
                httpHandler.DefaultError(throwable);
            }

        } else {
            throwable = new MyThrowable(e, "-1", "出现未知错误");
            if (null != requestUI && requestUI.isNeedShow()) {
                requestUI.onError(throwable);
            }
            if (null != httpHandler) {
                httpHandler.DefaultError(throwable);
            }

        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {
        onStart();
    }

    @Override
    public void onNext(T rsp) {
        MyThrowable throwable;
        if (rsp == null) {//判空
            throwable = new MyThrowable(null, "-1", context.getString(R.string.server_anomaly_try_later));
            if (requestUI != null && requestUI.isNeedShow()) {
                requestUI.onError(throwable);
            }

            if (null != httpHandler) {
                httpHandler.DefaultError(throwable);
            }

        } else {
            if (requestUI != null) {
                requestUI.onSuscess();
            }
            if (null != httpHandler) {
                if (rsp instanceof JsonObject) {
                    if (Config.isDebug)
                        Log.d(Config.OKHTTP, "Sending response:" + rsp.toString());
                    JsonObject jsonObject = (JsonObject) rsp;
                    if ("000000".equals(jsonObject.get("code").getAsString())) {
                        httpHandler.DefaultNext(rsp);
                    } else {
                        String errCode = jsonObject.get("code").getAsString();
                        String errMsg = jsonObject.get("msg").getAsString();
                        throwable = new MyThrowable(null, errCode, errMsg);
                        httpHandler.DefaultError(throwable);
                        if (requestUI != null) {
                            requestUI.onError(throwable);
                        }
                    }
                } else {
                    httpHandler.DefaultNext(rsp);
                }


            }

        }
    }

}
