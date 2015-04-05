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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
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
    LinearLayout layout;

    TextView bldgAndFloor;

    SVGImageView testView;

    ImageSwitcher imgSwitcher;

    private ImageButton leftButton, rightButton;
    private String buildingName;
    Integer currentFloor;
    SVGImageView currView;
    String[] floorInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_floorplan);
        layout = (LinearLayout) findViewById(R.id.floorSwitch);

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
        currentFloor = floorID;

        floorInfo = new String[2];
        floorInfo[0] = bldID;
        floorInfo[1] = Integer.toString(floorID);

        Log.v("hello1", "Doing the thing");

        setFloorDisplay(buildingName, currentFloor.toString());

        try {
            handler = new ConnectToDB(floorName).execute(bldID).get();
            Log.v("check1", "Handler set up");
            cache = new ImageCache(handler, getApplicationContext());
            Log.v("check2", "Cache set up");
            floorplanSVG = new ImageUpdaterTask(cache).execute(floorInfo).get();
            Log.v("check3", "SVG begged for");
        }
        catch(Exception e){
            return;
        }

        //Functional test:
        //Got there.
        if(floorplanSVG!=null) {
            Log.v("HUZZAH", "Floorplan isn't null!");
        }

        //For real this time.
        //Set up image switcher with view factory, in/out animation, initial image.
        imgSwitcher = (ImageSwitcher) findViewById(R.id.floorplanSwitcher);
        imgSwitcher.setFactory(this);

        Animation fadeIn = AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this,android.R.anim.fade_out);

        imgSwitcher.setInAnimation(fadeIn);
        imgSwitcher.setOutAnimation(fadeOut);

        currView = (SVGImageView) imgSwitcher.getCurrentView();
        currView.setSVG(floorplanSVG);



    }

    private void setFloorDisplay (String building, String floor) {
        bldgAndFloor = (TextView)findViewById(R.id.buildingAndFloor);
        bldgAndFloor.setText(buildingName + " - Floor " + floor);

    }

    //Called to set up buttons initially.
    protected void doTheButtonThing(List<Integer> floors, Integer floor) {

        final Integer flr = currentFloor;
        final List<Integer> flrList = floors;

        leftButton = (ImageButton)findViewById(R.id.leftButton);
        rightButton = (ImageButton)findViewById(R.id.rightButton);

        if (floors.contains(flr)) {

            //needs to be checked anytime a button is clicked
            checkButtonAble(floors, flr);

            //things that happen when buttons are clicked; re-enable/disable
            if (rightButton.isEnabled()) {
                rightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //switch imageSwitcher
                        final int floorPlusOne = currentFloor + 1;
                        setFloorDisplay(buildingName, ((Integer)floorPlusOne).toString());
                        //Everything after here might be terrible
                        currentFloor = floorPlusOne;
                        floorInfo[1] = currentFloor.toString();
                        try {
                            floorplanSVG = new ImageUpdaterTask(cache).execute(floorInfo).get();
                        }
                        catch(Exception e){
                            return;
                        }
                        currView.setSVG(floorplanSVG);
                        checkButtonAble(flrList, currentFloor);

                    }
                });
            }
            if (leftButton.isEnabled()) {
                leftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //switch imageSwitcher
                        final int floorMinusOne = currentFloor - 1;
                        setFloorDisplay(buildingName, ((Integer)floorMinusOne).toString());
                        //Hereafter lie today's changes
                        currentFloor = floorMinusOne;
                        floorInfo[1] = currentFloor.toString();
                        try {
                            floorplanSVG = new ImageUpdaterTask(cache).execute(floorInfo).get();
                        }
                        catch(Exception e){
                            return;
                        }
                        currView.setSVG(floorplanSVG);
                        checkButtonAble(flrList, currentFloor);

                    }
                });
            }
        }
    }



    //Button support.
    private void checkButtonAble(List<Integer> floors, Integer floor){
        //needs to be checked anytime a button is clicked
        if(floors.contains(floor)) {
            int curIndex = floors.indexOf(floor);
            if (curIndex <= 0) {
                leftButton.setEnabled(false);
            }
            else{leftButton.setEnabled(true);}
            if (curIndex >= floors.size() - 1) {
                rightButton.setEnabled(false);
            }
            else{rightButton.setEnabled(true);}
        }
        else{
            Log.v("Floor not found", "Something went wrong in this list of floors");
        }
    }

    /**
     * Updated to return the DBHandler being created for referencing purposes.
     */
    private class ConnectToDB extends AsyncTask<String, String, DBHandler> {


        List<Integer> floors;
        Integer floor;

        public ConnectToDB(String floor) {
            this.floor = Integer.parseInt(floor);
        }

        @Override
        protected DBHandler doInBackground(String... params) {
            DBHandler dbh;
            try {

                dbh = new DBHandler(STEAKSCORP_READ_ONLY);
                amenityFinder = new AmenityFinder(dbh);

//                amenityFinder.getAmenitiesInBuilding(params[0]);
                floors = amenityFinder.getFloorsInBuilding(params[0]);



                return dbh;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(DBHandler dbh) {
            super.onPostExecute(dbh);

            doTheButtonThing(floors, floor);
        }
    }

    public View makeView() {

        SVGImageView iView = new SVGImageView(getApplicationContext());
        iView.setScaleType(SVGImageView.ScaleType.FIT_CENTER);
        iView.setLayoutParams(new LayoutParams
                (ImageSwitcher.LayoutParams.MATCH_PARENT,
                        ImageSwitcher.LayoutParams.MATCH_PARENT));

        return iView;
    }

    /**
     * floorInfo = {bldgID, floorID} (Strings)
     */
    private class ImageUpdaterTask extends AsyncTask<String, Void, SVG> {
        ImageCache cache;
        public ImageUpdaterTask(ImageCache cache) { this.cache = cache;}
        protected SVG doInBackground(String... params) {
            if (params.length!=2) return null;
            try {
                SVG result = cache.get(params[0], Integer.parseInt(params[1]));
                return result;

            } catch (Exception e) {
                return null;
            }
        }
    }
}
