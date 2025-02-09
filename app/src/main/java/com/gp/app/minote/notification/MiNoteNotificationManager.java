package com.gp.app.minote.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.gp.app.minote.R;
import com.gp.app.minote.calendar.events.database.CalendarDBManager;
import com.gp.app.minote.data.Event;
import com.gp.app.minote.data.Note;
import com.gp.app.minote.data.NoteItem;
import com.gp.app.minote.data.TextNote;
import com.gp.app.minote.layout.manager.NotesLayoutManagerActivity;
import com.gp.app.minote.notes.database.NotesDBManager;
import com.gp.app.minote.notes.fragments.NotesManager;
import com.gp.app.minote.notification.service.AlarmRequestCreator;
import com.gp.app.minote.util.MiNoteParameters;
import com.gp.app.minote.util.MiNoteUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class MiNoteNotificationManager 
{
	public static void createNotifications() 
	{
	    Calendar calendar = Calendar.getInstance();
	    
	    String currentTime = getCurrentFormattedTime(calendar);

	    String currentDate = getFormattedDate(calendar);
	    
	    List<Event> events = CalendarDBManager.getInstance().readEvents(currentDate, currentTime);
	    
	    for(int i = 0; i < events.size(); i++)
	    {
	    	Event event = events.get(i);
	    	
	    	createNotificationForEvent(event);
	    }
	}

	private static void createNotificationForEvent(Event event)
	{
		Context context = MiNoteParameters.getApplicationContext();

		if(event.isAlarmActivated())
		{
			Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			MediaPlayer mp = MediaPlayer.create(context, alert);
			mp.setVolume(100, 100);
			mp.start();
			mp.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.release();
				}
			});
		}
		else
		{
			Intent notificationIntent = new Intent(context, NotesLayoutManagerActivity.class);

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

	public static void createNotifications(String notificationMessage)
	{
		Context context = MiNoteParameters.getApplicationContext();

		List<Note> notes = MiNoteUtil.createNotes(notificationMessage);

        for(int i = 0; i < notes.size(); i++)
        {
            Note note = notes.get(i);

            if(note != null)
            {
                if(note.getType() == Note.EVENT_NOTE)
                {
                    Event event = (Event)note;

                    CalendarDBManager.getInstance().saveEventToDatabase(event);

                    createNotificationForEvent(event);
                }
                else
                {
                    TextNote textNote = (TextNote)note;

                    if(textNote.getNoteItems().size() > 0)
                    {
                        NotesDBManager.getInstance().saveNotes(Arrays.asList(textNote));

                        createNotificationForNote(textNote);
                    }
                }
            }
        }
	}

	private static void createNotificationForNote(TextNote note)
	{
		Context context = MiNoteParameters.getApplicationContext();

		Intent notificationIntent =new Intent(context, NotesLayoutManagerActivity.class);

		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
				Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

		NotificationCompat.Builder notificationBuilder =
				new NotificationCompat.Builder(context)
						.setSmallIcon(R.drawable.ic_action_event)
						.setContentTitle("Note Shared")
						.setContentText("Note Content")
						.setContentIntent(pendingIntent);

		notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

		Notification notification = notificationBuilder.build();

		notification.flags = Notification.FLAG_AUTO_CANCEL;

		NotificationManager notificationManager =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(note.getId(), notification);

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

//if (note.getType() == Note.EVENT_NOTE)
//        {
//        Event event = (Event) note;
//
//        noteText.append("Event=true" + "$$");
//
//        noteText.append("EventName="+event.getEventName() + "$$");
//
//        noteText.append("EventLocation="+event.getLocation() + "$$");
//
//        noteText.append("EventStartDate="+event.getStartDate()+"$$");
//
//        noteText.append("EventStartTime="+event.getStartTime()+"$$");
//
//        noteText.append("EventEndDate="+event.getEndDate()+"$$");
//
//        noteText.append("EventEndTime="+event.getEndTime()+"$$");
//        }
//        else
//        {
//        TextNote textNote = (TextNote) note;
//
//        noteText.append("Event=false"+"$$");
//
//        noteText.append("NoteText=");
//
//        List<NoteItem> items = textNote.getNoteItems();
//
//        for (int j = 0; j < items.size(); j++)
//        {
//        NoteItem item = items.get(j);
//
//        String imageName = item.getImageName();
//
//        if (imageName != null && !imageName.trim().equals("")) {
//        String path = ImageLocationPathManager.getInstance().getImagePath(imageName);
//
//        imageUri = Uri.parse("file://" + path);
//        }
//
//        String text = item.getText();
//
//        if (text != null && !text.trim().equals(""))
//        {
//        noteText.append(text + ";");
//        }
//        }
//        }
