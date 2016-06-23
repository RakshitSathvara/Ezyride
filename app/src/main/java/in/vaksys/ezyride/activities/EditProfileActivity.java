package in.vaksys.ezyride.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import java.io.File;
import java.io.IOException;
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


public class EditProfileActivity extends AppCompatActivity {

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
    @Bind(R.id.carSpinner)
    Spinner carSpinner;
    @Bind(R.id.SaveBtn)
    Button SaveBtn;

    public static final String TAG = "DATE";

    private DatePickerDialog fromDatePickerDialog;
    ArrayList<String> Genderstrings = new ArrayList<>();
    ArrayList<String> carStrings = new ArrayList<>();

    private SimpleDateFormat dateFormatter;
    private String SelectedDate;
    private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";

    public Uri fileUri;
    private static final String IMAGE_DIRECTORY_NAME = "EzyRidePhotos";
    public int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static String timeStamp, myImageUrl;
    Bitmap bitmap;
    private Uri filePath;

    //private ExpandableView middleExpandableView;

    //    EditText etTime;
    TimePickerDialog mTimePicker;
    // private ExpandableView topExpandableView;
    private Spinner mGenderSpinner, mSelectCar;
    private String GenderSpinnItem, carSpinnItem;
    private boolean FbStatus;
    private boolean CorporateMailStatus;
    private boolean PanCardStatus;

    private String Emailid, PicUrl, mUserName, oldNumber, birthdate;
    private String newNumber;
    private int genderPosi, carDetailPosi;
    //private ExpandableView middleExpandableView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        mGenderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        mSelectCar = (Spinner) findViewById(R.id.carSpinner);

        setSupportActionBar(toolbar);

        setTitle("Profile");
        /*Calendar mcurrentTime = Calendar.getInstance();

        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(EditProfileActivity.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (timePicker.isShown()) {
                    etTime.setText(selectedHour + ":" + selectedMinute);
                }
            }
        }, hour, minute, true);//Yes 24 hour time*/
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //topExpandableView = (ExpandableView) findViewById(R.id.activity_main_top_expandable_view11111);
        // middleExpandableView = (ExpandableView) findViewById(R.id.activity_main_top_expandable_view1);

        //createTopExpandableView();
        //createMiddleExpandableView();
        setSpinners();
        setCarSpinners();
//        etTime = (EditText) findViewById(R.id.timeSupport);
        setDateTimeField();
//        etDepartDate.setInputType(InputType.TYPE_NULL);
//        etTime.setInputType(InputType.TYPE_NULL);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);


       /* etDepartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePickerDialog.show(mFragmentManager, "DepartDate");
                }
            }
        });*/

        setPreviosData();

        etBirthDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SelectfromDate();
                return true;
            }

        });
        /*etTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectTime();
                return true;
            }

        });
*/
/*
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime();

            }
        });*/
    }

    private void setPreviosData() {
        SharedPreferences sharedPreferences = getSharedPreferences("ProfileDetail", Context.MODE_PRIVATE);
        Emailid = sharedPreferences.getString("Emaiid", "");
        mUserName = sharedPreferences.getString("Name", "");
        birthdate = sharedPreferences.getString("Birthdate", "");
        oldNumber = sharedPreferences.getString("Mobile", "");

        FbStatus = sharedPreferences.getBoolean("facebookstatus", false);
        CorporateMailStatus = sharedPreferences.getBoolean("corporatemailstatus", false);
        PanCardStatus = sharedPreferences.getBoolean("pancardstatus", false);

        genderPosi = sharedPreferences.getInt("genderPosition", 0);
        carDetailPosi = sharedPreferences.getInt("carDetailPosition", 0);

        PicUrl = sharedPreferences.getString("Profilepic", "");

        Useremail.setText(Emailid);
        UserName.setText(mUserName);
        etBirthDate.setText(birthdate);
        Usermobile.setText(oldNumber);

        genderSpinner.setSelection(genderPosi);
        carSpinner.setSelection(carDetailPosi);

        Animation anim = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Glide.with(this)
                .load(PicUrl)
                .centerCrop()
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .dontAnimate()
                .animate(anim)
                .into(UserImage);

    }

    private void setSpinners() {
        Genderstrings.add("Select One");
        Genderstrings.add("Male");
        Genderstrings.add("Female");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfileActivity.this, R.layout.spinnerlayout, Genderstrings);
        mGenderSpinner.setAdapter(adapter);
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void setCarSpinners() {
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
    }

    private void selectTime() {

        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    /*public void addContentView(ExpandableView view, String stringList, boolean showCheckbox) {

        ExpandedListItemView itemView = new ExpandedListItemView(this);
        itemView.setText(stringList, showCheckbox);
        view.addContentView(itemView);

    }*/

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    Calendar c = Calendar.getInstance();

    private void SelectfromDate() {
        String formattedDate = sdf.format(c.getTime()); // current date
        Date d = null;
        try {
            d = sdf.parse(formattedDate);
        } catch (ParseException e) {
            Log.e(TAG, "SelectfromDate: " + e);
        }
        fromDatePickerDialog.getDatePicker().setMinDate(d.getTime());
        fromDatePickerDialog.show();
    }

   /* private void createTopExpandableView() {
        String[] androidVersionNameList = getResources().getStringArray(R.array.android_version_names);

        topExpandableView.fillData(R.drawable.dot, getString(R.string.android_names), false);
        addContentView(topExpandableView, "Audi A7", true);
        addContentView(topExpandableView, "Haundai", true);
//        topExpandableView.addContentView(expandableViewLevel1);
    }*/

    /*private void createMiddleExpandableView() {
        String[] androidVersionNameList = getResources().getStringArray(R.array.android_version_names);

        middleExpandableView.fillData(R.drawable.dot, getString(R.string.android_codes), false);
        //middleExpandableView.setVisibleLayoutHeight(getResources().getDimensionPixelSize(R.dimen.new_visible_height));
        addContentView(middleExpandableView, "Audi A7", true);
        addContentView(middleExpandableView, "Haundai", true);
        //addContentView(middleExpandableView, androidVersionNameList, false);
    }*/

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SelectedDate = dateFormatter.format(newDate.getTime());
                etBirthDate.setText(SelectedDate);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
// Create a media file name
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "You cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            if (resultCode == RESULT_OK) {
                filePath = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    UserImage.setImageBitmap(bitmap);

                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry! Failed to Select image", Toast.LENGTH_SHORT)
                            .show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "You cancelled image Selcetion", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void previewCapturedImage() {
        try {
            // hide video preview
            //videoPreview.setVisibility(View.GONE);

            UserImage.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            UserImage.setImageBitmap(bitmap);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void openBottomSheet() {

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
                captureImage();
                mBottomSheetDialog.dismiss();
            }
        });

        Open_gallary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showFileChooser();
                mBottomSheetDialog.dismiss();
            }
        });

    }

    @OnClick({R.id.UserImage, R.id.edit_username, R.id.facebook_verify, R.id.Mail_verify, R.id.composeMail,
            R.id.panCard_verify, R.id.editPanCard, R.id.SaveBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.UserImage:
                openBottomSheet();
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

        sendData(mUserName, Emailid, newNumber, oldNumber, birthdate, PicUrl, genderPosi, carDetailPosi, FbStatus, CorporateMailStatus, PanCardStatus);
    }

    private void sendData(String mUserName, String emailid, String newNumber, String oldNumber, String birthdate, String picUrl,
                          int genderPosi, int carDetailPosi, boolean fbStatus, boolean corporateMailStatus, boolean panCardStatus) {

        if (!newNumber.equals(oldNumber)) {
//            requestForSMS(mUserName, emailid, newNumber);

        }

    }



//    private void requestForSMS(final String name, final String email, final String mobile) {
//
//        ApiInterface apiService =
//                ApiClient.getClient().create(ApiInterface.class);
//
//
//
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                AppConfig.URL_REQUEST_SMS, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, response.toString());
//
//                try {
//                    JSONObject responseObj = new JSONObject(response);
//
//                    // Parsing json object response
//                    // response will be a json object
//                    boolean error = responseObj.getBoolean("error");
//                    String message = responseObj.getString("message");
//
//                    // checking for error, if not error SMS is initiated
//                    // device should receive it shortly
//                    if (!error) {
//                        // boolean flag saying device is waiting for sms
////                        pref.setIsWaitingForSms(true);
//
//                        // moving the screen to next pager item i.e otp screen
////                        viewPager.setCurrentItem(1);
////                        txtEditMobile.setText(pref.getMobileNumber());
////                        layoutEditMobile.setVisibility(View.VISIBLE);
//                        // TODO: 18-06-2016 here start new otp activity and make sure that you have to create new resend otp API
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        Toast.makeText(getApplicationContext(),
//                                "Error: " + message,
//                                Toast.LENGTH_LONG).show();
//                    }
//
//                    // hiding the progress bar
//                    progressBar.setVisibility(View.GONE);
//
//                } catch (JSONException e) {
//                    Toast.makeText(getApplicationContext(),
//                            "Error: " + e.getMessage(),
//                            Toast.LENGTH_LONG).show();
//
//                    progressBar.setVisibility(View.GONE);
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        "Errorrrrrrrrrrrrr : " +error.getMessage(), Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
//            }
//        }) {
//
//            /**
//             * Passing user parameters to our server
//             * @return
//             */
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("name", name);
//                params.put("email", email);
//                params.put("mobile", mobile);
//
//                Log.e(TAG, "Posting params: " + params.toString());
//
//                return params;
//            }
//
//        };
//
//        // Adding request to request queue
//        MyApplication.getInstance().addToRequestQueue(strReq);
//    }

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
}
