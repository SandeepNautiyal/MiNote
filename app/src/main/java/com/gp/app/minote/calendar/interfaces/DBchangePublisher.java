package com.gp.app.minote.calendar.interfaces;

import com.gp.app.minote.data.Event;

public interface DBchangePublisher 
{
    void addDataChangeListener(DBChangeListener listener);
    
	void notifyAllListeners(byte command, Event event);
}
