package edu.gatech.cs4911.mintyfresh;

import android.app.Activity;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.location.Location;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import android.widget.ViewSwitcher.ViewFactory;

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

import edu.gatech.cs4911.mintyfresh.db.queryresponse.Building;
import edu.gatech.cs4911.mintyfresh.io.ImageCache;
import edu.gatech.cs4911.mintyfresh.router.RelativeBuilding;

import static android.widget.FrameLayout.LayoutParams;
import static edu.gatech.cs4911.mintyfresh.db.DatabaseConfig.STEAKSCORP_READ_ONLY;


/**
 * Created by kateharlan on 3/24/15.
 */
public class ViewFloorplanActivity extends Activity implements ViewFactory{

    protected AmenityFinder amenityFinder;

    DBHandler handler;
    ImageCache cache;
    SVG floorplanSVG;
    LinearLayout floorSwitch;
    SVGImageView willItShowUp;


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
        Integer floorID = -5;

        if (extras != null) {
            buildingName = extras.getString("BUILDING_NAME");
            floorName = extras.getString("FLOOR_NAME");
            bldID = extras.getString("BUILDING_ID");
            floorID = extras.getInt("FLOOR_ID");
        }


        TextView bldgAndFloor = (TextView) findViewById(R.id.buildingAndFloor);
        //TextView amenityReminder = (TextView) findViewById(R.id.amenityReminder);
        //ImageSwitcher imgSwitch = (ImageSwitcher) findViewById(R.id.floorplan);

        //Setting up the image switcher; I think these are required
        /*
        imgSwitch.setFactory(this);
        imgSwitch.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in));
        imgSwitch.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out));
        */

        TextView amenityReminder = (TextView)findViewById(R.id.amenityReminder);
        ImageSwitcher imgSwitch = (ImageSwitcher)findViewById(R.id.floorplan);
        //imgSwitch.setImageResource(R.drawable.ic_launcher);
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

            /**
             * High-level cobbling things together here.
             *
             * Things that need to happen:
             * + Figure out how to get the building from the proper building and floor from the db
             * + Get that as an SVG
             * + Turn the SVG into a Drawable
             * + Display the Drawable (give it to the imageswitcher)
             *
             * + Define some animations for when the image is switched
             * --> (How is swipe input represented here?)
             * + Hook those up
             */

            /**
             *
             * static database config:
             *  new DBHandler(STEAKSCORP_READ_ONLY)
             *
             * method I need:
             * ImageCache.get(buildingId, level)
             * */

/*             Drawable drawable = new PictureDrawable(floorplanSVG.renderToPicture());
             imgSwitch.setImageDrawable(drawable);
*/
            /**
             * Let's see if anything happens at all.
             * */

            //floorSwitch = (LinearLayout) findViewById(R.id.floorSwitch);
            //willItShowUp = new SVGImageView(this);
            //floorSwitch.addView(willItShowUp);
            //setContentView(floorSwitch);

        /** Reminder of what was happening:
         * addView - the view to be added has to be a child view. Figure that out.
         * cache thing needs to not be null
         * */

//        new ConnectToDB().execute();

        setFloorDisplay(buildingName, floorName);

        new ConnectToDB(floorName).execute(bldID);
        cache = new ImageCache(handler, getApplicationContext());
        bldgAndFloor.setText(bldID + " - Floorest " + floorID);

       /*
          try {
            handler = new NetIoTask().execute("").get();
            bldgAndFloor.setText(buildingName + " - Floorer " + floorName);

            cache = new ImageCache(handler, getApplicationContext());
            new CacheUpdaterTask().execute(cache);
            bldgAndFloor.setText(bldID + " - Floorest " + floorID);

            floorplanSVG = cache.get(bldID, floorID);
            if (floorplanSVG!=null) bldgAndFloor.setText(buildingName+ " - Flooresti " + floorName);
        }
        catch (Exception e) {
            return;
        }
         */



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

    public View makeView() {

        SVGImageView iView = new SVGImageView(getApplicationContext());
        iView.setScaleType(SVGImageView.ScaleType.FIT_CENTER);
        iView.setLayoutParams(new LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));

        return iView;
    }


    private class NetIoTask extends AsyncTask<String, Void, DBHandler> {
        protected DBHandler doInBackground(String... handler) {
            try {
                return new DBHandler(STEAKSCORP_READ_ONLY);
            } catch (Exception e) {
                return null;
            }
        }
    }

    private class CacheUpdaterTask extends AsyncTask<ImageCache, Void, String> {
        protected String doInBackground(ImageCache... cache) {
            try {
                //cache[0].update();
            } catch (Exception e) {
                return e.toString();
            } return "SUCCESS";
        }
    }
}
