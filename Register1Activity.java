package com.android_examples.Connect2ControlHome;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Asha.Devaraja on 11/10/2016.
 */
public class Register1Activity extends Activity implements systemClassCallbacks {
    Button btn_popup;
    String spinner_item;
    SpinnerAdapter adapter;
    String[] title;
    SystemClass appInstance = null;
    ProgressDialog mProgressDialog;
    Handler phoneNoListWaithandler;
    Runnable phoneNoListWaitRunnable;
    ListView newsEntryListView;
    CustomPhoneNoListViewAdapter newsEntryAdapter;
    private static Register1Activity inst;
    public static Register1Activity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    public void getRegisteredPhoneNoList ()
    {
        if(true != appInstance.getPhoneNoList(this))
        {
            return;
        }

        phoneNoListWaithandler = new Handler();
        phoneNoListWaitRunnable = new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
                phoneNoListWaithandler.removeCallbacks(this);
                Toast.makeText(getApplicationContext(), "Not found!!!", Toast.LENGTH_SHORT).show();
            }
        };

        phoneNoListWaithandler.postDelayed(phoneNoListWaitRunnable, appInstance.timeToWait);
    }

    public void changeAdminStatus (Integer deviceIndex, Boolean adminFlag )
    {
        if(true != appInstance.changeAdminStatus(this, appInstance.phoneNoList.get(deviceIndex), adminFlag))
        {
            return;
        }

        appInstance.adminList.set(deviceIndex, adminFlag);
    }

    public void removePhoneNumber (Integer deviceIndex)
    {
        if(true != appInstance.removePhoneNumber(this, appInstance.phoneNoList.get(deviceIndex)))
        {
            return;
        }

        RowItemPhoneNoList entry = new RowItemPhoneNoList(appInstance.phoneNoList.get(deviceIndex), "", appInstance.adminList.get(deviceIndex) == true ? R.drawable.super_user : R.drawable.user, appInstance.adminList.get(deviceIndex));

        newsEntryAdapter.remove(entry);
        entry = newsEntryAdapter.getItem(deviceIndex);
        newsEntryAdapter.remove(entry);
        appInstance.phoneNoList.remove(deviceIndex);
        appInstance.adminList.remove(deviceIndex);
        --appInstance.phoneNosCount;

        newsEntryListView.setAdapter(newsEntryAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_main);
        appInstance = (SystemClass)getApplicationContext();

        btn_popup = (Button) findViewById(R.id.button1);
        title = getResources().getStringArray(R.array.CountryCodes);
        adapter=new SpinnerAdapter(getApplicationContext());

        // Setup the list view
        newsEntryListView = (ListView) findViewById(R.id.list);
        newsEntryAdapter = new CustomPhoneNoListViewAdapter(this, R.layout.register_list);
        newsEntryListView.setAdapter(newsEntryAdapter);

        if(true) {
            mProgressDialog = new ProgressDialog(Register1Activity.this);
            mProgressDialog.setTitle("Getting registered phone no list....");
            mProgressDialog.setMessage("Please wait!!!");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
            getRegisteredPhoneNoList();
        }
        else
        {
            Integer loopCount;

            for (loopCount = 0; loopCount < appInstance.phoneNosCount; ++loopCount) {
                RowItemPhoneNoList entry = new RowItemPhoneNoList(appInstance.phoneNoList.get(loopCount), "", appInstance.adminList.get(loopCount) == true ? R.drawable.super_user : R.drawable.user, appInstance.adminList.get(loopCount));
                newsEntryAdapter.add(entry);
            }
        }

        btn_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                final Dialog dialog = new Dialog(Register1Activity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.row_spinner);
                dialog.setCancelable(true);

                // set the custom dialog components - text, image and button
               // final Spinner spinner = (Spinner) dialog
                     //   .findViewById(R.id.spinner1);
                final EditText edittext = (EditText) dialog
                        .findViewById(R.id.editText1);

                final CheckBox Checkbox1 = (CheckBox)dialog.findViewById(R.id.checkBox2);

                Button button = (Button) dialog.findViewById(R.id.button1);

               // spinner.setAdapter(adapter);
              /*  spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        // TODO Auto-generated method stub
                        spinner_item = title[position];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub

                    }
                });*/

                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-ge§nerated method stub
                        //Toast.makeText(Register1Activity.this, "register button clicked ", Toast.LENGTH_SHORT).show();
                        String admin = "Not Admin";
                        Boolean isAdmin = false;
                        Integer loopCount;

                        if (edittext.getText().toString().isEmpty()) {
                            Toast.makeText(Register1Activity.this, "Enter valid phone no!!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        dialog.dismiss();

                        String phoneNumber = edittext.getText().toString().trim();

                        for ( loopCount  = 0; loopCount < appInstance.phoneNosCount; ++ loopCount)
                        {
                             if(phoneNumber.equals(appInstance.phoneNoList.get(loopCount)))
                             {
                                 Toast.makeText(getApplicationContext(), "PhoneNo is already added!!!", Toast.LENGTH_SHORT).show();
                                 return;
                             }
                        }

                        appInstance.phoneNoList.add(phoneNumber);
                        appInstance.phoneNoList.add(spinner_item);
                        appInstance.adminList.add(false);

                        //int cc = spinner.getSelectedItem().
                        if(Checkbox1.isChecked())
                        {
                            admin = "admin";
                            isAdmin = true;
                            appInstance.adminList.set(appInstance.phoneNosCount,false);
                        }

                        ++appInstance.phoneNosCount;

                       /* Toast.makeText(
                                getApplicationContext(),
                                spinner_item + " - "
                                        + edittext.getText().toString().trim() + " " + admin,
                                Toast.LENGTH_LONG).show();*/

                       RowItemPhoneNoList entry = new RowItemPhoneNoList(phoneNumber, "", isAdmin == true ? R.drawable.super_user : R.drawable.user, isAdmin);

                       newsEntryAdapter.add(entry);
                        newsEntryListView.setAdapter(newsEntryAdapter);

                        appInstance.addPhoneNumberToEsp(Register1Activity.this, phoneNumber, isAdmin);

                    }
                });
                dialog.show();

            }
        });
    }

    @Override
    public void onBackPressed() {


        Intent i = new Intent(Register1Activity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
  /* @Override
   public void onBackPressed()
   {
       Intent intent = new Intent(Register1Activity.this,MainActivity.class);
       intent.putExtra("Devices",1);
       startActivity(intent);
   }*/

    public class SpinnerAdapter extends BaseAdapter {
        Context context;
        private LayoutInflater mInflater;

        public SpinnerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return title.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ListContent holder;
            View v = convertView;
            if (v == null) {
                mInflater = (LayoutInflater) context
                        .getSystemService(context.LAYOUT_INFLATER_SERVICE);
                v = mInflater.inflate(R.layout.row_textview, null);
                holder = new ListContent();
                holder.text = (TextView) v.findViewById(R.id.textView1);

                v.setTag(holder);
            } else {

                holder = (ListContent) v.getTag();
            }

            holder.text.setText(title[position]);

            return v;
        }
    }

    static class ListContent {

        TextView text;
    }

    public void systemClassCallback(systemClassCallbacks.systemEvents event, Boolean status )
    {
        switch(event)
        {
            case PHONENOLIST:
            {
                if((null!=phoneNoListWaithandler)&&(null!=phoneNoListWaitRunnable)) {
                    phoneNoListWaithandler.removeCallbacks(phoneNoListWaitRunnable);
                }

                appInstance.systemCallback = null;

                mProgressDialog.dismiss();

                if ( appInstance.phoneNosCount > 0)
                {
                    Integer loopCount;

                    for (loopCount = 0; loopCount < appInstance.phoneNosCount; ++loopCount) {
                        RowItemPhoneNoList entry = new RowItemPhoneNoList(appInstance.phoneNoList.get(loopCount), "", appInstance.adminList.get(loopCount) == true ? R.drawable.super_user : R.drawable.user, appInstance.adminList.get(loopCount));
                        newsEntryAdapter.add(entry);
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
