package com.gp.app.professionalpa.calendar.interfaces;

import com.gp.app.professionalpa.calendar.events.Event;

public interface DBChangeListener 
{
    void recieveNotification(Event event);
}
