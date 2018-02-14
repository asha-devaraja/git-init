package com.android_examples.Connect2ControlHome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
public class Tab_3_Activity extends Fragment {
    ListView list;
    SystemClass appInstance = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_tab_3, container, false);
        list = (ListView) rootView.findViewById(R.id.listNotification);
        appInstance = (SystemClass)getActivity().getApplicationContext();
        CreateListView();

        return rootView;
    }
            private void CreateListView()
            {
                ArrayList<String> values = new ArrayList<String>();

                values = appInstance.getNotificationList();
                //Create an adapter for the listView and add the ArrayList to the adapter.
                list.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,values));
                list.setOnItemClickListener(new OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
                    {
                        //args2 is the listViews Selected index
                    }
                });
            }
        }



