package edu.gatech.cs4911.mintyfresh;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import edu.gatech.cs4911.mintyfresh.router.RelativeBuilding;


/**
 * Created by kateharlan on 2/23/15.
 */
public class ExpandableFloorListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<RelativeBuilding, List<Integer>> floors;
    private List<RelativeBuilding> buildings;

    public ExpandableFloorListAdapter(Activity context, List<RelativeBuilding> buildings,
                                      Map<RelativeBuilding, List<Integer>> floors) {
        this.context = context;
        this.floors = floors;
        this.buildings = buildings;
    }
    @Override
    //this needs to get a specific floor, i think
    public Object getChild(int groupPosition, int childPosition) {
        return floors.get(buildings.get(groupPosition)); //.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
//        String building = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.floor_picker_item, null);
        }

        LinearLayout floorsLayout = (LinearLayout) convertView.findViewById(R.id.floorsLayout);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1; //floors.get(buildings.get(groupPosition)).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return buildings.get(groupPosition).getBuilding().getName();
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
        return true;
    }
}
