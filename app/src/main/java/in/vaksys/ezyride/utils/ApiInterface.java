package in.vaksys.ezyride.utils;

import in.vaksys.ezyride.responces.OTPResponce;
import in.vaksys.ezyride.responces.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Harsh on 21-06-2016.
 */
public interface ApiInterface {

    /*
        @Multipart
        @POST("register")
        Call<RegisterResponse> getRegister(@Query("first_name") String Fname, @Query("last_name") String Lname,
                                           @Query("contact") String Contact, @Query("dob") String DateOB,
                                           @Query("gender") int Gender, @Query("corp_email") String CorporateEmail,
                                           @Query("email") String emailID, @Query("filename") String FileName, @Nullable @Part MultipartBody.Part file);
    */

    @FormUrlEncoded
    @POST(AppConfig.URL_REGISTER)
    Call<RegisterResponse> getRegister(@Field("first_name") String Fname, @Field("last_name") String Lname,
                                       @Field("contact") String Contact, @Field("dob") String DateOB,
                                       @Field("gender") String Gender,
                                       @Field("email") String emailID);

    @FormUrlEncoded
    @POST(AppConfig.URL_RESEND_OTP)
    Call<OTPResponce> Resend(@Header("Authorization") String header, @Field("phone") String mobile);

    @FormUrlEncoded
    @POST(AppConfig.URL_VERIFY_OTP)
    Call<OTPResponce> otp(@Header("Authorization") String header, @Field("otp") String otp);


}
