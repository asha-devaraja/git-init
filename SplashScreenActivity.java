package com.android_examples.Connect2ControlHome;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SplashScreenActivity extends Activity implements systemClassCallbacks {


    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    private static final int MY_PERMISSIONS_REQUEST_PHONE_STATE = 0;
    Handler InitWaithandler;
    Runnable InitWaitRunnable;
    Handler deviceListWaithandler;
    Runnable deviceListWaitRunnable;
    Handler groupListWaithandler;
    Runnable groupListWaitRunnable;
    SystemClass appInstance = null;
    int count = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        appInstance = (SystemClass)getApplicationContext();
        StartAnimations();
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        RelativeLayout l = (RelativeLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        TextView iv = (TextView) findViewById(R.id.logo);
        iv.clearAnimation();
        iv.startAnimation(anim);

        askAllUserPermissions();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(false == isInternetAvailable(SplashScreenActivity.this))
                {
                    Toast.makeText(SplashScreenActivity.this, "No internet connection!!! ", Toast.LENGTH_SHORT).show();
                    return;
                }
                init();
                //Intent i = new Intent(SplashScreenActivity.this,MainActivity.class);
                //startActivity(i);
                //finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public void handlerTest()
    {
        final Handler InitWaithandler = new Handler();
        Runnable InitWaitRunnable = new Runnable() {
            @Override
            public void run() {

               //InitWaithandler.removeCallbacks(this);
                ++count;
                Toast.makeText(SplashScreenActivity.this, "runnable " +count, Toast.LENGTH_SHORT).show();
                Log.i(this.getClass().getCanonicalName(), "inside runnable");
            }
        };

        InitWaithandler.postDelayed(InitWaitRunnable, 5000);
    }

    public void init ()
    {
        if(true != appInstance.systemInit(this))
        {
            appInstance.systemCallback = null;
            Log.i(this.getClass().getCanonicalName(), "system init failed ");
            Intent i = new Intent(SplashScreenActivity.this, LoginPageActivity.class);
            startActivity(i);
            finish();
            return;
        }

        InitWaithandler = new Handler();
        InitWaitRunnable = new Runnable() {
            @Override
            public void run() {
                appInstance.systemCallback = null;
                InitWaithandler.removeCallbacks(this);
                Log.i(this.getClass().getCanonicalName(), "calling login page from splash.. ");
                Intent i = new Intent(SplashScreenActivity.this, LoginPageActivity.class);
                startActivity(i);
                finish();
            }
        };

        InitWaithandler.postDelayed(InitWaitRunnable, appInstance.timeToWait);
    }

    public void getDeviceList ( )
    {
        if(true != appInstance.getDeviceList(this))
        {
            Log.i(this.getClass().getCanonicalName(), "getDeviceList failed ");
            Intent i = new Intent(SplashScreenActivity.this, LoginPageActivity.class);
            startActivity(i);
            finish();
            return;
        }

        deviceListWaithandler = new Handler();
        deviceListWaitRunnable = new Runnable() {
            @Override
            public void run() {
                appInstance.systemCallback = null;
                deviceListWaithandler.removeCallbacks(this);
                Intent i = new Intent(SplashScreenActivity.this, LoginPageActivity.class);
                startActivity(i);
                finish();
            }
        };

        deviceListWaithandler.postDelayed(deviceListWaitRunnable, appInstance.timeToWait);
    }
    public void getGroupList ( )
    {
        if(true != appInstance.getGroupList(this))
        {
            Log.i(this.getClass().getCanonicalName(), "getGroupList failed ");
            return;
        }

        groupListWaithandler = new Handler();
        groupListWaitRunnable = new Runnable() {
            @Override
            public void run() {
                appInstance.systemCallback = null;
                groupListWaithandler.removeCallbacks(this);

            }
        };

        groupListWaithandler.postDelayed(groupListWaitRunnable, appInstance.timeToWait);
    }

    public void systemClassCallback(systemClassCallbacks.systemEvents event, Boolean status )
    {
        switch(event)
        {
            case CONNECT:
            {
                if((null!=InitWaithandler)&&(null!=InitWaitRunnable)) {
                    InitWaithandler.removeCallbacks(InitWaitRunnable);
                }

                if ( status )
                {
                    Log.i(this.getClass().getCanonicalName(), "connect success callback recvd");
                    getDeviceList();
                }
                else
                {
                    Log.i(this.getClass().getCanonicalName(), "connect failure callback recvd");
                    Intent i = new Intent(SplashScreenActivity.this, LoginPageActivity.class);
                    startActivity(i);
                    finish();
                }
            }
            break;

         case DEVICELIST:
            {
                if((null!=deviceListWaithandler)&&(null!=deviceListWaitRunnable)) {
                    deviceListWaithandler.removeCallbacks(deviceListWaitRunnable);
                }

                if ( status )
                {
                    Log.i(this.getClass().getCanonicalName(), "deviceList success callback recvd");
                    /*Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();*/
                    getGroupList();
                }
                else
                {
                    Log.i(this.getClass().getCanonicalName(), "deviceList failure callback recvd");
                    Intent i = new Intent(SplashScreenActivity.this, LoginPageActivity.class);
                    startActivity(i);
                    finish();
                }
            }
            break;

            case GROUPLIST:
            {
                if((null!=groupListWaithandler)&&(null!=groupListWaitRunnable)) {
                    groupListWaithandler.removeCallbacks(groupListWaitRunnable);
                }

                if ( status )
                {
                    Log.i(this.getClass().getCanonicalName(), "groupList success callback recvd");
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Log.i(this.getClass().getCanonicalName(), "deviceList failure callback recvd");
                    Intent i = new Intent(SplashScreenActivity.this, LoginPageActivity.class);
                    startActivity(i);
                    finish();
                }
            }
            break;

        }
    }

    public void askAllUserPermissions ( )
    {
        if (ContextCompat.checkSelfPermission(SplashScreenActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(SplashScreenActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }

        if (ContextCompat.checkSelfPermission(SplashScreenActivity.this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this,
                    Manifest.permission.READ_PHONE_STATE)) {

            } else {

                ActivityCompat.requestPermissions(SplashScreenActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }

        if (ContextCompat.checkSelfPermission(SplashScreenActivity.this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this,
                    Manifest.permission.READ_SMS)) {

            } else {

                ActivityCompat.requestPermissions(SplashScreenActivity.this,
                        new String[]{Manifest.permission.READ_SMS},
                        MY_PERMISSIONS_REQUEST_PHONE_STATE);

            }
        }

        if (ContextCompat.checkSelfPermission(SplashScreenActivity.this,
                Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this,
                    Manifest.permission.RECEIVE_SMS)) {

            } else {

                ActivityCompat.requestPermissions(SplashScreenActivity.this,
                        new String[]{Manifest.permission.RECEIVE_SMS},
                        MY_PERMISSIONS_REQUEST_PHONE_STATE);

            }


        }

    }


    public static boolean isInternetAvailable(Context context)
    {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null)
        {
            Log.i(SplashScreenActivity.class.getCanonicalName(), "no internet connection");
            return false;
        }
        else
        {
            if(info.isConnected())
            {
                Log.i(SplashScreenActivity.class.getCanonicalName(), "internet connection available...");
                return true;
            }
            else
            {
                Log.i(SplashScreenActivity.class.getCanonicalName(), "internet connection");
                return false;
            }

        }
    }
}













