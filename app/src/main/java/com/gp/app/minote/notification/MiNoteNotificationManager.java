package com.gp.app.minote.notification;

import java.util.Calendar;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.gp.app.minote.calendar.events.database.CalendarDBManager;
import com.gp.app.minote.data.Event;
import com.gp.app.minote.layout.manager.NotesLayoutManagerActivity;
import com.gp.app.minote.notification.service.AlarmRequestCreator;
import com.gp.app.minote.util.MiNoteParameters;
import com.gp.app.minote.util.MiNoteUtil;
import com.gp.app.minote.R;

public class MiNoteNotificationManager 
{
	public static void createNotifications() 
	{
		Context context = MiNoteParameters.getApplicationContext();
		
	    Calendar calendar = Calendar.getInstance();
	    
	    String currentTime = getCurrentFormattedTime(calendar);

	    String currentDate = getFormattedDate(calendar);
	    
	    List<Event> events = CalendarDBManager.getInstance().readEvents(currentDate, currentTime);
	    
	    for(int i = 0; i < events.size(); i++)
	    {
	    	Event event = events.get(i);
	    	
	    	if(event.isAlarmActivated())
	    	{
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

			    notificationManager.notify(event.getId(), notification);
	    	}
	    }
	}

	private static String getFormattedDate(Calendar calendar) {
		String date = MiNoteUtil.pad(calendar.get(Calendar.DAY_OF_MONTH));
	    
	    String month = MiNoteUtil.pad(calendar.get(Calendar.MONTH)+1);
	    
	    String year = MiNoteUtil.pad(calendar.get(Calendar.YEAR));

	    String currentDate = new StringBuilder().append(date).append("/").append(month).append("/").append(year).toString();
		return currentDate;
	}

	private static String getCurrentFormattedTime(Calendar calendar) {
		String hour = MiNoteUtil.pad(calendar.get(Calendar.HOUR_OF_DAY));
	    
	    String minute = MiNoteUtil.pad(calendar.get(Calendar.MINUTE));

	    String currentTime = new StringBuilder().append(hour).append(":").append(minute).toString();
		return currentTime;
	}

	public static void recreateAllAlarms() 
	{
        Context context = MiNoteParameters.getApplicationContext();
		
	    Calendar calendar = Calendar.getInstance();
	    
	    String currentTime = getCurrentFormattedTime(calendar);

	    String currentDate = getFormattedDate(calendar);
	    
	    List<Event> events = CalendarDBManager.getInstance().readAllEventsAfter(currentDate, currentTime);
	    
	    for(int i = 0; i < events.size(); i++)
	    {
	    	Event event = events.get(i);
	    	
	    	if(event != null)
	    	{
	    		AlarmRequestCreator.createAlarmRequest(event.getStartDate(), event.getStartTime());
	    	}
	    }
	}
}
