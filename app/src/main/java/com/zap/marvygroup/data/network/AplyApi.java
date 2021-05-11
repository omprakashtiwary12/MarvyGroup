package com.zap.marvygroup.data.network;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AplyApi {
    @Multipart
    @POST("apply/add")
    Call<ResponseBody> uploadPdf(
            @Part("name") String name,
            @Part("mobile") String mobile,
            @Part("email") String email,
            @Part("subject") String subject,
            @Part("message") String message,
            @Part MultipartBody.Part resume
    );

}
