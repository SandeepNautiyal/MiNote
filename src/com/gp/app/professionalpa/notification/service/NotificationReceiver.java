package com.gp.app.professionalpa.notification.service;

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
		Intent serviceIntent = new Intent(context, NotificationProcessingService.class);
		
        context.startService(serviceIntent);
	}
}
