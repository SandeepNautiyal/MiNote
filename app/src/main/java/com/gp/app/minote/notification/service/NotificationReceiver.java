package com.gp.app.minote.notification.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver
{
	public static String BROADCAST_ACTION = "com.gp.professionalpa.notification.service.ACTION_BACKGROUND";
    public static String CATEGORY = "com.gp.professionalpa.notification.service.CATEGORY_BACKGROUND";
    		
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		if(intent.getAction() != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
		{
            Intent serviceIntent = new Intent(context, AlarmRecreatorService.class);
			
	        context.startService(serviceIntent);
		}
		else
		{
			Intent serviceIntent = new Intent(context, NotificationProcessingService.class);
			
	        context.startService(serviceIntent);
		}
	}
}
