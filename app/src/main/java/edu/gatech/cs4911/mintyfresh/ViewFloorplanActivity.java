package edu.gatech.cs4911.mintyfresh;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageSwitcher;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import edu.gatech.cs4911.mintyfresh.db.DBHandler;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.Building;
import edu.gatech.cs4911.mintyfresh.router.RelativeBuilding;

import static edu.gatech.cs4911.mintyfresh.db.DatabaseConfig.STEAKSCORP_READ_ONLY;

/**
 * Created by kateharlan on 3/24/15.
 */
public class ViewFloorplanActivity extends Activity {

    protected AmenityFinder amenityFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_floorplan);


        Bundle extras = getIntent().getExtras();
        String bldgName = "Building in the Sky";
        String floorName = "infinity";
        String bldID = "123";
        if (extras != null) {
            bldgName = extras.getString("BUILDING_NAME");
            floorName = extras.getString("FLOOR_NAME");
            bldID = extras.getString("BUILDING_ID");
        }

        TextView bldgAndFloor = (TextView)findViewById(R.id.buildingAndFloor);
        TextView amenityReminder = (TextView)findViewById(R.id.amenityReminder);
        ImageSwitcher imgSwitch = (ImageSwitcher)findViewById(R.id.floorplan);
//        imgSwitch.setImageDrawable();
        bldgAndFloor.setText(bldgName + " - Floor " + floorName);
        Log.v("hello1", "Doing the thing");

//        new ConnectToDB().execute();


    }


    private class ConnectToDB extends AsyncTask<Integer, Void, Void> {



        public ConnectToDB() {
        }

        @Override
        protected Void doInBackground(Integer... params) {
            DBHandler dbh;

            try {

                dbh = new DBHandler(STEAKSCORP_READ_ONLY);
                amenityFinder = new AmenityFinder(dbh);

//                amenityFinder.getAmenitiesInBuilding();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
