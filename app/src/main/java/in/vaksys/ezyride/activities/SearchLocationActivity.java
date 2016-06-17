package in.vaksys.ezyride.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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

import in.vaksys.ezyride.R;
import in.vaksys.ezyride.adapters.CustomPagerAdapter;

public class SearchLocationActivity extends AppCompatActivity implements LocationListener {

    GoogleMap mMap;
    Location location;

    private Toolbar toolbar;
    int[] mResources = {
            R.drawable.profile,
            R.drawable.profile,
            R.drawable.profile,
            R.drawable.profile,
            R.drawable.profile,
    };

    String[] km = {"105 KM Shared", "1067 KM Shared", "103 KM Shared", "199 KM Shared", "456 KM Shared"};
    String[] name = {"asd", "asd", "asd", "asd", "asd"};
    String[] pm = {"04:00 PM", "04:00 PM", "04:00 PM", "04:00 PM", "04:00 PM"};
    String[] rs = {"234", "467", "4356", "456", "78"};
    private CustomPagerAdapter mCustomPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

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

        final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        //boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }

                return false;
            }
        });

        mCustomPagerAdapter = new CustomPagerAdapter(this, mResources, km, name, pm, rs);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setPageMargin(-265);
        //mViewPager.setClipToPadding(false);
        //mViewPager.setPadding(20, 0, 260, 0);
        //mViewPager.setPageMargin(3);
        mViewPager.setAdapter(mCustomPagerAdapter);

        drawMarker(new LatLng(23.0367691, 72.5118986));
        drawMarker(new LatLng(23.0349539, 72.5123538));
        drawMarker(new LatLng(23.0423889, 72.5109653));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.0349539, 72.5123538), 15));
    }

    private void drawMarker(LatLng point) {

        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_car));
        // Setting latitude and longitude for the marker
        markerOptions.position(point);

        markerOptions.title(point.latitude + "," + point.longitude);
        // Adding marker on the Google Map
        mMap.addMarker(markerOptions);
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
}
