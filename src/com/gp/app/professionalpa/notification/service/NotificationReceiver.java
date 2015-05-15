package com.gp.app.professionalpa.notification.service;

import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;

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
    	boolean  isNotification = intent.getBooleanExtra(ProfessionalPAConstants.IS_NOTIFICATION, true);
        String eventName = intent.getStringExtra(ProfessionalPAConstants.EVENT_NAME);
        String eventMessage = intent.getStringExtra(ProfessionalPAConstants.EVENT_MESSAGE);
        int notificationId = intent.getIntExtra(ProfessionalPAConstants.NOTIFICATION_ID, -1);
        
    	System.out.println("onReceive -> isNotification="+isNotification+" eventName="+eventName
    			+" eventMessage="+eventMessage+" notificationId="+notificationId);

		Intent serviceIntent = new Intent(context, NotificationProcessingService.class);
		
		serviceIntent.putExtra(ProfessionalPAConstants.IS_NOTIFICATION, isNotification);
		serviceIntent.putExtra(ProfessionalPAConstants.EVENT_NAME, eventName);
		serviceIntent.putExtra(ProfessionalPAConstants.EVENT_MESSAGE, eventMessage);
		serviceIntent.putExtra(ProfessionalPAConstants.NOTIFICATION_ID, notificationId);
        
        context.startService(serviceIntent);
	}
}
