package com.gp.app.minote.data;


import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import java.util.Arrays;

public class Event extends Note implements BaseColumns
{
	private String eventName;
	private String eventLocation;
	private int eventId;
	private String startDay;
	private String startTime;
	private String endDay;
	private String endTime;
	private long creationTime;
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
	public static final String CREATION_TIME = "creation_time";


	public Event(String eventName, String location, String startDay, String startTime, String endDate, String endTime)
	{
		this.eventName = eventName;
		
		this.startDay = startDay;
		
		this.startTime = startTime;
		
		this.endDay = endDate;
		
		this.endTime = endTime;
		
		this.eventLocation = location;

		this.creationTime = System.currentTimeMillis();
		
		eventId = (int)Math.abs((Math.random() * 1000000));
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

    public void setCreationTime(long creationTime)
    {
        this.creationTime = creationTime;
    }

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("\neventName="+eventName);
		sb.append("\nlocation="+eventLocation);
		sb.append("\nstartDay="+startDay);
		sb.append("\nstartTime="+startTime);
		sb.append("\nendDate="+endDay);
		sb.append("\nendTime="+endTime);
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
		return ((Event)obj).getId() == this.getId();
	}

	public void setIsAlarmActivated(boolean isAlarmActivated) 
	{
		this.isAlarmActivated = isAlarmActivated;
	}
	
	public boolean isAlarmActivated()
	{
		return isAlarmActivated;
	}

	@Override
	public byte getType() 
	{
		return Note.EVENT_NOTE;
	}

	@Override
	public int getId() 
	{
		return eventId;
	}

	@Override
	public byte getState() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getCreationTime()
	{
		return creationTime;
	}

	@Override
	public int describeContents() 
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) 
	{
		dest.writeStringList(Arrays.asList(new String[]{eventName, eventLocation, startDay, startTime, endDay, endTime}));
		
		dest.writeBooleanArray(new boolean[]{isAlarmActivated});
		
		dest.writeInt(eventId);
	}
	
	public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {

		@Override
		public Event createFromParcel(Parcel source) 
		{
			String [] eventAttributes = new String[6];
			
			source.readStringArray(eventAttributes);
			
			boolean [] isAlarm = new boolean[1];
			
			source.readBooleanArray(isAlarm);
			
			int eventId = source.readInt();
			
			Event event = new Event(eventAttributes[0], eventAttributes[1], eventAttributes[2]
					, eventAttributes[3], eventAttributes[4], eventAttributes[5]);
			
			event.setIsAlarmActivated(isAlarm[0]);
			
			event.setEventId(eventId);
			
			return event;
		}

		@Override
		public Event[] newArray(int size)
		{
			return new Event[size];
		}
	};


}
