package in.vaksys.ezyride.utils;

/**
 * Created by Harsh on 07-01-2016.
 */
public class AppConfig {
    public static final String BASE_URL = "http://vakratundasystem.in/ezyride/api/v1/";

    public static final String URL_REGISTER = "register";

    public static final String URL_VERIFY_OTP = "verify_otp";

    public static final String URL_RESEND_OTP = "resend_otp";

    // SMS provider identification
    // It should match with your SMS gateway origin
    // You can use  MSGIND, TESTER and ALERTS as sender ID
    // If you want custom sender Id, approve MSG91 to get one
    public static final String SMS_ORIGIN = "EZYRDE";

    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String OTP_DELIMITER = ":";

}
