package com.android_examples.Connect2ControlHome;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Asha.Devaraja on 12/22/2016.
 */
public class schedularActivity extends Activity implements
        systemClassCallbacks,OnClickListener {


    Button btnCalendar, btnTimePicker, butnsave, buttoncancel;
    EditText txtDate, txtTime;
    SystemClass appInstance = null;
    ProgressDialog mProgressDialog;
    Handler ScheduleDateTimeListWaithandler;
    Runnable ScheduleDateTimeListWaitRunnable;
    ListView newsEntryListView1;
    customScheduleListAdapter newsEntryAdapter1;
    EditText editText;
    Integer deviceIndex;
    String deviceName;
    PendingIntent pi;
    BroadcastReceiver br;
    AlarmManager am;

    private static schedularActivity inst;

    public static schedularActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    public void getScheduleDateTimeList1()
    {
        if (true != appInstance.getscheduleList(this))
        {
            return;
        }

        ScheduleDateTimeListWaithandler = new Handler();
        ScheduleDateTimeListWaitRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                mProgressDialog.dismiss();
                ScheduleDateTimeListWaithandler.removeCallbacks(this);
                Toast.makeText(getApplicationContext(), "Not found!!!", Toast.LENGTH_SHORT).show();
            }
        };

        ScheduleDateTimeListWaithandler.postDelayed(ScheduleDateTimeListWaitRunnable, appInstance.timeToWait);
    }

        public void removeSchedule(Integer ScheduleDateTimeIndex)
    {
        if (true != appInstance.removeSchedule(this, appInstance.scheduleList.get(ScheduleDateTimeIndex)))
        {
            return;
        }

        RowItemScheduleList entry1 = new RowItemScheduleList(appInstance.scheduleList.get(ScheduleDateTimeIndex));
        newsEntryAdapter1.remove(entry1);
        entry1 = newsEntryAdapter1.getItem(ScheduleDateTimeIndex);
        newsEntryAdapter1.remove(entry1);
        appInstance.scheduleList.remove(ScheduleDateTimeIndex);
        --appInstance.schedulerDateTimeCount;

        newsEntryListView1.setAdapter(newsEntryAdapter1);
    }


    // Variable for storing current date and time
    private int mYear, mMonth, mDay, mHour, mMinute;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduling);

        appInstance = (SystemClass)getApplicationContext();

        btnCalendar = (Button) findViewById(R.id.btnCalendar);
        btnTimePicker = (Button) findViewById(R.id.btnTimePicker);
        butnsave = (Button) findViewById(R.id.butnsave);
        txtDate = (EditText) findViewById(R.id.txtDate);
        txtTime = (EditText) findViewById(R.id.txtTime);
       // editText = (EditText) findViewById(R.id.editText);
        buttoncancel =(Button) findViewById(R.id.buttoncancel);

        btnCalendar.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        butnsave.setOnClickListener(this);
        buttoncancel.setOnClickListener(this);


      /*appInstance = (SystemClass) getApplicationContext();

        Intent i = getIntent();
        deviceIndex = i.getIntExtra("deviceId", 0);

        deviceName = appInstance.deviceList.get(deviceIndex);
      *///  editText.setText(deviceName);



        // Setup the list view
        newsEntryListView1 = (ListView) findViewById(R.id.list1);
        newsEntryAdapter1 = new customScheduleListAdapter(this, R.layout.schedule_list);
        newsEntryListView1.setAdapter(newsEntryAdapter1);



        if (true)
        {
           /* mProgressDialog = new ProgressDialog(schedularActivity.this);
            mProgressDialog.setTitle("Getting schedule list....");
            mProgressDialog.setMessage("Please wait!!!");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();*/
           getScheduleDateTimeList1();
        } else
        {
            Integer loopCount;

            for (loopCount = 0; loopCount < appInstance.schedulerDateTimeCount; ++loopCount)
            {
                RowItemScheduleList entry = new RowItemScheduleList(appInstance.scheduleList.get(loopCount));
                newsEntryAdapter1.add(entry);
            }
        }

    }


    @Override
    public void onClick(View v)
    {

        if (v == btnCalendar)
        {

            // Process to get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener()
                    {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                        {
                            // Display Selected date in textbox
                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            dpd.show();
        }
        if (v == btnTimePicker)
        {

            // Process to get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog tpd = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            // Display Selected time in textbox
                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            tpd.show();
        }


        if (v == butnsave)
        {

            String scheduleDate = txtDate.getText().toString().trim();
            String scheduleTime = txtTime.getText().toString().trim();

            appInstance.scheduleList.add(scheduleDate);
            appInstance.scheduleList.add(scheduleTime);

        }

        if (v == buttoncancel)
            {

              //  finish();
            }
    }


    @Override
    protected void onDestroy() {
        am.cancel(pi);
        unregisterReceiver(br);
        super.onDestroy();
    }

    private void setup() {
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent i) {
                Toast.makeText(c, "Rise and Shine!", Toast.LENGTH_LONG).show();
            }
        };
        registerReceiver(br, new IntentFilter("com.authorwjf.wakeywakey") );
        pi = PendingIntent.getBroadcast( this, 0, new Intent("com.authorwjf.wakeywakey"),
                0 );
        am = (AlarmManager)(this.getSystemService( Context.ALARM_SERVICE ));
    }



   public void systemClassCallback(systemClassCallbacks.systemEvents event, Boolean status)
    {
        switch (event)
        {
            case SCHEDULELIST:
            {
                if ((null != ScheduleDateTimeListWaithandler) && (null != ScheduleDateTimeListWaitRunnable))
                {
                    ScheduleDateTimeListWaithandler.removeCallbacks(ScheduleDateTimeListWaitRunnable);
                }

                appInstance.systemCallback = null;
                mProgressDialog.dismiss();

                if (appInstance.schedulerDateTimeCount > 0)
                {
                    Integer loopCount;

                    for (loopCount = 0; loopCount < appInstance.schedulerDateTimeCount; ++loopCount)
                    {
                        RowItemScheduleList entry = new RowItemScheduleList(appInstance.scheduleList.get(loopCount));
                        newsEntryAdapter1.add(entry);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not found!!!", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }
}






