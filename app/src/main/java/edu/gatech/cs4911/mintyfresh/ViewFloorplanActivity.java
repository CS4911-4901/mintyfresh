package edu.gatech.cs4911.mintyfresh;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.caverock.androidsvg.SVGImageView;
import com.caverock.androidsvg.SVG;

import java.util.ArrayList;
import java.util.List;

import edu.gatech.cs4911.mintyfresh.db.DBHandler;

import static edu.gatech.cs4911.mintyfresh.db.DatabaseConfig.STEAKSCORP_READ_ONLY;

/**
 * Created by kateharlan on 3/24/15.
 */
public class ViewFloorplanActivity extends Activity {

    protected AmenityFinder amenityFinder;
    private Button leftButton, rightButton;
    private String buildingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_floorplan);


        Bundle extras = getIntent().getExtras();
        buildingName = "Building in the Sky";
        String floorName = "infinity";
        String bldID = "123";
        //SVG floorplanSVG;

        if (extras != null) {
            buildingName = extras.getString("BUILDING_NAME");
            floorName = extras.getString("FLOOR_NAME");
            bldID = extras.getString("BUILDING_ID");
        }

        TextView amenityReminder = (TextView)findViewById(R.id.amenityReminder);
        ImageSwitcher imgSwitch = (ImageSwitcher)findViewById(R.id.floorplan);
        imgSwitch.setImageResource(R.drawable.ic_launcher);
        imgSwitch.setFactory(new ViewSwitcher.ViewFactory() {
                                 public View makeView() {
                                     ImageView myView = new ImageView(getApplicationContext());
                                     return myView;
                                 }
                             });
//        imgSwitch.setImageDrawable();

        /**
         * High-level cobbling things together here.
         *
         * Things that need to happen:
         * + Figure out how to get the building from the proper building and floor from the db
         * + Get that as an SVG
         * + Turn the SVG into a Drawable
         * + Display the Drawable (give it to the imageswitcher)
         * + Define some animations for when the image is switched
         * --> (How is swipe input represented here?)
         * + Hook those up
         */

        /**
         * floorplanSVG = new ImageUpdaterTask().execute("CUL_1.svg").get();
         * imgSwitch.setSVG(floorplanSVG);
         * Drawable drawable = new PictureDrawable(floorplanSVG.renderToPicture());
         * imgSwitch.setImageDrawable(drawable);
        **/

//        bldgAndFloor.setText(bldgName + " - Floor " + floorName);
        Log.v("hello1", "Doing the thing");

        setFloorDisplay(buildingName, floorName);

        new ConnectToDB(floorName).execute(bldID);


    }

    private void setFloorDisplay (String building, String floor) {
        TextView bldgAndFloor = (TextView)findViewById(R.id.buildingAndFloor);
        bldgAndFloor.setText(buildingName + " - Floor " + floor);

    }

    protected void doTheButtonThing(List<Integer> floors, Integer floor) {

        final Integer flr = floor;

        leftButton = (Button)findViewById(R.id.leftButton);
        rightButton = (Button)findViewById(R.id.rightButton);
        if (floors.contains(floor)) {
            int curIndex = floors.indexOf(floor);
            if (curIndex == 0) {
                leftButton.setEnabled(false);
            }
            if (curIndex == floors.size()-1) {
                rightButton.setEnabled(false);
            }

            if (rightButton.isEnabled()) {
                rightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //switch imageSwitcher
                        final int floorPlusOne = flr + 1;
                        setFloorDisplay(buildingName, ((Integer)floorPlusOne).toString());
                    }
                });
            }
            if (leftButton.isEnabled()) {
                leftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //switch imageSwitcher
                        final int floorMinusOne = flr - 1;
                        setFloorDisplay(buildingName, ((Integer)floorMinusOne).toString());

                    }
                });
            }
        }
    }


    private class ConnectToDB extends AsyncTask<String, String, Void> {


        List<Integer> floors;
        Integer floor;

        public ConnectToDB(String floor) {
            this.floor = Integer.parseInt(floor);
        }

        @Override
        protected Void doInBackground(String... params) {
            DBHandler dbh;

            try {

                dbh = new DBHandler(STEAKSCORP_READ_ONLY);
                amenityFinder = new AmenityFinder(dbh);

//                amenityFinder.getAmenitiesInBuilding(params[0]);
                floors = amenityFinder.getFloorsInBuilding(params[0]);




            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            doTheButtonThing(floors, floor);
        }
    }

}
