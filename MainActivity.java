package com.android_examples.Connect2ControlHome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements systemClassCallbacks  {

    private static MainActivity inst;

    SystemClass appInstance = null;
    Toolbar toolbar ;
    TabLayout tabLayout ;
    ViewPager viewPager ;
    FragmentAdapterClass fragmentAdapter ;
    ProgressDialog mProgressDialog;
    Handler deviceListWaithandler;
    Runnable deviceListWaitRunnable;
    ListView listView;
    TabHost tabHost;


    public static MainActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sub_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Log.w("ANDROID MENU TUTORIAL:", "onOptionsItemSelected(MenuItem item)");

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                //Toast.makeText(this, "Clicked: Menu No. 1", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, LoginPageActivity.class);
                startActivity(i);
                finish();
                return true;
            case R.id.help:
                appInstance.deleteNotificationsFromDb();
                Toast.makeText(MainActivity.this, "NotificationHistory Cleared", Toast.LENGTH_SHORT).show();
                Intent i2 = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i2);
                finish();
                return true;
            case R.id.refresh:
                Toast.makeText(MainActivity.this, "Updating the device list...", Toast.LENGTH_SHORT).show();
                refreshDeviceList();
                return true;


            case R.id.registerPhone:
                if(true==appInstance.dbParams._isAdmin) {
                    Intent i1 = new Intent(MainActivity.this, Register1Activity.class);
                    startActivity(i1);
                    finish();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Only admin can view!!!", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void refreshDeviceList ()
    {
        if(true != appInstance.getDeviceList(this))
        {
            return;
        }

        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setTitle("Getting device list....");
        mProgressDialog.setMessage("Please wait!!!");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();

        deviceListWaithandler = new Handler();
        deviceListWaitRunnable = new Runnable() {
            @Override
            public void run() {
                deviceListWaithandler.removeCallbacks(this);
                mProgressDialog.dismiss();
            }
        };

        deviceListWaithandler.postDelayed(deviceListWaitRunnable, appInstance.timeToWait);
    }

    public void sendToggleCommand(String deviceName, String status )
    {
        appInstance.sendPowerToggleCommand(this, deviceName, status );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        appInstance = (SystemClass)getApplicationContext();

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout1);
        viewPager = (ViewPager) findViewById(R.id.pager1);

        setSupportActionBar(toolbar);

        tabLayout.addTab(tabLayout.newTab().setText("Group"));
        tabLayout.addTab(tabLayout.newTab().setText("Devices"));
        tabLayout.addTab(tabLayout.newTab().setText("Notification"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        fragmentAdapter = new FragmentAdapterClass(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(appInstance.tabIndex);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab LayoutTab) {
                appInstance.tabIndex = LayoutTab.getPosition();
                //fragmentAdapter.notifyDataSetChanged();
                viewPager.setCurrentItem(LayoutTab.getPosition());

         /* if (LayoutTab.getPosition() == 0) {
                   // Toast.makeText(MainActivity.this, "mainActivity", Toast.LENGTH_SHORT).show();
                    LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(MainActivity.this);
                    Intent i = new Intent("TAG_REFRESH");
                    lbm.sendBroadcast(i);
                }
                else if (LayoutTab.getPosition() == 1)
                {
                   // Toast.makeText(MainActivity.this, "mainActivity", Toast.LENGTH_SHORT).show();
                    LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(MainActivity.this);
                    Intent i1 = new Intent("TAG_REFRESH1");
                    lbm.sendBroadcast(i1);

                }*/
                LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(MainActivity.this);
                Intent i1 = new Intent("TAG_REFRESH");
                lbm.sendBroadcast(i1);
              }

            @Override
            public void onTabUnselected(TabLayout.Tab LayoutTab) {


            }


            @Override
            public void onTabReselected(TabLayout.Tab LayoutTab) {
                appInstance.tabIndex = LayoutTab.getPosition();

            }


        });
    }
    public void systemClassCallback(systemClassCallbacks.systemEvents event, Boolean status )
    {
        switch(event)
        {
            case DEVICELIST:
            {
                if((null!=deviceListWaithandler)&&(null!=deviceListWaitRunnable)) {
                    deviceListWaithandler.removeCallbacks(deviceListWaitRunnable);
                }

                if(mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                if ( status )
                {
                    Log.i(this.getClass().getCanonicalName(), "deviceList refresh success callback recvd " + appInstance.deviceCount);
                    tabLayout.invalidate();
                }
                else
                {

                    Log.i(this.getClass().getCanonicalName(), "deviceList refresh error callback recvd");
                }
            }
            break;

            case ACKNOWLEDGMENT:
            {
                if(true==status)
                {
                    Log.i(this.getClass().getCanonicalName(), "ack success recvd ");
                    MainActivity inst = MainActivity.instance();
                    inst.deviceStatusNotification();
                }
                else
                {
                    Log.i(this.getClass().getCanonicalName(), "ack failure recvd ");
                }
            }
            break;

            case ACKNOWLEDGMENT_GROUP_STATUS:
            {
                if(true==status)
                {
                    Log.i(this.getClass().getCanonicalName(), "group status ack success recvd ");
                    MainActivity inst = MainActivity.instance();
                    inst.groupStatusNotification();
                }
                else
                {
                    Log.i(this.getClass().getCanonicalName(), "ack failure recvd ");
                }
            }
            break;
        }
    }

    public void deviceStatusChanged(int deviceIndex)
    {
        sendToggleCommand(appInstance.deviceList.get(deviceIndex), appInstance.deviceStatus.get(deviceIndex));
    }

    public void groupStatusChanged(int groupIndex)
    {
        appInstance.sendGroupStatusChangeCommand(this,appInstance.GroupList.get(groupIndex), appInstance.GroupStatus.get(groupIndex));
    }
    public void deviceStatusNotification()
    {
        appInstance.systemCallback = null;
        Toast.makeText(MainActivity.this, appInstance.tempDeviceName + " turned " + appInstance.tempDeviceStatus, Toast.LENGTH_SHORT).show();
    }
    public void groupStatusNotification()
    {
        appInstance.systemCallback = null;
        Toast.makeText(MainActivity.this, appInstance.tempGroupName + " turned " + appInstance.tempGroupStatus, Toast.LENGTH_SHORT).show();
    }
    public void callDeviceListForGroup(int groupIndex )
    {
        Intent myIntent = new Intent(MainActivity.this, devicelistForGroup.class);
        myIntent.putExtra("groupId", groupIndex);
        startActivity(myIntent);
        finish();
    }


}