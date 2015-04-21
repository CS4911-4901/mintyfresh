package edu.gatech.cs4911.mintyfresh;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.caverock.androidsvg.SVGImageView;
import com.caverock.androidsvg.SVG;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import edu.gatech.cs4911.mintyfresh.db.DBHandler;

import edu.gatech.cs4911.mintyfresh.db.queryresponse.Amenity;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.FloorplanMeta;
import edu.gatech.cs4911.mintyfresh.io.ImageCache;
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
    FloorplanMeta currMeta;
    LinearLayout layout;
    HashMap<Integer,SVG> floorplanMap;
    List<Integer> floorsInBuilding;
    List<Amenity> relevantAmenities;
    HashMap<Integer,FloorplanMeta> metaMap;


    TextView bldgAndFloor;

    ImageSwitcher imgSwitcher;

    private ImageButton leftButton, rightButton, cancelButton, routeButton;
    private String buildingName, bldgID;
    Integer currentFloor;
    SVGImageView currView;
    String[] floorInfo;

    float scaleFactor;

    //Drawing things
    Canvas imageCanvas;
    Bitmap imageBitmap;
    int gtYellow = Color.rgb(235,182,9);


    //For testing.
    int dotR = 5;

    //Used to ensure proper plotting
    int viewHardcodeWidth;
    int imageRawWidth, imageRawHeight;

    //Things experimented with for pinch-zoom
    private ScaleGestureDetector pinchDetector;

    /**
     * Current location.
     */
    private LatLng curLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_floorplan);
        layout = (LinearLayout) findViewById(R.id.floorSwitch);

        Bundle extras = getIntent().getExtras();
        buildingName = "Building in the Sky";
        String floorName = "infinity";
        bldgID = "123";
        Integer floorID = -5;

        if (extras != null) {
            buildingName = extras.getString("BUILDING_NAME");
            floorID = extras.getInt("FLOOR_NAME");
            bldgID = extras.getString("BUILDING_ID");
            floorName = Integer.toString(floorID);
            relevantAmenities = extras.getParcelableArrayList("AMENITIES");
            curLocation = (LatLng) extras.get("current_location");
        }
        currentFloor = floorID;

        //Log.v("floorcheck", ""+currentFloor);
        //Log.v("spacecheck", "0mask0"+bldID+"0mask0");
        //Log.v("amenities null?", ""+relevantAmenities);

        floorInfo = new String[2];
        floorInfo[0] = bldgID;
        floorInfo[1] = floorName;

        floorsInBuilding = new ArrayList<>();
        Log.v("hello1", "Doing the thing");

        setFloorDisplay(buildingName, currentFloor.toString());

        try {
            handler = new ConnectToDB(floorName).execute(bldgID).get();
            Log.v("check1", "Handler set up");
            cache = new ImageCache(handler, getApplicationContext());
            Log.v("check2", "Cache set up");
            floorplanMap = new ImageUpdaterTask(cache).execute(floorInfo).get();
            Log.v("check3", "SVG begged for");
            metaMap = new MetaTask(cache).execute(floorInfo).get();
            Log.v("check4", "Metas popped");
        }
        catch(Exception e){
            return;
        }

        //Functional test:
        //Got there.
        if(floorsInBuilding!=null) {
            //Log.v("HUZZAH", "Floorplan list isn't null!");
        }
        if(floorplanMap==null){
            Log.v("WHY", "FloorplanMap is null?");
        }
        if(metaMap==null){
            Log.v("HOW","MetaMap is null.");
        }
        currMeta = metaMap.get(currentFloor);
        floorplanSVG = floorplanMap.get(currentFloor);
        imageRawWidth = currMeta.getNativeWidth();
        imageRawHeight = currMeta.getNativeHeight();

       // Log.v("Image dim check", "Width: " + imageRawWidth + ", Height: " + imageRawHeight);



        viewHardcodeWidth = 275*2;
        //Log.v("View check", ""+viewHardcodeWidth);
        //screen size*0.65 or so




        //For real this time.
        //Set up image switcher with view factory, in/out animation, initial image.
        imgSwitcher = (ImageSwitcher) findViewById(R.id.floorplanSwitcher);
        imgSwitcher.setFactory(this);
        Animation fadeIn = AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        Animation fadeOut = AnimationUtils.loadAnimation(this,android.R.anim.fade_out);
        imgSwitcher.setInAnimation(fadeIn);
        imgSwitcher.setOutAnimation(fadeOut);

        //Set current View and SVG.
        currView = (SVGImageView) imgSwitcher.getCurrentView();
        //currView.setBackgroundColor(Color.BLUE);
        currView.setScaleType(ImageView.ScaleType.MATRIX);

        //Set raw size.
        floorplanSVG.setDocumentWidth(imageRawWidth);
        floorplanSVG.setDocumentHeight(imageRawHeight);

        //Set to view.
        currView.setSVG(floorplanSVG);

        //Turn into a bitmap.
        plotAmenities();

        //Attempt to scale.
        scaleFloorplan();


    }

    //Plots all amenities on the current imageview.
    private void plotAmenities(){
        int testDotR = 15;
        imageBitmap = drawableToBitmap(currView.getDrawable());
        imageCanvas = new Canvas(imageBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        float strokeWidth = 4f;
        imageCanvas.drawBitmap(imageBitmap, 0, 0, null);
        int amenityX, amenityY;
        for(Amenity a: relevantAmenities){
            if(a.getLevel()==currentFloor && a.getBuildingName().equals(buildingName)){
                amenityX = a.getX();
                amenityY = a.getY();
                //Log.v("Amenity type", ""+a.getType());
                //Drawable starD = getResources().getDrawable(R.drawable.star);
                //Bitmap starBit = drawableToBitmap(starD);
                //imageCanvas.drawBitmap(starBit,amenityX, amenityY, null);
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(gtYellow);
                imageCanvas.drawCircle(amenityX, amenityY, testDotR, paint);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(strokeWidth);
                paint.setARGB(255, 0, 0, 0);
                imageCanvas.drawCircle(amenityX, amenityY, testDotR, paint);

            }
        }

        currView.setImageDrawable(new BitmapDrawable(imageBitmap));

    }

    //Utility for scaling.
    private void scaleFloorplan(){
        /**
        scaleFactor = (float)(viewHardcodeWidth)/(float)(imageRawWidth);
        double scaleFactorSqr = Math.sqrt(((float)(viewHardcodeWidth)/(float)(imageRawWidth)));
        float scaleFactorF = (float)scaleFactorSqr;
         */
        //Time to kludge it up

        float scaleFactorF = 2.0f;


        currView.setScaleType(ImageView.ScaleType.MATRIX);

        Matrix m = currView.getImageMatrix();
        m.setScale(scaleFactorF, scaleFactorF);
        currView.setImageMatrix(m);

        currView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    //Utility for drawing things on top of things shenanigans.
    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private void setFloorDisplay (String building, String floor) {
        bldgAndFloor = (TextView)findViewById(R.id.buildingAndFloor);
        bldgAndFloor.setText(building + " - Floor " + floor);
    }

    //Called to set up buttons initially.
    protected void doTheButtonThing(List<Integer> floors, Integer floor) {

        final Integer flr = currentFloor;
        final List<Integer> flrList = floors;

        leftButton = (ImageButton)findViewById(R.id.leftButton);
        rightButton = (ImageButton)findViewById(R.id.rightButton);

        cancelButton = (ImageButton)findViewById(R.id.cancelButton);
        routeButton = (ImageButton)findViewById(R.id.routeMeButton);

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
                            Log.v("onClickR", "Hit the right button");
                            floorplanSVG = floorplanMap.get(currentFloor);
                            currMeta = metaMap.get(currentFloor);
                            imageRawWidth = currMeta.getNativeWidth();
                            imageRawHeight = currMeta.getNativeHeight();
                            floorplanSVG.setDocumentWidth(imageRawWidth);
                            floorplanSVG.setDocumentHeight(imageRawHeight);
                            currView.setSVG(floorplanSVG);
                        }
                        catch(Exception e){
                            return;
                        }
                        //Reset floorplan, plot
                        plotAmenities();
                        scaleFloorplan();
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
                            Log.v("onClickL", "Hit the left button");
                            floorplanSVG = floorplanMap.get(currentFloor);
                            currMeta = metaMap.get(currentFloor);
                            imageRawWidth = currMeta.getNativeWidth();
                            imageRawHeight = currMeta.getNativeHeight();
                            floorplanSVG.setDocumentWidth(imageRawWidth);
                            floorplanSVG.setDocumentHeight(imageRawHeight);
                            currView.setSVG(floorplanSVG);
                        }
                        catch(Exception e){
                            Log.v("onClickL", "Welp, that didn't work");
                            return;
                        }
                        //Reset floorplan, plot
                        plotAmenities();
                        scaleFloorplan();
                        checkButtonAble(flrList, currentFloor);

                    }
                });
            }
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        routeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("routing button", "CLICK");
                startRouting();
            }
        });

    }

    private void startRouting() {
        Intent intent = new Intent(this, RouteActivity.class);
        intent.putExtra("building_id", bldgID);
        intent.putExtra("current_location", curLocation);
        this.startActivity(intent);
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
              
                floors = amenityFinder.getFloorsInBuilding(params[0]);
                for (Integer i : floors) {
                    floorsInBuilding.add(i);
                }
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
        iView.setScaleType(SVGImageView.ScaleType.MATRIX);
        iView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        iView.setAdjustViewBounds(true);

        return iView;
    }

    /**
     * floorInfo = {bldgID, floorID} (Strings)
     */
    private class ImageUpdaterTask extends AsyncTask<String, Void, HashMap<Integer,SVG>> {
        ImageCache cache;
        public ImageUpdaterTask(ImageCache cache) { this.cache = cache;}
        protected HashMap<Integer,SVG> doInBackground(String... params) {
            if (params.length!=2) {
                Log.v("You done goofed","Params length is wrong");
                return null;
            }
            try {
                HashMap<Integer,SVG> resultMap = new HashMap<>();
                for(Integer f : floorsInBuilding){
                    resultMap.put(f, cache.get(params[0], f));
                }
                return resultMap;

            } catch (Exception e) {
                Log.v("GOOFED", e.toString());
                return null;
            }
        }
    }

    //Populate the floorplan metas.
    private class MetaTask extends AsyncTask<String, Void, HashMap<Integer,FloorplanMeta>> {
        ImageCache cache;
        public MetaTask(ImageCache cache) { this.cache = cache;}
        protected HashMap<Integer,FloorplanMeta> doInBackground(String... params) {
            if (params.length!=2) {
                Log.v("You done goofed in meta","Params length is wrong");
                return null;
            }
            try {
                HashMap<Integer,FloorplanMeta> resultMap = new HashMap<>();
                FloorplanMeta tempMeta;
                for(Integer f : floorsInBuilding){
                    tempMeta = cache.getMeta(params[0], f);
                    if(tempMeta==null){
                        Log.v("Dude, why", "getMeta is returning null");
                    }
                    else {
                        resultMap.put(f, tempMeta);
                    }
                }
                return resultMap;

            } catch (Exception e) {
                Log.v("GOOFED IN META", e.toString());
                return null;
            }
        }
    }
}