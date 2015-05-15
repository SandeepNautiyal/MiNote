package com.gp.app.professionalpa.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.layout.manager.NotesLayoutManagerActivity;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class ProfessionalPANotificationManager 
{
	public static void createNotifications(Context context)
	{
	    
//	    Intent intent = new Intent(ctx, HomeActivity.class);
//	    PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//	    NotificationCompat.Builder b = new NotificationCompat.Builder(ctx);
//
//	    b.setAutoCancel(true)
//	     .setDefaults(Notification.DEFAULT_ALL)
//	     .setWhen(System.currentTimeMillis())         
//	     .setSmallIcon(R.drawable.ic_launcher)
//	     .setTicker("Hearty365")            
//	     .setContentTitle("Default notification")
//	     .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
//	     .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
//	     .setContentIntent(contentIntent)
//	     .setContentInfo("Info");
//
//
//	    NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
//	    notificationManager.notify(1, b.build());
	}

	public static void createNotifications(int notififcationId, String eventName, String eventMessage) 
	{
		Context context = ProfessionalPAParameters.getApplicationContext();
		
	    NotificationManager notificationManager =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	    
	    Intent notificationIntent =new Intent(context, NotesLayoutManagerActivity.class);
	    
	    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
	            Intent.FLAG_ACTIVITY_SINGLE_TOP);
	    
	    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

	    NotificationCompat.Builder mBuilder =
	    	    new NotificationCompat.Builder(context)
	    	    .setSmallIcon(R.drawable.ic_action_event)
	    	    .setContentTitle(eventName)
	    	    .setContentText(eventMessage)
	    	    .setContentIntent(pendingIntent);
	    
	    mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
	    
	    notificationManager.notify(notififcationId, mBuilder.build());
	}
}
