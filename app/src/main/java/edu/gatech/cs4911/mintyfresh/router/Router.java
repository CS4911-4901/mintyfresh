package edu.gatech.cs4911.mintyfresh.router;

import static edu.gatech.cs4911.mintyfresh.router.WebQuerier.getHttpDoc;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A Router is a wrapper around calls to the Google Maps API.
 */
public class Router {
    /**
     * The base String to build off of when querying the Google Maps HTTP API.
     */
    public static final String GMAPS_BASE_API_STRING =
            "https://maps.googleapis.com/maps/api/directions/json?mode=walking";

    /**
     * Returns the current location as a LatLng object.
     *
     * @return The current location as a LatLng object.
     */
    public static LatLng getCurrentLocation(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    /**
     * Queries the Google Maps API through HTTP GET, deconstructs the JSON response,
     * and returns a list of LatLng points pointing from start to destination.
     *
     * @param startLatitude The origin latitude.
     * @param startLongitude The origin longitude.
     * @param destLatitude The destination latitude.
     * @param destLongitude The destination longitude.
     * @return A list of LatLng points to form a path from origin to destination.
     * @throws IOException if the connection could not be established to the Google Maps server.
     */
    public static List<LatLng> getDirectionsTo(float startLatitude, float startLongitude,
        float destLatitude, float destLongitude) throws IOException {

        String uri = constructMapUri(startLatitude, startLongitude, destLatitude, destLongitude);

        return parseJsonToLatLng(getHttpDoc(uri));
    }

    /**
     * Constructs a URI String to query the Google Maps API through HTTP GET.
     *
     * @param startLatitude The origin latitude.
     * @param startLongitude The origin longitude.
     * @param destLatitude The destination latitude.
     * @param destLongitude The destination longitude.
     * @return A URI String to query the Google Maps API.
     */
    private static String constructMapUri(float startLatitude, float startLongitude,
                                         float destLatitude, float destLongitude) {
        return GMAPS_BASE_API_STRING
                + "&origin=" + startLatitude + "," + startLongitude
                + "&destination=" + destLatitude + "," + destLongitude;
    }

    /**
     * Parses a Document as a Google Maps JSON-encoded response and
     * constructs a List of LatLng objects from the response.
     *
     * @param doc The Document object returned from the server.
     * @return A List of LatLng objects constructed from the response.
     */
    private static List<LatLng> parseJsonToLatLng(Document doc) {
        List<LatLng> output = new ArrayList<>();
        // TODO: Complete

        return output;
    }

}
