package com.gp.app.professionalpa.notes.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.data.Event;

public class EventNoteFragmentAdapter extends BaseAdapter
{
    private Event event = null;
	
    private Context context = null;
    
	public EventNoteFragmentAdapter(Context context, Event event) 
	{
		this.event = event;
		
		this.context = context;
	}

	@Override
	public int getCount() 
	{
		return 1;
	}

	@Override
	public Object getItem(int position) 
	{
		return event;
	}

	@Override
	public long getItemId(int position) 
	{
		return event.getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		//TODO improve 1) introduce convert view reusing 2) if 1st cannot be done remove viewholder.
		convertView = LayoutInflater.from(context).inflate(R.layout.event_adapter_layout, null, false);
		
		TextView eventNameTextView = (TextView) convertView.findViewById(R.id.eventNameTextView);
		
		eventNameTextView.setText(event.getEventName());
		
		TextView eventLocationTextView = (TextView) convertView.findViewById(R.id.eventLocationTextView);

		eventLocationTextView.setText(event.getLocation());

		TextView editStartTime = (TextView) convertView.findViewById(R.id.eventStartTimeTextView);

		editStartTime.setText(event.getStartDate()+"\n"+event.getStartTime());

		TextView editEndTime = (TextView) convertView.findViewById(R.id.eventEndTimeTextView);
		
		editEndTime.setText(event.getEndDate()+" \n "+event.getEndTime());

		return convertView;
	}

}
