package in.vaksys.ezyride.fragments;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.vaksys.ezyride.R;
import in.vaksys.ezyride.activities.VerifyOtpActivity;
import in.vaksys.ezyride.extras.ProgresDialog;
import in.vaksys.ezyride.extras.Utils;
import in.vaksys.ezyride.responces.ApiInterface;
import in.vaksys.ezyride.responces.RegisterResponse;
import in.vaksys.ezyride.utils.ApiClient;
import in.vaksys.ezyride.utils.AppConfig;
import in.vaksys.ezyride.utils.PreferenceHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Harsh on 21-06-2016.
 */
public class RegisterDialog extends DialogFragment {
    @Bind(R.id.UserFname)
    EditText UserFname;
    /*@Bind(R.id.UserLname)
    EditText UserLname;*/
    @Bind(R.id.UseremailReg)
    EditText UseremailReg;
    @Bind(R.id.UsermobileReg)
    EditText UsermobileReg;
    @Bind(R.id.et_birthDateReg)
    EditText etBirthDateReg;
    @Bind(R.id.genderSpinnerReg)
    Spinner genderSpinnerReg;
    @Bind(R.id.SaveBtnReg)
    Button SaveBtnReg;
    private String Emailid, mFName, mLName, birthdate;
    private String newNumber;
    private int genderPosi;

    private DatePickerDialog fromDatePickerDialog;
    private String SelectedDate;

    private SimpleDateFormat dateFormatter;
    SimpleDateFormat sdf;
    Calendar c;
    private static final String TAG = "RegisterDialog";
    ArrayList<String> Genderstrings = new ArrayList<>();
    ProgresDialog dialog;
    Utils utils;
    PreferenceHelper helper;

    public RegisterDialog() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.register_dialog_fragment, container, false);
        ButterKnife.bind(this, rootView);

        dialog = new ProgresDialog(getActivity());
        utils = new Utils(getActivity());
        helper = new PreferenceHelper(getActivity(), AppConfig.PREF_USER_FILE_NAME);

        getDialog().setCancelable(true);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        setSpinners();
        setDateTimeField();
        sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        c = Calendar.getInstance();
        etBirthDateReg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    SelectfromDate();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /*@OnTouch(R.id.et_birthDateReg)
    public boolean onTouch() {
        SelectfromDate();
        return true;
    }*/

    @OnClick(R.id.SaveBtnReg)
    public void onClick() {
        validateData();
    }

    private void validateData() {
        utils.hideKeyboard();
        if (!validateFirstName()) {
            return;
        }
        if (!validateBirthDate()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }
        if (!validateNumber()) {
            return;
        }
        getdata();
    }

    private void setSpinners() {
        Genderstrings.add("Select One");
        Genderstrings.add("Male");
        Genderstrings.add("Female");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinnerlayout, Genderstrings);
        genderSpinnerReg.setAdapter(adapter);
        genderSpinnerReg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderPosi = position;
                TextView gendspi = (TextView) view;
//                GenderSpinnItem = gendspi.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "You have selected Nothing ..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SelectfromDate() {
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        fromDatePickerDialog.show();
    }

    private void getdata() {
        mFName = UserFname.getText().toString();
        Emailid = UseremailReg.getText().toString();
        newNumber = UsermobileReg.getText().toString();
        birthdate = etBirthDateReg.getText().toString();

        sendData(mFName, Emailid, newNumber, birthdate, genderPosi);
    }

    private void sendData(String mFName, String emailid, final String newNumber, String birthdate, int genderPosi) {

        dialog.createDialog(false);
        dialog.DialogMessage("Registering User ...");
        dialog.showDialog();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<RegisterResponse> call = apiService.REGISTER_RESPONSE_CALL(mFName, newNumber, birthdate, String.valueOf(genderPosi), emailid);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                dialog.hideDialog();
                RegisterResponse response1 = response.body();
//                Log.e(TAG, "onResponse: " + response.code());
                if (response.code() == 200) {
                    if (!response1.isError()) {
                        helper.initPref();
                        if (response1.isStatus()) {                 // status = true ( complete user )
                            helper.SaveStringPref(AppConfig.PREF_USER_MOBILE, newNumber);
                            helper.SaveStringPref(AppConfig.PREF_USER_API_KEY, response1.getApiKey());
                            helper.SaveBooleanPref(AppConfig.PREF_USER_OTP_STATUS, true);
                            utils.showLog(TAG, response1.getMessage());
                            Toast.makeText(getActivity(), response1.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {                                    // status = false ( user registered but VERIFY_OTP_RESPONCE_CALL verification is left
                            helper.SaveStringPref(AppConfig.PREF_USER_MOBILE, newNumber);
                            helper.SaveStringPref(AppConfig.PREF_USER_API_KEY, response1.getApiKey());
                            helper.SaveBooleanPref(AppConfig.PREF_USER_OTP_STATUS, false);
                            utils.showLog(TAG, response1.getApiKey() + "  " + response1.getMessage());
                            Toast.makeText(getActivity(), response1.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        helper.ApplyPref();
                        startActivity(new Intent(getActivity(), VerifyOtpActivity.class));
                        getDialog().dismiss();
                        getActivity().finish();
                    } else {
                        utils.showLog(TAG, response1.getMessage());
                        Toast.makeText(getActivity(), response1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Unexpected Error,Please Contact Customer Care.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                dialog.hideDialog();
                utils.ShowError();
                Log.e(TAG, "onFailure: Error");
            }
        });
    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SelectedDate = dateFormatter.format(newDate.getTime());
                etBirthDateReg.setText(SelectedDate);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private boolean validateFirstName() {
        if (UserFname.getText().toString().trim().isEmpty()) {
            UserFname.setError(getString(R.string.err_msg_first_name));
            requestFocus(UserFname);
            return false;
        } else {
            return true;
        }
    }


    private boolean validateBirthDate() {
        if (etBirthDateReg.getText().toString().trim().isEmpty()) {
            etBirthDateReg.setError(getString(R.string.err_msg_birth_date));
            requestFocus(etBirthDateReg);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateEmail() {
        String email = UseremailReg.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            UseremailReg.setError(getString(R.string.err_msg_email));
            requestFocus(UseremailReg);
            return false;
        } else {
            return true;
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateNumber() {
        if (UsermobileReg.getText().toString().trim().isEmpty()) {
            UsermobileReg.setError(getString(R.string.err_msg_number));
            requestFocus(UsermobileReg);
            return false;
        }
        if (UsermobileReg.length() != 10) {
            UsermobileReg.setError(getString(R.string.err_msg_valid_number));
            requestFocus(UsermobileReg);
            return false;
        } else {
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
