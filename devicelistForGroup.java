package com.android_examples.Connect2ControlHome;

/**
 * Created by Asha.Devaraja on 11/22/2016.
 */

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Asha.Devaraja on 11/11/2016.
 */
public class devicelistForGroup extends FragmentActivity {
    private static devicelistForGroup inst;

    List<RowItem> rowItems;
    CheckBox checkBox;
    Customlistadapter adapter;
    Button button2;
    Button button3;
    Button button4;
    ListView newsEntryListView;
    ProgressDialog mProgressDialog;
    SystemClass appInstance = null;
    Integer deviceIndex;
    String deviceName;
    String groupName="";
    EditText eText;
    private LayoutInflater vi;
    Handler deviceListWaithandler;
    Runnable deviceListWaitRunnable;
    ArrayList<ArrayList<String>> groupDeviceList=new ArrayList<ArrayList<String>>();
    ListView listView;
    HashMap<Long, Boolean> checkboxMap = new HashMap<Long, Boolean>();
    public String[] check_string_array;
    CustomPhoneNoListViewAdapter newsEntryAdapter;
    ArrayList<Integer> deviceListForGroup = new ArrayList<Integer>();
    Integer groupIndex;
    ArrayList<Integer> tempDeviceList = new ArrayList<Integer>();
    Boolean isNewGroup = false;
    EditText edittext;

    public static devicelistForGroup instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    public void onBackPressed() {
        // do something on back.
        Intent i = new Intent(devicelistForGroup.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_group_list);
        appInstance = (SystemClass)getApplicationContext();

        edittext = (EditText) findViewById(R.id.groupname);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4= (Button)  findViewById(R.id.button7);
                Intent i = getIntent();
        groupIndex = i.getIntExtra("groupId", 0);
        if( 1 != i.getIntExtra("addNewGroup", 0)) {
            groupName = appInstance.GroupList.get(groupIndex);
            edittext.setText(groupName);
        }
        else
        {
            isNewGroup = true;
        }
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(devicelistForGroup.this, "delete button clicked ", Toast.LENGTH_SHORT).show();
                showRadioButtonDialog();

            }
        });        button2.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View v) {
                // TODO Auto-ge§nerated method stub
                //Toast.makeText(devicelistForGroup.this, "create button clicked ", Toast.LENGTH_SHORT).show();

                if (edittext.getText().toString().isEmpty()) {
                    Toast.makeText(devicelistForGroup.this, "Enter Group Name !!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tempDeviceList.size() == 0) {
                    Toast.makeText(devicelistForGroup.this, "Select atleast one device !!", Toast.LENGTH_SHORT).show();
                    return;
                }

                    for(Integer i = 0; i < appInstance.GroupList.size();++i)
                    {
                        if(appInstance.GroupList.get(i).equals(groupName))
                        {
                            appInstance.GroupList.set(i, edittext.getText().toString().trim());
                            Toast.makeText(devicelistForGroup.this, "updated Group   "+edittext.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(devicelistForGroup.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }

                    appInstance.groupDeviceList.add(tempDeviceList);
                    deviceListForGroup.removeAll(deviceListForGroup);
                    Toast.makeText(devicelistForGroup.this, "created Group   "+edittext.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                    appInstance.addGroup(null, edittext.getText().toString().trim(), tempDeviceList);
                    appInstance.GroupList.add(edittext.getText().toString().trim());
                    appInstance.GroupStatus.add("off");
                    Intent i = new Intent(devicelistForGroup.this, MainActivity.class);
                    startActivity(i);
                    finish();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(devicelistForGroup.this, "delete button clicked ", Toast.LENGTH_SHORT).show();
                if(false == appInstance.GroupList.get(groupIndex).equals("")) {
                    appInstance.deleteGroup(null, appInstance.GroupList.get(groupIndex));
                }

            }
        });





        listView = (ListView) findViewById(R.id.list);
        if( 1 != i.getIntExtra("addNewGroup", 0)) {
            tempDeviceList = appInstance.groupDeviceList.get(groupIndex);

        }

        rowItems = new ArrayList<RowItem>();
        for (int j = 0; j < appInstance.deviceCount; j++) {
            RowItem item;
            item = new RowItem(R.drawable.icon_on, appInstance.deviceList.get(j));
            rowItems.add(item);

        }
        listView = (ListView) findViewById(R.id.list);
        adapter = new Customlistadapter(devicelistForGroup.this, R.layout.list_select_devices_group, rowItems, getApplicationContext(), appInstance, tempDeviceList);
        listView.setAdapter(adapter);
    }

    public void deviceSelectedForGroup (Integer deviceIndex, Boolean status)
    {
        if(false == isNewGroup) {
            if(true == status) {
                appInstance.addDeviceToGroup(null, appInstance.GroupList.get(groupIndex), appInstance.deviceList.get(deviceIndex));
            }
            else
            {
                appInstance.removeDeviceFromGroup(null,appInstance.GroupList.get(groupIndex), appInstance.deviceList.get(deviceIndex));
            }
        }
    }

    private void showRadioButtonDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radiobutton_schedule);
        List<String> stringList=new ArrayList<>();  // here is list
        stringList.add("Absolute");
        stringList.add("Relative");

        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);

        for(int i1=0;i1<stringList.size();i1++)
        {
            RadioButton rb=new RadioButton(this); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(stringList.get(i1));
            rg.addView(rb);
        }

        dialog.show();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        Log.e("selected RadioButton->", btn.getText().toString());
                        if (btn.getText().toString().equals("Absolute"))
                        {
                            Intent i = new Intent(devicelistForGroup.this, schedularActivity.class);
                            startActivity(i);
                            finish();
                        }
                        if (btn.getText().toString().equals("Relative")) {
                            Intent i = new Intent(devicelistForGroup.this, Relative_SchedularActivity.class);
                            startActivity(i);
                            finish();

                        }
                    }
                }
            }
        });


    }
}