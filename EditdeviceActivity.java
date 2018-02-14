package com.android_examples.Connect2ControlHome;

/**
 * Created by Asha.Devaraja on 10/12/2016.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditdeviceActivity extends Activity {
    EditText eText;
    Button btn,btn2;

    SystemClass appInstance = null;
    Integer deviceIndex;
    ListView listView;
    ArrayList<String> GroupList = new ArrayList<String>();
    String deviceName;
    Integer groupIndex;
    String groupName;
    ArrayList<Integer> tempDeviceList = new ArrayList<Integer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editdevice);
        eText = (EditText) findViewById(R.id.edittext);
        btn = (Button) findViewById(R.id.button);
        btn2=(Button) findViewById(R.id.button6);
        listView = (ListView) findViewById(R.id.list);
        /*String[] values = new String[]{"Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };*/
        //String[] values = new String[25];
        ArrayList<String> values = new ArrayList<String>();


        appInstance = (SystemClass) getApplicationContext();

        Intent i = getIntent();
        deviceIndex = i.getIntExtra("deviceId", 0);

        deviceName = appInstance.deviceList.get(deviceIndex);

        //groupName = appInstance.GroupList.get(groupIndex);
        for (int j = 0; j < appInstance.GroupList.size(); ++j)
        {
            groupName = appInstance.GroupList.get(j);
            //Toast.makeText(EditdeviceActivity.this, "group "+ groupName, Toast.LENGTH_SHORT).show();
            tempDeviceList = appInstance.groupDeviceList.get(j);
            for(Integer k=0;k<tempDeviceList.size();k++){
                //Toast.makeText(EditdeviceActivity.this, "device "+ tempDeviceList.get(k), Toast.LENGTH_SHORT).show();
                Integer deviceIndex = tempDeviceList.get(k);
                if(deviceName.equals(appInstance.deviceList.get(deviceIndex))){
                    values.add(groupName);
                    break;
                }
            }
        }
        btn2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showRadioButtonDialog();
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);

                 eText.setText(deviceName);
                 btn.setOnClickListener(new OnClickListener() {
                     public void onClick(View v) {
                         appInstance.deviceList.set(deviceIndex, eText.getText().toString());
                         appInstance.editDeviceName(null, deviceName, eText.getText().toString());
                         Toast.makeText(EditdeviceActivity.this, "device name changed ", Toast.LENGTH_SHORT).show();

                         finish();
                         Intent i = new Intent(EditdeviceActivity.this, MainActivity.class);
                         startActivity(i);
                     }
                 });
             }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(EditdeviceActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }


    private void showRadioButtonDialog() {

        final Dialog dialog = new Dialog(EditdeviceActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radiobutton_schedule);
        List<String> stringList=new ArrayList<>();  // here is list
        stringList.add("Absolute");
        stringList.add("Relative");

        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);

        for(int i1=0;i1<stringList.size();i1++)
        {
            RadioButton rb=new RadioButton(EditdeviceActivity.this); // dynamically creating RadioButton and adding to RadioGroup.
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
                            Intent i = new Intent(EditdeviceActivity.this, schedularActivity.class);
                            i.putExtra("deviceId", deviceIndex);
                            startActivity(i);
                            finish();
                        }
                        if (btn.getText().toString().equals("Relative")) {
                            Intent i = new Intent(EditdeviceActivity.this, Relative_SchedularActivity.class);
                            i.putExtra("deviceId", deviceIndex);
                            startActivity(i);
                            finish();

                        }
                    }
                }
            }
        });


    }

}


