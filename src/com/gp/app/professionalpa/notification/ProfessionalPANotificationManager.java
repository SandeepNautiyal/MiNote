package com.gp.app.professionalpa.notification;

import java.util.Calendar;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.calendar.events.Event;
import com.gp.app.professionalpa.calendar.events.database.CalendarDBManager;
import com.gp.app.professionalpa.layout.manager.NotesLayoutManagerActivity;
import com.gp.app.professionalpa.notification.service.AlarmRequestCreator;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;
import com.gp.app.professionalpa.util.ProfessionalPAUtil;

public class ProfessionalPANotificationManager 
{
	public static void createNotifications() 
	{
		Context context = ProfessionalPAParameters.getApplicationContext();
		
	    Calendar calendar = Calendar.getInstance();
	    
	    String currentTime = getCurrentFormattedTime(calendar);

	    String currentDate = getFormattedDate(calendar);
	    
	    List<Event> events = CalendarDBManager.getInstance().readEvents(currentDate, currentTime);
	    
	    for(int i = 0; i < events.size(); i++)
	    {
	    	Event event = events.get(i);
	    	
	    	if(event.isAlarmActivated())
	    	{
	    		System.out.println("createNotifications -> alarm");
	    		
	    		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		    	MediaPlayer mp = MediaPlayer.create(context, alert);
		    	mp.setVolume(100, 100);
		    	mp.start();
		    	mp.setOnCompletionListener(new OnCompletionListener() 
		    	{
		    	    @Override
		    	    public void onCompletion(MediaPlayer mp) 
		    	    {
		    	        mp.release();
		    	    }
		    	});
	    	}
	    	else
	    	{
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
			    	
			    NotificationManager notificationManager =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

			    notificationManager.notify(event.getEventId(), notification);
	    	}
	    }
	}

	private static String getFormattedDate(Calendar calendar) {
		String date = ProfessionalPAUtil.pad(calendar.get(Calendar.DAY_OF_MONTH));
	    
	    String month = ProfessionalPAUtil.pad(calendar.get(Calendar.MONTH)+1);
	    
	    String year = ProfessionalPAUtil.pad(calendar.get(Calendar.YEAR));

	    String currentDate = new StringBuilder().append(date).append("/").append(month).append("/").append(year).toString();
		return currentDate;
	}

	private static String getCurrentFormattedTime(Calendar calendar) {
		String hour = ProfessionalPAUtil.pad(calendar.get(Calendar.HOUR_OF_DAY));
	    
	    String minute = ProfessionalPAUtil.pad(calendar.get(Calendar.MINUTE));

	    String currentTime = new StringBuilder().append(hour).append(":").append(minute).toString();
		return currentTime;
	}

	public static void recreateAllAlarms() 
	{
        Context context = ProfessionalPAParameters.getApplicationContext();
		
	    Calendar calendar = Calendar.getInstance();
	    
	    String currentTime = getCurrentFormattedTime(calendar);

	    String currentDate = getFormattedDate(calendar);
	    
	    List<Event> events = CalendarDBManager.getInstance().readAllEventsAfter(currentDate, currentTime);
	    
	    for(int i = 0; i < events.size(); i++)
	    {
	    	Event event = events.get(i);
	    	
	    	if(event != null)
	    	{
	    		AlarmRequestCreator.createAlarmRequest(event);
	    	}
	    }
	}
}
