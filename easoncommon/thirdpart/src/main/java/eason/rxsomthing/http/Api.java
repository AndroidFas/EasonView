package eason.rxsomthing.http;


import com.google.gson.JsonObject;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 所有请求
 */
public interface Api {
    //    ==========文件上传=======
    @Multipart
    @POST("{url}")
    Observable<JsonObject> upLoadFile(@Path(value = "url", encoded = true) String url, @Query("pjobId") String pjobId, @Part MultipartBody.Part file);

    //登录接口
    @POST("{url}")
    Observable<retrofit2.Response<JsonObject>> postBody(@Path(value = "url", encoded = true) String url, @Body RequestBody param);
    @POST("{url}")
    Observable<retrofit2.Response<JsonObject>> postBodyNoBody2(@Path(value = "url", encoded = true) String url);

    @POST("{url}")
    Observable<JsonObject> postBody2(@Path(value = "url", encoded = true) String url, @Body RequestBody param);

    @POST("{url}")
    Observable<JsonObject> postBodyNoBody(@Path(value = "url", encoded = true) String url);

}
