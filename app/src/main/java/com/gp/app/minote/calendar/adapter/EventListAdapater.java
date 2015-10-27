package com.gp.app.minote.calendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.gp.app.minote.R;
import com.gp.app.minote.calendar.events.database.CalendarDBManager;
import com.gp.app.minote.calendar.ui.EventCreationUI;
import com.gp.app.minote.data.Event;

import java.util.List;

public class EventListAdapater extends ArrayAdapter<Event>
{
	private final boolean isReadOnly;

	private List<Event> events = null;
	
	private Context context = null;
	
	public EventListAdapater(Context context, List<Event> events, boolean isReadOnly)
	{
		super(context, 0, events);
		
		this.context  = context;
		
		this.events = events;

		this.isReadOnly = isReadOnly;
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

		Button editButton = (Button) convertView.findViewById(R.id.eventsDialogEditButton);

        Button cancelButton = (Button) convertView.findViewById(R.id.eventsDialogCancelButton);

		if(!isReadOnly)
		{
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

					CalendarDBManager.getInstance().deleteEvent(event);
				}
			});
		}
		else
		{
			cancelButton.setVisibility(View.GONE);

			editButton.setVisibility(View.GONE);
		}
        
	    return convertView;
	}

	private void createEventEditGUI(Event event) 
	{
		EventCreationUI eventCreationGUI = new EventCreationUI(context);

		eventCreationGUI.createEventUI(true);

		eventCreationGUI.setEvent(event);
	}
}
