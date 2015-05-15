package com.gp.app.professionalpa.notification.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.layout.manager.NotesLayoutManagerActivity;
import com.gp.app.professionalpa.notification.ProfessionalPANotificationManager;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class NotificationActionPerformer extends AsyncTask<String, Void, Void>
{
	private int notififcationId;
	
	private String eventName;
	
	private String eventMessage;
	
	private boolean isNotification;
	
	private Context context;

	public NotificationActionPerformer(int notififcationId, String eventName, String eventMessage, boolean isNotification)
	{
		this.notififcationId = notififcationId;
		
		this.eventName = eventName;
		
		this.eventMessage = eventMessage;
		
		this.isNotification = isNotification;
	}
	
	protected Void doInBackground(String... completeLocation) 
	{
    	if(isNotification)
    	{
    		ProfessionalPANotificationManager.createNotifications(notififcationId, eventName, eventMessage);
    	}
        return null;
    }
	
}
