package edu.gatech.cs4911.mintyfresh.router;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;

/**
 * A Router is a wrapper around calls to the Google Maps API.
 */
public class Router {

    /**
     * Returns the current location as a LatLng object.
     *
     * @return The current location as a LatLng object.
     */
    public static LatLng getCurrentLocation(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }
}
