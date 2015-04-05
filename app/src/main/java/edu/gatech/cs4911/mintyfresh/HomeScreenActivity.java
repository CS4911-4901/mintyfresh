package edu.gatech.cs4911.mintyfresh;

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
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.router.RelativeBuilding;

import static edu.gatech.cs4911.mintyfresh.db.DatabaseConfig.STEAKSCORP_READ_ONLY;


public class HomeScreenActivity extends ActionBarActivity {


//    http://theopentutorials.com/tutorials/android/listview/android-expandable-list-view-example/
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ExpandableListView expListView;
    protected AmenityFinder amenityFinder;
    private enum elvType {NONE, VENDING, BATHROOMS, PRINTERS};
    private elvType current = elvType.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        expListView = (ExpandableListView) findViewById(R.id.buildingList);
        expListView.setVisibility(View.GONE);

        ImageButton bathroom = (ImageButton) findViewById(R.id.bathroomButton);
        bathroom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == elvType.BATHROOMS) {
                    current = elvType.NONE;
                    expListView.setVisibility(View.GONE);
                }
                else {
                    expandMenu(0);
                }
            }
        });
        ImageButton vending = (ImageButton) findViewById(R.id.vendingButton);
        vending.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == elvType.VENDING) {
                    current = elvType.NONE;
                    expListView.setVisibility(View.GONE);
                }
                else {
                    expandMenu(1);
                }
            }
        });
        ImageButton printing = (ImageButton) findViewById(R.id.printerButton);
        printing.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == elvType.PRINTERS) {
                    current = elvType.NONE;
                    expListView.setVisibility(View.GONE);
                }
                else {
                    expandMenu(2);
                }
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
        Log.v("sigh", "yeah");

        Location curLocation = new Location("TEST_PROVIDER");
        curLocation.setLatitude(33.7751878);
        curLocation.setLongitude(-84.39687341);

        new ConnectToDB(type).execute(curLocation);
    }

    protected void showEFLA(ArrayList<RelativeBuilding> buildings, Map<RelativeBuilding, List<Integer>> map, elvType curType, Map<String, String> spinnerContents) {


        if ((current != curType) || (current == elvType.NONE)) {
            current = curType;
            LinearLayout showing = (LinearLayout) findViewById(R.id.showingLayout);
            Spinner showingSpinner = (Spinner) findViewById(R.id.selectedSpinner);
            ArrayAdapter<CharSequence> adapter;
            Collection<String> coll = spinnerContents.values();
            List list;
            if (coll instanceof List)
                list = (List)coll;
            else
                list = new ArrayList(coll);
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
            //bathroom or printer or vending
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            showingSpinner.setAdapter(adapter);
            Log.v("showefla", "hopefully set the array adapter?");


            showing.setVisibility(View.VISIBLE);

            final ExpandableFloorListAdapter expListAdapter = new ExpandableFloorListAdapter(
                    this, buildings, map);
            expListView.setAdapter(expListAdapter);

            expListView.setVisibility(View.VISIBLE);
            Log.v("SHOWEFLA", "DONE?");
        }
        else {
            current = elvType.NONE;
            expListView.setVisibility(View.GONE);
        }
    }


    private class ConnectToDB extends AsyncTask <Location, Integer, Void> {

//        private String name;
        private elvType curElvType;
        private int type;
        private ArrayList<RelativeBuilding> buildings;
        private Map<RelativeBuilding, List<Integer>> buildingToFloorMap;
        private Map<String, String> spinnerContents;

        public ConnectToDB(int type) {
            this.type = type;
            //showing button-name
            if (type == 0) {
//                name = "Bathrooms";
                curElvType = elvType.BATHROOMS;

            }
            else if (type == 1) {
//                name = "Vending";
                curElvType = elvType.VENDING;
            }
            else {
//                name = "Printers";
                curElvType = elvType.PRINTERS;
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
                buildingToFloorMap = new ArrayMap<RelativeBuilding, List<Integer>>();

                List<Integer> floors = new ArrayList<Integer>();

                while (!buildingsPQ.isEmpty()) {
                    RelativeBuilding rb = buildingsPQ.poll();

                    buildings.add(rb);
                    floors = amenityFinder.getFloorsInBuilding(rb.getBuilding().getId());
                    buildingToFloorMap.put(rb, floors);
                }

                if (curElvType == elvType.BATHROOMS) {

                    spinnerContents  = amenityFinder.getDistinctAttributesByType("bathroom");
                }
                else if (curElvType == elvType.VENDING) {
                    spinnerContents = amenityFinder.getDistinctAttributesByType("vending");
                }
                else {
                    spinnerContents = amenityFinder.getDistinctAttributesByType("printer");
                }

                Log.v("button", type + "clicked");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showEFLA(buildings, buildingToFloorMap, curElvType, spinnerContents);
        }
    }

}
