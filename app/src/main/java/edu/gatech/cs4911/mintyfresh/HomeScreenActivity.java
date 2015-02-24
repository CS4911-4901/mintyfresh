package edu.gatech.cs4911.mintyfresh;

import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class HomeScreenActivity extends ActionBarActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        setUpMapIfNeeded();

        Button bathroom = (Button) findViewById(R.id.bathroomButton);
        bathroom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                expandMenu(0);
            }
        });
        Button vending = (Button) findViewById(R.id.vendingButton);
        vending.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                expandMenu(1);
            }
        });
        Button printing = (Button) findViewById(R.id.printerButton);
        printing.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                expandMenu(2);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void expandMenu(int type) {
        //do nothing
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated. Zooms into the phone's current
     * detected location.
     * <p/>
     * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
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
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maplap))
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
                        // And do it!
                        mMap.animateCamera(zoomCamera);
                    }
                });
            }
        }
    }
}
