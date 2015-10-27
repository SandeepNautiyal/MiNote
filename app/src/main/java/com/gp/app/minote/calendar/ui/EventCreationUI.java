package com.gp.app.minote.calendar.ui;


import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gp.app.minote.R;
import com.gp.app.minote.calendar.events.EventManager;
import com.gp.app.minote.data.Event;
import com.gp.app.minote.util.MiNoteParameters;
import com.gp.app.minote.util.MiNoteUtil;

public class EventCreationUI
{
    private static final byte START_DATE_CLICKED = 1;

    private static final byte END_DATE_CLICKED = 2;

    private Context context = null;

    private EditText eventNameEditText;

    private EditText eventLocationEditText;

    private CheckBox everyDayCheckBox;

    private CheckBox notificationCheckBox;

    private TextView startDate;

    private TextView endDate;

    private TextView startTime;

    private TextView endTime;

    private Button saveButton;

    private Button discardButton;

    private byte dateClicked = -1;

    private Event event;

    public EventCreationUI(Context context)
    {
        this.context = context;
    }

    public void createEventUI(final boolean createGUIInEditMode)
    {
        this.context = context;
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        view.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.event_gui);
        dialog.setCancelable(true);
        dialog.show();

        eventNameEditText =  (EditText)dialog.findViewById(R.id.eventName);

        eventLocationEditText =  (EditText)dialog.findViewById(R.id.eventLocation);

        everyDayCheckBox = (CheckBox)dialog.findViewById(R.id.everyDayCheck);

        notificationCheckBox = (CheckBox)dialog.findViewById(R.id.notificationDayCheck);

        startDate = (TextView)dialog.findViewById(R.id.startDate);

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dateClicked = START_DATE_CLICKED;
                createCalendar();
            }
        });

        endDate = (TextView)dialog.findViewById(R.id.endDate);

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateClicked = END_DATE_CLICKED;
                createCalendar();
            }
        });

        startTime = (TextView)dialog.findViewById(R.id.startTime);

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.analog_clock_layout);
                final TimePicker timePicker = (TimePicker)dialog.findViewById(R.id.timePicker);
                Button saveButton = (Button)dialog.findViewById(R.id.saveButton);

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String fromTime = new StringBuilder().append(pad(timePicker.getCurrentHour()))
                                .append(":").append(pad(timePicker.getCurrentMinute())).toString();

                        startTime.setText(fromTime);

                        dialog.dismiss();
                    }

                });

                dialog.setCancelable(true);
                dialog.show();
            }
        });

        endTime = (TextView)dialog.findViewById(R.id.endTime);

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.analog_clock_layout);
                final TimePicker timePicker = (TimePicker)dialog.findViewById(R.id.timePicker);
                Button saveButton = (Button)dialog.findViewById(R.id.saveButton);

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String fromTime = new StringBuilder().append(pad(timePicker.getCurrentHour()))
                                .append(":").append(pad(timePicker.getCurrentMinute())).toString();

                        endTime.setText(fromTime);

                        dialog.dismiss();
                    }

                });

                dialog.setCancelable(true);
                dialog.show();
            }
        });

        saveButton = (Button)dialog.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(createRequestForNewEvent(createGUIInEditMode))
                {
                    dialog.dismiss();
                }
            }
        });

        discardButton = (Button)dialog.findViewById(R.id.dimissButton);

        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

    }



    private void createCalendar()
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MiNoteCalendar noteCalendar = new MiNoteCalendar(context);
        noteCalendar.setDateClickListener(new CalendarDateClickListener() {
            @Override
            public void clickedDate(int day, int month, int year) {
                if (dateClicked == START_DATE_CLICKED) {
                    startDate.setText(MiNoteUtil.pad(day) + "/" + MiNoteUtil.pad(month) + "/" + MiNoteUtil.pad(year));
                } else if (dateClicked == END_DATE_CLICKED) {
                    endDate.setText(MiNoteUtil.pad(day) + "/" + MiNoteUtil.pad(month) + "/" + MiNoteUtil.pad(year));
                }

                dialog.dismiss();
            }
        });

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        noteCalendar.setLayoutParams(params);
//              view.setGravity(Gravity.CENTER);
        dialog.setContentView(noteCalendar);
        dialog.setCancelable(true);
        dialog.show();
    }

    private boolean createRequestForNewEvent(boolean createGUIInEditMode)
    {
    	boolean result = false;

    	long startTime = MiNoteUtil.createTime(getStartDate(), getStartTime());

    	long endTime = MiNoteUtil.createTime(getEndDate(), getEndTime());

    	if(startTime != 0l && endTime != 0l)
    	{
    		if(endTime >= startTime)
        	{
                if(event == null)
                {
                    event = new Event(getEventName(), getEventLocation(),
                            getStartDate(), getStartTime(), getEndDate(), getEndTime());
                }
                else
                {
                    int eventId = event.getId();

                    event = new Event(getEventName(), getEventLocation(),
                            getStartDate(), getStartTime(), getEndDate(), getEndTime());

                    event.setEventId(eventId);
                }

                EventManager.addEvent(event, createGUIInEditMode);

        		result = true;
        	}
        	else
        	{
        		Toast.makeText(MiNoteParameters.getApplicationContext(), "event start time "+getStartDate()+
                                " "+getStartTime()+" should be less event end time"+getEndDate()+
                                " "+getEndTime(), Toast.LENGTH_LONG).show();

        		result = false;
        	}
    	}
    	else
    	{
    		Toast.makeText(MiNoteParameters.getApplicationContext(), "Invalid event start or end time",
    				Toast.LENGTH_LONG).show();

    		result = false;
    	}

    	return result;
    }


    private String getEventName()
    {
        return eventNameEditText.getText().toString();
    }

    private String getEventLocation()
    {
        return eventLocationEditText.getText().toString();
    }

    private boolean getEveryDayCheckBox()
    {
        return everyDayCheckBox.isChecked();
    }

    private boolean getNotificationCheckBox()
    {
        return notificationCheckBox.isChecked();
    }

    private String getStartDate()
    {
        return startDate.getText().toString();
    }

    private String getEndDate()
    {
        return endDate.getText().toString();
    }

    private String getStartTime()
    {
        return startTime.getText().toString();
    }

    private String getEndTime()
    {
        return endTime.getText().toString();
    }

    private static String pad(int c)
	{
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}

    public void setEvent(Event event)
    {
        this.event = event;

        eventNameEditText.setText(event.getEventName());

        eventLocationEditText.setText(event.getLocation());

        startDate.setText(event.getStartDate());

        endDate.setText(event.getEndDate());

        startTime.setText(event.getStartTime());

        endTime.setText(event.getEndTime());


    }
}
