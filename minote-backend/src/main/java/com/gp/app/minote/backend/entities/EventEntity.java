package com.gp.app.minote.backend.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


/**
 * Created by dell on 7/9/2015.
 */
@Entity
public class EventEntity
{
    private String eventName;

    private String eventLocation;

    @Id
    private Long eventId;

    private String startDay;

    private String startTime;

    private String endDay;

    private String endTime;

    private boolean isAlarmActivated;

    public EventEntity()
    {
        eventId = 1000000L;
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

    public void setStartDate(String startDate)
    {
        this.startDay = startDate;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public String getStartDate()
    {
        return startDay;
    }

    public void setEndDate(String endDate)
    {
        this.endDay = endDate;
    }
    public String getEndDate()
    {
        return endDay;
    }

//	public void setIsNotification(boolean isNotificationActivated)
//	{
//		this.isNotificationActivated = isNotificationActivated;
//	}

    public void setIsAlarmActivated(boolean isAlarmActivated)
    {
        this.isAlarmActivated = isAlarmActivated;
    }

    public boolean isAlarmActivated()
    {
        return isAlarmActivated;
    }

    public byte getType()
    {
        return 3;
    }

    public Long getKey() {
        return eventId;
    }

}
