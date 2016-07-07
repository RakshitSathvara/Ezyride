package in.vaksys.ezyride.responces;

import in.vaksys.ezyride.utils.AppConfig;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Harsh on 21-06-2016.
 */
public interface ApiInterface {

    /*
        @Multipart
        @POST("register")
        Call<RegisterResponse> REGISTER_RESPONSE_CALL(@Query("first_name") String Fname, @Query("last_name") String Lname,
                                           @Query("contact") String Contact, @Query("dob") String DateOB,
                                           @Query("gender") int Gender, @Query("corp_email") String CorporateEmail,
                                           @Query("email") String emailID, @Query("filename") String FileName, @Nullable @Part MultipartBody.Part file);
    */

    @FormUrlEncoded
    @POST(AppConfig.URL_REGISTER)
    Call<RegisterResponse> REGISTER_RESPONSE_CALL(@Field("first_name") String Fname, @Field("contact") String Contact, @Field("dob") String DateOB,
                                                  @Field("gender") String Gender, @Field("email") String emailID);

    @FormUrlEncoded
    @POST(AppConfig.URL_RESEND_OTP)
    Call<OTPResponce> OTP_RESPONCE_CALL(@Header("Authorization") String header, @Field("phone") String mobile);

    @FormUrlEncoded
    @POST(AppConfig.URL_VERIFY_OTP)
    Call<OTPResponce> VERIFY_OTP_RESPONCE_CALL(@Header("Authorization") String header, @Field("VERIFY_OTP_RESPONCE_CALL") String otp);

    @FormUrlEncoded
    @POST(AppConfig.URL_OFFER_RIDE)
    Call<OfferRideResponse> OFFER_RIDE_RESPONSE_CALL(@Header("Authorization") String header, @Field("car_id") int car_id
            , @Field("from_lat") double from_lat, @Field("to_lat") double to_lat, @Field("from_long") double from_long
            , @Field("to_long") double to_long, @Field("from_main_address") String from_main_address
            , @Field("from_sub_address") String from_sub_address, @Field("to_main_address") String to_main_address
            , @Field("to_sub_address") String to_sub_address, @Field("ride_date") String ride_date
            , @Field("ride_time") String ride_time, @Field("price_per_seat") float price_per_seat
            , @Field("seat_availability") int seat_availability, @Field("only_ladies") int only_ladies);

    @GET(AppConfig.URL_GET_CARS)
    Call<CarResponse> GET_CAR_RESPONSE_CALL(@Header("Authorization") String header);

    @Multipart
    @POST(AppConfig.URL_UPLOAD_IMAGE)
    Call<ImageUploadResponse> IMAGE_UPLOAD_RESPONSE_CALL(@Header("Authorization") String auuth, @Part MultipartBody.Part aPart);


    @FormUrlEncoded
    @POST(AppConfig.URL_GET_CARS)
    Call<CarRegisterResponse> CAR_REGISTER_RESPONSE_CALL(@Header("Authorization") String auuth, @Field("car_no") String car_no
            , @Field("car_model") String car_model, @Field("car_layout") int car_layout, @Field("ac_availability") int ac_availability
            , @Field("music_system") int music_system, @Field("air_bag") int air_bag, @Field("seat_belt") int seat_belt
            , @Field("car_url") String car_url);

    @GET(AppConfig.URL_GET_MY_CARS)
    Call<MyCarResponse> MY_CAR_RESPONSE_CALL(@Header("Authorization") String auuth);

    @FormUrlEncoded
    @PUT(AppConfig.URL_GET_CARS + "/{id}")
    Call<CarRegisterResponse> CAR_EDIT_RESPONSE_CALL(@Header("Authorization") String auuth, @Path("id") int CarId, @Field("car_no") String car_no
            , @Field("car_model") String car_model, @Field("car_layout") int car_layout, @Field("ac_availability") int ac_availability
            , @Field("music_system") int music_system, @Field("air_bag") int air_bag, @Field("seat_belt") int seat_belt
            , @Field("car_url") String car_url);

    @DELETE(AppConfig.URL_GET_CARS + "/{id}")
    Call<CarRegisterResponse> CAR_DELETE_RESPONSE_CALL(@Header("Authorization") String auuth, @Path("id") int CarId);

    @FormUrlEncoded
    @PUT(AppConfig.URL_UPDATE_USER)
    Call<CarRegisterResponse> PROFILE_UPDATE_RESPONSE_CALL(@Header("Authorization") String auuth, @Field("first_name") String first_name
            , @Field("contact") String contact, @Field("dob") String Dob, @Field("profile_pic") String profile_pic
            , @Field("gender") int gender, @Field("fb_stat") boolean fb_stat, @Field("corp_email_verify") boolean corp_email_verify
            , @Field("pan_image") String pan_image, @Field("pan_verify") boolean pan_verify);

    @FormUrlEncoded
    @POST(AppConfig.URL_SEARCH_RIDE)
    Call<SearchRideResponse> SEARCH_RIDE_RESPONSE_CALL(@Header("Authorization") String auuth, @Field("to_lat") String to_lat
            , @Field("to_long") String to_long, @Field("from_lat") String from_lat
            , @Field("from_long") String from_long, @Field("date") String date);
}
