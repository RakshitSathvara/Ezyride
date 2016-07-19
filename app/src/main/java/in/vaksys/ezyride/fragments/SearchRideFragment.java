package in.vaksys.ezyride.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.vaksys.ezyride.R;
import in.vaksys.ezyride.activities.OfferRideActivity;
import in.vaksys.ezyride.activities.SearchRideActivity;
import in.vaksys.ezyride.extras.ProgresDialog;
import in.vaksys.ezyride.extras.Utils;
import in.vaksys.ezyride.utils.AppConfig;
import in.vaksys.ezyride.utils.PreferenceHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchRideFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "SearchRideFragment";

    @Bind(R.id.btn_search_ride)
    Button btnSearchRide;
    @Bind(R.id.btn_offer_ride)
    Button btnOfferRide;
    @Bind(R.id.SearchFromMainName)
    TextView SearchFromMainName;
    @Bind(R.id.SearchFromSubName)
    TextView SearchFromSubName;
    @Bind(R.id.SearchGetFromLocation)
    LinearLayout SearchGetFromLocation;
    @Bind(R.id.SearchGetCurrentLocation)
    ImageView SearchGetCurrentLocation;
    @Bind(R.id.SearchToMainName)
    TextView SearchToMainName;
    @Bind(R.id.SearchToSubName)
    TextView SearchToSubName;
    @Bind(R.id.SearchGetToLocation)
    LinearLayout SearchGetToLocation;
    @Bind(R.id.Search_btn_swap)
    ImageView SearchBtnSwap;

    private GoogleApiClient mGoogleApiClient;
    Utils utils;
    private ProgresDialog pDialog;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_TO = 2;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final int ACCESS_FINE_LOCATION = 1;

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
    private String mFromMain;
    private String mFromSub;
    private String mToMain;
    private String mToSub;

    PreferenceHelper helper;

    public SearchRideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new, container, false);
        /*inputFrom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (inputFrom.getRight() - inputFrom.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });*/
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Init();
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            createLocationRequest();
        }
    }

    private void Init() {
        utils = new Utils(getActivity());
        pDialog = new ProgresDialog(getActivity());
        pDialog.createDialog(false);
        helper = new PreferenceHelper(getActivity(), AppConfig.PREF_SEARCH_FILE_NAME);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.SearchGetFromLocation, R.id.SearchGetCurrentLocation, R.id.SearchGetToLocation, R.id.Search_btn_swap, R.id.btn_search_ride, R.id.btn_offer_ride})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.SearchGetFromLocation:
                ChooseFromLoc();
                break;
            case R.id.SearchGetCurrentLocation:
                guessCurrentPlace();
                break;
            case R.id.SearchGetToLocation:
                ChooseToLoc();
                break;
            case R.id.Search_btn_swap:
                break;
            case R.id.btn_search_ride:
                SaveData();
                break;
            case R.id.btn_offer_ride:
                startActivity(new Intent(getActivity(), OfferRideActivity.class));
                break;
        }
    }

    private void SaveData() {
        helper.initPref();
        helper.SaveStringPref(AppConfig.PREF_SEARCH_FROM_LAT, String.valueOf(FromLat));
        helper.SaveStringPref(AppConfig.PREF_SEARCH_TO_LAT, String.valueOf(ToLat));
        helper.SaveStringPref(AppConfig.PREF_SEARCH_TO_LONG, String.valueOf(ToLng));
        helper.SaveStringPref(AppConfig.PREF_SEARCH_FROM_LONG, String.valueOf(FromLng));
        helper.ApplyPref();
        startActivity(new Intent(getActivity(), SearchRideActivity.class));
    }

    private void ChooseFromLoc() {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
//                                    .setBoundsBias(new LatLngBounds(new LatLng(), new LatLng()))
                            .build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Log.e(TAG, " ajsd : " + message);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
                            .build(getActivity());
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_TO);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Log.e(TAG, "kjsadhkjas" + message);
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getActivity(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                getActivity().finish();
            }
            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void guessCurrentPlace() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            runtime_permissions();
            return;
        }
        try {
            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);
            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(@NonNull PlaceLikelihoodBuffer likelyPlaces) {
                    utils.showLog(TAG, "" + likelyPlaces.getCount());
                    if (likelyPlaces.getCount() > 0) {
                        PlaceLikelihood placeLikelihood = likelyPlaces.get(0);
                        //                String content = "";
                        if (placeLikelihood != null && placeLikelihood.getPlace() != null && !TextUtils.isEmpty(placeLikelihood.getPlace().getName())) {

                            SearchFromMainName.setText(placeLikelihood.getPlace().getName());
                            SearchFromSubName.setText(placeLikelihood.getPlace().getAddress());

                            LatLng FromLatLng = placeLikelihood.getPlace().getLatLng();

                            FromLat = FromLatLng.latitude;
                            FromLng = FromLatLng.longitude;
                            //                    content = "Most likely place: " + placeLikelihood.getPlace().getName() + "\n" + placeLikelihood.getPlace().getAddress();
                        }
                        //                if (placeLikelihood != null)
                        //                    content += "Percent change of being there: " + (int) (placeLikelihood.getLikelihood() * 100) + "%";
                        //                Log.e(TAG, "displayLocation: " + content);
                    } else {
                        Toast.makeText(getActivity(), "Auto Location can't fetch", Toast.LENGTH_SHORT).show();
                    }
                    likelyPlaces.release();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            runtime_permissions();
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);


                SearchFromMainName.setText(place.getName());
                SearchFromSubName.setText(place.getAddress());
                LatLng FromLatLng = place.getLatLng();

                FromLat = FromLatLng.latitude;
                FromLng = FromLatLng.longitude;

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.e(TAG, "hehh : " + status.getStatusMessage());

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // TODO: 23-06-2016 The user canceled the operation.
            }
        }
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_TO) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                SearchToMainName.setText(place.getName());
                SearchToSubName.setText(place.getAddress());
                LatLng FromLatLng = place.getLatLng();

                ToLat = FromLatLng.latitude;
                ToLng = FromLatLng.longitude;

                utils.showLog(TAG, "tolat:" + ToLat + " tolng:" + ToLng);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.i(TAG, " here : " + status.getStatusMessage());

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // TODO: 23-06-2016 The user canceled the operation.
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        checkPlayServices();
        if (mGoogleApiClient == null) {
            buildGoogleApiClient();
        }
        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
            mGoogleApiClient.disconnect();
        }
    }

    private void runtime_permissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            Log.i(TAG,
                    "Displaying camera permission rationale to provide additional context.");
            Snackbar.with(getActivity())
                    .type(SnackbarType.MULTI_LINE)
                    .text("Location permission is needed to get the current Location.")
                    .actionLabel("OK")
                    .actionColor(Color.CYAN)
                    .actionListener(new ActionClickListener() {
                        @Override
                        public void onActionClicked(Snackbar snackbar) {
                            Ask_Permission();
                        }
                    })
                    .duration(com.nispok.snackbar.Snackbar.SnackbarDuration.LENGTH_LONG)
                    .swipeToDismiss(false)
                    .show(getActivity());
        } else {
            // Camera permission has not been granted yet. Request it directly.
            Ask_Permission();

        }
    }

    private void Ask_Permission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                ACCESS_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Location permission has now been granted. Getting Location...");
                    Snackbar.with(getActivity())
                            .type(SnackbarType.MULTI_LINE)
                            .text("Location permission has now been granted. Getting Location...")
                            .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                            .swipeToDismiss(false)
                            .show(getActivity());
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            guessCurrentPlace();
                        }
                    }, 1000);

                } else {
                    Log.i(TAG, "Location permission was NOT granted.");
                    Snackbar.with(getActivity())
                            .type(SnackbarType.MULTI_LINE)
                            .text("Permissions were not granted.")
                            .duration(com.nispok.snackbar.Snackbar.SnackbarDuration.LENGTH_SHORT)
                            .swipeToDismiss(false)
                            .show(getActivity());
                }
            }
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
// Once connected with google api, get the location
        guessCurrentPlace();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }
}
