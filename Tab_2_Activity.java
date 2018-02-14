package com.android_examples.Connect2ControlHome;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Tab_2_Activity extends Fragment  {


    List<RowItem> rowItems;
    SystemClass appInstance = null;
    CustomListViewAdapter adapter = null;
    ProgressDialog mProgressDialog;
    Handler deviceListWaithandler;
    Runnable deviceListWaitRunnable;

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.device_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.list);
     appInstance = (SystemClass)getActivity().getApplicationContext();



     /*   appInstance.deviceCount=2;
        appInstance.deviceList.add("fan");
        appInstance.deviceStatus.add("off");
        appInstance.deviceList.add("light");
        appInstance.deviceStatus.add("on");*/

        rowItems = new ArrayList<RowItem>();
        for (int j = 0; j < appInstance.deviceCount; j++) {
            RowItem item;
            //Log.i(this.getClass().getCanonicalName(), "row " + tokens[i] + " " + );
            if (appInstance.deviceStatus.get(j).equals("on")) {
                item = new RowItem(R.drawable.icon_on, appInstance.deviceList.get(j));
            } else {
                item = new RowItem(R.drawable.icon_off, appInstance.deviceList.get(j));
            }
            rowItems.add(item);
        }

        listView = (ListView) rootView.findViewById(R.id.list);
        adapter = new CustomListViewAdapter(getActivity(), R.layout.list_item, rowItems, getActivity().getApplicationContext(), appInstance );
        listView.setAdapter(adapter);
        //adapter.notifyDataSetChanged();

        return rootView;
    }

    MyReceiver r;
    public void refresh() {
        //yout code in refresh.
        Log.i("Refresh", "YES");

        adapter.refreshFragment();

    }
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(r);
    }

    @Override
    public void onResume() {
        super.onResume();
        r = new MyReceiver();
        //Toast.makeText(getActivity(), "Tab_2_Activity", Toast.LENGTH_SHORT).show();

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(r,
                new IntentFilter("TAG_REFRESH"));
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Tab_2_Activity.this.refresh();
        }
    }
}
