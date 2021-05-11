package com.zap.marvygroup.data.network

import com.google.android.gms.common.server.converter.StringToIntConverter
import com.zap.marvygroup.data.network.responses.*
import com.zap.marvygroup.ui.documents_type.DocListResponse
import com.zap.marvygroup.ui.documents_type.DocTypeResponse
import com.zap.marvygroup.ui.uploaded_documents.DocResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface MyApi {

    @FormUrlEncoded
    @POST("login")
    public suspend fun userLogin(
        @Field("username") email: String,
        @Field("password") password: String
    ): Response<AuthResponse>

    @FormUrlEncoded
    @POST("forgot_password")
    public suspend fun resetPasswod(
        @Field("employee_code") userName: String
    ): Response<AuthResponse>


    @FormUrlEncoded
    @POST("contact/add")
    public suspend fun addContact(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("subject") subject: String,
        @Field("message") message: String
    ): Response<ContactUserResponse>

    @FormUrlEncoded
    @POST("auth")
    public suspend fun getUserDetails(
        @Field("employee_code") employeeCode: String,
        @Field("access_token") accessToken: String
    ): Response<ProfileResponse>

    @FormUrlEncoded
    @POST("salary")
    public suspend fun getSalary(
        @Field("employee_code") employeeCode: String,
        @Field("access_token") accessToken: String
    ): Response<SalaryResponse>

    @Multipart
    @POST("apply/add")
    public suspend fun applyForm(
        @Part("name") name: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("email") email: RequestBody,
        @Part("subject") subject: RequestBody,
        @Part("message") message: RequestBody,
        @Part resume: MultipartBody.Part

    ): Response<ApplyFormResponse>

    @Multipart
    @POST("document/add")
    public suspend fun attachDocFile(
        @Part("employee_code") employee_code: RequestBody,
        @Part("access_token") access_token: RequestBody,
        @Part("type") fileType: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<DocumentResponse>

    @Multipart
    @POST("update_photo")
    public suspend fun changeProfileImage(
        @Part("employee_code") employee_code: RequestBody,
        @Part("access_token") access_token: RequestBody,
        @Part file: MultipartBody.Part
    ):Response<ChangeImageResponse>

    @FormUrlEncoded
    @POST("attendance/add")
    public suspend fun addAttendance(
       @Field("employee_code") employeeCode: String,
       @Field("access_token") accessToken: String,
       @Field("latitude")latitude:Double,
       @Field("longitude")longitude:Double,
       @Field("is_entry")is_entry:Boolean
   ):Response<AttendanceResponse>

    @FormUrlEncoded
    @POST("showlist_img.php")
    public suspend fun addUploadDoc(
        @Field("employee_code") employeeCode: String
    ):Response<DocResponse>

    @FormUrlEncoded
    @POST("showlist_pdf.php")
    public suspend fun showPdfDoc(
        @Field("employee_code") employeeCode: String
    ):Response<DocResponse>


    @FormUrlEncoded
    @POST("get_documentlist.php")
    public suspend fun showDocList(
        @Field("employee_code") employeeCode: String
    ):Response<DocListResponse>

    @GET("get_documenttype.php")
    public suspend fun getDocType():Response<DocTypeResponse>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ): MyApi {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(networkConnectionInterceptor)
                .build()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://online.marvygroup.com/marvy_payroll/apiv2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }

}