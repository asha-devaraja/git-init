package com.android_examples.Connect2ControlHome;

/**
 * Created by Asha.Devaraja on 11/16/2016.
 */

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class viewHolder {
    public TextView text;
  //  public Switch aswitch;
  // public CheckBox checkbox;
    public ImageView imageview;

    public viewHolder(View v) {
        this.text = (TextView)v.findViewById(R.id.text1);
      //  this.aswitch=(Switch)v.findViewById(R.id.switch1);
       //this.checkbox = (CheckBox)v.findViewById(R.id.cbx1);
        this.imageview = (ImageView)v.findViewById(R.id.ImageToogle);
    }

}
