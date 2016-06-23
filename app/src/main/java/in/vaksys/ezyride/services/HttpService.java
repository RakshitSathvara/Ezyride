package in.vaksys.ezyride.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import in.vaksys.ezyride.activities.HomeActivity;
import in.vaksys.ezyride.extras.Utils;
import in.vaksys.ezyride.responces.OTPResponce;
import in.vaksys.ezyride.utils.ApiClient;
import in.vaksys.ezyride.utils.ApiInterface;
import in.vaksys.ezyride.utils.MyApplication;
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
     * @param otp otp received in the SMS
     */
    private void verifyOtp(final String otp) {

        apiService = ApiClient.getClient().create(ApiInterface.class);
        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        APIkey = sharedPreferences.getString("APIkey", "");

        Call<OTPResponce> call = apiService.otp(APIkey, otp);

        call.enqueue(new Callback<OTPResponce>() {
            @Override
            public void onResponse(Call<OTPResponce> call, Response<OTPResponce> response) {
                SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                OTPResponce response1 = response.body();

                if (response.code() == 200) {
                    if (!response1.isError()) {
                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putBoolean("OTPstatus", true);
                        edit.putBoolean("RegStatus", true);
                        edit.putString("Fname", response1.getFname());
                        edit.putString("Lname", response1.getLname());
                        edit.putString("Email", response1.getEmail());
                        edit.putString("Bdate", response1.getDob());
                        edit.putInt("Gender", response1.getGender());
                        edit.apply();

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