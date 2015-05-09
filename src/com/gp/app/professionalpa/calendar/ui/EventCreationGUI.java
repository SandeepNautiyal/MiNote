package com.gp.app.professionalpa.calendar.ui;

import java.util.Calendar;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TimePicker;
import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.calendar.events.EventManager;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

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

     public void createGuiForEventAddition(Context context, final int day, final int month, final int year)
     {
 		final Dialog dialog = new Dialog(context);

    	RelativeLayout base = new RelativeLayout(context);
    	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	base.setLayoutParams(params);
    	
    	titleEditText = createEventTitleEditText(context);
		base.addView(titleEditText);
		
		locationEditText = createEventLocationEditText(context);
		base.addView(locationEditText);
		
		LinearLayout linearLayout = createLinearLayoutForTimeFields(context);
		
		startTimeButton = createStartTimeButton(context);
		linearLayout.addView(startTimeButton);
		
		endTimeButton = createEndTimeButton(context);
		linearLayout.addView(endTimeButton);
		
		notificationImageButton = createNotificationImageButton(context);
		linearLayout.addView(notificationImageButton);
		
		alarmImageButton = createAlarmImageButton(context);
		linearLayout.addView(alarmImageButton);

		base.addView(linearLayout);

		saveButton = createSaveButton(context);
		saveButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				createRequestForNewEvent(day, month, year);
				
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

    private void createRequestForNewEvent(int day, int month, int year) 
    {
    	String startDay = pad(day)+"/"+ pad(month)+"/"+Integer.toString(year);
    	
    	String startTimeToken = startTimeButton.getText().toString();

    	String endDate = pad(day)+ "/" +pad(month) + "/" +Integer.toString(year);
    	
    	String endTimeToken = endTimeButton.getText().toString();

		EventManager.addEvent(titleEditText.getText().toString(), locationEditText.getText().toString(), startDay, startTimeToken, endDate, endTimeToken);	
	}

	private EditText createEventTitleEditText(Context context) 
	{
    	LayoutParams editTextParams = new LayoutParams(LayoutParams.MATCH_PARENT,250);
		EditText titleEditText = new EditText(context);
		titleEditText.setId(1);
		titleEditText.setMaxLines(4);
		titleEditText.setHint("Event Name");
		titleEditText.setLayoutParams(editTextParams);
		return titleEditText;
	}
	
	private EditText createEventLocationEditText(Context context) 
	{
    	LayoutParams editTextParams = new LayoutParams(LayoutParams.MATCH_PARENT,250);
		EditText titleEditText = new EditText(context);
		titleEditText.setId(2);
		titleEditText.setMaxLines(2);
		titleEditText.setHint("Location");
		editTextParams.addRule(RelativeLayout.BELOW, 1);
		titleEditText.setLayoutParams(editTextParams);
		return titleEditText;
	}
	
	private LinearLayout createLinearLayoutForTimeFields(Context context) 
	{
		//Plus Button
		LayoutParams linearLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 120);
		linearLayoutParams.leftMargin = 10;
		linearLayoutParams.rightMargin = 10;
		linearLayoutParams.bottomMargin = 10;
		linearLayoutParams.topMargin = 10;
		linearLayoutParams.addRule(RelativeLayout.BELOW, 2);

		LinearLayout timeFieldLinearLayout = new LinearLayout(context);
		timeFieldLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		timeFieldLinearLayout.setId(10);
		timeFieldLinearLayout.setBackgroundColor(Color.parseColor("#7F7CD9"));
		timeFieldLinearLayout.setLayoutParams(linearLayoutParams);
		return timeFieldLinearLayout;
	}
	
	private Button createStartTimeButton(Context context) 
	{
		//Plus Button
		LinearLayout.LayoutParams hourParams = new LinearLayout.LayoutParams(140,100);
		hourParams.leftMargin = 10;
		hourParams.rightMargin = 30;
		hourParams.bottomMargin = 10;
		hourParams.topMargin = 10;

		final Button startTimeEditText = new Button(context);
		
		startTimeEditText.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				final TimePickerDialog.OnTimeSetListener timePickerListener = 
			            new TimePickerDialog.OnTimeSetListener() {
					
					public void onTimeSet(TimePicker view, final int selectedHour,
							final int selectedMinute) 
					{
						// set current time into textview
						startTimeEditText.setText(new StringBuilder().append(pad(selectedHour))
								.append(":").append(pad(selectedMinute)));
					}
				};
				
				final TimePickerFragment newFragment = new TimePickerFragment(timePickerListener);

			    newFragment.show(ProfessionalPAParameters.getNotesActivity().getFragmentManager(), "timePicker");
			}
		});
		
		startTimeEditText.setId(3);
		startTimeEditText.setLayoutParams(hourParams);
		startTimeEditText.setText("From");
		
		return startTimeEditText;
	}
	
	private Button createEndTimeButton(Context context) 
	{
		LinearLayout.LayoutParams endTimeEditTextLayoutParams = new LinearLayout.LayoutParams(140,100);
		endTimeEditTextLayoutParams.topMargin = 10;
		endTimeEditTextLayoutParams.rightMargin = 30;
		endTimeEditTextLayoutParams.bottomMargin = 10;
//		endTimeEditTextLayoutParams.addRule(RelativeLayout.BELOW, 2);
//		endTimeEditTextLayoutParams.addRule(RelativeLayout.ALIGN_BASELINE, 3);
		final Button endTimeEditText = new Button(context);
		
		endTimeEditText.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				final TimePickerDialog.OnTimeSetListener timePickerListener = 
			            new TimePickerDialog.OnTimeSetListener() {
					
					public void onTimeSet(TimePicker view, final int selectedHour,
							final int selectedMinute) {
						
						// set current time into textview
						endTimeEditText.setText(new StringBuilder().append(pad(selectedHour))
								.append(":").append(pad(selectedMinute)));
					}
				};
				
				final TimePickerFragment newFragment = new TimePickerFragment(timePickerListener);

			    newFragment.show(ProfessionalPAParameters.getNotesActivity().getFragmentManager(), "timePicker");
			}
		});
		
		endTimeEditText.setId(4);
		endTimeEditText.setText("To");
		endTimeEditText.setLayoutParams(endTimeEditTextLayoutParams);
		return endTimeEditText;
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
		notificationImageButton.setId(5);
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
		alarmImageButton.setId(6);
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
		saveEditTextLayoutParams.addRule(RelativeLayout.BELOW, 10);
    	Button saveImageView = new Button(context);
		String saveString = context.getResources().getString(R.string.check_mark);
		saveImageView.setText(saveString);
		saveImageView.setBackgroundResource(R.drawable.day_selected);
		saveImageView.setId(7);
		saveImageView.setLayoutParams(saveEditTextLayoutParams);
		return saveImageView;
	}
	
	private Button createCancelButton(Context context) {
		LayoutParams cancelEditTextLayoutParams = new LayoutParams(75,75);
		cancelEditTextLayoutParams.leftMargin = 10;
		cancelEditTextLayoutParams.topMargin = 10;
		cancelEditTextLayoutParams.rightMargin = 10;
		cancelEditTextLayoutParams.bottomMargin = 10;
		cancelEditTextLayoutParams.addRule(RelativeLayout.BELOW, 10);
		cancelEditTextLayoutParams.addRule(RelativeLayout.RIGHT_OF, 7);
		cancelEditTextLayoutParams.addRule(RelativeLayout.ALIGN_BASELINE, 7);
		Button cancelImageView = new Button(context);
		cancelImageView.setId(8);
//		cancelImageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.day_selected));
		String cancelString = context.getResources().getString(R.string.cross_mark);
		cancelImageView.setText(cancelString);
		cancelImageView.setBackgroundResource(R.drawable.day_selected);
		cancelImageView.setLayoutParams(cancelEditTextLayoutParams);
		return cancelImageView;
	}
	
	private class TimePickerFragment extends DialogFragment
	{
		private OnTimeSetListener timeListener = null;
		
		public TimePickerFragment(OnTimeSetListener timePickerListener) 
		{
			timeListener = timePickerListener;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int selectedHour = c.get(Calendar.HOUR_OF_DAY);
			int selectedMin = c.get(Calendar.MINUTE);
			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), timeListener, selectedHour, selectedMin,
					DateFormat.is24HourFormat(getActivity()));
		}
    }
	
	private static String pad(int c) 
	{
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}
}
