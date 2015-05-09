package com.gp.app.professionalpa.calendar.events;

import java.util.List;

import com.gp.app.professionalpa.calendar.events.database.CalendarDBManager;


public class EventManager
{
    public static List<Event> getEvents(String startDay)
    {
    	return CalendarDBManager.getInstance().readEvents(startDay);
    }
    
    public static void deleteEvent(int day, int month, int year, int eventId)
    {
    	
    }
    
    public static void deleteEvents(int day, int month, int year)
    {
    	
    }
    
	public static void addEvent(String eventTitle, String location, String startDay, String startTimeToken, String endDate, String endTime)
	{
        Event event = new Event(eventTitle, location, startDay, startTimeToken, endDate, endTime);
		
		CalendarDBManager.getInstance().saveEventToDatabase(event);
	}
}
