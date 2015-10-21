package com.gp.app.minote.calendar.events.database;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;

import com.gp.app.minote.calendar.interfaces.DBChangeListener;
import com.gp.app.minote.calendar.interfaces.DBchangePublisher;
import com.gp.app.minote.data.Event;
import com.gp.app.minote.database.search.NotesSearch;
import com.gp.app.minote.util.MiNoteParameters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CalendarDBManager extends SQLiteOpenHelper implements DBchangePublisher, NotesSearch
{
	private static final String DATABASE_NAME = "Calendar";
	private static final int DATABASE_VERSION = 4;
	private static final String  AUTHORITY = "com.gp.professionalpa.calendarprovider";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/events");
	public static final Uri CONTENT_ID_URI_BASE = Uri.parse("content://" + AUTHORITY + "/events/");
	private static final UriMatcher uriMatcher;
	List<DBChangeListener> listsners = new ArrayList<DBChangeListener>();
	
    private static CalendarDBManager instance;
	
	private CalendarDBManager() 
    {
        super(MiNoteParameters.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

	public static CalendarDBManager getInstance()
	{
		if(instance == null)
		{
			instance = new CalendarDBManager();
		}
		
		return instance;
	}
	
    @Override
    public void onCreate(SQLiteDatabase db) 
    {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, 
                          int newVersion) 
    {
        db.execSQL("DROP TABLE IF EXISTS "+Event.EVENTS_TABLE_NAME);
        
        onCreate(db);
    }
    
//    public void dropEventTable()
//    {
//		SQLiteDatabase db = getWritableDatabase();
//
//        db.execSQL("DROP TABLE IF EXISTS "+Event.EVENTS_TABLE_NAME);
//    }
    
	private void createTables(SQLiteDatabase db){
		db.execSQL("CREATE TABLE " + Event.EVENTS_TABLE_NAME + "(" + Event.ID + " integer primary key autoincrement, " +
                Event.EVENT_NAME + " TEXT, " + Event.LOCATION + " TEXT, " + Event.CREATION_TIME + " INTEGER, "
                + Event.START_DAY + " INTEGER, "
                + Event.START_TIME + " INTEGER, " + Event.END_DAY + " INTEGER, "
                + Event.END_TIME + " INTEGER, "
                + Event.IS_ALARM + " INTEGER);");
	}

	static 
	{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, Event.EVENTS_TABLE_NAME, 1);
		uriMatcher.addURI(AUTHORITY, Event.EVENTS_TABLE_NAME + "/#", 2);
		uriMatcher.addURI(AUTHORITY, Event.EVENTS_TABLE_NAME + "/#/#", 3);
	}
	
	public void saveEventToDatabase(Event event) 
	{
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(Event.ID, event.getId());
		values.put(Event.EVENT_NAME, event.getEventName());
		values.put(Event.CREATION_TIME, event.getCreationTime());
		values.put(Event.START_TIME, event.getStartTime());
		values.put(Event.END_TIME, event.getEndTime());
		values.put(Event.START_DAY, event.getStartDate());
		values.put(Event.END_DAY, event.getEndDate());
		values.put(Event.LOCATION, event.getLocation());
		values.put(Event.IS_ALARM, event.isAlarmActivated() ? 1 : 0);
		
		db.insert(
				 Event.EVENTS_TABLE_NAME,null,
				 values);
		
    	notifyAllListeners(DBChangeListener.INSERT_COMMAND, event);
	}
	
	public List<Event> readAllEvents()
	{
        List<Event> events = new ArrayList<Event>();
		
		SQLiteDatabase db = getReadableDatabase();

    	Cursor	cursor = db.rawQuery("select * from "+Event.EVENTS_TABLE_NAME, null);
    	
    	cursor.moveToFirst();
    	
    	while (cursor.isAfterLast() == false)
    	{
    		int eventId = cursor.getInt(cursor.getColumnIndexOrThrow(Event.ID));
        	String eventName = cursor.getString(cursor.getColumnIndexOrThrow(Event.EVENT_NAME));
        	String eventLocation = cursor.getString(cursor.getColumnIndexOrThrow(Event.LOCATION));
        	String startDate = cursor.getString(cursor.getColumnIndexOrThrow(Event.START_DAY));
        	String startTime = cursor.getString(cursor.getColumnIndexOrThrow(Event.START_TIME));
        	String endDate = cursor.getString(cursor.getColumnIndexOrThrow(Event.END_DAY));
        	String endTime = cursor.getString(cursor.getColumnIndexOrThrow(Event.END_TIME));
    		int alarmAttribte = cursor.getInt(cursor.getColumnIndexOrThrow(Event.IS_ALARM));
			long creationTime = cursor.getLong(cursor.getColumnIndexOrThrow(Event.CREATION_TIME));
    		boolean isAlarm = alarmAttribte == 1 ? true : false;
        	Event event = new Event(eventName, eventLocation, startDate, startTime, endDate, endTime);
        	event.setEventId(eventId);
        	event.setIsAlarmActivated(isAlarm);
			event.setCreationTime(creationTime);
        	events.add(event);
        	cursor.moveToNext();
    	}
    	
    	return events;
	}
	
	public List<Event> readEvents(String startDay)
	{
		List<Event> events = new ArrayList<Event>();
		
		SQLiteDatabase db = getReadableDatabase();

    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    	    Event.ID,
    	    Event.START_DAY,
    	    Event.END_DAY,
    	    Event.START_TIME,
            Event.END_TIME,
            Event.EVENT_NAME,
            Event.LOCATION,
            Event.IS_ALARM,
			Event.CREATION_TIME,
    	    };

    	// How you want the results sorted in the resulting Cursor
    	String sortOrder =
    			Event.START_TIME + " DESC";

    	Cursor cursor = db.query(
    			Event.EVENTS_TABLE_NAME,  // The table to query
    	    projection,                               // The columns to return
    	    Event.START_DAY+ "=?",                                // The columns for the WHERE clause
    	    new String []{startDay},                            // The values for the WHERE clause
    	    null,                                     // don't group the rows
    	    null,                                     // don't filter by row groups
    	    sortOrder                                 // The sort order
    	    );
    	
    	cursor.moveToFirst();
    	
    	while (cursor.isAfterLast() == false)
    	{
    		int eventId = cursor.getInt(cursor.getColumnIndexOrThrow(Event.ID));
        	String eventName = cursor.getString(cursor.getColumnIndexOrThrow(Event.EVENT_NAME));
        	String eventLocation = cursor.getString(cursor.getColumnIndexOrThrow(Event.LOCATION));
        	String startDate = cursor.getString(cursor.getColumnIndexOrThrow(Event.START_DAY));
        	String startTime = cursor.getString(cursor.getColumnIndexOrThrow(Event.START_TIME));
        	String endDate = cursor.getString(cursor.getColumnIndexOrThrow(Event.END_DAY));
        	String endTime = cursor.getString(cursor.getColumnIndexOrThrow(Event.END_TIME));
    		int alarmAttribte = cursor.getInt(cursor.getColumnIndexOrThrow(Event.IS_ALARM));
            long creationTime = cursor.getLong(cursor.getColumnIndexOrThrow(Event.CREATION_TIME));
            boolean isAlarm = alarmAttribte == 1 ? true : false;
        	Event event = new Event(eventName, eventLocation, startDate, startTime, endDate, endTime);
        	event.setEventId(eventId);
        	event.setIsAlarmActivated(isAlarm);
            event.setCreationTime(creationTime);
        	events.add(event);
        	cursor.moveToNext();
    	}
    	
    	return events;
	}
	
	public List<Event> readEvents(String startDay, String startTime)
	{
		List<Event> events = new ArrayList<Event>();
		
		SQLiteDatabase db = getReadableDatabase();

    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    	    Event.ID,
    	    Event.START_DAY,
    	    Event.END_DAY,
    	    Event.START_TIME,
            Event.END_TIME,
            Event.EVENT_NAME,
            Event.LOCATION,
            Event.IS_ALARM,
            Event.CREATION_TIME,
        };

    	// How you want the results sorted in the resulting Cursor
    	String sortOrder =
    			Event.START_TIME + " DESC";

    	String where = Event.START_DAY+"=?"+" AND "+Event.START_TIME+"=?";

    	Cursor cursor = db.query(
    			Event.EVENTS_TABLE_NAME,  // The table to query
    	    projection,                               // The columns to return
    	    where,                               // The columns for the WHERE clause
    	    new String []{startDay, startTime},                            // The values for the WHERE clause
    	    null,                                     // don't group the rows
    	    null,                                     // don't filter by row groups
    	    sortOrder                                 // The sort order
    	    );
    	
    	cursor.moveToFirst();
    	
    	while (cursor.isAfterLast() == false)
    	{
    		int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(Event.ID));
        	String eventName = cursor.getString(cursor.getColumnIndexOrThrow(Event.EVENT_NAME));
        	String eventLocation = cursor.getString(cursor.getColumnIndexOrThrow(Event.LOCATION));
        	String startDate = cursor.getString(cursor.getColumnIndexOrThrow(Event.START_DAY));
        	String readStartTime = cursor.getString(cursor.getColumnIndexOrThrow(Event.START_TIME));
        	String endDate = cursor.getString(cursor.getColumnIndexOrThrow(Event.END_DAY));
        	String endTime = cursor.getString(cursor.getColumnIndexOrThrow(Event.END_TIME));
        	int alarmAttribte = cursor.getInt(cursor.getColumnIndexOrThrow(Event.IS_ALARM));
            long creationTime = cursor.getLong(cursor.getColumnIndexOrThrow(Event.CREATION_TIME));
            boolean isAlarm = alarmAttribte == 1 ? true : false;
    		
        	Event event = new Event(eventName, eventLocation, startDate, readStartTime, endDate, endTime);
        	event.setEventId(itemId);
        	event.setIsAlarmActivated(isAlarm);
            event.setCreationTime(creationTime);
        	events.add(event);
    	    cursor.moveToNext();
    	}
    	
    	return events;
	}
	
	public void deleteEvent(Event event)
	{
		SQLiteDatabase db = getWritableDatabase();

		db.delete(Event.EVENTS_TABLE_NAME, Event.ID + "=?", new String[]{Integer.toString(event.getId())});

        notifyAllListeners(DBChangeListener.DELETE_COMMAND, event);
	}

	public long updateEventInDatabase(Event event)
	{
        SQLiteDatabase db = getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(Event.EVENT_NAME, event.getEventName());
		values.put(Event.START_TIME, event.getStartTime());
		values.put(Event.END_TIME, event.getEndTime());
		values.put(Event.START_DAY, event.getStartDate());
		values.put(Event.END_DAY, event.getEndDate());
		values.put(Event.LOCATION, event.getLocation());
        values.put(Event.CREATION_TIME, event.getCreationTime());
        values.put(Event.IS_ALARM, event.isAlarmActivated() ? 1 : 0);
		
    	String where = Event.ID+"=?";

		long newRowId = db.update(Event.EVENTS_TABLE_NAME, values, where, new String[]{String.valueOf(event.getId())});
    	
    	notifyAllListeners(DBChangeListener.UPDATE_COMMAND, event);
    	
    	return newRowId;
	}

	@Override
	public void addDataChangeListener(DBChangeListener listener) 
	{
		if(listener != null)
		{
			listsners.add(listener);
		}
	}

	@Override
	public void notifyAllListeners(byte command, Event event)
	{
		for(DBChangeListener listener : listsners)
		{
			System.out.println("notifyAllListeners -> listener="+listener);

			listener.recieveNotification(command, event);
		}
	}

	public List<Event> readAllEventsAfter(String currentDate, String currentTime) 
	{
        List<Event> events = new ArrayList<Event>();
		
		SQLiteDatabase db = getReadableDatabase();

    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    	    Event.ID,
    	    Event.START_DAY,
    	    Event.END_DAY,
    	    Event.START_TIME,
            Event.END_TIME,
            Event.EVENT_NAME,
            Event.LOCATION,
            Event.IS_ALARM,
    	    };

    	// How you want the results sorted in the resulting Cursor
    	String sortOrder =
    			Event.START_TIME + " DESC";

    	String where = Event.START_DAY+">=?"+" AND "+Event.START_TIME+">=?";
    	
    	Cursor cursor = db.query(
    			Event.EVENTS_TABLE_NAME,  // The table to query
    	    projection,                               // The columns to return
    	    where,                               // The columns for the WHERE clause
    	    new String []{currentDate, currentTime},                            // The values for the WHERE clause
    	    null,                                     // don't group the rows
    	    null,                                     // don't filter by row groups
    	    sortOrder                                 // The sort order
    	    );
    	
    	cursor.moveToFirst();
    	
    	while (cursor.isAfterLast() == false)
    	{
    		int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(Event.ID));
        	String eventName = cursor.getString(cursor.getColumnIndexOrThrow(Event.EVENT_NAME));
        	String eventLocation = cursor.getString(cursor.getColumnIndexOrThrow(Event.LOCATION));
        	String startDate = cursor.getString(cursor.getColumnIndexOrThrow(Event.START_DAY));
        	String readStartTime = cursor.getString(cursor.getColumnIndexOrThrow(Event.START_TIME));
        	String endDate = cursor.getString(cursor.getColumnIndexOrThrow(Event.END_DAY));
        	String endTime = cursor.getString(cursor.getColumnIndexOrThrow(Event.END_TIME));
            long creationTime = cursor.getLong(cursor.getColumnIndexOrThrow(Event.CREATION_TIME));
            int alarmAttribte = cursor.getInt(cursor.getColumnIndexOrThrow(Event.IS_ALARM));
    		boolean isAlarm = alarmAttribte == 1 ? true : false;
    		
        	Event event = new Event(eventName, eventLocation, startDate, readStartTime, endDate, endTime);
        	event.setEventId(itemId);
        	event.setIsAlarmActivated(isAlarm);
        	events.add(event);
            event.setCreationTime(creationTime);
    	    cursor.moveToNext();
    	}
    	
    	return events;
	}

	@Override
	public Set<Integer> getMatchingNoteIds(String query) 
	{
		List<Event> events = readAllEvents();
		
		Set<Integer> eventIds = null;


			eventIds = new HashSet<Integer>();

			for(int i = 0; i < events.size(); i++)
			{
				Event event = events.get(i);

				if(query != null)
				{
					if(event.getEventName().contains(query) || event.getLocation().contains(query) ||
							event.getStartDate().contains(query) || event.getStartTime().contains(query) ||
							event.getEndDate().contains(query) || event.getEndTime().contains(query))
					{
						eventIds.add(events.get(i).getId());
					}
				}
				else
				{
					eventIds.add(events.get(i).getId());
				}
			}

		return eventIds;
	}

	private class NotifyListeners extends AsyncTask<Void,Void, Void>
	{
		private byte command;

		private Event event;

		private final DBChangeListener listener;

		private NotifyListeners(byte command, Event event, DBChangeListener listener)
		{
			this.command = command;

			this.event = event;

			this.listener = listener;
		}

		@Override
		protected Void doInBackground(Void... params)
		{
			listener.recieveNotification(command, event);

			return  null;
		}
	}
}
