package edu.gatech.cs4911.mintyfresh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.gatech.cs4911.mintyfresh.db.queryresponse.Amenity;
import edu.gatech.cs4911.mintyfresh.db.queryresponse.Building;


/**
 * Created by kateharlan on 2/23/15.
 */
public class ExpandableFloorListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<Building, List<Integer>> floors;
    private List<Building> buildings;
    private ArrayList<Amenity> amenities;

    public ExpandableFloorListAdapter(Activity context, List<Building> buildings,
                                      Map<Building, List<Integer>> floors, ArrayList<Amenity> amenities) {
        this.context = context;
        this.floors = floors;
        this.buildings = buildings;
        this.amenities = amenities;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return floors.get(buildings.get(childPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    private View buildChildView(final int groupPosition, View convertView) {
        View a = convertView;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView != null) {
            if (convertView.getId() == groupPosition) {
                return convertView;
            }
        }
        if ((convertView == null)||(convertView.getId() != groupPosition)) {
            convertView = null;
            convertView = inflater.inflate(R.layout.floor_picker_item, null);

            LinearLayout floorsLayout = (LinearLayout) convertView;

            Building b = buildings.get(groupPosition);

            Building x = b;
            boolean complete = false;

            Iterator<Building> it = floors.keySet().iterator();

            while (!complete && it.hasNext()) {
                Building j = it.next();
                if (j.getId().equals(b.getId())) {
                    x = j;
                    complete = true;
                }
            }

            for (int i = 0; i<floors.get(x).size(); i++) {
                Integer floorID = floors.get(x).get(i);
                FloorButton floorButton = new FloorButton(convertView.getContext(), floorID);
                floorButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer i = ((FloorButton)v).getFloorNumber();
                        String bldgName = getGroup(groupPosition);
                        String bldgID = buildings.get(groupPosition).getId();
                        Intent intent = new Intent(context, ViewFloorplanActivity.class);
                        intent.putExtra("BUILDING_NAME", bldgName);
                        intent.putExtra("FLOOR_NAME", ((FloorButton)v).getFloorNumber());
                        intent.putExtra("BUILDING_ID", bldgID);
                        intent.putExtra("FLOOR_ID", i);
                        intent.putParcelableArrayListExtra("AMENITIES", amenities);
                        context.startActivity(intent);

                    }
                });
                LayoutParams lp = new LayoutParams(96, LayoutParams.WRAP_CONTENT);
                floorsLayout.addView(floorButton, lp);
                Log.v("sigh", "yes yes");
            }
            floorsLayout.setId(groupPosition);
            return floorsLayout;
        }

        return convertView;

    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {

        return buildChildView(groupPosition, convertView);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public String getGroup(int groupPosition) {
        return buildings.get(groupPosition).getName();
    }

    @Override
    public int getGroupCount() {
        return buildings.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {



        String buildingName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.building_group_item, null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.buildingName);
        item.setText(buildingName);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public class FloorButton extends ImageButton {

        int floorNumber;

        public FloorButton(Context context, int floorNumber) {
            super(context);
            this.floorNumber = floorNumber;

            int resId = context.getResources().getIdentifier(("button_" + floorNumber), "drawable", context.getPackageName());
            setImageResource(resId);
            setBackgroundColor(Color.TRANSPARENT);
            setPadding(5,5,5,5);
            setMinimumHeight(48);
            setMinimumWidth(48);
        }

        public int getFloorNumber() {
            return floorNumber;
        }

        public void setFloorNumber(int number) {
            floorNumber = number;
        }
    }
}
