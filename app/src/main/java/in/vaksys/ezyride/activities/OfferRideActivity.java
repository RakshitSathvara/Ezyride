package in.vaksys.ezyride.activities;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.vaksys.ezyride.R;
import in.vaksys.ezyride.adapters.CarsSpinnerAdapter;
import in.vaksys.ezyride.extras.ProgresDialog;
import in.vaksys.ezyride.extras.Utils;
import in.vaksys.ezyride.responces.ApiInterface;
import in.vaksys.ezyride.responces.CarResponse;
import in.vaksys.ezyride.responces.OfferRideResponse;
import in.vaksys.ezyride.utils.ApiClient;
import in.vaksys.ezyride.MyApplication;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferRideActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "OfferRideActivity";
    private static final int ACCESS_FINE_LOCATION = 1;
    FragmentManager mFragmentManager;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.GetFromLocation)
    LinearLayout GetFromLocation;
    @Bind(R.id.GetCurrentLocation)
    ImageView GetCurrentLocation;
    @Bind(R.id.GetToLocation)
    LinearLayout GetToLocation;
    @Bind(R.id.dateSupport)
    TextView dateSupport;
    @Bind(R.id.timeSupport)
    TextView timeSupport;
    @Bind(R.id.OfferNowBtn)
    Button OfferNowBtn;
    @Bind(R.id.FromMainName)
    TextView FromMainName;
    @Bind(R.id.FromSubName)
    TextView FromSubName;
    @Bind(R.id.ToMainName)
    TextView ToMainName;
    @Bind(R.id.ToSubName)
    TextView ToSubName;
    @Bind(R.id.spinner_car_list)
    Spinner spinnerCarList;
    @Bind(R.id.et_Price)
    EditText etPrice;
    @Bind(R.id.et_price_input)
    TextInputLayout etPriceInput;
    @Bind(R.id.et_seat)
    EditText etSeat;
    @Bind(R.id.et_seat_input)
    TextInputLayout etSeatInput;
    @Bind(R.id.ladies_status)
    CheckBox ladiesStatus;

    private GoogleApiClient mGoogleApiClient;
    Utils utils;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_TO = 2;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = true;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 100000; // 100 sec
    private static int FATEST_INTERVAL = 60000; // 60 sec
//    private static int DISPLACEMENT = 10; // 10 meters

    private double FromLat;
    private double FromLng;
    private double ToLat;
    private double ToLng;
    private int carListPosi;
    private String carSpinnItem;
    private int mLadiesStatus = 0;
    private String mRideDate;
    private String mRideTime;
    private String mPrice;
    private String mSeats;

    ApiInterface apiService;
    CarResponse carResponse;

    private String APIkey;
    private ProgresDialog pDialog;
    private String ActualCarId;
    private int ForLastPosi;
    private String mFromMain;
    private String mFromSub;
    private String mToMain;
    private String mToSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_ride);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle("Offer Ride");

        utils = new Utils(this);
        pDialog = new ProgresDialog(this);
        pDialog.createDialog(false);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = MyApplication.getInstance().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        APIkey = sharedPreferences.getString("APIkey", "");

        apiService = ApiClient.getClient().create(ApiInterface.class);

        setCarSpinners();

        mFragmentManager = getFragmentManager();

        dateSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(mFragmentManager, "DepartDate");
            }
        });


        timeSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show(mFragmentManager, "Time");
            }
        });

        etPrice.addTextChangedListener(new MyTextWatcher(etPrice));
        etSeat.addTextChangedListener(new MyTextWatcher(etSeat));

        ladiesStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(OfferRideActivity.this, "" + b, Toast.LENGTH_SHORT).show();
                if (b) {
                    mLadiesStatus = 1;
                } else {
                    mLadiesStatus = 0;
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        timePickerDialog = TimePickerDialog.newInstance(this, Calendar.HOUR_OF_DAY, Calendar.MINUTE, false);
       /* mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(this, this)
                .addApi(LocationServices.API)
                .build();*/
        // First we need to check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    guessCurrentPlace();

                } else {

                    Toast.makeText(OfferRideActivity.this, "You Have to Grant That Permission.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);

                FromMainName.setText(place.getName());
                FromSubName.setText(place.getAddress());
                LatLng FromLatLng = place.getLatLng();

                FromLat = FromLatLng.latitude;
                FromLng = FromLatLng.longitude;

                /*String placeDetailsStr = place.getName() + "\n"
                        + place.getId() + "\n"
                        + place.getLatLng().toString() + "\n"
                        + place.getAddress() + "\n"
                        + place.getAttributions();
                Log.e(TAG, "onPlaceSelected: " + placeDetailsStr);
                Log.e(TAG, "onPlaceSelected11: " + formatPlaceDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()));
                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {
                    Log.e(TAG, "onActivityResult: " + Html.fromHtml(attributions.toString()));
                } else {
                    Log.e(TAG, "onActivityResult: ");

                }*/
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "hehh : " + status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // TODO: 23-06-2016 The user canceled the operation.
            }
        }
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_TO) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                ToMainName.setText(place.getName());
                ToSubName.setText(place.getAddress());
                LatLng FromLatLng = place.getLatLng();

                ToLat = FromLatLng.latitude;
                ToLng = FromLatLng.longitude;

                utils.showLog(TAG, "tolat:" + ToLat + " tolng:" + ToLng);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i(TAG, " here : " + status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // TODO: 23-06-2016 The user canceled the operation.
            }
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
        dateSupport.setText(date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String secondString = second < 10 ? "0" + second : "" + second;
        String time = hourString + "h" + minuteString + "m" + secondString + "s";
        timeSupport.setText(time);
    }

    private void setCarSpinners() {

        pDialog.DialogMessage("Getting Car list ...");
        pDialog.showDialog();

        Call<CarResponse> call = apiService.GET_CAR_RESPONSE_CALL("b96c450cb827366525f4df7007a121d2");

        call.enqueue(new Callback<CarResponse>() {
            @Override
            public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                pDialog.hideDialog();
                if (response.code() == 200) {
                    CarResponse response1 = response.body();
//                    utils.showLog(TAG, response.raw().toString());

                    if (!response.body().isError()) {
//                        utils.showLog(TAG, String.valueOf(response.body().isError()));

                        List<CarResponse.CarsEntity> aa = response.body().getCars();
                        CarsSpinnerAdapter adapter = new CarsSpinnerAdapter(OfferRideActivity.this, aa);
                        spinnerCarList.setAdapter(adapter);
                        ForLastPosi = aa.size();
                    } else {
                        utils.showLog(TAG, response1.getMessage());
                        Toast.makeText(OfferRideActivity.this, response1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OfferRideActivity.this, "Unexpected Error,Please Contact Customer Care.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CarResponse> call, Throwable t) {
                pDialog.hideDialog();
                utils.ShowError();
                Log.e(TAG, "onFailure: Error");
            }
        });

        spinnerCarList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carListPosi = position;
                ActualCarId = ((TextView) findViewById(R.id.rowid)).getText().toString();
                carSpinnItem = ((TextView) findViewById(R.id.rowText)).getText().toString();
                if (carListPosi == (ForLastPosi - 1)) {
                    startActivity(new Intent(OfferRideActivity.this, CarDetailsActivity.class));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(OfferRideActivity.this, "You have selected Nothing ..", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
            mGoogleApiClient.disconnect();
        }
    }

    private void guessCurrentPlace() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION);
            return;
        }
        try {
            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);
            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(PlaceLikelihoodBuffer likelyPlaces) {

                    PlaceLikelihood placeLikelihood = likelyPlaces.get(0);
                    //                String content = "";
                    if (placeLikelihood != null && placeLikelihood.getPlace() != null && !TextUtils.isEmpty(placeLikelihood.getPlace().getName())) {
                        FromMainName.setText(placeLikelihood.getPlace().getName());
                        FromSubName.setText(placeLikelihood.getPlace().getAddress());

                        LatLng FromLatLng = placeLikelihood.getPlace().getLatLng();

                        FromLat = FromLatLng.latitude;
                        FromLng = FromLatLng.longitude;
                        //                    content = "Most likely place: " + placeLikelihood.getPlace().getName() + "\n" + placeLikelihood.getPlace().getAddress();
                    }
                    //                if (placeLikelihood != null)
                    //                    content += "Percent change of being there: " + (int) (placeLikelihood.getLikelihood() * 100) + "%";
                    //                Log.e(TAG, "displayLocation: " + content);
                    likelyPlaces.release();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        guessCurrentPlace();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        /*Toast.makeText(getApplicationContext(), "Location changed!",
                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        guessCurrentPlace();*/
    }

    @OnClick({R.id.GetFromLocation, R.id.GetCurrentLocation, R.id.GetToLocation, R.id.OfferNowBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.GetFromLocation:
                ChooseFromLoc();
                break;
            case R.id.GetCurrentLocation:
                guessCurrentPlace();
                break;
            case R.id.GetToLocation:
                ChooseToLoc();
                break;
            case R.id.OfferNowBtn:
                ValidateData();
                break;
        }
    }

    private void ValidateData() {
        if (!validateDate()) {
            return;
        }
        if (!validateTime()) {
            return;
        }
        if (!validateCar()) {
            return;
        }
        if (!validatePrice()) {
            return;
        }
        if (!validateSeats()) {
            return;
        }
        getData();
    }

    private void getData() {

        mRideDate = dateSupport.getText().toString();
        mRideTime = timeSupport.getText().toString();
        mPrice = etPrice.getText().toString();
        mSeats = etSeat.getText().toString();
        mFromMain = FromMainName.getText().toString();
        mFromSub = FromSubName.getText().toString();
        mToMain = ToMainName.getText().toString();
        mToSub = ToSubName.getText().toString();
        SendData(ActualCarId, FromLat, ToLat, FromLng, ToLng, mFromMain, mFromSub, mToMain, mToSub, mRideDate, mRideTime, mPrice, mSeats, mLadiesStatus);
    }

    private void SendData(String actualCarId, double fromLat, double toLat, double fromLng, double toLng
            , String fromMainName, String fromSubName, String toMainName, String toSubName
            , String mRideDate, String mRideTime, String mPrice, String mSeats, int mLadiesStatus) {

        pDialog.DialogMessage("Sending OTP request ...");
        pDialog.showDialog();

        Call<OfferRideResponse> call = apiService.OFFER_RIDE_RESPONSE_CALL("b96c450cb827366525f4df7007a121d2", Integer.parseInt(actualCarId), fromLat, toLat, fromLng, toLng
                , fromMainName, fromSubName, toMainName, toSubName, mRideDate, mRideTime,
                (Float.parseFloat(mPrice)), Integer.parseInt(mSeats), mLadiesStatus);

        call.enqueue(new Callback<OfferRideResponse>() {
            @Override
            public void onResponse(Call<OfferRideResponse> call, Response<OfferRideResponse> response) {
                pDialog.hideDialog();
                if (response.code() == 200) {
                    OfferRideResponse response1 = response.body();
                    if (!response.body().isError()) {
                        Toast.makeText(OfferRideActivity.this, response1.getMessage(), Toast.LENGTH_SHORT).show();
                        utils.showLog(TAG, response1.getMessage());
                    } else {
                        utils.showLog(TAG, response1.getMessage());
                        Toast.makeText(OfferRideActivity.this, response1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OfferRideActivity.this, "Unexpected Error,Please Contact Customer Care.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OfferRideResponse> call, Throwable t) {
                pDialog.hideDialog();
                utils.ShowError();
                Log.e(TAG, "onFailure: Error");
            }
        });

    }

    private void ChooseFromLoc() {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
//                                    .setBoundsBias(new LatLngBounds(new LatLng(), new LatLng()))
                            .build(OfferRideActivity.this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(OfferRideActivity.this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Log.e(TAG, " ajsd : " + message);
            Toast.makeText(OfferRideActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void ChooseToLoc() {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
//                                    .setBoundsBias(new LatLngBounds(new LatLng(), new LatLng()))
                            .build(OfferRideActivity.this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_TO);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(OfferRideActivity.this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Log.e(TAG, "kjsadhkjas" + message);
            Toast.makeText(OfferRideActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.et_Price:
                    validatePrice();
                    break;
                case R.id.et_seat:
                    validateSeats();
                    break;
            }
        }
    }

    private boolean validatePrice() {
        if (etPrice.getText().toString().trim().isEmpty()) {
            etPriceInput.setError(getString(R.string.err_msg_price));
            requestFocus(etPrice);
            return false;
        } else {
            etPriceInput.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateSeats() {
        if (etSeat.getText().toString().trim().isEmpty()) {
            etSeatInput.setError(getString(R.string.err_msg_seat));
            requestFocus(etSeat);
            return false;
        } else {
            etSeatInput.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateDate() {
        if (dateSupport.getText().toString().trim().equalsIgnoreCase("Date")) {
            Toast.makeText(OfferRideActivity.this, getString(R.string.err_msg_date), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateCar() {
        if (carListPosi == 0) {
            Toast.makeText(OfferRideActivity.this, getString(R.string.err_msg_car_spinner), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateTime() {
        if (timeSupport.getText().toString().trim().equalsIgnoreCase("Time")) {
            Toast.makeText(OfferRideActivity.this, getString(R.string.err_msg_time), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
