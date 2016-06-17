package in.vaksys.ezyride.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import in.vaksys.ezyride.R;


public class EditProfileActivity extends AppCompatActivity {

    ImageView composeMail;

    private Toolbar toolbar;
    public static final String TAG = "DATE";

    private DatePickerDialog fromDatePickerDialog;
    ArrayList<String> Genderstrings = new ArrayList<>();
    ArrayList<String> carStrings = new ArrayList<>();

    private SimpleDateFormat dateFormatter;
    private String SelectedDate;
    private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";


    //private ExpandableView middleExpandableView;

    EditText etDate;
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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        etDate = (EditText) findViewById(R.id.et_departDate);
//        etTime = (EditText) findViewById(R.id.timeSupport);
        setDateTimeField();
//        etDate.setInputType(InputType.TYPE_NULL);
//        etTime.setInputType(InputType.TYPE_NULL);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        composeMail = (ImageView) findViewById(R.id.composeMail);

        composeMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(EditProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.compose_mail);
                //dialog.setTitle("Title...");
                dialog.show();
            }
        });

       /* etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePickerDialog.show(mFragmentManager, "DepartDate");
                }
            }
        });*/

        etDate.setOnTouchListener(new View.OnTouchListener() {
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
                etDate.setText(SelectedDate);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


}
