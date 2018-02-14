package com.android_examples.Connect2ControlHome;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.widget.TabHost;

/**
 * Created by JUNED on 5/30/2016.
 */
public class FragmentAdapterClass extends FragmentStatePagerAdapter {

    int TabCount;
    TabHost tabHost;

    public FragmentAdapterClass( android.support.v4.app.FragmentManager fragmentManager, int CountTabs) {

        super(fragmentManager);

        this.TabCount = CountTabs;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {

        switch (position) {
            case 0:
                Tab_1_Activity Group = new Tab_1_Activity();
                return Group;

            case 1:
                Tab_2_Activity DeviceList = new Tab_2_Activity();
                return DeviceList;

            case 2:
                Tab_3_Activity Notifications = new Tab_3_Activity();
                return Notifications;

            default:
                return null;
        }
    }

    @Override
    public  int getCount() {
        return TabCount;
    }


}