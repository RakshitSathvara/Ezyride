package in.vaksys.ezyride.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.facebook.stetho.Stetho;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.vaksys.ezyride.R;
import in.vaksys.ezyride.extras.ProgresDialog;
import in.vaksys.ezyride.extras.Utils;
import in.vaksys.ezyride.responces.ApiInterface;
import in.vaksys.ezyride.responces.CarRegisterResponse;
import in.vaksys.ezyride.responces.ImageUploadResponse;
import in.vaksys.ezyride.utils.ApiClient;
import in.vaksys.ezyride.utils.AppConfig;
import in.vaksys.ezyride.utils.PreferenceHelper;
import in.vaksys.ezyride.utils.ProgressRequestBody;
import okhttp3.MultipartBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.EasyImageConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarDetailsActivity extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks {

    private static final String TAG = "CarDetailsActivity";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.input_car_no)
    EditText inputCarNo;
    @Bind(R.id.input_layout_car_no)
    TextInputLayout inputLayoutCarNo;
    @Bind(R.id.input_car_model)
    EditText inputCarModel;
    @Bind(R.id.input_layout_car_model)
    TextInputLayout inputLayoutCarModel;
    @Bind(R.id.spinner_car_layout)
    Spinner spinnerCarLayout;
    @Bind(R.id.btn_offer_now)
    Button btnOfferNow;
    @Bind(R.id.car_image)
    ImageView CarImage;
    ArrayList<String> carLayoutStrings = new ArrayList<>();
    @Bind(R.id.seat_belt_toggle)
    ToggleButton SeatBeltToggle;
    @Bind(R.id.music_toggle)
    ToggleButton musicToggle;
    @Bind(R.id.air_con_toggle)
    ToggleButton ACToggle;
    @Bind(R.id.air_bag_toggle)
    ToggleButton AirBagToggle;

    ArcProgress arcProgress;
    Dialog confirm;

    private String CarLayoutSpinnItem;

    private static final String IMAGE_DIRECTORY_NAME = "EzyRidePhotos";
    private static final int REQUEST_CAMERA = 0;

    Utils utils;

    ApiInterface apiService;
    private ProgresDialog pDialog;
    private File files;
    Call<ImageUploadResponse> responseCall;
    Call<CarRegisterResponse> carRegisterResponseCall;
    private int SeatBeltStatus = 0;
    private int musicStatus = 0;
    private int ACStatus = 0;
    private int AirBagStatus = 0;
    private String carNumber;
    private String carModel;
    int CarLayoutPos;
    private String BLANK = "";
    private boolean EditStatus;
    private String CarImageUrl;
    private int CarId;
    PreferenceHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Initialize();
        setSpinner();
        SetToggels();
        ImageUploadDialog();
        loadEditData();
    }

    private void loadEditData() {
        EditStatus = helper.LoadBooleanPref(AppConfig.PREF_CAR_EDIT_STATUS, false);
        if (EditStatus) {
            carModel = helper.LoadStringPref(AppConfig.PREF_CAR_MODEL, BLANK);
            carNumber = helper.LoadStringPref(AppConfig.PREF_CAR_NUMBER, BLANK);
            CarLayoutPos = helper.LoadIntPref(AppConfig.PREF_CAR_LAYOUT, 0);
            CarImageUrl = helper.LoadStringPref(AppConfig.PREF_CAR_IMG_URL, BLANK);
            CarId = helper.LoadIntPref(AppConfig.PREF_CAR_ID, 0);
            ACStatus = helper.LoadIntPref(AppConfig.PREF_CAR_AC, 0);
            SeatBeltStatus = helper.LoadIntPref(AppConfig.PREF_CAR_SEAT_BELT, 0);
            AirBagStatus = helper.LoadIntPref(AppConfig.PREF_CAR_SEAT_AIR_BAG, 0);
            musicStatus = helper.LoadIntPref(AppConfig.PREF_CAR_MUSIC, 0);
            Glide.with(this)
                    .load(CarImageUrl)
                    .priority(Priority.HIGH)
                    .crossFade()
                    .into(CarImage);
            helper.initPref();
            helper.SaveBooleanPref(AppConfig.PREF_CAR_IMG_CHANGE, false);
            helper.ApplyPref();
            inputCarModel.setText(carModel);
            inputCarNo.setText(carNumber);
            // TODO: 02-07-2016 here i have to manage carlayout by sir given charts
//            spinnerCarLayout.setSelection(CarLayoutPos);
            if (ACStatus == 1) {
                ACToggle.setChecked(true);
            }
            if (musicStatus == 1) {
                musicToggle.setChecked(true);
            }
            if (AirBagStatus == 1) {
                AirBagToggle.setChecked(true);
            }
            if (SeatBeltStatus == 1) {
                SeatBeltToggle.setChecked(true);
            }
        }
    }

    private void Initialize() {
        Stetho.initializeWithDefaults(this);
        utils = new Utils(this);
        apiService = ApiClient
                .getClient()
                .create(ApiInterface.class);
        pDialog = new ProgresDialog(this);
        pDialog.createDialog(false);
        files = new File("");
        utils.showLog(TAG, files.toString());
        EasyImage.configuration(this)
                .setImagesFolderName("Choose Car Image")
                .setImagesFolderName(String.valueOf(new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        IMAGE_DIRECTORY_NAME)));
        helper = new PreferenceHelper(this, AppConfig.PREF_CAR_FILE_NAME);
        helper.initPref();
        helper.SaveBooleanPref(AppConfig.PREF_CAR_IMG_CHANGE, false);
        helper.ApplyPref();
    }

    private void SetToggels() {
        SeatBeltToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    SeatBeltStatus = 1;
                    Toast.makeText(CarDetailsActivity.this, "Tea Checked. ", Toast.LENGTH_SHORT).show();
                } else {
                    SeatBeltStatus = 0;
                    Toast.makeText(CarDetailsActivity.this, "Tea UnChecked ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        musicToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    musicStatus = 1;
                    Toast.makeText(CarDetailsActivity.this, "Music Checked. ", Toast.LENGTH_SHORT).show();
                } else {
                    musicStatus = 0;
                    Toast.makeText(CarDetailsActivity.this, "Music UnChecked ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ACToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ACStatus = 1;
                    Toast.makeText(CarDetailsActivity.this, "Sound Checked. ", Toast.LENGTH_SHORT).show();
                } else {
                    ACStatus = 0;
                    Toast.makeText(CarDetailsActivity.this, "Sound UnChecked ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        AirBagToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    AirBagStatus = 1;
                    Toast.makeText(CarDetailsActivity.this, "Smoking Checked. ", Toast.LENGTH_SHORT).show();
                } else {
                    AirBagStatus = 0;
                    Toast.makeText(CarDetailsActivity.this, "Smoking UnChecked ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setSpinner() {
        carLayoutStrings.add("Select any one");
        carLayoutStrings.add("2 Front + 3 Back");
        carLayoutStrings.add("2 Front + 3 Back");
        carLayoutStrings.add("2 Front + 3 Back");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(CarDetailsActivity.this, R.layout.spinnerlayout, carLayoutStrings);
        spinnerCarLayout.setAdapter(adapter);
        spinnerCarLayout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView carspinner = (TextView) view;
                CarLayoutSpinnItem = carspinner.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(CarDetailsActivity.this, "You have selected Nothing ..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateSpinnerItems() {
        CarLayoutPos = spinnerCarLayout.getSelectedItemPosition();
        View selectedView = spinnerCarLayout.getSelectedView();
        TextView selectedTextView = (TextView) selectedView;
        if (CarLayoutPos != 0 && selectedView != null) {
            CarLayoutSpinnItem = spinnerCarLayout.getSelectedItem().toString();
            selectedTextView.setError(null);
            return true;
        } else {
            String errorString = selectedTextView.getResources().getString(R.string.mErrorStringResource);
            selectedTextView.setError(errorString);
            Toast.makeText(CarDetailsActivity.this,
                    "Please Select the Car Layout !!", Toast.LENGTH_LONG)
                    .show();
            return false;
        }

    }

    private boolean validateCarNumber() {
        if (inputCarNo.getText().toString().trim().isEmpty()) {
            inputLayoutCarNo.setError(getString(R.string.err_msg_car_number));
            requestFocus(inputCarNo);
            return false;
        } else {
            inputLayoutCarNo.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateCarModel() {
        if (inputCarModel.getText().toString().trim().isEmpty()) {
            inputLayoutCarModel.setError(getString(R.string.err_msg_car_number));
            requestFocus(inputCarModel);
            return false;
        } else {
            inputLayoutCarModel.setErrorEnabled(false);
        }
        return true;
    }

    public void openBottomSheet() {

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        TextView take_picture = (TextView) view.findViewById(R.id.take_picture);
        TextView Open_gallary = (TextView) view.findViewById(R.id.open_gallary);

        final Dialog mBottomSheetDialog = new Dialog(CarDetailsActivity.this, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();

        take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(CarDetailsActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestCameraPermission();
                } else {
                    Log.i(TAG,
                            "CAMERA permission has already been granted. Displaying camera preview.");
                    EasyImage.openCamera(CarDetailsActivity.this, EasyImageConfig.REQ_TAKE_PICTURE);
                }
//                captureImage();
                mBottomSheetDialog.dismiss();
            }
        });

        Open_gallary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                showFileChooser();
                EasyImage.openGallery(CarDetailsActivity.this, EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY);
                mBottomSheetDialog.dismiss();
            }
        });

    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {

                //Handle the image
                files = imageFile;
                Glide.with(CarDetailsActivity.this)
                        .load(files)
                        .crossFade()
                        .centerCrop()
                        .into(CarImage);
                helper.initPref();
                helper.SaveBooleanPref(AppConfig.PREF_CAR_IMG_CHANGE, true);
                helper.ApplyPref();
                long length = files.length();
                length = length / 1024;
                Toast.makeText(CarDetailsActivity.this, length + " kb " + Long.parseLong(String.valueOf(length / 1024)) + " MB", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @OnClick({R.id.btn_offer_now, R.id.edit_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_offer_now:
                RegisterCar();
                break;
            case R.id.edit_image:
                openBottomSheet();
                break;
        }
    }

    private void RegisterCar() {
        if (!validateCarNumber()) {
            return;
        }
        if (!validateCarModel()) {
            return;
        }
        if (!validateSpinnerItems()) {
            return;
        }
        GetData();
    }

    private void GetData() {
        carNumber = inputCarNo.getText().toString();
        carModel = inputCarModel.getText().toString();
        if (helper.LoadBooleanPref(AppConfig.PREF_CAR_IMG_CHANGE, false)) {
            UploadImage(carNumber, carModel, CarLayoutPos, SeatBeltStatus, musicStatus, ACStatus, AirBagStatus);
        } else {
            if (helper.LoadBooleanPref(AppConfig.PREF_CAR_EDIT_STATUS, false)) {
                SendingUpdateCarParameters(CarId, carNumber, carModel, CarLayoutPos, SeatBeltStatus, musicStatus, ACStatus, AirBagStatus, CarImageUrl);
            } else {
                Toast.makeText(CarDetailsActivity.this, "You Must Have to Select Car Image...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SendingUpdateCarParameters(int carId, String carNumber, String carModel, int carLayoutPos
            , int seatBeltStatus, int musicStatus, int acStatus, int airBagStatus, String carImageUrl) {
        pDialog.DialogMessage("Updating Car ...");
        pDialog.showDialog();

        carRegisterResponseCall = apiService.CAR_EDIT_RESPONSE_CALL("b96c450cb827366525f4df7007a121d2", carId
                , carNumber, carModel, carLayoutPos, seatBeltStatus, musicStatus, acStatus, airBagStatus, carImageUrl);

        carRegisterResponseCall.enqueue(new Callback<CarRegisterResponse>() {
            @Override
            public void onResponse(Call<CarRegisterResponse> call, Response<CarRegisterResponse> response) {
                pDialog.hideDialog();
                utils.showLog(TAG, String.valueOf(response.code()));
                if (response.code() == 200) {
                    CarRegisterResponse response1 = response.body();
                    utils.showLog(TAG, String.valueOf(response1.isError()));
                    if (!response.body().isError()) {

                        utils.showLog(TAG, response1.getMessage());
                        Toast.makeText(CarDetailsActivity.this, "Car SuccessFully Updated ...", Toast.LENGTH_SHORT).show();
                        helper.initPref();
                        helper.SaveBooleanPref(AppConfig.PREF_CAR_IMG_CHANGE, false);
                        helper.SaveBooleanPref(AppConfig.PREF_CAR_EDIT_STATUS, false);
                        helper.ApplyPref();
                    } else {
                        utils.showLog(TAG, response1.getMessage());
                        Toast.makeText(CarDetailsActivity.this, response1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CarDetailsActivity.this, "Unexpected Error,Please Contact Customer Care.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CarRegisterResponse> call, Throwable t) {
                pDialog.hideDialog();
                utils.ShowError();
                Log.e(TAG, "onFailure: Error");
            }
        });
    }

    private void UploadImage(final String carNumber, final String carModel, final int carLayoutPos, final int teaStatus, final int musicStatus
            , final int soundStatus, final int smokeStatus) {
        utils.showLog(TAG, files.toString());
        confirm.show();

        ProgressRequestBody requestBody = new ProgressRequestBody(files, this);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("car_image", String.valueOf((System.currentTimeMillis() / 1000)) + ".jpg", requestBody);

        responseCall = apiService.IMAGE_UPLOAD_RESPONSE_CALL("b96c450cb827366525f4df7007a121d2"
                , body);

        responseCall.enqueue(new Callback<ImageUploadResponse>() {
            @Override
            public void onResponse(Call<ImageUploadResponse> call, Response<ImageUploadResponse> response) {
                confirm.dismiss();
//                pDialog.hideDialog();
                utils.showLog(TAG, String.valueOf(response.code()));
                if (response.code() == 200) {
                    ImageUploadResponse response1 = response.body();
                    utils.showLog(TAG, String.valueOf(response.body().isError()));
                    if (!response.body().isError()) {
                        CarImageUrl = response.body().getUrl();
                        utils.showLog(TAG, CarImageUrl);
                        Toast.makeText(CarDetailsActivity.this, "Image Uploaded...", Toast.LENGTH_SHORT).show();
                        if (!CarImageUrl.isEmpty())
                            if (helper.LoadBooleanPref(AppConfig.PREF_CAR_EDIT_STATUS, false)) {
                                SendingUpdateCarParameters(CarId, carNumber, carModel, CarLayoutPos, SeatBeltStatus, musicStatus, ACStatus, AirBagStatus, CarImageUrl);
                            } else {
                                SendingRegisterCarParameters(carNumber, carModel, carLayoutPos, teaStatus, musicStatus, soundStatus, smokeStatus, CarImageUrl);
                            }
                        else
                            Toast.makeText(CarDetailsActivity.this, "There are some error. Please Upload Image Again.", Toast.LENGTH_SHORT).show();
                    } else {
                        utils.showLog(TAG, response1.getMessage());
                        Toast.makeText(CarDetailsActivity.this, response1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CarDetailsActivity.this, "Unexpected Error,Please Contact Customer Care.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                confirm.dismiss();
                if (call.isCanceled()) {
                    Toast.makeText(CarDetailsActivity.this, "Image Upload Canceled.", Toast.LENGTH_SHORT).show();
                } else {
                    utils.ShowError();
                }
                Log.e(TAG, "onFailure: Error");

            }
        });

    }

    private void SendingRegisterCarParameters(String carNumber, String carModel, int carLayoutPos, int teaStatus,
                                              int musicStatus, int soundStatus, int smokeStatus, String imageUrl) {
        pDialog.DialogMessage("Registering Car ...");
        pDialog.showDialog();

        carRegisterResponseCall = apiService.CAR_REGISTER_RESPONSE_CALL("b96c450cb827366525f4df7007a121d2"
                , carNumber, carModel, carLayoutPos, teaStatus, musicStatus, soundStatus, smokeStatus, imageUrl);

        carRegisterResponseCall.enqueue(new Callback<CarRegisterResponse>() {
            @Override
            public void onResponse(Call<CarRegisterResponse> call, Response<CarRegisterResponse> response) {
                pDialog.hideDialog();
                utils.showLog(TAG, String.valueOf(response.code()));
                if (response.code() == 200) {
                    CarRegisterResponse response1 = response.body();
                    utils.showLog(TAG, String.valueOf(response1.isError()));
                    if (!response.body().isError()) {

                        utils.showLog(TAG, response1.getMessage());
                        Toast.makeText(CarDetailsActivity.this, "Car SuccessFully Registered ...", Toast.LENGTH_SHORT).show();
                        helper.initPref();
                        helper.SaveBooleanPref(AppConfig.PREF_CAR_IMG_CHANGE, false);
                        helper.SaveBooleanPref(AppConfig.PREF_CAR_EDIT_STATUS, false);
                        helper.ApplyPref();
                    } else {
                        utils.showLog(TAG, response1.getMessage());
                        Toast.makeText(CarDetailsActivity.this, response1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CarDetailsActivity.this, "Unexpected Error,Please Contact Customer Care.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CarRegisterResponse> call, Throwable t) {
                pDialog.hideDialog();
                utils.ShowError();
                Log.e(TAG, "onFailure: Error");
            }
        });
    }

    private void ImageUploadDialog() {
        confirm = new Dialog(this);
        confirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirm.setContentView(R.layout.confirm_dialog);
        confirm.setCancelable(false);

        arcProgress = (ArcProgress) confirm.findViewById(R.id.arc_progress);
        arcProgress.setMax(100);

        Button cancelBtn = (Button) confirm.findViewById(R.id.et_context_cancel);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (responseCall.isExecuted()) {
                    responseCall.cancel();
                }
                confirm.dismiss();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressUpdate(int percentage) {
        arcProgress.setProgress(percentage);
    }

    @Override
    public void onError() {
        confirm.dismiss();
    }

    @Override
    public void onFinish() {
        confirm.dismiss();
    }

    private void requestCameraPermission() {
        Log.i(TAG, "CAMERA permission has NOT been granted. Requesting permission.");

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            Log.i(TAG,
                    "Displaying camera permission rationale to provide additional context.");
            Snackbar.with(CarDetailsActivity.this)
                    .type(SnackbarType.MULTI_LINE)
                    .text("Camera permission is needed to show the camera preview.")
                    .actionLabel("OK")
                    .actionColor(Color.CYAN)
                    .actionListener(new ActionClickListener() {
                        @Override
                        public void onActionClicked(Snackbar snackbar) {
                            ActivityCompat.requestPermissions(CarDetailsActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);

                        }
                    })
                    .duration(com.nispok.snackbar.Snackbar.SnackbarDuration.LENGTH_LONG)
                    .swipeToDismiss(false)
                    .show(CarDetailsActivity.this);
        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA);
        }
        // END_INCLUDE(camera_permission_request)
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CAMERA) {

            Log.i(TAG, "Received response for Camera permission request.");
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "CAMERA permission has now been granted. Showing preview.");
                Snackbar.with(CarDetailsActivity.this)
                        .type(SnackbarType.MULTI_LINE)
                        .text("Camera Permission has been granted. Preview can now be opened.")
                        .duration(com.nispok.snackbar.Snackbar.SnackbarDuration.LENGTH_SHORT)
                        .swipeToDismiss(false)
                        .show(CarDetailsActivity.this);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EasyImage.openCamera(CarDetailsActivity.this, EasyImageConfig.REQ_TAKE_PICTURE);
                    }
                }, 1000);

            } else {
                Log.i(TAG, "CAMERA permission was NOT granted.");
                Snackbar.with(CarDetailsActivity.this)
                        .type(SnackbarType.MULTI_LINE)
                        .text("Permissions were not granted.")
                        .duration(com.nispok.snackbar.Snackbar.SnackbarDuration.LENGTH_SHORT)
                        .swipeToDismiss(false)
                        .show(CarDetailsActivity.this);

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
