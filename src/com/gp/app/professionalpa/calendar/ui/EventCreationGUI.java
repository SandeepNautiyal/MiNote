package com.gp.app.professionalpa.calendar.ui;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TimePicker;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.calendar.events.EventManager;

public class EventCreationGUI 
{
	 private EditText titleEditText = null;
	 private EditText locationEditText = null;
	 private Button startTimeButton = null;
	 private Button endTimeButton = null;
	 private Button saveButton = null;
	 private Button cancelButton = null;
	 private ImageButton notificationImageButton = null;
	 private ImageButton alarmImageButton = null;
	 private String fromDate = null;
	 private String fromTime = null;
	 private String toDate = null;
	 private String toTime = null;

     public void createGuiForEventAddition(Context context, final int day, final int month, final int year)
     {
 		final Dialog dialog = new Dialog(context);

    	RelativeLayout base = new RelativeLayout(context);
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	base.setLayoutParams(params);
    	base.setId(1);
    	
        LinearLayout linearLayout1 = createLinearLayoutForTimeFields(context);
		startTimeButton = createStartTimeButton(context);
		linearLayout1.addView(startTimeButton);
		
		endTimeButton = createEndTimeButton(context);
		linearLayout1.addView(endTimeButton);
		base.addView(linearLayout1);
		
    	titleEditText = createEventTitleEditText(context);
		base.addView(titleEditText);
		
		locationEditText = createEventLocationEditText(context);
		base.addView(locationEditText);
		
		LinearLayout linearLayout2 = createLinearLayoutForNotificationFields(context);
		
		notificationImageButton = createNotificationImageButton(context);
		linearLayout2.addView(notificationImageButton);
		
		alarmImageButton = createAlarmImageButton(context);
		linearLayout2.addView(alarmImageButton);

		base.addView(linearLayout2);

		saveButton = createSaveButton(context);
		saveButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				createRequestForNewEvent();
				
				dialog.dismiss();
			}
		});
		
		base.addView(saveButton);
		
		cancelButton = createCancelButton(context);
		base.addView(cancelButton);
		
	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    dialog.setContentView(base);
	    dialog.setCancelable(true);
	    dialog.show();
    }

    private LinearLayout createLinearLayoutForTimeFields(Context context) 
 	{
 		//Plus Button
 		LayoutParams linearLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 180);
 		linearLayoutParams.leftMargin = 10;
 		linearLayoutParams.rightMargin = 10;
 		linearLayoutParams.bottomMargin = 10;
 		linearLayoutParams.topMargin = 10;
 		linearLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);

 		LinearLayout timeFieldLinearLayout = new LinearLayout(context);
 		timeFieldLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
 		timeFieldLinearLayout.setId(2);
 		timeFieldLinearLayout.setBackgroundColor(Color.parseColor("#7F7CD9"));
 		timeFieldLinearLayout.setLayoutParams(linearLayoutParams);
 		return timeFieldLinearLayout;
 	}
    
    private Button createStartTimeButton(final Context context) 
 	{
 		//Plus Button
 		LinearLayout.LayoutParams hourParams = new LinearLayout.LayoutParams(245,160);
 		hourParams.leftMargin = 10;
 		hourParams.rightMargin = 20;
 		hourParams.bottomMargin = 10;
 		hourParams.topMargin = 10;

 		final Button startTimeEditText = new Button(context);
 		
 		startTimeEditText.setOnClickListener(new OnClickListener()
 		{
 			@Override
 			public void onClick(View v)
 			{
 				final Dialog dialog = new Dialog(context);
 				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
 			    dialog.setContentView(R.layout.time_picker_and_date_picker_layout);
 			    final TimePicker timePicker = (TimePicker)dialog.findViewById(R.id.timePicker1);
 			    final DatePicker datePicker = (DatePicker)dialog.findViewById(R.id.datePicker1);
 			    Button saveButton = (Button)dialog.findViewById(R.id.button1);

 			    saveButton.setOnClickListener(new OnClickListener() 
 			    {
 					@Override
 					public void onClick(View v) 
 					{
 						fromDate = new StringBuilder().append(pad(datePicker.getDayOfMonth()))
 							    .append("/").append(pad(datePicker.getMonth()+1)).append("/").append(pad(datePicker.getYear())).toString();
 					
 						fromTime = new StringBuilder().append(pad(timePicker.getCurrentHour()))
 							    .append(":").append(pad(timePicker.getCurrentMinute())).toString();
 						
 						setEventDate(true);
 						
 						dialog.dismiss();
 					}

 				});
 				
 			    dialog.setCancelable(true);
 			    dialog.show();
 			}
 		});
 		
 		startTimeEditText.setId(3);
 		startTimeEditText.setLayoutParams(hourParams);
 		startTimeEditText.setText("From");
 		
 		return startTimeEditText;
 	}
     
    private Button createEndTimeButton(final Context context) 
 	{
 		LinearLayout.LayoutParams endTimeButtonLayoutParams = new LinearLayout.LayoutParams(245,160);
 		endTimeButtonLayoutParams.topMargin = 10;
 		endTimeButtonLayoutParams.rightMargin = 10;
 		endTimeButtonLayoutParams.bottomMargin = 10;
 		final Button endTimeButton = new Button(context);
 		
 		endTimeButton.setOnClickListener(new OnClickListener()
 		{
 			@Override
 			public void onClick(View v)
 			{
 				final Dialog dialog = new Dialog(context);
 				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
 			    dialog.setContentView(R.layout.time_picker_and_date_picker_layout);
 			    
 				final TimePicker timePicker = (TimePicker)dialog.findViewById(R.id.timePicker1);
 				new StringBuilder().append(pad(timePicker.getCurrentHour()))
 			    .append(":").append(pad(timePicker.getCurrentHour())).toString();
 			    final DatePicker datePicker = (DatePicker)dialog.findViewById(R.id.datePicker1);
 			    Button saveButton = (Button)dialog.findViewById(R.id.button1);

 			    saveButton.setOnClickListener(new OnClickListener() 
 			    {
 					@Override
 					public void onClick(View v) 
 					{
 						toDate = new StringBuilder().append(pad(datePicker.getDayOfMonth()))
 							    .append("/").append(pad(datePicker.getMonth()+1)).append("/").append(pad(datePicker.getYear())).toString();
 					
 						toTime = new StringBuilder().append(pad(timePicker.getCurrentHour()))
 							    .append(":").append(pad(timePicker.getCurrentMinute())).toString();
 						setEventDate(false);
 						
 						dialog.dismiss();
 					}
 				});
 			    
 			    dialog.setCancelable(true);
 			    dialog.show();
 			}
 		});
 		
 		endTimeButton.setId(4);
 		endTimeButton.setText("To");
 		endTimeButton.setLayoutParams(endTimeButtonLayoutParams);
 		return endTimeButton;
 	}

    private void createRequestForNewEvent() 
    {
		EventManager.addEvent(titleEditText.getText().toString(), locationEditText.getText().toString(), fromDate, fromTime, toDate, toTime);	
	}

	private EditText createEventTitleEditText(Context context) 
	{
    	LayoutParams editTextParams = new LayoutParams(LayoutParams.MATCH_PARENT,250);
		EditText titleEditText = new EditText(context);
		titleEditText.setId(5);
		titleEditText.setMaxLines(4);
		titleEditText.setHint("Event Name");
		editTextParams.addRule(RelativeLayout.BELOW, 2);
		titleEditText.setLayoutParams(editTextParams);
		return titleEditText;
	}
	
	private EditText createEventLocationEditText(Context context) 
	{
    	LayoutParams editTextParams = new LayoutParams(LayoutParams.MATCH_PARENT,250);
		EditText titleEditText = new EditText(context);
		titleEditText.setId(6);
		titleEditText.setMaxLines(2);
		titleEditText.setHint("Location");
		editTextParams.addRule(RelativeLayout.BELOW, 5);
		titleEditText.setLayoutParams(editTextParams);
		return titleEditText;
	}
	
	private LinearLayout createLinearLayoutForNotificationFields(Context context) 
	{
		//Plus Button
		LayoutParams linearLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 120);
		linearLayoutParams.leftMargin = 10;
		linearLayoutParams.rightMargin = 10;
		linearLayoutParams.bottomMargin = 10;
		linearLayoutParams.topMargin = 10;
		linearLayoutParams.addRule(RelativeLayout.BELOW, 6);

		LinearLayout timeFieldLinearLayout = new LinearLayout(context);
		timeFieldLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		timeFieldLinearLayout.setId(7);
		timeFieldLinearLayout.setBackgroundColor(Color.parseColor("#7F7CD9"));
		timeFieldLinearLayout.setLayoutParams(linearLayoutParams);
		return timeFieldLinearLayout;
	}
	
	private void setEventDate(boolean isFromDate) 
	{
		if (isFromDate) 
		{
			startTimeButton.setText(fromDate+"  "+fromTime);
		} 
		else 
		{
			endTimeButton.setText(toDate+"  "+toTime);
		}
	}
	
	private ImageButton createNotificationImageButton(Context context) 
	{
		LinearLayout.LayoutParams notificationButtonLayoutParams = new LinearLayout.LayoutParams(70,70);
		notificationButtonLayoutParams.topMargin = 25;
		notificationButtonLayoutParams.rightMargin = 30;
		notificationButtonLayoutParams.bottomMargin = 25;
		final ImageButton notificationImageButton = new ImageButton(context);
		notificationImageButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
			}
		});
		
		notificationImageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_event));
		notificationImageButton.setBackgroundResource(R.drawable.day_selected);
		notificationImageButton.setId(8);
		notificationImageButton.setLayoutParams(notificationButtonLayoutParams);
		return notificationImageButton;
	}
	
	private ImageButton createAlarmImageButton(Context context) 
	{
		LinearLayout.LayoutParams alarmImageButtonLayoutParams = new LinearLayout.LayoutParams(70,70);
		alarmImageButtonLayoutParams.topMargin = 25;
		alarmImageButtonLayoutParams.bottomMargin = 25;
		final ImageButton alarmImageButton = new ImageButton(context);
		alarmImageButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
			}
		});
		alarmImageButton.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_add_alarm));
		alarmImageButton.setBackgroundResource(R.drawable.day_selected);
		alarmImageButton.setId(9);
		alarmImageButton.setLayoutParams(alarmImageButtonLayoutParams);
		return alarmImageButton;
	}
	
	private Button createSaveButton(Context context) 
	{
		LayoutParams saveEditTextLayoutParams = new LayoutParams(75,75);
		saveEditTextLayoutParams.leftMargin = 10;
		saveEditTextLayoutParams.topMargin = 10;
		saveEditTextLayoutParams.rightMargin = 10;
		saveEditTextLayoutParams.bottomMargin = 10;
		saveEditTextLayoutParams.addRule(RelativeLayout.BELOW, 7);
    	Button saveImageView = new Button(context);
		String saveString = context.getResources().getString(R.string.check_mark);
		saveImageView.setText(saveString);
		saveImageView.setBackgroundResource(R.drawable.day_selected);
		saveImageView.setId(10);
		saveImageView.setLayoutParams(saveEditTextLayoutParams);
		return saveImageView;
	}
	
	private Button createCancelButton(Context context) 
	{
		LayoutParams cancelEditTextLayoutParams = new LayoutParams(75,75);
		cancelEditTextLayoutParams.leftMargin = 10;
		cancelEditTextLayoutParams.topMargin = 10;
		cancelEditTextLayoutParams.rightMargin = 10;
		cancelEditTextLayoutParams.bottomMargin = 10;
		cancelEditTextLayoutParams.addRule(RelativeLayout.BELOW, 7);
		cancelEditTextLayoutParams.addRule(RelativeLayout.RIGHT_OF, 10);
		cancelEditTextLayoutParams.addRule(RelativeLayout.ALIGN_BASELINE, 10);
		Button cancelImageView = new Button(context);
		cancelImageView.setId(11);
//		cancelImageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.day_selected));
		String cancelString = context.getResources().getString(R.string.cross_mark);
		cancelImageView.setText(cancelString);
		cancelImageView.setBackgroundResource(R.drawable.day_selected);
		cancelImageView.setLayoutParams(cancelEditTextLayoutParams);
		return cancelImageView;
	}
	
	private static String pad(int c) 
	{
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}
}
