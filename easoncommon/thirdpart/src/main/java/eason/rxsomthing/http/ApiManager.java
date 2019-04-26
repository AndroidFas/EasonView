package eason.rxsomthing.http;


import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rxsomthing.bus.LifeCycleEvent;

public class ApiManager {
    private static Api mApi;

    public static Api getApi() {
        if (null == mApi)
            synchronized (ApiManager.class) {
                if (null == mApi)
                    mApi = MyRetrofit.getInstance().create(Api.class);
            }
        return mApi;
    }

    @NonNull
    public static <T> ObservableTransformer<T, T> bindUntilEvent(@NonNull final LifeCycleEvent event, final PublishSubject<LifeCycleEvent> lifecycleSubject) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                Observable<LifeCycleEvent> compareLifecycleObservable =
                        lifecycleSubject.skipWhile(new Predicate<LifeCycleEvent>() {
                            @Override
                            public boolean test(LifeCycleEvent o) throws Exception {
                                return !o.equals(event);
                            }
                        });
                return upstream.takeUntil(compareLifecycleObservable);
            }

        };
    }


    public static <T> void post(Observable<T> observable, Context context, RequestUI requestUI, Tips tips, HttpHandler httpHandler) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber<T>(context, requestUI, tips, httpHandler));
    }

    public static void upLoadFile(String url, Context context, RequestUI requestUI, String pjobId, Tips tips, HttpHandler httpHandler, File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        // MultipartBody.Part  和后端约定好Key，这里的partName是用image
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        getApi().upLoadFile(url, pjobId, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySubscriber(context, requestUI, tips, httpHandler));
    }

    /**
     * 下载文件
     *
     * @param fileUrl     文件网络url
     * @param destFileDir 存储目标文件路径
     */
    public static void downLoadFile(String fileUrl, File destFileDir, final DownLoadFileCallback downLoadFileCallback) {
        final File file = destFileDir;
        final Request request = new Request.Builder().url(fileUrl).build();
        final Call call = MyRetrofit.getInstance().getClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                downLoadFileCallback.result("下载失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    long total = response.body().contentLength();
                    long current = 0;
                    is = response.body().byteStream();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        current += len;
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    downLoadFileCallback.result("下载成功");
                } catch (IOException e) {
                    e.printStackTrace();
                    downLoadFileCallback.result("下载失败");
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public interface DownLoadFileCallback {
        void result(String msg);
    }
}
