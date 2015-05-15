package com.gp.app.professionalpa.calendar.events;

import java.util.List;

import com.gp.app.professionalpa.calendar.events.database.CalendarDBManager;
import com.gp.app.professionalpa.notification.service.AlarmRequestCreator;
import com.gp.app.professionalpa.util.ProfessionalPATools;


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
    
	public static void addEvent(String eventTitle, String location, String startDay, String startTime, String endDate, String endTime)
	{
        Event event = new Event(eventTitle, location, startDay, startTime, endDate, endTime);
		
		CalendarDBManager.getInstance().saveEventToDatabase(event);
		
		AlarmRequestCreator.createAlarmRequest(ProfessionalPATools.createTime(startDay, startTime), true, eventTitle, location, event.getEventId());
	}
}
