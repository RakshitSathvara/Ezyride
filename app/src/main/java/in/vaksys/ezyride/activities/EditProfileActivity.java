package in.vaksys.ezyride.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    @Bind(R.id.et_departDate)
    EditText etDepartDate;
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

        etDepartDate.setOnTouchListener(new View.OnTouchListener() {
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

    private void setSpinners() {
        Genderstrings.add("Select One");
        Genderstrings.add("Male");
        Genderstrings.add("Female");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProfileActivity.this, R.layout.spinnerlayout, Genderstrings);
        mGenderSpinner.setAdapter(adapter);
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                etDepartDate.setText(SelectedDate);
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

    @OnClick({R.id.UserImage, R.id.edit_username, R.id.facebook_verify, R.id.Mail_verify, R.id.composeMail, R.id.panCard_verify, R.id.editPanCard})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.UserImage:
                openBottomSheet();
                break;
            case R.id.edit_username:
                break;
            case R.id.facebook_verify:
                break;
            case R.id.Mail_verify:
                break;
            case R.id.composeMail:
                final Dialog dialog = new Dialog(EditProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.compose_mail);
                //dialog.setTitle("Title...");
                dialog.show();
                break;
            case R.id.panCard_verify:
                break;
            case R.id.editPanCard:
                break;
        }
    }
}
