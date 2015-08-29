package com.gp.app.minote.calendar.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gp.app.minote.calendar.adapter.EventListAdapater;
import com.gp.app.minote.calendar.events.EventManager;
import com.gp.app.minote.calendar.events.database.CalendarDBManager;
import com.gp.app.minote.calendar.interfaces.DBChangeListener;
import com.gp.app.minote.data.Event;

import java.util.List;

public class EventModificationGUI implements DBChangeListener
{
	private EventListAdapater listAdapter = null;
	
	private ListView listView = null; 
	
	public EventModificationGUI()
	{
		CalendarDBManager.getInstance().addDataChangeListener(this);
	}
	
    public void createEventModificationList(Context context, int day, int month, int year)
    {
    	String startDay = pad(day)+"/"+ pad(month)+"/"+Integer.toString(year);
    	List<Event> events = EventManager.getEvents(startDay);
    	
    	LinearLayout linearLayout = new LinearLayout(context);
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 400);
    	linearLayout.setLayoutParams(params);
    	
    	listView = new ListView(context);
    	
    	LayoutParams listViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	listView.setLayoutParams(listViewParams);
    	linearLayout.addView(listView);
    	
    	Dialog dialog = new Dialog(context);
	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    dialog.setContentView(linearLayout);
	    listAdapter = new EventListAdapater(context, events);
	    listView.setAdapter(listAdapter);
	    dialog.setCancelable(true);
	    dialog.show();
    }
    
    private static String pad(int c) 
	{
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}

	@Override
	public void recieveNotification(Event event) 
	{
		listAdapter.remove(event);
		listAdapter.add(event);
		listAdapter.notifyDataSetChanged();
	}
}
