package com.gp.app.professionalpa.calendar.events.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.gp.app.professionalpa.calendar.events.Event;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class CalendarDBManager extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "Calendar";
	private static final int DATABASE_VERSION = 4;
	private static final String  AUTHORITY = "com.gp.professionalpa.calendarprovider";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/events");
	public static final Uri CONTENT_ID_URI_BASE = Uri.parse("content://" + AUTHORITY + "/events/");
	private static final UriMatcher uriMatcher;
    private static CalendarDBManager instance;
	
	private CalendarDBManager() 
    {
        super(ProfessionalPAParameters.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
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
        db.execSQL("DROP TABLE IF EXISTS events");
        onCreate(db);
    }
    
	private void createTables(SQLiteDatabase db){
		db.execSQL("CREATE TABLE " + Event.EVENTS_TABLE_NAME + "(" + Event.ID + " integer primary key autoincrement, " +
				Event.EVENT_NAME + " TEXT, " + Event.LOCATION + " TEXT, "
				+ Event.START_TIME + " INTEGER, "+ Event.END_TIME + " INTEGER, " + Event.START_DAY + " INTEGER, " + Event.END_DAY + " INTEGER, " + Event.COLOR +" INTEGER);");
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
    	System.out.println("saveEventToDatabase -> event="+event);

		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(Event.ID, event.getEventId());
		values.put(Event.EVENT_NAME, event.getEventName());
		values.put(Event.START_TIME, event.getStartTime());
		values.put(Event.END_TIME, event.getEndTime());
		values.put(Event.START_DAY, event.getStartDate());
		values.put(Event.END_DAY, event.getEndDate());
		values.put(Event.LOCATION, event.getLocation());
		
		long newRowId = db.insert(
				 Event.EVENTS_TABLE_NAME,null,
				 values);
		
    	System.out.println("saveEventToDatabase -> newRowId="+newRowId+" starttime="+event.getStartTime()+" startdate="+event.getStartDate());
	}
	
	public List<Event> readEvents(String startDay)
	{
		List<Event> events = new ArrayList<Event>();
		
		SQLiteDatabase db = getReadableDatabase();

    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    	    Event._ID,
    	    Event.START_DAY,
    	    Event.END_DAY,
    	    Event.START_TIME,
            Event.END_TIME,
            Event.EVENT_NAME,
            Event.LOCATION,
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
    		long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(Event._ID));
        	String eventName = cursor.getString(cursor.getColumnIndexOrThrow(Event.EVENT_NAME));
        	String eventLocation = cursor.getString(cursor.getColumnIndexOrThrow(Event.LOCATION));
        	String startDate = cursor.getString(cursor.getColumnIndexOrThrow(Event.START_DAY));
        	String startTime = cursor.getString(cursor.getColumnIndexOrThrow(Event.START_TIME));
        	String endDate = cursor.getString(cursor.getColumnIndexOrThrow(Event.END_DAY));
        	String endTime = cursor.getString(cursor.getColumnIndexOrThrow(Event.END_TIME));
        	System.out.println("readEvents -> itemId="+itemId+" column count"+cursor.getColumnCount()+" eventName="+eventName);
        	events.add(new Event(eventName, eventLocation, startDate, startTime, endDate, endTime));
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
    	    Event._ID,
    	    Event.START_DAY,
    	    Event.END_DAY,
    	    Event.START_TIME,
            Event.END_TIME,
            Event.EVENT_NAME,
            Event.LOCATION,
    	    };

    	// How you want the results sorted in the resulting Cursor
    	String sortOrder =
    			Event.START_TIME + " DESC";

    	String where = Event.START_DAY+"=?"+" AND "+Event.START_TIME+"=?";
    	
    	System.out.println("readEvents -> startTime="+startTime+"startDay="+startDay);
    	
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
    		long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(Event._ID));
        	String eventName = cursor.getString(cursor.getColumnIndexOrThrow(Event.EVENT_NAME));
        	String eventLocation = cursor.getString(cursor.getColumnIndexOrThrow(Event.LOCATION));
        	String startDate = cursor.getString(cursor.getColumnIndexOrThrow(Event.START_DAY));
        	String readStartTime = cursor.getString(cursor.getColumnIndexOrThrow(Event.START_TIME));
        	String endDate = cursor.getString(cursor.getColumnIndexOrThrow(Event.END_DAY));
        	String endTime = cursor.getString(cursor.getColumnIndexOrThrow(Event.END_TIME));
        	System.out.println("readEvents -> itemId="+itemId+" column count"+cursor.getColumnCount()+" eventName="+eventName);
        	events.add(new Event(eventName, eventLocation, startDate, readStartTime, endDate, endTime));
    	    cursor.moveToNext();
    	}
    	
    	return events;
	}
}
