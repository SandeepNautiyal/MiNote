package com.gp.app.professionalpa.notification.service;


import android.os.AsyncTask;

import com.gp.app.professionalpa.notification.ProfessionalPANotificationManager;

public class NotificationActionPerformer extends AsyncTask<Boolean, Void, Void>
{
	public NotificationActionPerformer()
	{
		
	}
	
	protected Void doInBackground(Boolean... areAlarmsToBeRecreated) 
	{
		boolean recreateAlarms = areAlarmsToBeRecreated[0];
		
		if(recreateAlarms)
		{
			ProfessionalPANotificationManager.recreateAllAlarms();
		}
		else
		{
			ProfessionalPANotificationManager.createNotifications();
		}
    	
        return null;
    }
	
}
