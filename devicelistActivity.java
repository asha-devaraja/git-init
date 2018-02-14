package com.android_examples.Connect2ControlHome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asha.Devaraja on 11/11/2016.
 */
public class devicelistActivity extends FragmentActivity implements systemClassCallbacks {
    private static devicelistActivity inst;

    List<RowItem> rowItems;
    SystemClass appInstance = null;
    CustomListViewAdapter adapter;
    ProgressDialog mProgressDialog;
    Handler deviceListWaithandler;
    Runnable deviceListWaitRunnable;
    ListView listView;

    public static devicelistActivity instance() {
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
        // do something on back.
        finish();
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Log.w("ANDROID MENU TUTORIAL:", "onOptionsItemSelected(MenuItem item)");

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                //Toast.makeText(this, "Clicked: Menu No. 1", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(devicelistActivity.this, LoginPageActivity.class);
                startActivity(i);
                finish();
                return true;
            case R.id.help:
                Toast.makeText(this, "Clicked: Menu No. 2", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.refresh:
                Toast.makeText(this, "Updating the device list...", Toast.LENGTH_SHORT).show();
                refreshDeviceList();
                return true;
            case R.id.registerPhone:
                if(true==appInstance.dbParams._isAdmin) {
                    Intent i1 = new Intent(devicelistActivity.this, Register1Activity.class);
                    startActivity(i1);
                    finish();
                }
                else
                {
                    Toast.makeText(this, "Only admin can view!!!", Toast.LENGTH_SHORT).show();
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

        mProgressDialog = new ProgressDialog(devicelistActivity.this);
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
        setContentView(R.layout.device_list);
        listView = (ListView) findViewById(R.id.list);

        appInstance = (SystemClass)getApplicationContext();

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

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListViewAdapter(devicelistActivity.this, R.layout.list_item, rowItems, getApplicationContext(), appInstance );
        listView.setAdapter(adapter);
    }

    public void deviceStatusNotification()
    {
        appInstance.systemCallback = null;
        Toast.makeText(devicelistActivity.this, appInstance.tempDeviceName + " turned " + appInstance.tempDeviceStatus, Toast.LENGTH_SHORT).show();
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

                    rowItems.removeAll(rowItems);
                    for (int j = 0; j < appInstance.deviceCount; j++) {
                        RowItem item;
                        Log.i(this.getClass().getCanonicalName(), "row " + appInstance.deviceList.get(j) );
                        if (appInstance.deviceStatus.get(j).equals("on")) {
                            item = new RowItem(R.drawable.icon_on, appInstance.deviceList.get(j));
                        } else {
                            item = new RowItem(R.drawable.icon_off, appInstance.deviceList.get(j));
                        }
                        rowItems.add(item);
                    }

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Log.i(this.getClass().getCanonicalName(), "refreshing screen...");
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);
                            Toast.makeText(devicelistActivity.this, "device list updated..", Toast.LENGTH_SHORT).show();
                        }
                    });
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
                    devicelistActivity inst = devicelistActivity.instance();
                    inst.deviceStatusNotification();
                }
                else
                {
                    Log.i(this.getClass().getCanonicalName(), "ack failure recvd ");
                }
            }

        }
    }
}
