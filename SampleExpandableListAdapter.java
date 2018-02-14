package com.android_examples.Connect2ControlHome;

/**
 * Created by Asha.Devaraja on 11/16/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SampleExpandableListAdapter extends BaseExpandableListAdapter implements ExpandableListAdapter {
    public Context context;
    CheckBox checkBox;
    List<RowItem> rowItems;
    private LayoutInflater vi;
    int _objInt;
    public static Boolean checked[] = new Boolean[1];
    SystemClass _appInstance = null;
    HashMap<Long,Boolean> checkboxMap = new HashMap<Long,Boolean>();
    private static final int GROUP_ITEM_RESOURCE = R.layout.group_item;
    private static final int CHILD_ITEM_RESOURCE = R.layout.child_item;
    public String[]check_string_array;

    public SampleExpandableListAdapter(Context context, Activity activity, SystemClass appInstance) {
        this.context = context;
        vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        _objInt = appInstance.GroupList.size();
        _appInstance = appInstance;
        check_string_array = new String[_objInt];
        popolaCheckMap();
    }
    public void popolaCheckMap(){

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String buffer = null;

        for(int i=0; i<_objInt; i++){
            buffer = settings.getString(String.valueOf((int)i),"false");
            if(buffer.equals("false"))
                checkboxMap.put((long)i, false);
            else checkboxMap.put((long)i, true);
        }
    }

    public class CheckListener implements OnCheckedChangeListener {

        long pos;

        public void setPosition(long p){
            pos = p;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            Log.i("checkListenerChanged", String.valueOf(pos)+":"+ String.valueOf(isChecked));
            checkboxMap.put(pos, isChecked);
            if(isChecked == true) check_string_array[(int)pos] = "true";
            else
                check_string_array[(int)pos] = "false";
            // save checkbox state of each group
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor preferencesEditor = settings.edit();
            preferencesEditor.putString(String.valueOf((int)pos), check_string_array[(int)pos]);
            preferencesEditor.commit();
        }
    }
    public String getChild(int groupPosition, int childPosition) {
        ArrayList<Integer> tempDeviceList = new ArrayList<Integer>();
        tempDeviceList = _appInstance.groupDeviceList.get(groupPosition);
        Integer deviceIndex = tempDeviceList.get(childPosition);
        return _appInstance.deviceList.get(deviceIndex);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        ArrayList<Integer> tempDeviceList = new ArrayList<Integer>();
        tempDeviceList = _appInstance.groupDeviceList.get(groupPosition);
        return tempDeviceList.size();
    }

    public View getChildView(int groupPosition,final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;
        final String child = getChild(groupPosition, childPosition);
        if (child != null) {
            v = vi.inflate(CHILD_ITEM_RESOURCE, null);
           final viewHolder holder = new viewHolder(v);
            holder.text.setText(Html.fromHtml(child));
            for(int i =0; i < _appInstance.deviceList.size(); ++i)
            {
                if(_appInstance.deviceList.get(i).equals(Html.fromHtml(child).toString()))
                {
                    if(_appInstance.deviceStatus.get(i).equals("on")) {
                        holder.imageview.setImageResource(R.drawable.icon_on);
                    }
                    else
                    {
                        holder.imageview.setImageResource(R.drawable.icon_off);
                    }
                    break;
                }
            }


         holder.text.setOnClickListener(new View.OnClickListener() {

                       @Override
        public void onClick(View v)
                       {
                            Integer deviceIndex = 0;
                           //Toast.makeText(context, "clicked " + Html.fromHtml(child), Toast.LENGTH_SHORT).show();
                            for (int j1 = 0; j1 < _appInstance.deviceList.size(); j1++)
                            {
                                if(Html.fromHtml(child).toString().equals(_appInstance.deviceList.get(j1)))
                                {
                                    deviceIndex = j1;
                                    Intent myIntent = new Intent(context, EditdeviceActivity.class);
                                    myIntent.putExtra("deviceId", deviceIndex);
                                    context.startActivity(myIntent);
                                }
                            }
                       }
            });

            holder.imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "clicked " + childPosition, Toast.LENGTH_SHORT).show();
                    Log.i(this.getClass().getCanonicalName(), "row clicked " + childPosition);
                    for (int j1 = 0; j1 < _appInstance.deviceList.size(); j1++)
                    {
                        if(Html.fromHtml(child).toString().equals(_appInstance.deviceList.get(j1)))
                        {
                            if(_appInstance.deviceStatus.get(j1).equals("on")) {
                                _appInstance.deviceStatus.set(j1,"off");
                                holder.imageview.setImageResource(R.drawable.icon_on);
                            }
                            else
                            {
                                _appInstance.deviceStatus.set(j1,"on");
                                holder.imageview.setImageResource(R.drawable.icon_off);
                            }
                            MainActivity inst = MainActivity.instance();
                            inst.deviceStatusChanged(j1);
                        }
                    }

                    notifyDataSetChanged();
                }
            });
        }
        return v;
    }
    public String getGroup(int groupPosition) {
        return "group-" + groupPosition;
    }
    public int getGroupCount() {
        return _appInstance.GroupList.size();
    }
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = convertView;
        String group = null;
        int group_id = (int)getGroupId(groupPosition);

         group = _appInstance.GroupList.get(group_id);

        if (group != null) {
            v = vi.inflate(GROUP_ITEM_RESOURCE, null);
            final viewHolder holder = new viewHolder(v);

            holder.text.setText(Html.fromHtml(group));
            _appInstance.GroupStatus.set(groupPosition, "off");
            ArrayList<Integer> groupDeviceList = _appInstance.groupDeviceList.get(groupPosition);
            for (int j1 = 0; j1 < groupDeviceList.size(); j1++)
            {
                if(_appInstance.deviceStatus.get(groupDeviceList.get(j1)).equals("on"))
                {
                    _appInstance.GroupStatus.set(groupPosition, "on");
                }
            }

            if(_appInstance.GroupStatus.get(groupPosition).equals("on")) {
                holder.imageview.setImageResource(R.drawable.icon_on);
            }
            else
            {
                holder.imageview.setImageResource(R.drawable.icon_off);
            }
            holder.imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "clicked " + groupPosition, Toast.LENGTH_SHORT).show();
                    Log.i(this.getClass().getCanonicalName(), "row clicked " + groupPosition );

                    if(_appInstance.GroupStatus.get(groupPosition).equals("on")) {
                        _appInstance.GroupStatus.set(groupPosition, "off");
                        holder.imageview.setImageResource(R.drawable.icon_off);
                        ArrayList<Integer> groupDeviceList = _appInstance.groupDeviceList.get(groupPosition);
                        for (int j1 = 0; j1 < groupDeviceList.size(); j1++)
                        {
                            _appInstance.deviceStatus.set(groupDeviceList.get(j1), "off");
                        }
                    }
                    else
                    {
                        _appInstance.GroupStatus.set(groupPosition, "on");
                        holder.imageview.setImageResource(R.drawable.icon_on);
                        ArrayList<Integer> groupDeviceList = _appInstance.groupDeviceList.get(groupPosition);
                        //Toast.makeText(context, "devices " + groupDeviceList, Toast.LENGTH_SHORT).show();
                        for (int j1 = 0; j1 < groupDeviceList.size(); j1++)
                        {
                            _appInstance.deviceStatus.set(groupDeviceList.get(j1), "on");
                        }
                    }

                    notifyDataSetChanged();
                    MainActivity inst = MainActivity.instance();
                    inst.groupStatusChanged(groupPosition);
                }
            });

            holder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity inst = MainActivity.instance();
                    inst.callDeviceListForGroup(groupPosition);

                }
            });


            CheckListener checkL = new CheckListener();
            checkL.setPosition(group_id);

        }
        return v;
    }
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    public boolean hasStableIds() {
        return true;
    }
}
