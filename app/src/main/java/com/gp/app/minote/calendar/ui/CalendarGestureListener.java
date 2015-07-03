package com.gp.app.minote.calendar.ui;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class CalendarGestureListener extends SimpleOnGestureListener 
{
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	 
	@Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
            float velocityY) 
	{
		System.out.println("onFling -> e1="+e1+" e2="+e2);
		
        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
        {
                return false;
        }
            
        return false;
    }
}
