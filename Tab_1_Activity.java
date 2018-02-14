package com.android_examples.Connect2ControlHome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.List;

public class Tab_1_Activity extends Fragment {
    ListView listView;
    Context thiscontext;
    List<RowItem> rowItems;
    SystemClass appInstance = null;
    //private static final String[][] data = {{"audia4", "audiq7", "audir8"}, {"bmwm6", "bmwx6"}, {"ferrarienzo", "ferrarif430", "ferrarif430italia"}};
    private ExpandableListView expandableListView;
    Button button1;
    SampleExpandableListAdapter adapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        thiscontext = getActivity();
        View rootView = inflater.inflate(R.layout.activity_group, container, false);
        listView = (ListView) rootView.findViewById(R.id.list);
        appInstance = (SystemClass) getActivity().getApplicationContext();
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableListView1);

        /* for(Integer i =0 ; i< appInstance.GroupList.size();++i)
        {
            ArrayList<String> tempDeviceList = new ArrayList<String>();
            tempDeviceList = appInstance.groupDeviceList.get(i);
            for(Integer j =0 ; j< tempDeviceList.size();++j)
            {
                data[i][j] = tempDeviceList.get(j);
            }
        }
*/
        adapter = new SampleExpandableListAdapter(thiscontext,getActivity(), appInstance);
        expandableListView.setAdapter(adapter);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                      //  .setAction("Action", null).show();
                Intent i = new Intent(getActivity(), devicelistForGroup.class);
                i.putExtra("addNewGroup", 1);
                startActivity(i);

            }

        });

        return rootView;
   }

    MyReceiver r;
    public void refresh() {
        //yout code in refresh.
        Log.i("Refresh", "YES");
        adapter.notifyDataSetChanged();
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
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(r,
                new IntentFilter("TAG_REFRESH"));
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Tab_1_Activity.this.refresh();
           // Toast.makeText(getActivity(), "Tab_1_Activity", Toast.LENGTH_SHORT).show();
        }
    }

}








