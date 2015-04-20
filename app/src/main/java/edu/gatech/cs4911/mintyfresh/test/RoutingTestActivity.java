package edu.gatech.cs4911.mintyfresh.test;

import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import edu.gatech.cs4911.mintyfresh.AmenityFinder;
import edu.gatech.cs4911.mintyfresh.R;
import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.Building;
import edu.gatech.cs4911.mintyfresh.exception.RouteException;
import edu.gatech.cs4911.mintyfresh.router.Router;

import static edu.gatech.cs4911.mintyfresh.db.DatabaseConfig.STEAKSCORP_READ_ONLY;

public class RoutingTestActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private boolean gotLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated. Zooms into the phone's current
     * detected location.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setMyLocationEnabled(true);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                //setUpMap();

                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location location) {
                        // Get current location
                        LatLng myLocation = new LatLng(location.getLatitude(),
                                location.getLongitude());
                        // Create an animation to zoom in on location
                        CameraUpdate zoomCamera = CameraUpdateFactory.newLatLngZoom(myLocation, 20);
                        // Set a pin
                        mMap.addMarker(new MarkerOptions().position(
                                myLocation).title("You are here!"));

                        // Test route
                        try {
                            // The value below should be filled with a destination building
                            // from a previous screen - this is just a test route!
                            String destBuildingId = "CUL";
                            RouteVector vector = new RouteVector(myLocation, destBuildingId);

                            List<LatLng> result = new NetIoTask().execute(vector).get();
                            for (int i = 0; i < result.size(); i++) {
                                // Add a pin
                                mMap.addMarker(new MarkerOptions().position(
                                        result.get(i)).title("Step " + i));
                            }
                        } catch (Exception e) {
                            return;
                        }

                        if (!gotLocation) {
                            // And do it!
                            mMap.animateCamera(zoomCamera);
                            gotLocation = true;
                        }
                    }
                });
            }
        }
    }

    private class NetIoTask extends AsyncTask<RouteVector, Void, List<LatLng>> {
        protected List<LatLng> doInBackground(RouteVector... vector) {
            try {
                AmenityFinder finder = new AmenityFinder(new DBHandler(STEAKSCORP_READ_ONLY));
                Building toBuilding = finder.getBuildingById(vector[0].buildingId);
                List<LatLng> result = Router.getDirectionsTo(
                        vector[0].curLocation.latitude, vector[0].curLocation.longitude,
                        toBuilding.getLatitude(), toBuilding.getLongitude());
                return result;
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * A RouteVector stores a current location and a destination building by ID.
     */
    private class RouteVector {
        /**
         * The current location.
         */
        private LatLng curLocation;
        /**
         * A destination building, by ID.
         */
        private String buildingId;

        /**
         * Constructs a new RouteVector, storing information about a current LatLng
         * location as well as a destination building by String ID.
         *
         * @param curLocation The current location.
         * @param buildingId A destination building, by ID.
         */
        private RouteVector(LatLng curLocation, String buildingId) {
            this.curLocation = curLocation;
            this.buildingId = buildingId;
        }
    }
}
