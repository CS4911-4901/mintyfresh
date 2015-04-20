package edu.gatech.cs4911.mintyfresh;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.Amenity;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.Building;
import edu.gatech.cs4911.mintyfresh.exception.NoDbResultException;
import edu.gatech.cs4911.mintyfresh.router.RelativeAmenity;

import static edu.gatech.cs4911.mintyfresh.db.DatabaseConfig.STEAKSCORP_READ_ONLY;


public class MainActivity extends ActionBarActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private Location cl;
    private ArrayList<Amenity> nearbyAmenities, nearbyAmenitiesB, nearbyAmenitiesP, nearbyAmenitiesV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
//                Intent i = new Intent(MainActivity.this, HomeScreenActivity.class);
//                startActivity(i);

                // close this activity
//                finish();
                cl = new Location("TEST_PROVIDER");
                cl.setLatitude(33.7751878);
                cl.setLongitude(-84.39687341);
                new ConnectToDB().execute(cl);
            } //todo i need to make it turn this off at a time so yeah
        }, SPLASH_TIME_OUT);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private class ConnectToDB extends AsyncTask<Object, Void, Void> {

        private ArrayList<Building> buildings, buildingsB, buildingsV, buildingsP;
        private Map<Building, List<Integer>> buildingToFloorMapBathroom, buildingToFloorMapVending, buildingToFloorMapPrinter;
//        private Map<String, String> spinnerContents;
        AmenityFinder amenityFinder;

        public ConnectToDB() {
            DBHandler dbh;

            try {
                dbh = new DBHandler(STEAKSCORP_READ_ONLY);
                amenityFinder = new AmenityFinder(dbh);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private Map<Building, List<Integer>> constructMap(PriorityQueue<RelativeAmenity> amenitiesPQ) {
            buildings = new ArrayList<Building>();
            //todo buildings each time
            nearbyAmenities = new ArrayList<Amenity>();
            Map <Building, List<Integer>> floorMap = new HashMap<Building, List<Integer>>();
            List<Integer> floors;

//            Log.v("constructMap", ""+amenitiesPQ.size());
            while (!amenitiesPQ.isEmpty()) {
//                Log.v("loop forever", "looploop");
                RelativeAmenity ra = amenitiesPQ.poll();
                nearbyAmenities.add(ra.getAmenity());

                String bID = ra.getAmenity().getBuildingId();

                try {
                    Building curBldg = amenityFinder.getBuildingById(bID);
                    int floor = ra.getAmenity().getLevel();
                    Building bldg = doesContain(buildings, curBldg);
                    if (bldg == null) {
                        buildings.add(curBldg);
                        floors = new ArrayList<Integer>();
                        floors.add(floor);
                        floorMap.put(curBldg, floors);
                    } else {
                        floors = floorMap.get(bldg);
                        if (!floors.contains(floor)) {
                            floors.add(floor);
                        }
                        floorMap.put(curBldg, floors);
                    }
                }
                catch (NoDbResultException ndbre) {
                    ndbre.printStackTrace();
                    return null;
                }
            }
            return floorMap;
        }

        private Building doesContain(ArrayList<Building> list, Building b) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId().equals(b.getId())) {
                    return list.get(i);
                }
            }
            return null;
        }

        @Override
        protected Void doInBackground(Object... params) {
            DBHandler dbh;

            try {
                dbh = new DBHandler(STEAKSCORP_READ_ONLY);
                amenityFinder = new AmenityFinder(dbh);
                PriorityQueue<RelativeAmenity> amenitiesPQBathroom, amenitiesPQVending, amenitiesPQPrinter;
                amenitiesPQBathroom = amenityFinder.getNearbyAmenitiesByType((Location)params[0], "Bathroom");
                amenitiesPQVending = amenityFinder.getNearbyAmenitiesByType((Location)params[0], "Vending");
                amenitiesPQPrinter = amenityFinder.getNearbyAmenitiesByType((Location)params[0], "Printer");
                buildingToFloorMapBathroom = constructMap(amenitiesPQBathroom);
                nearbyAmenitiesB = nearbyAmenities;
                buildingsB = buildings;
                buildingToFloorMapVending = constructMap(amenitiesPQVending);
                nearbyAmenitiesV = nearbyAmenities;
                buildingsV = buildings;
                buildingToFloorMapPrinter = constructMap(amenitiesPQPrinter);
                nearbyAmenitiesP = nearbyAmenities;
                buildingsP = buildings;


            } catch (Exception e) {
//                Log.e("doInBackground", "FUCK");
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //todo close the splash screen?

            Intent i = new Intent(MainActivity.this, HomeScreenActivity.class);

            i.putExtra("bathroomMap", (Serializable)buildingToFloorMapBathroom);
            i.putExtra("vendingMap", (Serializable)buildingToFloorMapVending);
            i.putExtra("printerMap", (Serializable)buildingToFloorMapPrinter);
            i.putExtra("buildingsB", buildingsB);
            i.putExtra("buildingsP", buildingsP);
            i.putExtra("buildingsV", buildingsV);
            i.putExtra("nearbyAmenitiesB", (Serializable)nearbyAmenitiesB);
            i.putExtra("nearbyAmenitiesP", (Serializable)nearbyAmenitiesP);
            i.putExtra("nearbyAmenitiesV", (Serializable)nearbyAmenitiesV);
            startActivity(i);

        }
    }

}
