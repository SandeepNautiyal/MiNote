package com.gp.app.minote.calendar.interfaces;

import com.gp.app.minote.data.Event;

public interface DBChangeListener 
{
    void recieveNotification(Event event);
}
