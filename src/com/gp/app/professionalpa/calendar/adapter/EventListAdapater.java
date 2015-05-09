package com.gp.app.professionalpa.calendar.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.calendar.events.Event;

public class EventListAdapater extends ArrayAdapter<Event>
{
	private List<Event> events = null;
	
	public EventListAdapater(Context context, List<Event> events)
	{
		super(context, 0, events);
		
		this.events = events;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		//TODO improve 1) introduce convert view reusing 2) if 1st cannot be done remove viewholder.
	    Event event = events.get(position);
	    
		convertView = LayoutInflater.from(getContext()).inflate(R.layout.events_modification_dialog_layout, parent, false);
		
		TextView eventName = (TextView) convertView.findViewById(R.id.eventsModificationDialogEventNameTextView);
		
		eventName.setText(event.getEventName());
		
        TextView eventLocation = (TextView) convertView.findViewById(R.id.eventsModificationDialogEventLocationTextView);
		
        eventLocation.setText(event.getLocation());
        
        TextView eventStartTime = (TextView) convertView.findViewById(R.id.eventsModificationFromTimeTextView);
		
        eventStartTime.setText(event.getStartTime());
        
        TextView eventEndTime = (TextView) convertView.findViewById(R.id.eventsModificationToTimeTextView);
		
        eventEndTime.setText(event.getEndTime());
		
	    return convertView;
	}
}
