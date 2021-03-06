package edu.gatech.cs4911.mintyfresh.router;

import static edu.gatech.cs4911.mintyfresh.router.WebQuerier.getHttpDoc;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
    public static List<LatLng> getDirectionsTo(double startLatitude, double startLongitude,
        double destLatitude, double destLongitude) throws IOException {

        List<LatLng> result;
        String uri = constructMapUri(startLatitude, startLongitude, destLatitude, destLongitude);

        try {
            result = parseJsonToLatLng(getHttpDoc(uri));
            result = postProcessRoute(
                    result,
                    new LatLng(startLatitude, startLongitude),
                    new LatLng(destLatitude, destLongitude));
        } catch (JSONException e) {
            // No points in result, so we assume no routable path
            return null;
        }

        return result;
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
    private static String constructMapUri(double startLatitude, double startLongitude,
                                         double destLatitude, double destLongitude) {
        return GMAPS_BASE_API_STRING
                + "&origin=" + startLatitude + "," + startLongitude
                + "&destination=" + destLatitude + "," + destLongitude;
    }

    /**
     * Parses a String as a Google Maps JSON-encoded response and
     * constructs a List of LatLng objects from the response.
     *
     * @param doc The String returned from the server.
     * @return A List of LatLng objects constructed from the response.
     * @throws JSONException if there was an issue parsing the response.
     */
    private static List<LatLng> parseJsonToLatLng(String doc) throws JSONException {
        List<LatLng> output = new ArrayList<>();
        JSONObject responseBody = new JSONObject(doc);

        // First route of response
        JSONObject route = responseBody.getJSONArray("routes").getJSONObject(0);

        // All legs of route
        JSONArray legs = route.getJSONArray("legs");
        for (int j = 0; j < legs.length(); j++) {
            // For each leg, add all points to list

            // Starting from the start and end points...
            LatLng[] startEnd = constructLatLngFromJson(legs.getJSONObject(j));

            // Absolute starting location
            output.add(startEnd[0]);

            // And all points in between:
            JSONArray steps = legs.getJSONObject(j).getJSONArray("steps");

            for (int i = 0; i < steps.length(); i++) {
                LatLng[] stepStartEnd = constructLatLngFromJson(steps.getJSONObject(i));

                // Start point
                output.add(stepStartEnd[0]);
                // End point
                output.add(stepStartEnd[1]);
            }

            // Absolute ending location
            output.add(startEnd[1]);
        }

        return output;
    }

    /**
     * Constructs a new LatLng array from a Google Maps JSON element containing
     * a start and end location. The first element of the returned array will
     * be the start location, and the last element of the returned array will
     * be the end location.
     *
     * @param latlng The JSONObject containing "start_location" and "end_location" fields.
     * @return A new LatLng array with start and end points, respectively.
     * @throws JSONException if the input object does not contain the required fields.
     */
    private static LatLng[] constructLatLngFromJson(JSONObject latlng) throws JSONException {
        LatLng[] result = new LatLng[2];
        result[0] = new LatLng(
                latlng.getJSONObject("start_location").getDouble("lat"),
                latlng.getJSONObject("start_location").getDouble("lng"));
        result[1] = new LatLng(
                latlng.getJSONObject("start_location").getDouble("lat"),
                latlng.getJSONObject("start_location").getDouble("lng"));

        return result;
    }

    /**
     * Given a raw route as a list of LatLng objects, prunes duplicate points
     * and adds the absolute start and absolute end location to the start and end
     * of the route.
     *
     * @param rawRoute A raw LatLng route returned by the Google Maps API.
     * @param startLocation The absolute start location.
     * @param endLocation The absolute end location.
     * @return A final route from start to destination of distinct points.
     */
    private static List<LatLng> postProcessRoute(List<LatLng> rawRoute,
              LatLng startLocation, LatLng endLocation) {
        // Add start and end location to result
        rawRoute.add(0, startLocation);
        rawRoute.add(rawRoute.size() - 1, endLocation);

        // Prune duplicate points
        // (If there are duplicates, they should be right next to each other!)
        for (int i = 0; i < rawRoute.size() - 1; i++) {
            if (rawRoute.get(i).equals(rawRoute.get(i + 1))) {
                rawRoute.remove(i + 1);
            }
        }

        return rawRoute;
    }

    /**
     * Calculates the distance between two points, specified as two sets of
     * provided latitude and longitudes.
     *
     * @param startLatitude The origin latitude.
     * @param startLongitude The origin longitude.
     * @param destLatitude The destination latitude.
     * @param destLongitude The destination longitude.
     * @return
     */
    public static double calcRelativeDistance(double startLatitude, double startLongitude,
                                             double destLatitude, double destLongitude) {
        return Math.sqrt(Math.pow((destLatitude - startLatitude), 2)
                + Math.pow((destLongitude - startLongitude), 2));

    }

}
