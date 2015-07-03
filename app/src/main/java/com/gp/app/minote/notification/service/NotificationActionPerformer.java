package com.gp.app.minote.notification.service;


import android.os.AsyncTask;

import com.gp.app.minote.notes.backup.NotesBackupManager;
import com.gp.app.minote.notification.MiNoteNotificationManager;

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
			MiNoteNotificationManager.recreateAllAlarms();
		}
		else
		{
			MiNoteNotificationManager.createNotifications();
		}
		
		NotesBackupManager.startBackupProcess();
    	
        return null;
    }
	
}
