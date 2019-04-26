package eason.rxsomthing.http;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static eason.rxsomthing.http.Config.SERVER_ROOT_URI;
import static eason.rxsomthing.http.Config.SESSIONID;
import static eason.rxsomthing.http.Config.SESSIONID_KEY;


class MyRetrofit {

    private Retrofit mRetrofit;

    private static class SingletonHolder {
        private static final MyRetrofit INSTANCE = new MyRetrofit();
    }

    /**
     * 获取RetrofitServiceManager
     *
     * @return
     */
    public static MyRetrofit getInstance() {
        return SingletonHolder.INSTANCE;
    }


    private MyRetrofit() {
        init();
    }

    private void init() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        //对象转换
        mRetrofit = new Retrofit.Builder()
                .client(getClient())
                .baseUrl(SERVER_ROOT_URI)
                .addConverterFactory(GsonConverterFactory.create(gson))//对象转换
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//rxJava
                .build();
    }

    public OkHttpClient getClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);//连接 超时时间
        builder.writeTimeout(10, TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(10, TimeUnit.SECONDS);//读操作 超时时间
        builder.retryOnConnectionFailure(true);//错误重连

        builder.addInterceptor(headerInterceptor);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        return builder.build();
    }

    //项目中设置头信息
    private Interceptor headerInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            if (Config.isDebug)
                Log.d(Config.OKHTTP, String.format("Sending request %s on %s",
                        originalRequest.url(), originalRequest.body()));

            Request.Builder requestBuilder = originalRequest.newBuilder()
                    .addHeader("User-Agent", "android")
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json; charset=utf-8")
                    .method(originalRequest.method(), originalRequest.body());
            requestBuilder.addHeader(SESSIONID_KEY, SESSIONID);//添加请求头信息，服务器进行token有效性验证
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }
    };


    /**
     * 获取对应的Service
     *
     * @param service Service 的 class
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }

}
