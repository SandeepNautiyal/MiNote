package com.gp.app.professionalpa.calendar.ui;

import java.util.ArrayList;
import java.util.List;

import com.gp.app.professionalpa.calendar.adapter.EventListAdapater;
import com.gp.app.professionalpa.calendar.events.Event;
import com.gp.app.professionalpa.calendar.events.EventManager;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;

public class EventModificationGUI
{
    public void createEventModificationList(Context context, int day, int month, int year)
    {
    	String startDay = pad(day)+"/"+ pad(month)+"/"+Integer.toString(year);
    	List<Event> events = EventManager.getEvents(startDay);
    	
    	LinearLayout linearLayout = new LinearLayout(context);
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 400);
    	linearLayout.setLayoutParams(params);
    	
    	ListView listView = new ListView(context);
    	LayoutParams listViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    	listView.setLayoutParams(listViewParams);
    	
    	linearLayout.addView(listView);
    	
    	Dialog dialog = new Dialog(context);
	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    dialog.setContentView(linearLayout);
	    listView.setAdapter(new EventListAdapater(context, events));
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
}
