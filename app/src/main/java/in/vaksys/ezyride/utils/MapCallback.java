package in.vaksys.ezyride.utils;

/**
 * Created by Harsh on 11-07-2016.
 */
public class MapCallback {

    private final double latitude;

    private final double longitude;

    public MapCallback(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
