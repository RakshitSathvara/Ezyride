package in.vaksys.ezyride.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import in.vaksys.ezyride.R;
import in.vaksys.ezyride.adapters.ExpandableView;
import in.vaksys.ezyride.adapters.ExpandedListItemView;

public class OfferRideActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final String TAG = "DATE";
    android.app.FragmentManager mFragmentManager;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    private ExpandableView topExpandableView;
    private ExpandableView middleExpandableView;

    EditText etDate, etTime;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_ride);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        setTitle("Offer Ride");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        topExpandableView = (ExpandableView) findViewById(R.id.activity_main_top_expandable_view);
        middleExpandableView = (ExpandableView) findViewById(R.id.activity_main_middle_expandable_view);

        createTopExpandableView();
        createMiddleExpandableView();

        mFragmentManager = getFragmentManager();


        etDate = (EditText) findViewById(R.id.dateSupport);
        etDate.setTextIsSelectable(false);

        etTime = (EditText) findViewById(R.id.timeSupport);
        etTime.setTextIsSelectable(false);

        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePickerDialog.show(mFragmentManager, "DepartDate");
                }
            }
        });

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(mFragmentManager, "DepartDate");
            }
        });

        etTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    timePickerDialog.show(mFragmentManager, "Time");
                }
            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show(mFragmentManager, "Time");
            }
        });

        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        timePickerDialog = TimePickerDialog.newInstance(this, Calendar.HOUR_OF_DAY, Calendar.MINUTE, false);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
        etDate.setText(date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        String time = hourString + "h" + minuteString + "m" + secondString + "s";
        etTime.setText(time);
    }

    public void addContentView(ExpandableView view, String stringList, boolean showCheckbox) {

        ExpandedListItemView itemView = new ExpandedListItemView(this);
        itemView.setText(stringList, showCheckbox);
        view.addContentView(itemView);

    }


    private void createTopExpandableView() {
        String[] androidVersionNameList = getResources().getStringArray(R.array.android_version_names);

        topExpandableView.fillData(R.drawable.dot, getString(R.string.android_names), false);
        addContentView(topExpandableView, "Audi A7", true);
        addContentView(topExpandableView, "Haundai", true);
        //topExpandableView.addContentView(expandableViewLevel1);
    }


    private void createMiddleExpandableView() {
        String[] androidVersionNameList = getResources().getStringArray(R.array.android_version_names);

        middleExpandableView.fillData(R.drawable.dot, getString(R.string.android_codes), false);
        //middleExpandableView.setVisibleLayoutHeight(getResources().getDimensionPixelSize(R.dimen.new_visible_height));
        addContentView(middleExpandableView, "Audi A7", true);
        addContentView(middleExpandableView, "Haundai", true);
        //addContentView(middleExpandableView, androidVersionNameList, false);
    }
}
