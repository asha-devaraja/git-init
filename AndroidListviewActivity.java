package com.android_examples.Connect2ControlHome;

/**
 * Created by Asha.Devaraja on 8/23/2016.
 */

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AndroidListviewActivity extends ListActivity {

    Integer espIndex = 0;
    Integer deviceIndex;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // storing string resources into Array
    //    String[] adobe_products = getResources().getStringArray(R.array.adobe_products);

        // Binding resources Array to ListAdapter
     //   this.setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item_devicelist, R.id.label, adobe_products));

        ListView lv = getListView();

        Intent i = getIntent();
        espIndex = i.getIntExtra("espId", 0);
        deviceIndex = i.getIntExtra("deviceId", 0);

        // listening to single list item on click
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String myString = (String) parent.getAdapter().getItem(position);
                if(myString.equals("Schedule")) {

                 //  Intent i1 = new Intent(AndroidListviewActivity.this, SchedularActivity.class);
                 //  startActivity(i1);
                }

                String myString1 = (String) parent.getAdapter().getItem(position);
                if(myString.equals("Edit Device")) {

                   // Intent i1 = new Intent(AndroidListviewActivity.this, EditdeviceActivity.class);
                //    i1.putExtra("espId", espIndex);
                 //   i1.putExtra("deviceId", deviceIndex);
                //    startActivity(i1);
                }


                // selected item
              /*  String product = ((TextView) view).getText().toString();

                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), SingleListItem.class);
                // sending data to new activity
                i.putExtra("product", product);
                startActivity(i);*/

                /*onBackPressed();
                return;*/
                //finish();

            }
        });
    }
}