package in.vaksys.ezyride.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.vaksys.ezyride.R;
import in.vaksys.ezyride.extras.ProgresDialog;
import in.vaksys.ezyride.extras.Utils;
import in.vaksys.ezyride.responces.OTPResponce;
import in.vaksys.ezyride.services.HttpService;
import in.vaksys.ezyride.utils.ApiClient;
import in.vaksys.ezyride.utils.ApiInterface;
import in.vaksys.ezyride.utils.MyApplication;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VerifyOtpActivity extends AppCompatActivity {

    private static final String TAG = "VerifyOTP";
    @Bind(R.id.et_otp)
    EditText etOtp;
    @Bind(R.id.tv_resend)
    TextView tvResend;
    @Bind(R.id.tv_instruction)
    TextView tvInstruction;
    @Bind(R.id.tv_optError)
    TextView tvOptError;
    @Bind(R.id.tv_verified)
    TextView tvVerified;
    @Bind(R.id.btn_verify)
    Button btnVerify;
    @Bind(R.id.tv_notReceive)
    TextView tvNotReceive;
    @Bind(R.id.tv_editNumber)
    TextView tvEditNumber;
    private String number;
    private String APIkey;
    private Toolbar mToolbar;
    private ProgresDialog pDialog;
    ApiInterface apiService;
    private Utils utils;
    private boolean OTPstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        number = sharedPreferences.getString("mobile", "");
        APIkey = sharedPreferences.getString("APIkey", "");
        OTPstatus = sharedPreferences.getBoolean("OTPstatus", false);
        utils = new Utils(this);
        pDialog = new ProgresDialog(this);
        pDialog.createDialog(false);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
//        mToolbar.setLogo(R.drawable.ic_action_name1);

        if (!OTPstatus) {
            if (!number.equals("")) {
                resendOTP();
            }
        }
    }

    @OnClick({R.id.tv_resend, R.id.btn_verify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_resend:
                if (!number.equals("")) {
                    resendOTP();
                }
                break;
            case R.id.btn_verify:
                if (!number.equals("")) {
                    verifyOtp();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        new Utils(this).ExitSnackBar();
    }

    private void resendOTP() {

        pDialog.DialogMessage("Sending OTP request ...");
        pDialog.showDialog();

        Call<OTPResponce> call = apiService.Resend(APIkey, number);

        call.enqueue(new Callback<OTPResponce>() {
            @Override
            public void onResponse(Call<OTPResponce> call, Response<OTPResponce> response) {
                pDialog.hideDialog();
                if (response.code() == 200) {
                    OTPResponce response1 = response.body();
                    if (!response.body().isError()) {
                        utils.showLog(TAG, response1.getMessage());
                        Toast.makeText(VerifyOtpActivity.this, response1.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        utils.showLog(TAG, response1.getMessage());
                        Toast.makeText(VerifyOtpActivity.this, response1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VerifyOtpActivity.this, "Unexpected Error,Please Contact Customer Care.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OTPResponce> call, Throwable t) {
                pDialog.hideDialog();
                utils.ShowError();
                Log.e(TAG, "onFailure: Error");
            }
        });
    }

    private void verifyOtp() {
        String otp = etOtp.getText().toString().trim();

        if (!otp.isEmpty()) {
            Intent grapprIntent = new Intent(getApplicationContext(), HttpService.class);
            grapprIntent.putExtra("mobile", number);
            grapprIntent.putExtra("otp", otp);
            startService(grapprIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
        }
    }

}
