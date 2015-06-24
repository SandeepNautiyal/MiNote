package com.gp.app.professionalpa.calendar.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.calendar.events.Event;
import com.gp.app.professionalpa.calendar.events.database.CalendarDBManager;
import com.gp.app.professionalpa.calendar.ui.EventCreationGUI;

public class EventListAdapater extends ArrayAdapter<Event>
{
	private List<Event> events = null;
	
	private Context context = null;
	
	public EventListAdapater(Context context, List<Event> events)
	{
		super(context, 0, events);
		
		this.context  = context;
		
		this.events = events;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		//TODO improve 1) introduce convert view reusing 2) if 1st cannot be done remove viewholder.
	    final Event event = events.get(position);
	    
		convertView = LayoutInflater.from(getContext()).inflate(R.layout.events_modification_dialog_layout, parent, false);
		
		TextView eventName = (TextView) convertView.findViewById(R.id.eventsModificationDialogEventNameTextView);
		
		eventName.setText(event.getEventName());
		
        TextView eventLocation = (TextView) convertView.findViewById(R.id.eventsModificationDialogEventLocationTextView);
		
        eventLocation.setText(event.getLocation());
        
        TextView eventStartTime = (TextView) convertView.findViewById(R.id.eventsModificationFromTimeTextView);
		
        eventStartTime.setText(event.getStartTime());
        
        TextView eventEndTime = (TextView) convertView.findViewById(R.id.eventsModificationToTimeTextView);
		
        eventEndTime.setText(event.getEndTime());
         
        Button cancelButton = (Button) convertView.findViewById(R.id.eventsDialogCancelButton);
		
        convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				createEventEditGUI(event);
			}
		});
        
        cancelButton.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v) 
			{
				remove(event);
				
				notifyDataSetChanged();
				
				CalendarDBManager.getInstance().deleteEvent(event.getEventId());
			}
		});
        
	    return convertView;
	}

	private void createEventEditGUI(Event event) 
	{
		String date = event.getStartDate();
		
		String [] dateAsArray = date.split("/");
		
		EventCreationGUI eventCreationGUI = new EventCreationGUI();
		
		eventCreationGUI.createGuiForEventAddition(context, Integer.valueOf(dateAsArray[0]), Integer.valueOf(dateAsArray[1]), Integer.valueOf(dateAsArray[2]), EventCreationGUI.CREATE_GUI_IN_EDIT_MODE);
		
		eventCreationGUI.setEventId(event.getEventId());
		
		eventCreationGUI.setEventName(event.getEventName());
		
		eventCreationGUI.setLocation(event.getLocation());
		
		eventCreationGUI.setStartTime(event.getStartDate(), event.getStartTime());
		
		eventCreationGUI.setEndTime(event.getEndDate(), event.getEndTime());
		
		if(event.isAlarmActivated())
		{
			eventCreationGUI.activateAlarm();
		}
	}
}
