package in.vaksys.ezyride.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.vaksys.ezyride.R;

public class CarDetailsActivity extends AppCompatActivity {

    private static final String TAG = "harsh";
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
   /* @Bind(R.id.switch_ac)
    SwitchButton switchAc;
    @Bind(R.id.switch_music)
    SwitchButton switchMusic;
    @Bind(R.id.switch_airbag)
    SwitchButton switchAirbag;
    @Bind(R.id.switch_seat_belt)
    SwitchButton switchSeatBelt;*/
    @Bind(R.id.btn_offer_now)
    Button btnOfferNow;
    @Bind(R.id.car_image)
    ImageView CarImage;
    ArrayList<String> carLayoutStrings = new ArrayList<>();
    private String CarLayoutSpinnItem;
    public Uri fileUri;
    private static final String IMAGE_DIRECTORY_NAME = "EzyRidePhotos";
    public int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static String timeStamp, myImageUrl;
    Bitmap bitmap;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setSpinner();

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
        int pos = spinnerCarLayout.getSelectedItemPosition();
        View selectedView = spinnerCarLayout.getSelectedView();
        TextView selectedTextView = (TextView) selectedView;
        if (pos != 0 && selectedView != null) {
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
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
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
        SharedPreferences sharedPreferences = getSharedPreferences("ProfileDetail", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
                editor.putString("imgType", "manual");
                editor.apply();
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
                    CarImage.setImageBitmap(bitmap);
                    editor.putString("imgType", "manual");
                    editor.apply();
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

            CarImage.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            CarImage.setImageBitmap(bitmap);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.edit_image)
    public void onClick1() {
        openBottomSheet();
    }

    @OnClick(R.id.btn_offer_now)
    public void onClick() {
        if (!validateSpinnerItems()) {
            return;
        }
        Toast.makeText(CarDetailsActivity.this, "success", Toast.LENGTH_SHORT).show();

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
}
