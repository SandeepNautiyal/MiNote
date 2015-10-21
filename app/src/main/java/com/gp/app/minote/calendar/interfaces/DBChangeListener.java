package com.gp.app.minote.calendar.interfaces;

import com.gp.app.minote.data.Event;

public interface DBChangeListener 
{
    byte INSERT_COMMAND = 1;
    byte UPDATE_COMMAND = 2;
    byte DELETE_COMMAND = 3;

    void recieveNotification(byte command, Event event);
}
