package com.gp.app.professionalpa.calendar.events;

import android.provider.BaseColumns;

public class Event implements BaseColumns
{
	private String eventName;
	private String description;
	private String eventLocation;
	private int eventId;
	private String startDay;
	private String startTime;
	private String endDay;
	private String endTime;
	private boolean isAlarmActivated;
	public static final String EVENT_NAME = "event";
	public static final String EVENTS_TABLE_NAME = "events";
	public static final int DEFAULT_EVENT_ICON = 0;
	public static final int COLOR_RED = 1;
	public static final int COLOR_BLUE = 2;
	public static final int COLOR_YELLOW = 3;
	public static final int COLOR_PURPLE = 4;
	public static final int COLOR_GREEN = 5;
	public static final String LOCATION = "location";
	public static final String START_TIME = "start";
	public static final String END_TIME = "end";
	public static final String ID = "event_id";
	public static final String START_DAY = "start_day";
	public static final String END_DAY = "end_day";
	public static final String COLOR = "color";
	public static final String IS_ALARM = "is_alarm";

	public Event(String eventName, String location, String startDay, String startTime, String endDate, String endTime)
	{
		this.eventName = eventName;
		
		this.startDay = startDay;
		
		this.startTime = startTime;
		
		this.endDay = endDate;
		
		this.endTime = endTime;
		
		this.eventLocation = location;
		
		eventId = (int)Math.abs((Math.random() * 1000000));
	}
	
	public int getEventId()
	{
		return (int)eventId;
	}
	
	public void setEventName(String name)
	{
		this.eventName = name;
	} 
	
	public String getEventName() 
	{
		return eventName;
	}

	public void setStartTime(String startTime) 
	{
		this.startTime = startTime;
	}
	
	public String getStartTime() 
	{
		return startTime;
	}

	public void setLocation(String location)
	{
		this.eventLocation = location;
	}
	
	public String getLocation()
	{
		return eventLocation;
	}
	
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

	public String getEndTime()
	{
		return endTime;
	}

	public String getStartDate()
	{
		return startDay;
	}

	public String getEndDate() 
	{
		return endDay;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("\neventName="+eventName);
		sb.append("\ndescription="+description);
		sb.append("\nlocation="+eventLocation);
		sb.append("\nstart="+startDay);
		sb.append("\nend="+endDay);
		sb.append("\neventId="+eventId);

		return sb.toString();
	}

	public void setEventId(int itemId)
	{
		eventId = itemId;
	}

//	public void setIsNotification(boolean isNotificationActivated) 
//	{
//		this.isNotificationActivated = isNotificationActivated;
//	}

	@Override
	public int hashCode() 
	{
		return eventId;
	}

	@Override
	public boolean equals(Object obj) 
	{
		return ((Event)obj).getEventId() == this.getEventId();
	}

	public void setIsAlarmActivated(boolean isAlarmActivated) 
	{
		this.isAlarmActivated = isAlarmActivated;
	}
	
	public boolean isAlarmActivated()
	{
		return isAlarmActivated;
	}
	
//	public boolean isNotificationActivated()
//	{
//		return isNotificationActivated;
//	}
}
