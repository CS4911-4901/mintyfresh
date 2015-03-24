package edu.gatech.cs4911.mintyfresh;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by kateharlan on 3/24/15.
 */
public class ViewFloorplanActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_floorplan);


        Bundle extras = getIntent().getExtras();
        String bldgName = "Building in the Sky";
        String floorName = "infinity";
        if (extras != null) {
            bldgName = extras.getString("BUILDING_NAME");
            floorName = extras.getString("FLOOR_NAME");
        }

        TextView bldgAndFloor = (TextView)findViewById(R.id.buildingAndFloor);
        TextView amenityReminder = (TextView)findViewById(R.id.amenityReminder);
        bldgAndFloor.setText(bldgName + " - Floor " + floorName);
        Log.v("hello1", "Doing the thing");

    }

}
