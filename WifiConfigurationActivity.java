package com.android_examples.Connect2ControlHome;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Asha.Devaraja on 12/14/2016.
 */
public class WifiConfigurationActivity extends Activity {

    EditText ssid;
    EditText pwd;
    Button btn;
    EditText pin1_Name;
    EditText pin2_Name;

    @Override
    public void onBackPressed() {

        Intent i = new Intent(WifiConfigurationActivity.this, LoginPageActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wificonfiguration);
        ssid = (EditText) findViewById(R.id.ssid);
        pwd = (EditText) findViewById(R.id.Pwd);
        btn = (Button) findViewById(R.id.button5);
       pin1_Name = (EditText) findViewById(R.id.edittext);
        pin2_Name = (EditText) findViewById(R.id.EditText2);


    }
}
