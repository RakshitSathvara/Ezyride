package in.vaksys.ezyride.utils;

/**
 * Created by Harsh on 07-01-2016.
 */
public class AppConfig {
    public static final String BASE_URL = "http://vakratundasystem.in/ezyride/api/v1/";

    public static final String URL_REGISTER = "register";

    public static final String URL_VERIFY_OTP = "verify_otp";

    public static final String URL_RESEND_OTP = "resend_otp";

    public static final String URL_OFFER_RIDE = "rides";

    public static final String URL_GET_CARS = "car";

    public static final String URL_UPDATE_USER = "user";

    public static final String URL_UPLOAD_IMAGE = "upload_car";

    public static final String URL_GET_MY_CARS = "car_details";

    public static final String URL_SEARCH_RIDE = "search_ride";

    // SMS provider identification
    // It should match with your SMS gateway origin
    // You can use  MSGIND, TESTER and ALERTS as sender ID
    // If you want custom sender Id, approve MSG91 to get one
    public static final String SMS_ORIGIN = "EZYRDE";

    // special character to prefix the VERIFY_OTP_RESPONCE_CALL. Make sure this character appears only once in the sms
    public static final String OTP_DELIMITER = ":";


    public static final String PREF_CAR_FILE_NAME = "car_details";

    public static final String PREF_CAR_ID = "car_id";
    public static final String PREF_CAR_IMG_URL = "car_img_url";
    public static final String PREF_CAR_MODEL = "car_model";
    public static final String PREF_CAR_NUMBER = "car_number";
    public static final String PREF_CAR_LAYOUT = "car_layout";
    public static final String PREF_CAR_AC = "car_ac";
    public static final String PREF_CAR_MUSIC = "car_music";
    public static final String PREF_CAR_SEAT_BELT = "car_belt";
    public static final String PREF_CAR_SEAT_AIR_BAG = "car_air_bag";
    public static final String PREF_CAR_IMG_CHANGE = "car_image_change";
    public static final String PREF_CAR_EDIT_STATUS = "car_edit";


    public static final String PREF_USER_FILE_NAME = "user_details";

    public static final String PREF_USER_MAIL_ID = "Emaiid";
    public static final String PREF_USER_NAME = "Name";
    public static final String PREF_USER_BIRTHDATE = "Birthdate";
    public static final String PREF_USER_MOBILE = "Mobile";
    public static final String PREF_USER_FB_STATUS = "facebookstatus";
    public static final String PREF_USER_CORP_MAIL_STATUS = "pancardstatus";
    public static final String PREF_USER_PAN_CARD_STATUS = "corporatemailstatus";
    public static final String PREF_USER_GENDER_POSITION = "genderPosition";
    public static final String PREF_USER_CAR_DETAIL_POSITION = "carDetailPosition";
    public static final String PREF_USER_PROFILR_PIC = "Profilepic";
    public static final String PREF_USER_OTP_STATUS = "OTPstatus";
    public static final String PREF_USER_API_KEY = "APIkey";
    public static final String PREF_USER_IMG_CHANGE = "user_image_change";
    public static final String PREF_USER_REG_STATUS = "user_reg_status";
    public static final String PREF_USER_LOGIN_STATUS = "loggin_status";
    public static final String PREF_USER_ENTRY_TIME = "entryTime";
    public static final String PREF_USER_PROF_IMG = "profile_image";
    public static final String PREF_USER_PROF_IMG_URL = "profile_image_url";
    public static final String PREF_USER_PAN_IMG = "pan_image";


    public static final String PREF_SEARCH_FILE_NAME = "search_details";

    public static final String PREF_SEARCH_TO_LAT = "to_lat";
    public static final String PREF_SEARCH_TO_LONG = "to_long";
    public static final String PREF_SEARCH_FROM_LONG = "from_long";
    public static final String PREF_SEARCH_FROM_LAT = "from_lat";

}
