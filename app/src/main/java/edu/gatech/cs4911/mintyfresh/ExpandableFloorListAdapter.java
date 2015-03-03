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

import java.util.List;
import java.util.Map;

import edu.gatech.cs4911.mintyfresh.db.queryresponse.Building;


/**
 * Created by kateharlan on 2/23/15.
 */
public class ExpandableFloorListAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> floors;
    private List<String> buildings;

    public ExpandableFloorListAdapter(Activity context, List<String> buildings,
                                 Map<String, List<String>> floors) {
        this.context = context;
        this.floors = floors;
        this.buildings = buildings;
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return floors.get(buildings.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        String building = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.floor_picker_item, null);
        }

        LinearLayout floorsLayout = (LinearLayout) convertView.findViewById(R.id.floorsLayout);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public String getGroup(int groupPosition) {
        return buildings.get(groupPosition);
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
