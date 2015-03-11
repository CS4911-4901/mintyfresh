package edu.gatech.cs4911.mintyfresh;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.exception.NoDbResultException;
import edu.gatech.cs4911.mintyfresh.router.RelativeBuilding;

import static edu.gatech.cs4911.mintyfresh.db.DatabaseConfig.STEAKSCORP_READ_ONLY;


public class HomeScreenActivity extends ActionBarActivity {


//    http://theopentutorials.com/tutorials/android/listview/android-expandable-list-view-example/
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ExpandableListView expListView;
    protected AmenityFinder amenityFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        expListView = (ExpandableListView) findViewById(R.id.buildingList);
        expListView.setVisibility(View.GONE);

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

    protected Activity returnThis() {
        return this;
    }

    private void expandMenu(int type) {
        //do nothing
        //find the amenities
        //make them into the list view
        //post them

        Log.v("sigh", "yeah");

        Location curLocation = new Location("TEST_PROVIDER");
        curLocation.setLatitude(33.7751878);
        curLocation.setLongitude(-84.39687341);

        new ConnectToDB(type).execute(curLocation);
    }

    protected void showEFLA(ArrayList<RelativeBuilding> buildings, Map<RelativeBuilding, List<Integer>> map) {
        final ExpandableFloorListAdapter expListAdapter = new ExpandableFloorListAdapter(
                this, buildings, map);
        expListView.setAdapter(expListAdapter);

        expListView.setVisibility(View.VISIBLE);
        Log.v("SHOWEFLA", "DONE?");
    }


    private class ConnectToDB extends AsyncTask <Location, Integer, Void> {

        private String name;
        private int type;
        private ArrayList<RelativeBuilding> buildings;
        private Map<RelativeBuilding, List<Integer>> map;

        public ConnectToDB(int type) {
            this.type = type;
            //showing button-name
            String name = "";
            if (type == 0) {
                name = "Bathrooms";
            }
            else if (type == 1) {
                name = "Vending";
            }
            else {
                name = "Printers";
            }
        }

        @Override
        protected Void doInBackground(Location... params) {
            DBHandler dbh;

            try {

                dbh = new DBHandler(STEAKSCORP_READ_ONLY);
                amenityFinder = new AmenityFinder(dbh);

                PriorityQueue<RelativeBuilding> buildingsPQ = amenityFinder.getNearbyBuildings(params[0]);
                buildings = new ArrayList<RelativeBuilding>();
                map = new ArrayMap<RelativeBuilding, List<Integer>>();

                List<Integer> floors;
                while (!buildingsPQ.isEmpty()) {
                    RelativeBuilding rb = buildingsPQ.poll();
                    buildings.add(rb);
                    floors = amenityFinder.getFloorsInBuilding(rb.getBuilding());
                    map.put(rb, floors);
                }

                LinearLayout showing = (LinearLayout) findViewById(R.id.showingLayout);
                Button showingButton = (Button) findViewById(R.id.selectedButton);
                showingButton.setText("All " + name); //it'll need a little arrow buddy
                showing.setVisibility(View.VISIBLE);

                Log.v("button", type + "clicked");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showEFLA(buildings, map);
        }
    }

}
