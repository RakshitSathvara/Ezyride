package in.vaksys.ezyride.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import in.vaksys.ezyride.R;
import in.vaksys.ezyride.extras.ProgresDialog;
import in.vaksys.ezyride.extras.Utils;
import in.vaksys.ezyride.responces.ApiInterface;
import in.vaksys.ezyride.responces.CarRegisterResponse;
import in.vaksys.ezyride.responces.ImageUploadResponse;
import in.vaksys.ezyride.utils.ApiClient;
import in.vaksys.ezyride.utils.AppConfig;
import in.vaksys.ezyride.utils.ImageUploadHelper;
import in.vaksys.ezyride.utils.PreferenceHelper;
import in.vaksys.ezyride.utils.ProgressRequestBody;
import in.vaksys.ezyride.utils.UploadImageCallBack;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.EasyImageConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfileActivity extends AppCompatActivity implements ProgressRequestBody.UploadCallbacks {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.UserImage)
    CircleImageView UserImage;
    @Bind(R.id.UserName)
    TextView UserName;
    @Bind(R.id.edit_username)
    ImageView editUsername;
    @Bind(R.id.Useremail)
    EditText Useremail;
    @Bind(R.id.Usermobile)
    EditText Usermobile;
    @Bind(R.id.et_birthDate)
    EditText etBirthDate;
    @Bind(R.id.genderSpinner)
    Spinner genderSpinner;
    @Bind(R.id.facebook_verify)
    ImageView facebookVerify;
    @Bind(R.id.Mail_verify)
    ImageView MailVerify;
    @Bind(R.id.composeMail)
    ImageView composeMail;
    @Bind(R.id.panCard_verify)
    ImageView panCardVerify;
    @Bind(R.id.editPanCard)
    ImageView editPanCard;
    /*    @Bind(R.id.carSpinner)
        Spinner carSpinner;*/
    @Bind(R.id.SaveBtn)
    Button SaveBtn;

    ArcProgress arcProgress;

    private static final String IMAGE_DIRECTORY_NAME = "EzyRidePhotos";

    private static final String TAG = "EditProfileActivity";
    private DatePickerDialog fromDatePickerDialog;
    ArrayList<String> Genderstrings = new ArrayList<>();
//    ArrayList<String> carStrings = new ArrayList<>();

//    private SimpleDateFormat dateFormatter;

    private String GenderSpinnItem, carSpinnItem;
    private boolean FbStatus;
    private boolean CorporateMailStatus;
    private boolean PanCardStatus;

    private String Emailid, ProfilePicaUrl, mUserName, oldNumber, birthdate;
    private String newNumber;
    private int genderPosi, carDetailPosi;

    private static final int REQUEST_CAMERA = 0;
    private File files;

    Utils utils;
    Dialog confirm;

    ApiInterface apiService;
    private ProgresDialog pDialog;
    Call<ImageUploadResponse> responseCall;
    PreferenceHelper helper;
    private boolean ImageType;
    private String PanCardImageUrl;
    Call<CarRegisterResponse> carRegisterResponseCall;
    SimpleDateFormat sdf;
    Calendar c;
    Date d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);


//        mGenderSpinner = (Spinner) findViewById(R.id.genderSpinner);
//        mSelectCar = (Spinner) findViewById(R.id.carSpinner);

        setSupportActionBar(toolbar);

        Init();
        /*getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        setSpinners();
//        setCarSpinners();

        ImageUploadDialog();
        setPreviosData();

        etBirthDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SelectfromDate();
                return true;
            }

        });
    }

    private void Init() {
        setTitle("Profile");
        files = new File("");

        utils = new Utils(this);
        apiService = ApiClient
                .getClient()
                .create(ApiInterface.class);
        pDialog = new ProgresDialog(this);
        pDialog.createDialog(false);
//        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        EasyImage.configuration(this)
                .setImagesFolderName("Choose Car Image")
                .setImagesFolderName(String.valueOf(new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        IMAGE_DIRECTORY_NAME)));
        helper = new PreferenceHelper(this, AppConfig.PREF_USER_FILE_NAME);
        helper.initPref();
        helper.SaveBooleanPref(AppConfig.PREF_USER_IMG_CHANGE, false);
        helper.ApplyPref();


        sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        c = Calendar.getInstance();
        d = null;
        c.add(Calendar.DAY_OF_MONTH, 0);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        String formattedDate = sdf.format(c.getTime());
        try {
            d = sdf.parse(formattedDate);
        } catch (ParseException e) {
            Log.e(TAG, "SelectfromDate: " + e);
        }
        fromDatePickerDialog = new DatePickerDialog(this, dateSetListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.getDatePicker().setMinDate(d.getTime());

    }

    private void setPreviosData() {
        Emailid = helper.LoadStringPref(AppConfig.PREF_USER_MAIL_ID, "");
        mUserName = helper.LoadStringPref(AppConfig.PREF_USER_NAME, "");
        birthdate = helper.LoadStringPref(AppConfig.PREF_USER_BIRTHDATE, "");
        oldNumber = helper.LoadStringPref(AppConfig.PREF_USER_MOBILE, "");

        FbStatus = helper.LoadBooleanPref("facebookstatus", false);
        CorporateMailStatus = helper.LoadBooleanPref("corporatemailstatus", false);
        PanCardStatus = helper.LoadBooleanPref("pancardstatus", false);

        genderPosi = helper.LoadIntPref("genderPosition", 0);
//        carDetailPosi = helper.LoadIntPref("carDetailPosition", 0);

        ProfilePicaUrl = helper.LoadStringPref(AppConfig.PREF_USER_PROF_IMG_URL, "");
        Log.e(TAG, "setPreviosData: " + ProfilePicaUrl);
        Useremail.setText(Emailid);
        UserName.setText(mUserName);
        etBirthDate.setText(birthdate);
        Usermobile.setText(oldNumber);

        genderSpinner.setSelection(genderPosi);
//        carSpinner.setSelection(carDetailPosi);

//        Animation anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Glide.with(this)
                .load(ProfilePicaUrl)
                .crossFade()
                .centerCrop()
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .dontAnimate()
//                .animate(anim)
                .into(UserImage);

    }

    private void setSpinners() {
        Genderstrings.add("Select One");
        Genderstrings.add("Male");
        Genderstrings.add("Female");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfileActivity.this, R.layout.spinnerlayout, Genderstrings);
        genderSpinner.setAdapter(adapter);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderPosi = position;
                TextView gendspi = (TextView) view;
                GenderSpinnItem = gendspi.getText().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(EditProfileActivity.this, "You have selected Nothing ..", Toast.LENGTH_SHORT).show();
            }
        });
    }

   /* private void setCarSpinners() {
        carStrings.add("Select One");
        carStrings.add("Audi A4");
        carStrings.add("BMW");
        carStrings.add("Add New Car");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfileActivity.this, R.layout.spinnerlayout, carStrings);
        mSelectCar.setAdapter(adapter);
        mSelectCar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carDetailPosi = position;
                TextView gendspi = (TextView) view;
                carSpinnItem = gendspi.getText().toString();
                if (position == 3) {
                    startActivity(new Intent(EditProfileActivity.this, CarDetailsActivity.class));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(EditProfileActivity.this, "You have selected Nothing ..", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

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


    private void SelectfromDate() {

       /* String formattedDate = sdf.format(c.getTime()); // current date
        try {
            d = sdf.parse(formattedDate);
        } catch (ParseException e) {
            Log.e(TAG, "SelectfromDate: " + e);
        }
//        assert d != null;
        fromDatePickerDialog.getDatePicker().setMinDate(d.getTime());
        fromDatePickerDialog.show();*/

        fromDatePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            c.set(i, i1, i2);
            etBirthDate.setText(sdf.format(c.getTime()));
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                ImageType = helper.LoadBooleanPref(AppConfig.PREF_USER_PROF_IMG, true);
                //Handle the image
                if (ImageType) {
                    files = imageFile;
                    Glide.with(EditProfileActivity.this)
                            .load(imageFile)
                            .crossFade()
                            .placeholder(R.drawable.profile)
                            .error(R.drawable.profile)
                            .centerCrop()
                            .dontAnimate()
                            .into(UserImage);
                    long length = imageFile.length();
                    length = length / 1024;
                    Toast.makeText(EditProfileActivity.this
                            , length + " kb " + Long.parseLong(String.valueOf(length / 1024)) + " MB", Toast.LENGTH_SHORT).show();
                    new ImageUploadHelper(EditProfileActivity.this).Upload(imageFile, "b96c450cb827366525f4df7007a121d2", ImageType, "Profile_");
                } else {
                    new ImageUploadHelper(EditProfileActivity.this).Upload(imageFile, "b96c450cb827366525f4df7007a121d2", ImageType, "Pan_");
                }

            }
        });
    }


    public void openBottomSheet(final boolean ProfileImage) {

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        TextView take_picture = (TextView) view.findViewById(R.id.take_picture);
        TextView Open_gallary = (TextView) view.findViewById(R.id.open_gallary);

        final Dialog mBottomSheetDialog = new Dialog(EditProfileActivity.this, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();

        take_picture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestCameraPermission();
                } else {
                    Log.i(TAG,
                            "CAMERA permission has already been granted. Displaying camera preview.");
                    helper.initPref();
                    helper.SaveBooleanPref(AppConfig.PREF_USER_PROF_IMG, ProfileImage);
                    helper.ApplyPref();
                    EasyImage.openCamera(EditProfileActivity.this, EasyImageConfig.REQ_TAKE_PICTURE);
                }
                mBottomSheetDialog.dismiss();
            }
        });

        Open_gallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.initPref();
                helper.SaveBooleanPref(AppConfig.PREF_USER_PROF_IMG, ProfileImage);
                helper.ApplyPref();
                EasyImage.openGallery(EditProfileActivity.this, EasyImageConfig.REQ_PICK_PICTURE_FROM_GALLERY);
                mBottomSheetDialog.dismiss();
            }
        });

    }

    @OnClick({R.id.UserImage, R.id.edit_username, R.id.facebook_verify, R.id.Mail_verify, R.id.composeMail,
            R.id.panCard_verify, R.id.editPanCard, R.id.SaveBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.UserImage:
                openBottomSheet(true);
                break;
            case R.id.edit_username:
                final Dialog dialog1 = new Dialog(EditProfileActivity.this);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.change_name_dialog);

                final EditText name = (EditText) dialog1.findViewById(R.id.et_name);
                Button NameconfrmBtn = (Button) dialog1.findViewById(R.id.name_Confirm_btn);

                NameconfrmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String MName = name.getText().toString();
                        if (!MName.isEmpty()) {
                            UserName.setText(MName);
                            dialog1.dismiss();
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                //dialog.setTitle("Title...");
                dialog1.show();
                break;
            case R.id.facebook_verify:
                break;
            case R.id.Mail_verify:
                break;
            case R.id.composeMail:
                final Dialog dialog = new Dialog(EditProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.compose_mail);

                final EditText CMailid = (EditText) dialog.findViewById(R.id.et_corporate_email);
                Button confrmBtn = (Button) dialog.findViewById(R.id.Email_Confirm_btn);

                confrmBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = CMailid.getText().toString();

                    }
                });

                //dialog.setTitle("Title...");
                dialog.show();
                break;
            case R.id.panCard_verify:
                break;
            case R.id.editPanCard:
                openBottomSheet(false);
                break;
            case R.id.SaveBtn:
                validateData();
                break;
        }
    }

    private void validateData() {
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

    private void getdata() {
        mUserName = UserName.getText().toString();
        Emailid = Useremail.getText().toString();
        newNumber = Usermobile.getText().toString();
        birthdate = etBirthDate.getText().toString();

        sendData(mUserName, newNumber, oldNumber, birthdate, ProfilePicaUrl, genderPosi, FbStatus, CorporateMailStatus, PanCardStatus, PanCardImageUrl);
    }

    private void sendData(final String mUserName, final String newNumber, String oldNumber, final String birthdate, final String picUrl,
                          final int genderPosi, final boolean fbStatus, final boolean corporateMailStatus, final boolean panCardStatus, final String panCardImageUrl) {

       /* if (!newNumber.equals(oldNumber)) {
            requestForSMS(mUserName, emailid, newNumber);

        }*/
        pDialog.DialogMessage("Updating User Profile ...");
        pDialog.showDialog();

        carRegisterResponseCall = apiService.PROFILE_UPDATE_RESPONSE_CALL("b96c450cb827366525f4df7007a121d2"
                , mUserName, newNumber, birthdate, picUrl, genderPosi, fbStatus, corporateMailStatus, panCardImageUrl, panCardStatus);

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
                        Toast.makeText(EditProfileActivity.this, "Profile SuccessFully updated...", Toast.LENGTH_SHORT).show();
                        helper.initPref();
                        helper.SaveStringPref(AppConfig.PREF_USER_NAME, mUserName);
                        helper.SaveStringPref(AppConfig.PREF_USER_MOBILE, newNumber);
                        helper.SaveStringPref(AppConfig.PREF_USER_BIRTHDATE, birthdate);
                        helper.SaveStringPref(AppConfig.PREF_USER_PROF_IMG_URL, picUrl);
                        Log.e(TAG, "onResponse: " + picUrl);
//                        helper.SaveStringPref(AppConfig.PREF_USER_PROF_IMG, picUrl);
                        helper.SaveStringPref(AppConfig.PREF_USER_PAN_IMG, panCardImageUrl);
                        helper.SaveIntPref(AppConfig.PREF_USER_GENDER_POSITION, genderPosi);
                        helper.SaveBooleanPref(AppConfig.PREF_USER_FB_STATUS, fbStatus);
                        helper.SaveBooleanPref(AppConfig.PREF_USER_CORP_MAIL_STATUS, corporateMailStatus);
                        helper.SaveBooleanPref(AppConfig.PREF_USER_PAN_CARD_STATUS, panCardStatus);
                        helper.ApplyPref();
                    } else {
                        utils.showLog(TAG, response1.getMessage());
                        Toast.makeText(EditProfileActivity.this, response1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this, "Unexpected Error,Please Contact Customer Care.", Toast.LENGTH_SHORT).show();
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

    /*private void UploadImage(final String carNumber, final String carModel, final int carLayoutPos, final int teaStatus, final int musicStatus
            , final int soundStatus, final int smokeStatus) {

        utils.showLog(TAG, files.toString());

        confirm.show();

        ProgressRequestBody requestBody = new ProgressRequestBody(files, this);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("car_image", String.valueOf((System.currentTimeMillis() / 1000)) + ".jpg", requestBody);

        responseCall = apiService.IMAGE_UPLOAD_RESPONSE_CALL("b96c450cb827366525f4df7007a121d2", body);

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
                        ProfilePicaUrl = response.body().getUrl();
                        utils.showLog(TAG, ProfilePicaUrl);
                        Toast.makeText(EditProfileActivity.this, "Image Uploaded...", Toast.LENGTH_SHORT).show();
                        if (!ProfilePicaUrl.isEmpty())
                            SendingRegisterCarParameters(carNumber, carModel, carLayoutPos, teaStatus, musicStatus, soundStatus, smokeStatus, ProfilePicaUrl);
                        else
                            Toast.makeText(EditProfileActivity.this, "There are some error. Please Upload Image Again.", Toast.LENGTH_SHORT).show();
                    } else {
                        utils.showLog(TAG, response1.getMessage());
                        Toast.makeText(EditProfileActivity.this, response1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this, "Unexpected Error,Please Contact Customer Care.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageUploadResponse> call, Throwable t) {
                confirm.dismiss();
                if (call.isCanceled()) {
                    Toast.makeText(EditProfileActivity.this, "Image Upload Canceled.", Toast.LENGTH_SHORT).show();
                } else {
                    utils.ShowError();
                }
                Log.e(TAG, "onFailure: Error");

            }
        });

    }
*/
    @Subscribe
    public void onEvent(UploadImageCallBack imageCallBack) {

        imageCallBack.getImageType();
        String imgurl = imageCallBack.getImageURL();
        if (imageCallBack.getImageType()) {
            ProfilePicaUrl = imgurl;
        } else {
            PanCardImageUrl = imgurl;
        }
    }

    private void requestCameraPermission() {
        Log.i(TAG, "CAMERA permission has NOT been granted. Requesting permission.");

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i(TAG,
                    "Displaying camera permission rationale to provide additional context.");
            Snackbar.with(EditProfileActivity.this)
                    .type(SnackbarType.MULTI_LINE)
                    .text("Camera permission is needed to show the camera preview.")
                    .actionLabel("OK")
                    .actionColor(Color.CYAN)
                    .actionListener(new ActionClickListener() {
                        @Override
                        public void onActionClicked(Snackbar snackbar) {
                            ActivityCompat.requestPermissions(EditProfileActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA);

                        }
                    })
                    .duration(com.nispok.snackbar.Snackbar.SnackbarDuration.LENGTH_LONG)
                    .swipeToDismiss(false)
                    .show(EditProfileActivity.this);
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
            // BEGIN_INCLUDE(permission_result)
            // Received permission result for camera permission.
            Log.i(TAG, "Received response for Camera permission request.");
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                Log.i(TAG, "CAMERA permission has now been granted. Showing preview.");
                Snackbar.with(EditProfileActivity.this)
                        .type(SnackbarType.MULTI_LINE)
                        .text("Camera Permission has been granted. Preview can now be opened.")
                        .duration(com.nispok.snackbar.Snackbar.SnackbarDuration.LENGTH_SHORT)
                        .swipeToDismiss(false)
                        .show(EditProfileActivity.this);

            } else {
                Log.i(TAG, "CAMERA permission was NOT granted.");
                Snackbar.with(EditProfileActivity.this)
                        .type(SnackbarType.MULTI_LINE)
                        .text("Permissions were not granted.")
                        .duration(com.nispok.snackbar.Snackbar.SnackbarDuration.LENGTH_SHORT)
                        .swipeToDismiss(false)
                        .show(EditProfileActivity.this);

            }
            // END_INCLUDE(permission_result)

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

   /* private void requestForSMS(final String name, final String email, final String mobile) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);



        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REQUEST_SMS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());

                try {
                    JSONObject responseObj = new JSONObject(response);

                    // Parsing json object response
                    // response will be a json object
                    boolean error = responseObj.getBoolean("error");
                    String message = responseObj.getString("message");

                    // checking for error, if not error SMS is initiated
                    // device should receive it shortly
                    if (!error) {
                        // boolean flag saying device is waiting for sms
//                        pref.setIsWaitingForSms(true);

                        // moving the screen to next pager item i.e VERIFY_OTP_RESPONCE_CALL screen
//                        viewPager.setCurrentItem(1);
//                        txtEditMobile.setText(pref.getMobileNumber());
//                        layoutEditMobile.setVisibility(View.VISIBLE);
                        // TODO: 18-06-2016 here start new VERIFY_OTP_RESPONCE_CALL activity and make sure that you have to create new resend VERIFY_OTP_RESPONCE_CALL API
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Error: " + message,
                                Toast.LENGTH_LONG).show();
                    }

                    // hiding the progress bar
                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();

                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Errorrrrrrrrrrrrr : " +error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }) {

            */

    /**
     * Passing user parameters to our server
     *
     * @return
     *//*
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("mobile", mobile);

                Log.e(TAG, "Posting params: " + params.toString());

                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
*/
    private boolean validateFirstName() {
        if (UserName.getText().toString().trim().isEmpty()) {
            UserName.setError(getString(R.string.err_msg_first_name));
            requestFocus(UserName);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateBirthDate() {
        if (etBirthDate.getText().toString().trim().isEmpty()) {
            etBirthDate.setError(getString(R.string.err_msg_birth_date));
            requestFocus(etBirthDate);
            return false;
        } else {
            return true;
        }
    }

    private boolean validateEmail() {
        String email = Useremail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            Useremail.setError(getString(R.string.err_msg_email));
            requestFocus(Useremail);
            return false;
        } else {
            return true;
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateNumber() {
        if (Usermobile.getText().toString().trim().isEmpty()) {
            Usermobile.setError(getString(R.string.err_msg_number));
            requestFocus(Usermobile);
            return false;
        }
        if (Usermobile.length() != 10) {
            Usermobile.setError(getString(R.string.err_msg_valid_number));
            requestFocus(Usermobile);
            return false;
        } else {
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
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
}
