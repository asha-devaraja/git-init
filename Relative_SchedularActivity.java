package com.android_examples.Connect2ControlHome;

/**
 * Created by Asha.Devaraja on 1/2/2017.
 */
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class Relative_SchedularActivity extends Activity implements OnCheckedChangeListener {
    private Context ctx;
    private CheckBox chk_monday, chk_tuesday, chk_wednesday, chk_thursday, chk_friday, chk_sat, chk_sunday;
    private Button btnSetAlarm, btnQuitAlarm;
    private TextView alarmTitle;
    private TimePicker tpTime;
    private boolean timerflag = false;
    private AlarmManager alarmManager;
    private PendingIntent operation, operation1, operation2, operation3, operation4, operation5, operation6;
    private Intent i;
    TextView textView;
    SystemClass appInstance = null;
    Integer deviceIndex;
    String deviceName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relative_schedule);

        appInstance = (SystemClass) getApplicationContext();

        Intent i = getIntent();
        deviceIndex = i.getIntExtra("deviceId", 0);

        deviceName = appInstance.deviceList.get(deviceIndex);

        ctx = this;
        setMapping();
        setListener();
    }

    private void setListener() {

        btnSetAlarm.setOnClickListener(setClickListener);

        btnQuitAlarm.setOnClickListener(quitClickListener);

        chk_monday.setOnCheckedChangeListener(this);

        chk_tuesday.setOnCheckedChangeListener(this);

        chk_wednesday.setOnCheckedChangeListener(this);

        chk_thursday.setOnCheckedChangeListener(this);

        chk_friday.setOnCheckedChangeListener(this);

        chk_sat.setOnCheckedChangeListener(this);

        chk_sunday.setOnCheckedChangeListener(this);

        tpTime.setOnTimeChangedListener(new OnTimeChangedListener() {

            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                // TODO Auto-generated method stub

                timerflag = true;

            }

        });

    }

    OnClickListener quitClickListener = new OnClickListener() {

        public void onClick(View v) {

            finish();

        }

    };

    private void setMapping() {

        tpTime = (TimePicker) findViewById(R.id.tp_time);
        textView = (TextView) findViewById(R.id.ScheduleComponent);
        btnSetAlarm = (Button) findViewById(R.id.btn_set_alarm);
        btnQuitAlarm = (Button) findViewById(R.id.btn_quit_alarm);
        chk_monday = (CheckBox) findViewById(R.id.chk_monday);
        chk_tuesday = (CheckBox) findViewById(R.id.chk_tuesday);
        chk_wednesday = (CheckBox) findViewById(R.id.chk_wednesday);
        chk_thursday = (CheckBox) findViewById(R.id.chk_thursday);
        chk_friday = (CheckBox) findViewById(R.id.chk_friday);
        chk_sat = (CheckBox) findViewById(R.id.chk_sat);
        chk_sunday = (CheckBox) findViewById(R.id.chk_sunday);

        textView.setText(deviceName);

    }

    OnClickListener setClickListener = new OnClickListener() {

        public void onClick(View v) {

            if (!timerflag) {
                Toast.makeText(ctx, "Please first select time", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(ctx, "Alarm set successfully", Toast.LENGTH_LONG).show();
            }

        }

    };

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        // TODO Auto-generated method stub

        if (timerflag) {

            if (buttonView == chk_monday) {

                if (isChecked) {

                } else {

                }

            } else if (buttonView == chk_tuesday) {

                if (isChecked) {


                } else {


                }

            } else if (buttonView == chk_wednesday) {

                if (isChecked) {

                } else {

                }

            } else if (buttonView == chk_thursday) {

                if (isChecked) {

                } else {
                }

            } else if (buttonView == chk_friday) {

                if (isChecked) {

                } else {
                }

            } else if (buttonView == chk_sat) {

                if (isChecked) {
                } else {
                }

            } else if (buttonView == chk_sunday) {

                if (isChecked) {
                } else {
                }

            }

        } else {

            buttonView.setChecked(false);

            Toast.makeText(ctx, "Please first select time", Toast.LENGTH_LONG).show();

        }

    }


}

