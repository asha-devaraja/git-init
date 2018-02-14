package com.android_examples.Connect2ControlHome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginPageActivity extends Activity implements systemClassCallbacks {
    ImageButton b1;
    Button b2;
    EditText ed1, ed2, ed3;
    ProgressDialog mProgressDialog;
    SystemClass appInstance = null;
    Handler ConnectWaithandler;
    Runnable ConnectWaitRunnable;
    Handler groupListWaithandler;
    Runnable groupListWaitRunnable;
    Handler deviceListWaithandler;
    Runnable deviceListWaitRunnable;
    Handler regWaithandler;
    Runnable regWaitRunnable;
    Context context;
    String m_Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        appInstance = (SystemClass)getApplicationContext();

       /* final Spinner spinner = (Spinner) findViewById(R.id.CountryCodes1);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.CountryCodes, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        final DatabaseParameters logindetails = appInstance.dbParams;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });*/
 final DatabaseParameters logindetails = appInstance.dbParams;
        b1 = (ImageButton) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button4);
        ed1 = (EditText) findViewById(R.id.Phone_no);
        ed2 = (EditText) findViewById(R.id.Client_id);
        ed3 = (EditText) findViewById(R.id.Password);

        Log.i(this.getClass().getCanonicalName(), "logindetails1 " + logindetails._IP);
        Log.i(this.getClass().getCanonicalName(), "logindetails1 " + logindetails._port);
        Log.i(this.getClass().getCanonicalName(), "logindetails1 " + logindetails._name);
        Log.i(this.getClass().getCanonicalName(), "logindetails1 " + logindetails._Phone_no);
        Log.i(this.getClass().getCanonicalName(), "logindetails1 " + logindetails._Password);
        Log.i(this.getClass().getCanonicalName(), "logindetails1 " + logindetails._clientid);
        Log.i(this.getClass().getCanonicalName(), "logindetails1 " + logindetails._countryCode);

        ed1.setText(logindetails._Phone_no);
        ed2.setText(logindetails._name);
        ed3.setText(logindetails._Password);
      //  int spinnerPosition = adapter.getPosition(logindetails._countryCode);
    //    spinner.setSelection(spinnerPosition);



        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  Log.i(this.getClass().getCanonicalName(), "Connecting...." + spinner.getSelectedItem().toString());
                final EditText edittext = new EditText(LoginPageActivity.this);
                final AlertDialog.Builder alert = new AlertDialog.Builder(LoginPageActivity.this);
                alert.setMessage("Enter admin Password");
                alert.setTitle("PASSWORD");
                alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(LoginPageActivity.this, "password entered : " + edittext.getText().toString(), Toast.LENGTH_SHORT).show();
                        if (edittext.getText().toString().equals("12345")) {
                            dialog.dismiss();
                            Intent i = new Intent(LoginPageActivity.this, WifiConfigurationActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(LoginPageActivity.this, "Incorrect password!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.setView(edittext);
                alert.show();
            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  Log.i(this.getClass().getCanonicalName(), "Connecting...." + spinner.getSelectedItem().toString());

                if (ed1.getText().toString().isEmpty() || ed2.getText().toString().isEmpty() ||
                        ed3.getText().toString().isEmpty()) {
                    Toast.makeText(LoginPageActivity.this, "Enter valid Login details", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    /*AlertDialog.Builder builder = new AlertDialog.Builder(LoginPageActivity.this);
                    // I'm using fragment here so I'm using getView() to provide ViewGroup
                    // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                    View viewInflated = LayoutInflater.from(LoginPageActivity.this).inflate(R.layout.otp_alert, (ViewGroup) LoginPageActivity.this, false);
                    // Set up the input
                    final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    builder.setView(viewInflated);

                    // Set up the buttons
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            m_Text = input.getText().toString();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();*/
                }

                appInstance.deleteFromDatabase(logindetails);
                Log.i(this.getClass().getCanonicalName(), "delete contact ");


                logindetails._Phone_no = ed1.getText().toString();
                logindetails._clientid = ed2.getText().toString();
                logindetails._Password = ed3.getText().toString();
                logindetails._name = logindetails._clientid;
            //    logindetails._countryCode = spinner.getSelectedItem().toString();

                appInstance.addToDatabase(logindetails);

                Log.i(this.getClass().getCanonicalName(), "logindetails1 " + logindetails._IP);
                Log.i(this.getClass().getCanonicalName(), "logindetails1 " + logindetails._port);
                Log.i(this.getClass().getCanonicalName(), "logindetails1 " + logindetails._name);
                Log.i(this.getClass().getCanonicalName(), "logindetails1 " + logindetails._Phone_no);
                Log.i(this.getClass().getCanonicalName(), "logindetails1 " + logindetails._Password);
                Log.i(this.getClass().getCanonicalName(), "logindetails1 " + logindetails._clientid);
                Log.i(this.getClass().getCanonicalName(), "logindetails1 " + logindetails._countryCode);

                mProgressDialog = new ProgressDialog(LoginPageActivity.this);
                mProgressDialog.setTitle("Connecting to server....");
                mProgressDialog.setMessage("Please wait!!!");
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                newConnect();
            }

        });
    }

    public void newConnect ( )
    {
        if(true != appInstance.connectToServer(this))
        {
            Log.i(this.getClass().getCanonicalName(), "connectToServer failed ");
            if(mProgressDialog.isShowing())
                mProgressDialog.dismiss();
                return;
        }

        ConnectWaithandler = new Handler();
        ConnectWaitRunnable = new Runnable() {
            @Override
            public void run() {
                Log.i(this.getClass().getCanonicalName(), "connectToServer timeout ");
                appInstance.systemCallback = null;
                ConnectWaithandler.removeCallbacks(this);
                if(mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            }
        };

        ConnectWaithandler.postDelayed(ConnectWaitRunnable, appInstance.timeToWait);
    }

    public void registerMobile ( )
    {
        if(mProgressDialog.isShowing())
            mProgressDialog.dismiss();

        if(true != appInstance.registerMobileDevice(this))
        {
            Log.i(this.getClass().getCanonicalName(), "registerMobileDevice failed ");
            return;
        }

       // mProgressDialog.setTitle("Registering your phone....");
        final EditText edittext = new EditText(LoginPageActivity.this);
        AlertDialog.Builder alert = new AlertDialog.Builder(LoginPageActivity.this);
        alert.setMessage("Enter OTP");
        alert.setTitle("PASSWORD");
        alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                appInstance.otpReceived(edittext.getText().toString());
                dialog.dismiss();
                mProgressDialog.setTitle("Registering your phone....");
                mProgressDialog.show();
            }
        });
        alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.setView(edittext);
        alert.show();

        regWaithandler = new Handler();
        regWaitRunnable = new Runnable() {
            @Override
            public void run() {
                appInstance.systemCallback = null;
                regWaithandler.removeCallbacks(this);
                if(mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            }
        };

        regWaithandler.postDelayed(regWaitRunnable,60000);
    }

    public void getDeviceList ( )
    {
         if(true != appInstance.getDeviceList(this))
        {
            Log.i(this.getClass().getCanonicalName(), "getDeviceList failed ");
            if(mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            return;
        }

        mProgressDialog.setTitle("Getting device list....");

        deviceListWaithandler = new Handler();
        deviceListWaitRunnable = new Runnable() {
            @Override
            public void run() {
                appInstance.systemCallback = null;
                deviceListWaithandler.removeCallbacks(this);
                mProgressDialog.dismiss();
            }
        };

        deviceListWaithandler.postDelayed(deviceListWaitRunnable, appInstance.timeToWait);
    }

    public void getGroupList ( )
    {
        if(true != appInstance.getGroupList(this))
        {
            Log.i(this.getClass().getCanonicalName(), "getGroupList failed ");
            if(mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            return;
        }

        mProgressDialog.setTitle("Getting Group list....");

        groupListWaithandler = new Handler();
        groupListWaitRunnable = new Runnable() {
            @Override
            public void run() {
                appInstance.systemCallback = null;
                groupListWaithandler.removeCallbacks(this);
                mProgressDialog.dismiss();
            }
        };

        groupListWaithandler.postDelayed(groupListWaitRunnable, appInstance.timeToWait);
    }

    public void systemClassCallback(systemClassCallbacks.systemEvents event, Boolean status ) {
        switch (event) {
            case CONNECT: {
                if ((null != ConnectWaithandler) && (null != ConnectWaitRunnable)) {
                    ConnectWaithandler.removeCallbacks(ConnectWaitRunnable);
                }

                if (true == status) {
                    Log.i(this.getClass().getCanonicalName(), "connect success callback recvd");
                    if (true != appInstance.dbParams._RegStatus) {
                        registerMobile();
                    } else {
                        getDeviceList();
                    }
                } else {
                    Log.i(this.getClass().getCanonicalName(), "connect failure callback recvd");
                    Intent i = new Intent(LoginPageActivity.this, LoginPageActivity.class);
                    startActivity(i);
                    finish();
                }
            }
            break;

            case REGISTRATION: {
                if ((null != regWaithandler) && (null != regWaitRunnable)) {
                    regWaithandler.removeCallbacks(regWaitRunnable);
                }

                if (status) {
                    DatabaseParameters dbOldParams = appInstance.dbParams;
                    appInstance.dbParams._RegStatus = true;
                    appInstance.updateDatabase(dbOldParams, appInstance.dbParams);

                    Log.i(this.getClass().getCanonicalName(), "Registration success callback recvd");
                    getDeviceList();
                } else {
                    Log.i(this.getClass().getCanonicalName(), "Registration failure callback recvd");
                    // Toast.makeText(LoginPageActivity.this,"Connection Error!!",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginPageActivity.this, LoginPageActivity.class);
                    startActivity(i);
                    finish();
                }
            }
            break;


            case GROUPLIST: {
                if ((null != groupListWaithandler) && (null != groupListWaitRunnable)) {
                    groupListWaithandler.removeCallbacks(groupListWaitRunnable);
                }

                mProgressDialog.dismiss();

                if (status) {
                    Log.i(this.getClass().getCanonicalName(), "groupList success callback recvd");
                    Intent i = new Intent(LoginPageActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    //getGroupList();
                } else {
                    Log.i(this.getClass().getCanonicalName(), "deviceList failure callback recvd");
                    Intent i = new Intent(LoginPageActivity.this, LoginPageActivity.class);
                    startActivity(i);
                    finish();
                }
            }
            break;

            case DEVICELIST: {
                if ((null != deviceListWaithandler) && (null != deviceListWaitRunnable)) {
                    deviceListWaithandler.removeCallbacks(deviceListWaitRunnable);
                }

                if (status) {
                    Log.i(this.getClass().getCanonicalName(), "deviceList success callback recvd");
                    getGroupList();
                } else {
                    Log.i(this.getClass().getCanonicalName(), "deviceList failure callback recvd");
                    Intent i = new Intent(LoginPageActivity.this, LoginPageActivity.class);
                    startActivity(i);
                    finish();
                }
            }
            break;


        }
    }

}






