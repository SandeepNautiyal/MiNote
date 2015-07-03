package com.gp.app.minote.notification.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.gp.app.minote.util.MiNoteParameters;
import com.gp.app.minote.util.MiNoteUtil;

public class AlarmRequestCreator 
{
	private static int REQUEST_CODE = 25;
	
    public static void createAlarmRequest(String startDate, String startTime)
    {
    	if(startDate != null && startTime != null)
    	{
    		Context context = MiNoteParameters.getApplicationContext();
        	AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, NotificationReceiver.class);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_ONE_SHOT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, MiNoteUtil.createTime(startDate, startTime), alarmIntent);
    	}
    }
}
