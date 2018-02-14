package com.android_examples.Connect2ControlHome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomListViewAdapter extends ArrayAdapter<RowItem> {

    Context context;
    Context _appContext;
    CustomListViewAdapter instance;
    List<RowItem> _rowItems;
    SystemClass _appInstance = null;


    public CustomListViewAdapter(Context context, int resourceId,
                                 List<RowItem> items,
                                 Context appContext,
                                 SystemClass appInstance) {
        super(context, resourceId, items);
        this.context = context;
        this._appContext = appContext;
        this._rowItems = items;
        _appInstance = appInstance;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);

            holder.imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //Toast.makeText(getContext(), "clicked " +position, Toast.LENGTH_SHORT).show();
                    Log.i(this.getClass().getCanonicalName(), "row clicked " + position );

                    if(0 == position)
                    {
                        for(int k=1;k<_appInstance.deviceList.size();k++)
                        {
                            if(_appInstance.deviceStatus.get(0).equals("on"))
                            {
                                _appInstance.deviceStatus.set(k, "off");
                            }
                            else
                            {
                                _appInstance.deviceStatus.set(k, "on");
                            }
                        }
                    }

                    if(_appInstance.deviceStatus.get(position).equals("on")) {
                        _appInstance.deviceStatus.set(position, "off");
                    }
                    else
                    {
                       _appInstance.deviceStatus.set(position, "on");

                    }

                 _rowItems.removeAll(_rowItems);

                        for(int k=0;k<_appInstance.deviceCount;k++) {
                            RowItem item;
                            //Log.i(this.getClass().getCanonicalName(), "row " + tokens[i] + " " + );
                            if (_appInstance.deviceStatus.get(k).equals("on")) {
                                item = new RowItem(R.drawable.icon_on, _appInstance.deviceList.get(k));
                            } else {
                                item = new RowItem(R.drawable.icon_off, _appInstance.deviceList.get(k));
                            }
                            _rowItems.add(item);
                        }

                    notifyDataSetChanged();
                    MainActivity inst = MainActivity.instance();
                    inst.deviceStatusChanged(position);

                   //callback.deviceStatusChanged(espIndex, deviceIndex);
                }
            });

            holder.txtTitle.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(context, EditdeviceActivity.class);
                    i.putExtra("deviceId", position);
                    context.startActivity(i);
                }
            });
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtTitle.setText(rowItem.getTitle());
        holder.imageView.setImageResource(rowItem.getImageId());

        if( rowItem.getTitle().equals("Main Switch")){
            holder.txtTitle.setTextColor(Color.rgb(200,0,0));
            Boolean isAllDevicesTurnedOff = true;
            for(int k=1;k<_appInstance.deviceList.size();k++) {
                if (_appInstance.deviceStatus.get(k).equals("on")) {
                    _appInstance.deviceStatus.set(0, "on");
                    RowItem item;
                    item = new RowItem(R.drawable.icon_on, _appInstance.deviceList.get(0));
                    _rowItems.set(0, item);
                    rowItem.setImageId(R.drawable.icon_on);
                    holder.imageView.setImageResource(rowItem.getImageId());
                    isAllDevicesTurnedOff = false;
                    break;
                }
            }

            if(true == isAllDevicesTurnedOff)
            {
                _appInstance.deviceStatus.set(0, "off");
                RowItem item;
                item = new RowItem(R.drawable.icon_off, _appInstance.deviceList.get(0));
                _rowItems.set(0, item);
                rowItem.setImageId(R.drawable.icon_off);
                holder.imageView.setImageResource(rowItem.getImageId());
            }
        }
        else
        {
            holder.txtTitle.setTextColor(Color.rgb(0,100,0));
        }

        return convertView;
    }

    public void setInstance(CustomListViewAdapter adapter)
    {
        instance = adapter;
    }

    public void refreshFragment ()
    {
        _rowItems.removeAll(_rowItems);

        for(int k=0;k<_appInstance.deviceCount;k++) {
            RowItem item;
            //Log.i(this.getClass().getCanonicalName(), "row " + tokens[i] + " " + );
            if (_appInstance.deviceStatus.get(k).equals("on")) {
                item = new RowItem(R.drawable.icon_on, _appInstance.deviceList.get(k));
            } else {
                item = new RowItem(R.drawable.icon_off, _appInstance.deviceList.get(k));
            }
            _rowItems.add(item);
        }

        notifyDataSetChanged();
    }
}