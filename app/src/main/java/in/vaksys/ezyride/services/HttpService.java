package in.vaksys.ezyride.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import in.vaksys.ezyride.activities.HomeActivity;
import in.vaksys.ezyride.extras.Utils;
import in.vaksys.ezyride.responces.ApiInterface;
import in.vaksys.ezyride.responces.OTPResponce;
import in.vaksys.ezyride.utils.ApiClient;
import in.vaksys.ezyride.utils.AppConfig;
import in.vaksys.ezyride.utils.PreferenceHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Harsh on 07-01-2016.
 */
public class HttpService extends IntentService {

    private static String TAG = HttpService.class.getSimpleName();
    ApiInterface apiService;
    private String APIkey;
    PreferenceHelper helper;

    public HttpService() {
        super(HttpService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String otp = intent.getStringExtra("otp");
            verifyOtp(otp);
        }
    }

    /**
     * Posting the OTP to server and activating the user
     *
     * @param otp VERIFY_OTP_RESPONCE_CALL received in the SMS
     */
    private void verifyOtp(final String otp) {

        apiService = ApiClient.getClient().create(ApiInterface.class);
        helper = new PreferenceHelper(getApplicationContext(), AppConfig.PREF_USER_FILE_NAME);

        APIkey = helper.LoadStringPref(AppConfig.PREF_USER_API_KEY, "");

        Call<OTPResponce> call = apiService.VERIFY_OTP_RESPONCE_CALL(APIkey, otp);

        call.enqueue(new Callback<OTPResponce>() {
            @Override
            public void onResponse(Call<OTPResponce> call, Response<OTPResponce> response) {

                OTPResponce response1 = response.body();

                if (response.code() == 200) {
                    if (!response1.isError()) {
                        helper.initPref();
                        helper.SaveBooleanPref(AppConfig.PREF_USER_OTP_STATUS, true);
                        helper.SaveBooleanPref(AppConfig.PREF_USER_REG_STATUS, true);
                        helper.SaveStringPref(AppConfig.PREF_USER_NAME, response1.getFname());
//                        helper.SaveStringPref("Lname", response1.getLname());
                        helper.SaveStringPref(AppConfig.PREF_USER_MAIL_ID, response1.getEmail());
                        helper.SaveStringPref(AppConfig.PREF_USER_BIRTHDATE, response1.getDob());
                        helper.SaveIntPref(AppConfig.PREF_USER_GENDER_POSITION, response1.getGender());
                        helper.ApplyPref();

                        Toast.makeText(HttpService.this, response1.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(HttpService.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), response1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error,Please Contact Customer Care.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OTPResponce> call, Throwable t) {
                new Utils((Activity) getApplicationContext()).ShowError();
            }
        });
    }

}