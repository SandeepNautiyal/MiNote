package com.gp.app.professionalpa.notification;

import java.util.Calendar;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.calendar.events.Event;
import com.gp.app.professionalpa.calendar.events.database.CalendarDBManager;
import com.gp.app.professionalpa.layout.manager.NotesLayoutManagerActivity;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;
import com.gp.app.professionalpa.util.ProfessionalPAUtil;

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

	public static void createNotifications() 
	{
		Context context = ProfessionalPAParameters.getApplicationContext();
		
	    NotificationManager notificationManager =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	    
	    Calendar calendar = Calendar.getInstance();
	    
	    String hour = ProfessionalPAUtil.pad(calendar.get(Calendar.HOUR_OF_DAY));
	    
	    String minute = ProfessionalPAUtil.pad(calendar.get(Calendar.MINUTE));

	    String date = ProfessionalPAUtil.pad(calendar.get(Calendar.DAY_OF_MONTH));
	    
	    String month = ProfessionalPAUtil.pad(calendar.get(Calendar.MONTH)+1);
	    
	    String year = ProfessionalPAUtil.pad(calendar.get(Calendar.YEAR));

	    String currentTime = new StringBuilder().append(hour).append(":").append(minute).toString();
	    
	    String currentDate = new StringBuilder().append(date).append("/").append(month).append("/").append(year).toString();
	    
	    List<Event> events = CalendarDBManager.getInstance().readEvents(currentDate, currentTime);
	    
	    for(int i = 0; i < events.size(); i++)
	    {
	    	Event event = events.get(i);
	    	
		    System.out.println("createNotifications -> event="+event);

	    	Intent notificationIntent =new Intent(context, NotesLayoutManagerActivity.class);
		    
		    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
		            Intent.FLAG_ACTIVITY_SINGLE_TOP);
		    
		    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		    NotificationCompat.Builder notificationBuilder =
		    	    new NotificationCompat.Builder(context)
		    	    .setSmallIcon(R.drawable.ic_action_event)
		    	    .setContentTitle(event.getEventName())
		    	    .setContentText(event.getLocation())
		    	    .setContentIntent(pendingIntent);
		    
		    notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		    
		    Notification notification = notificationBuilder.build();
		    
		    notification.flags = Notification.FLAG_AUTO_CANCEL;
		    		
		    notificationManager.notify(event.getEventId(), notification);
	    }
	}
}
