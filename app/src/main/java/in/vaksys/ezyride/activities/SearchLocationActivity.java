package in.vaksys.ezyride.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.vaksys.ezyride.R;
import in.vaksys.ezyride.adapters.MapListAdapter;
import in.vaksys.ezyride.extras.ProgresDialog;
import in.vaksys.ezyride.extras.Utils;
import in.vaksys.ezyride.responces.ApiInterface;
import in.vaksys.ezyride.responces.SearchRideResponse;
import in.vaksys.ezyride.utils.ApiClient;
import in.vaksys.ezyride.utils.AppConfig;
import in.vaksys.ezyride.utils.MapCallback;
import in.vaksys.ezyride.utils.PreferenceHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchLocationActivity extends AppCompatActivity implements LocationListener {

    private static final String TAG = "SearchLocationActivity";
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycler_view_location_ride)
    RecyclerView recyclerViewSearchRide;
    private GoogleMap mMap;
    public Location location;
    private EventBus eventBus = EventBus.getDefault();
    MapListAdapter mapListAdapter;

    ProgresDialog pDialog;
    PreferenceHelper helper;
    Utils utils;
    ApiInterface apiService;
    private String to_lat;
    private String to_long;
    private String from_lat;
    private String from_long;
    List<SearchRideResponse.RidesEntity> aa;
    private int posi;
    static SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    static Calendar c = Calendar.getInstance();
    private static String TodayDate = df.format(c.getTime());
    private Date myDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        ButterKnife.bind(this);

        Bundle getBundle = this.getIntent().getExtras();

        if (!getBundle.isEmpty()) {
            posi = getIntent().getExtras().getInt("posi", 1);
        } else {
            Toast.makeText(SearchLocationActivity.this, "Empty Parameters ", Toast.LENGTH_SHORT).show();
            posi = 1;
        }

        setSupportActionBar(toolbar);

        setTitle("Search Location");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // Showing status
        if (status == ConnectionResult.SUCCESS) {
            Toast.makeText(this, "Available", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Google Play Services not Available", Toast.LENGTH_SHORT).show();
        }
        utils = new Utils(this);
        apiService = ApiClient
                .getClient()
                .create(ApiInterface.class);
        pDialog = new ProgresDialog(this);
        pDialog.createDialog(false);

        helper = new PreferenceHelper(this, AppConfig.PREF_SEARCH_FILE_NAME);
        to_lat = helper.LoadStringPref(AppConfig.PREF_SEARCH_TO_LAT, "0.0");
        to_long = helper.LoadStringPref(AppConfig.PREF_SEARCH_TO_LONG, "0.0");
        from_lat = helper.LoadStringPref(AppConfig.PREF_SEARCH_FROM_LAT, "0.0");
        from_long = helper.LoadStringPref(AppConfig.PREF_SEARCH_FROM_LONG, "0.0");


        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        //boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


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
        mMap.setMyLocationEnabled(true);


        // Getting Current Location
        location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            onLocationChanged(location);
        }
        locationManager.requestLocationUpdates(provider, 20000, 0, this);

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (gps) {
                    Toast.makeText(getApplicationContext()
                            , "setMyLocationEnabled Pressed"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext()
                            , "setMyLocationEnabled not working"
                            , Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }

                return false;
            }
        });


        recyclerViewSearchRide.setHasFixedSize(true);
        RecyclerView.LayoutManager gridLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerViewSearchRide.setLayoutManager(gridLayoutManager);

        if (posi == 2) {
            try {
                myDate = df.parse(TodayDate);
            } catch (ParseException e) {
                e.printStackTrace();
            } finally {
                TodayDate = Utils.addDays(myDate, 1);
            }
        }
//        NetworkCall(to_lat, to_long, from_lat, from_long, TodayDate);
        NetworkCall("19.0759837", "72.8776559", "21.170240099999997", "72.83106070000001", "15-07-2016");

//        drawMarker(new LatLng(23.0367691, 72.5118986));
//        drawMarker(new LatLng(23.0349539, 72.5123538));
//        drawMarker(new LatLng(23.0423889, 72.5109653));
//
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.0349539, 72.5123538), 15));
    }

    private void NetworkCall(String to_lat, String to_long, String from_lat, String from_long, String todayDate) {

        pDialog.DialogMessage("Fetching Available Rides ...");
        pDialog.showDialog();

        Call<SearchRideResponse> call = apiService.SEARCH_RIDE_RESPONSE_CALL("9bb2f29fe5a1ce84866c4f42bad91237"
                , to_lat, to_long, from_lat, from_long, todayDate);
        utils.showLog(TAG, to_lat + " " + to_long + " " + from_lat + " " + from_long + " " + todayDate);
        call.enqueue(new Callback<SearchRideResponse>() {
            @Override
            public void onResponse(Call<SearchRideResponse> call, Response<SearchRideResponse> response) {
                pDialog.hideDialog();
                if (response.code() == 200) {
//                    if (recyclerViewSearchRide.getVisibility() == View.GONE) {
//                        recyclerViewSearchRide.setVisibility(View.VISIBLE);
//                        errorView.setVisibility(View.GONE);
//                    }
                    SearchRideResponse response1 = response.body();

                    if (!response.body().isError()) {

                        aa = response.body().getRides();
//                        MyCarAdapter myCarAdapter = new MyCarAdapter(getActivity(), aa);
                        mapListAdapter = new MapListAdapter(SearchLocationActivity.this, aa);
                        recyclerViewSearchRide.setAdapter(mapListAdapter);

                    } else {
                        utils.showLog(TAG, response1.getMessage());
                        Toast.makeText(SearchLocationActivity.this, response1.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SearchLocationActivity.this, "Unexpected Error,Please Contact Customer Care.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchRideResponse> call, Throwable t) {
                pDialog.hideDialog();
                utils.ShowError();
                Log.e(TAG, "onFailure: Error");
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        eventBus.register(this);
    }

    @Override
    public void onPause() {
        eventBus.unregister(this);
        super.onPause();
    }

    @Subscribe
    public void onEvent(MapCallback mapCallback) {
        drawMarker(new LatLng(mapCallback.getLatitude(), mapCallback.getLongitude()));
    }


    private void drawMarker(LatLng point) {
        mMap.clear();
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_car));
        // Setting latitude and longitude for the marker
        markerOptions.position(point);

        markerOptions.title(point.latitude + "," + point.longitude);
        // Adding marker on the Google Map
        mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(point.latitude, point.longitude), 18));

    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

       /* // Showing the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));*/

       /* Toast.makeText(this
                , "Latitude:" + latitude + ", Longitude:" + longitude
                , Toast.LENGTH_SHORT).show();*/

        Log.e("MAP: ", "Latitude:" + latitude + ", Longitude:" + longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
/*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, SearchRideActivity.class));
        finish();
    }*/
}
