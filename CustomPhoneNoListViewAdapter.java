package com.android_examples.Connect2ControlHome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

/**
 * Adapts RowItemPhoneNoList objects onto views for lists
 */
public final class CustomPhoneNoListViewAdapter extends ArrayAdapter<RowItemPhoneNoList> {

	private final int newsItemLayoutResource;

	public CustomPhoneNoListViewAdapter(final Context context, final int newsItemLayoutResource)
	{
		super(context, 0);
		this.newsItemLayoutResource = newsItemLayoutResource;
	}

	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		
		// We need to get the best view (re-used if possible) and then
		// retrieve its corresponding ViewHolder, which optimizes lookup efficiency
		final View view = getWorkingView(convertView);
		final ViewHolder viewHolder = getViewHolder(view);
		final RowItemPhoneNoList entry = getItem(position);
		
		// Setting the title view is straightforward
		viewHolder.titleView.setText(entry.getTitle());
		viewHolder.cb.setChecked(entry.getAdminFlag());
		// Setting image view is also simple
		viewHolder.cb.setOnCheckedChangeListener(
				new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (buttonView.isPressed()) {
							Register1Activity inst = Register1Activity.instance();
							inst.changeAdminStatus(position, viewHolder.cb.isChecked());
						}
					}
				});

		viewHolder.titleView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//Toast.makeText(getContext(), "clicked " +position, Toast.LENGTH_SHORT).show();
				AlertDialog.Builder alert = new AlertDialog.Builder(
						getContext());
				alert.setTitle("Alert!!");
				alert.setMessage("Are you sure to delete record");
				alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//do your work here
						dialog.dismiss();
						Register1Activity inst = Register1Activity.instance();
						inst.removePhoneNumber(position);
					}
				});
				alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});

				alert.show();
			}
		});

		return view;
	}




	private View getWorkingView(final View convertView) {
		// The workingView is basically just the convertView re-used if possible
		// or inflated new if not possible
		View workingView = null;
		
		if(null == convertView) {
			final Context context = getContext();
			final LayoutInflater inflater = (LayoutInflater)context.getSystemService
		      (Context.LAYOUT_INFLATER_SERVICE);
			
			workingView = inflater.inflate(newsItemLayoutResource, null);
		} else {
			workingView = convertView;
		}
		
		return workingView;
	}
	
	private ViewHolder getViewHolder(final View workingView) {
		// The viewHolder allows us to avoid re-looking up view references
		// Since views are recycled, these references will never change
		final Object tag = workingView.getTag();
		ViewHolder viewHolder = null;
		
		
		if(null == tag || !(tag instanceof ViewHolder)) {
			viewHolder = new ViewHolder();
			
			viewHolder.titleView = (TextView) workingView.findViewById(R.id.news_entry_title);
			viewHolder.cb = (CheckBox) workingView.findViewById(R.id.cb);
			workingView.setTag(viewHolder);


		} else {
			viewHolder = (ViewHolder) tag;
		}
		
		return viewHolder;
	}
	
	/**
	 * ViewHolder allows us to avoid re-looking up view references
	 * Since views are recycled, these references will never change
	 */
	private static class ViewHolder {
		public TextView titleView;
		CheckBox cb;
	}
	
	
}