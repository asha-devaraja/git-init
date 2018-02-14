package com.android_examples.Connect2ControlHome;

/**
 * Created by Asha.Devaraja on 11/23/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Customlistadapter extends ArrayAdapter<RowItem> {

    Context context;
    Context _appContext;
    CustomListViewAdapter instance;
    List<RowItem> _rowItems;
    SystemClass _appInstance = null;
    ArrayList<Integer> groupDeviceList;


    public Customlistadapter(Context context, int resourceId,
                                 List<RowItem> items,
                                 Context appContext,
                                 SystemClass appInstance,
                             ArrayList<Integer> tempDeviceList ) {
        super(context, resourceId, items);
        this.context = context;
        this._appContext = appContext;
        this._rowItems = items;
        this._appInstance = appInstance;
        this.groupDeviceList = tempDeviceList;
    }

    /*private view holder class*/
    private class ViewHolder {
       CheckBox checkbox;
        TextView txtTitle;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_select_devices_group, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.checkbox = (CheckBox) convertView.findViewById(R.id.cbx1);

            convertView.setTag(holder);

        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtTitle.setText(rowItem.getTitle());

        holder.checkbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CheckBox chk=(CheckBox)v; // important line and code work
                devicelistForGroup inst = devicelistForGroup.instance();
                if(chk.isChecked()){
                   // Toast.makeText(getContext(), "checked !! "+position, Toast.LENGTH_SHORT).show();
                    selectDeviceForGroup(position, true);
                    devicelistForGroup inst1 = devicelistForGroup.instance();
                    inst1.deviceSelectedForGroup(position, true);

                }else{

                  //  Toast.makeText(getContext(), "unchecked !! "+position, Toast.LENGTH_SHORT).show();
                    selectDeviceForGroup(position, false);
                    devicelistForGroup inst1 = devicelistForGroup.instance();
                    inst1.deviceSelectedForGroup(position, false);
                }
            }
        });

        Boolean status = false;

        for (int k = 0; k < groupDeviceList.size(); k++) {
            Integer index = groupDeviceList.get(k);
            if(_appInstance.deviceList.get(index).equals(rowItem.getTitle()))
            {
                status = true;
                break;
            }
        }

        holder.checkbox.setChecked(status);

     return convertView;
    }

    public void setInstance(CustomListViewAdapter adapter) {
        instance = adapter;
    }

    public void selectDeviceForGroup(Integer index, Boolean status)
    {
        String deviceName = _appInstance.deviceList.get(index);
       // Toast.makeText(getContext(), index + " asha!!!", Toast.LENGTH_SHORT).show();
        for (int k = 0; k < groupDeviceList.size(); k++) {
            Integer index1 = groupDeviceList.get(k);
            //Toast.makeText(getContext(), index1 + " asha1!!!", Toast.LENGTH_SHORT).show();
            if(_appInstance.deviceList.get(index1).equals(deviceName))
            {
                if( status == true ) {
                    return;
                }
                else
                {
                    groupDeviceList.remove(k);
                  //  Toast.makeText(getContext(), deviceName + " removed!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        groupDeviceList.add(index);
       // Toast.makeText(getContext(), deviceName + " added!!!", Toast.LENGTH_SHORT).show();
    }
}
