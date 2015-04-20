package edu.gatech.cs4911.mintyfresh;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.Collection;
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


public class HomeScreenActivity extends ActionBarActivity {


//    http://theopentutorials.com/tutorials/android/listview/android-expandable-list-view-example/
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ExpandableListView expListView;
    protected AmenityFinder amenityFinder;
    private enum elvType {NONE, VENDING, BATHROOMS, PRINTERS};
    private elvType current = elvType.NONE;
    private ImageButton bathroom, vending, printing;
    public static boolean[] checkSelected;
    private boolean expanded;
    private PopupWindow pw;
    private Location cl;
    protected Map<Building, List<Integer>> initialBathroom, initialVending, initialPrinter;
    protected ArrayList<Building> initialBuildings;
    private ArrayList<Amenity> nearbyAmenities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        Bundle extras = getIntent().getExtras();
        initialBathroom = (Map<Building, List<Integer>>)extras.get("bathroomMap");
        initialVending = (Map<Building, List<Integer>>)extras.get("vendingMap");
        initialPrinter = (Map<Building, List<Integer>>)extras.get("printerMap");
        initialBuildings = (ArrayList<Building>)extras.get("buildings");

        expListView = (ExpandableListView) findViewById(R.id.buildingList);
        expListView.setVisibility(View.GONE);

        bathroom = (ImageButton) findViewById(R.id.bathroomButton);
        final LinearLayout showing = (LinearLayout) findViewById(R.id.showingLayout);
        bathroom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == elvType.BATHROOMS) {
                    current = elvType.NONE;
                    expListView.setVisibility(View.GONE);
                    showing.setVisibility(View.GONE);
                }
                else {
                    expandMenu(elvType.BATHROOMS);
                    bathroom.setImageResource(R.drawable.button_toilet_active);
                    vending.setImageResource(R.drawable.button_vending_inactive);
                    printing.setImageResource(R.drawable.button_printer_inactive);
                }
            }
        });
        vending = (ImageButton) findViewById(R.id.vendingButton);
        vending.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == elvType.VENDING) {
                    current = elvType.NONE;
                    expListView.setVisibility(View.GONE);
                    showing.setVisibility(View.GONE);
                }
                else {
                    expandMenu(elvType.VENDING);
                    bathroom.setImageResource(R.drawable.button_toilet_inactive);
                    vending.setImageResource(R.drawable.button_vending_active);
                    printing.setImageResource(R.drawable.button_printer_inactive);
                }
            }
        });
        printing = (ImageButton) findViewById(R.id.printerButton);
        printing.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current == elvType.PRINTERS) {
                    current = elvType.NONE;
                    expListView.setVisibility(View.GONE);
                    showing.setVisibility(View.GONE);
                } else {
                    expandMenu(elvType.PRINTERS);
                    bathroom.setImageResource(R.drawable.button_toilet_inactive);
                    vending.setImageResource(R.drawable.button_vending_inactive);
                    printing.setImageResource(R.drawable.button_printer_active);
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

    private void expandMenu(elvType type) {
        Log.v("sigh", "yeah");

        cl = new Location("TEST_PROVIDER");
        cl.setLatitude(33.7751878);
        cl.setLongitude(-84.39687341);

        new ConnectToDB(type).execute(cl, "");
    }

    protected void showEFLA(ArrayList<Building> buildings, Map<Building, List<Integer>> map, elvType curType, Map<String, String> spinnerContents, final boolean refreshSpinner) {
        LinearLayout showing = (LinearLayout) findViewById(R.id.showingLayout);
        if ((refreshSpinner) || (current != curType) || (current == elvType.NONE)) {
            current = curType;
            Collection<String> idColl = spinnerContents.keySet(); //todo WHY ARE YOU BROKEN
            Collection<String> coll = spinnerContents.values();
            List list;
            List idList;
            if (coll instanceof List) {
                list = (List) coll;
                idList = (List) idColl;
            } else {
                list = new ArrayList(coll);
                idList = new ArrayList(idColl);
            }
            final List finalList = list;
            final List finalIDList = idList;
            checkSelected = new boolean[list.size()];
            for (int i = 0; i < checkSelected.length; i++) {
                checkSelected[i] = false;
            }

            final TextView tv = (TextView) findViewById(R.id.dropDownList_SelectBox);
            OnClickListener ocl = new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!expanded) {
                        //display all selected values
                        String selected = "";
                        int flag = 0;
                        for (int i = 0; i < finalList.size(); i++) {
                            if (checkSelected[i] == true) {
                                selected += finalList.get(i);
                                selected += ", ";
                                flag = 1;
                            }
                        }
                        if (flag == 1)
                            tv.setText(selected);
                        expanded = true;
                    } else {
                        //display shortened representation of selected values
                        tv.setText(DropDownListAdapter.getSelected());
                        expanded = false;
                    }
                }
            };

            tv.setOnClickListener(ocl);

            //onClickListener to initiate the dropDown list
            Button createButton = (Button) findViewById(R.id.dropDownList_create);
            createButton.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    initiatePopUp((ArrayList) finalList, (ArrayList) finalIDList, tv);
                }
            });

            Log.v("showefla", "hopefully set the array adapter?");
            showing.setVisibility(View.VISIBLE);

//            final ExpandableFloorListAdapter expListAdapter = new ExpandableFloorListAdapter(
//                    this, buildings, map);
//            expListView.setAdapter(expListAdapter);
                final ExpandableFloorListAdapter expListAdapter = new ExpandableFloorListAdapter(
                        this, buildings, map, nearbyAmenities);
                expListView.setAdapter(expListAdapter);

                expListView.setVisibility(View.VISIBLE);
                Log.v("SHOWEFLA", "DONE?");
            }
        else {
            current = elvType.NONE;

            Log.v("SHOWEFLA", "none?");
            showing.setVisibility(View.INVISIBLE);
            showing.setVisibility(View.GONE);
            expListView.setVisibility(View.GONE);
        }
    }

    private String refreshList(ArrayList<String> items) {
        //todo what i need to do is make this return a list of the attributes
        //and make efla whatever take in that list
        //and when the list is null or empty, do what it does now
        //but if it isn't, it needs to consider the attribute list
        String attributes = "";
        Log.v("refresh list", ""+ checkSelected);
        Log.v("refresh list", "" + checkSelected.length);
        for (int i = 0; i<checkSelected.length; i++) {
            if (checkSelected[i]) {
                Log.v("refresh list", items.get(i));
                attributes += " " + items.get(i);
            }
        }
        return attributes;
    }

    /*
    * Function to set up the pop-up window which acts as drop-down list
    * */
    private void initiatePopUp(ArrayList<String> items, ArrayList<String> itemIDs, TextView tv){
        final ArrayList<String> finalItemIDs = itemIDs;
        LayoutInflater inflater = (LayoutInflater)HomeScreenActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
        LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.pop_up_window, (ViewGroup)findViewById(R.id.PopUpView));

        //get the view to which drop-down layout is to be anchored
        RelativeLayout layout1 = (RelativeLayout)findViewById(R.id.relativeLayout1);
        pw = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setTouchable(true);

        //let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
        pw.setOutsideTouchable(true);
        pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
        pw.setTouchInterceptor(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                Log.v("ontouch","i guess i clicked something");
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    Log.v("onTouch", "should be dismissed");


                    pw.dismiss();
                    return true;
                }
                else {
                    Log.v("ontouch", "sigh more");
                }
                return false;
            }
        });

        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                Log.v("ondismiss", "dismissing");
                String atts = refreshList(finalItemIDs);
//                    showEFLA(buildings, map, curType, spinnerContents, refreshSpinner);

                new ConnectToDB(current).execute(cl, atts);
            }
        });

        //provide the source layout for drop-down
        pw.setContentView(layout);

        //anchor the drop-down to bottom-left corner of 'layout1'
        pw.showAsDropDown(layout1);

        //populate the drop-down list
        final ListView list = (ListView) layout.findViewById(R.id.dropDownList_dropDownList);
        DropDownListAdapter adapter = new DropDownListAdapter(this, items, itemIDs, tv);
        list.setAdapter(adapter);
    }

    private class ConnectToDB extends AsyncTask <Object, elvType, Void> {

        private elvType curElvType;
//        private int type;
        private String name;
        private ArrayList<Building> buildings;
        private Map<Building, List<Integer>> buildingToFloorMap;
        private Map<String, String> spinnerContents;
        private String atts;

        public ConnectToDB(elvType type) {
//            this.type = type;
            //showing button-name
            Log.v("ctdb", "UGH PLEASE");
            if (type == elvType.BATHROOMS) {
                name = "Bathroom";
                curElvType = elvType.BATHROOMS;

            }
            else if (type == elvType.VENDING) {
                name = "Vending";
                curElvType = elvType.VENDING;
            }
            else {
                name = "Printer";
                curElvType = elvType.PRINTERS;
            }
        }

        private Map<Building, List<Integer>> constructMap(PriorityQueue<RelativeAmenity> amenitiesPQ) {
            buildings = new ArrayList<Building>();
            nearbyAmenities = new ArrayList<Amenity>();
            Map <Building, List<Integer>> floorMap = new HashMap<Building, List<Integer>>();
            List<Integer> floors;

            Log.v("constructMap", ""+amenitiesPQ.size());
            while (!amenitiesPQ.isEmpty()) {
                Log.v("loop forever", "looploop");
                RelativeAmenity ra = amenitiesPQ.poll();
                nearbyAmenities.add(ra.getAmenity());

                String bID = ra.getAmenity().getBuildingId();

                try {
                    Building curBldg = amenityFinder.getBuildingById(bID);
                    int floor = ra.getAmenity().getLevel();
//                    Log.v("construct1", buildings.toString());
//                    Log.v("construct2", curBldg.toString());
                    Building bldg = doesContain(buildings, curBldg);
                    if (bldg == null) {
                        Log.v("constructMap", "adding to buildings");
                        buildings.add(curBldg);
                        floors = new ArrayList<Integer>();
                        floors.add(floor);
                        floorMap.put(curBldg, floors);
                    } else {
                        floors = floorMap.get(bldg);
//                        Log.v("floor", ((Integer)floor).toString());
//                        Log.v("floors", floors.toString());

                        if (!floors.contains(floor)) {
                            floors.add(floor);
                        }
                        floorMap.put(curBldg, floors);
                    }
                }
                catch (NoDbResultException ndbre) {
                    Log.v("ndbre", "BOOM");
                    ndbre.printStackTrace();
                    return null;
                }
            }
            Log.v("constructMap", "" + floorMap.size());
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

//        protected Void refreshVisibleList(Location l) {
//          todo i need to set this up so it basically calls the showEFLA on a modified list but doesn't hide shit.
//        }

        @Override
        protected Void doInBackground(Object... params) {
            DBHandler dbh;

            try {
                atts = (String)params[1];
                dbh = new DBHandler(STEAKSCORP_READ_ONLY);
                amenityFinder = new AmenityFinder(dbh);
                PriorityQueue<RelativeAmenity> amenitiesPQ;
                Log.v("dib", (String)params[1]);
//                if ((atts == null)||(atts == "")) {
////                    amenitiesPQ = amenityFinder.getNearbyAmenitiesByType((Location)params[0], name);
//                    if (curElvType == elvType.BATHROOMS) {
////                        initialBathroom = (Map<Building, List<Integer>>)extras.get("bathroomMap");
//                        amenitiesPQ = initialBathroom;
//                    }
//                    else if (curElvType == elvType.VENDING) {
//
//                    }
//                    else {
//
//                    }
//                    Log.v("dib", "nullnull");
//                }
                if ((atts != null) && (atts != "")) {
                    amenitiesPQ = amenityFinder.getNearbyAmenitiesByTypeAndAttribute((Location) params[0], name, (String) atts);
                    Log.v("dib", ""+amenitiesPQ.size());
                    buildingToFloorMap = constructMap(amenitiesPQ);
                }
                else if (curElvType == elvType.BATHROOMS) {
                    buildingToFloorMap = initialBathroom;
                    Log.v("new stuff", "bathroom");
                }
                else if (curElvType == elvType.VENDING) {
                    buildingToFloorMap = initialVending;
                }
                else {
                    buildingToFloorMap = initialPrinter;
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

//                Log.v("button", type + "clicked");

            } catch (Exception e) {
                Log.e("doInBackground", "FUCK");
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (atts == "") {
                //buildings is null.
                if (buildings == null) {
                    showEFLA(initialBuildings, buildingToFloorMap, curElvType, spinnerContents, true);
                }
                else {
                    showEFLA(buildings, buildingToFloorMap, curElvType, spinnerContents, true);
                }
            }
            else {
                showEFLA(buildings, buildingToFloorMap, curElvType, spinnerContents, true);
            }
        }
    }

}
