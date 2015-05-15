package com.gp.app.professionalpa.notification.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;
import com.gp.app.professionalpa.util.ProfessionalPAUtil;

public class AlarmRequestCreator 
{
	private static int REQUEST_CODE = 25;
	
    public static void createAlarmRequest(long eventTime, boolean isNotification, String eventName, String eventMessage, int notificationId)
    {
    	System.out.println("createAlarmRequest -> eventTime="+eventTime+" isNotification="+isNotification+
    			"eventName ="+eventName+" eventMessage="+eventMessage+" notificationId="+notificationId);
    	
    	System.out.println("createAlarmRequest -> formateed time="+ProfessionalPAUtil.createStringForDate(eventTime, "E yyyy.MM.dd 'at' hh:mm:ss:SSS a zzz"));
    	Context context = ProfessionalPAParameters.getApplicationContext();
    	AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, eventTime, alarmIntent);
    }
}
