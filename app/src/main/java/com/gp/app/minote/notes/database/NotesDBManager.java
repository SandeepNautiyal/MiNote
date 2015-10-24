package com.gp.app.minote.notes.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gp.app.minote.data.NoteItem;
import com.gp.app.minote.data.TextNote;
import com.gp.app.minote.database.search.NotesSearch;
import com.gp.app.minote.util.MiNoteParameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class NotesDBManager extends SQLiteOpenHelper implements NotesSearch
{
    private static NotesDBManager instance;
	
	private NotesDBManager() 
    {
        super(MiNoteParameters.getApplicationContext(), MiNoteDBConstants.DATABASE_NAME, null, MiNoteDBConstants.DATABASE_VERSION);
    }

	public static NotesDBManager getInstance()
	{
		if(instance == null)
		{
			instance = new NotesDBManager();
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
        db.execSQL("DROP TABLE IF EXISTS "+TextNote.NOTE_TABLE_NAME);
        
        db.execSQL("DROP TABLE IF EXISTS "+NoteItem.NOTE_ITEM_TABLE_NAME);

        onCreate(db);
    }
    
	private void createTables(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE " + TextNote.NOTE_TABLE_NAME + "(" + TextNote.NOTE_ID +  " INTEGER, " +
				TextNote.NOTE_TYPE + " INTEGER, " + TextNote.NOTE_COLOR + " INTEGER, " + TextNote.NOTE_CREATION_TIME + " INTEGER, "
				+ TextNote.NOTE_MODIFIED_TIME + " INTEGER);");
		
		db.execSQL("CREATE TABLE " + NoteItem.NOTE_ITEM_TABLE_NAME + "(" + NoteItem.NOTE_ID +  " INTEGER, " +
				NoteItem.DATA + " TEXT, " + NoteItem.IMAGE_NAME + " TEXT, " + NoteItem.TEXT_COLOR + " INTEGER, "+
				NoteItem.IS_TITLE + " TEXT, "+
				" FOREIGN KEY ("+NoteItem.NOTE_ID+") REFERENCES "+TextNote.NOTE_TABLE_NAME+" ("+TextNote.NOTE_ID+"));");
	}

	public void saveNotes(List<TextNote> notes) 
	{
	    for(int i = 0; i < notes.size(); i++)
	    {
	    	TextNote note = notes.get(i);
	    	
	    	saveNote(note);
	    }
	}
	
	private void saveNote(TextNote note) 
	{
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues noteValues = new ContentValues();
		noteValues.put(TextNote.NOTE_ID, note.getId());
		noteValues.put(TextNote.NOTE_TYPE, note.getType());
		noteValues.put(TextNote.NOTE_COLOR, note.getNoteColor());
		noteValues.put(TextNote.NOTE_CREATION_TIME, note.getCreationTime());
		noteValues.put(TextNote.NOTE_MODIFIED_TIME, note.getLastEditedTime());
		
		db.insert(
				 TextNote.NOTE_TABLE_NAME, null,
				 noteValues);
		
		List<NoteItem> noteItems = note.getNoteItems();
		
		for(int i = 0; i < noteItems.size(); i++)
		{
			NoteItem item = noteItems.get(i);
			ContentValues noteItemValues = new ContentValues();
			noteItemValues.put(NoteItem.NOTE_ID, note.getId());
			noteItemValues.put(NoteItem.TEXT_COLOR, item.getTextColour());
			noteItemValues.put(NoteItem.IMAGE_NAME, item.getImageName());
			noteItemValues.put(NoteItem.DATA, item.getText());
			noteItemValues.put(NoteItem.IS_TITLE, Boolean.toString(item.isTitle()));
			
			db.insert(
					 NoteItem.NOTE_ITEM_TABLE_NAME,null,
					 noteItemValues);
		}
	}
	
	public List<TextNote> readNotes()
	{
		List<TextNote> notes = readNotes(null);
		
		return notes;
	}
	
	public TextNote readNote(int noteId)
	{
		List<TextNote> notes = readNotes(Integer.toString(noteId));
		
		return notes.get(0);
	}
	
	private List<TextNote> readNotes(String noteId) 
	{
		SQLiteDatabase db = getReadableDatabase();

		List<TextNote> notes = new LinkedList<TextNote>();

    	String sortOrder =
    			TextNote.NOTE_MODIFIED_TIME + " DESC";

    	Cursor cursor = null;
    	
    	if(noteId == null)
    	{
    		cursor = db.rawQuery("select * from "+TextNote.NOTE_TABLE_NAME+" order by "+sortOrder, null);
    	}
    	else
    	{
    		String where = NoteItem.NOTE_ID+"=?";

        	String [] whereArguments = new String []{noteId};
        	
        	final String[] PROJECTION_FOR_NOTE = 
        		{
                TextNote.NOTE_ID,
                TextNote.NOTE_TYPE,
                TextNote.NOTE_COLOR,
                TextNote.NOTE_CREATION_TIME,
                TextNote.NOTE_MODIFIED_TIME,
            };
        	
        	cursor = db.query(
        		TextNote.NOTE_TABLE_NAME,  // The table to query
        		PROJECTION_FOR_NOTE,                               // The columns to return
        	    where,                                // The columns for the WHERE clause
        	    whereArguments,       // The values for the WHERE clause
        	    null,                                     // don't group the rows
        	    null,                                     // don't filter by row groups
        	    sortOrder                                 // The sort order
        	    );
    	}
    	
    	cursor.moveToFirst();
    	
    	while (cursor.isAfterLast() == false)
    	{
    		int readNoteId = (int)cursor.getInt(cursor.getColumnIndexOrThrow(TextNote.NOTE_ID));
    		byte noteType = (byte)cursor.getInt(cursor.getColumnIndexOrThrow(TextNote.NOTE_TYPE));
        	int noteColor = (int)cursor.getInt(cursor.getColumnIndexOrThrow(TextNote.NOTE_COLOR));
        	long creationTime = cursor.getLong(cursor.getColumnIndexOrThrow(TextNote.NOTE_CREATION_TIME));
        	long lastEditedTime = cursor.getLong(cursor.getColumnIndexOrThrow(TextNote.NOTE_MODIFIED_TIME));
        	TextNote note = new TextNote(readNoteId, noteType, noteColor, creationTime, lastEditedTime);
    	    notes.add(note);
        	List<NoteItem> noteItems = readNoteItems(readNoteId);
        	note.setNoteItems(noteItems);
        	cursor.moveToNext();
    	}
    	
    	return notes;
	}

	private List<NoteItem> readNoteItems(int noteId) 
	{
        List<NoteItem> noteItems = new ArrayList<NoteItem>();
		
		SQLiteDatabase db = getReadableDatabase();

    	String sortOrder =
    			NoteItem.NOTE_ID + " DESC";

    	String where = NoteItem.NOTE_ID+"=?";

    	final String[] PROJECTION_FOR_NOTE_ITEM =  {
    			
    			NoteItem.TEXT_COLOR,
    			NoteItem.IMAGE_NAME,
    			NoteItem.DATA,
    			NoteItem.IS_TITLE,
    	};
    	
    	Cursor cursor = db.query(
    		NoteItem.NOTE_ITEM_TABLE_NAME,  // The table to query
    	    PROJECTION_FOR_NOTE_ITEM,                               // The columns to return
    	    where,                                // The columns for the WHERE clause
    	    new String []{Integer.toString(noteId)},                            // The values for the WHERE clause
    	    null,                                     // don't group the rows
    	    null,                                     // don't filter by row groups
    	    sortOrder                                 // The sort order
    	    );
    	
    	cursor.moveToFirst();
    	
    	NoteItem item = null;
    	
    	while (cursor.isAfterLast() == false)
    	{
        	int noteColor = cursor.getInt(cursor.getColumnIndexOrThrow(NoteItem.TEXT_COLOR));
        	String imageName = cursor.getString(cursor.getColumnIndexOrThrow(NoteItem.IMAGE_NAME));
        	String itemData = cursor.getString(cursor.getColumnIndexOrThrow(NoteItem.DATA));
        	String isTitle = cursor.getString(cursor.getColumnIndexOrThrow(NoteItem.IS_TITLE));
        	item = new NoteItem(itemData, imageName);
        	item.setTextColour(noteColor);
        	item.setIsTitle(Boolean.valueOf(isTitle));
            noteItems.add(item);
        	cursor.moveToNext();
    	}
    	
    	return noteItems;
	}
	
	private Map<String, Integer> readNoteItems() 
	{
        Map<String, Integer> noteItems = new HashMap<String, Integer>();
		
		SQLiteDatabase db = getReadableDatabase();

    	Cursor cursor = db.rawQuery("select * from "+NoteItem.NOTE_ITEM_TABLE_NAME, null);
    	
    	cursor.moveToFirst();
    	
    	while (cursor.isAfterLast() == false)
    	{
    		int noteId =  cursor.getInt(cursor.getColumnIndexOrThrow(NoteItem.NOTE_ID));
        	
    		String itemData = cursor.getString(cursor.getColumnIndexOrThrow(NoteItem.DATA));
    		
    		noteItems.put(itemData, noteId);
    		
    		cursor.moveToNext();
    	}
    	
    	return noteItems;
	}

	public void deleteNotes(List<Integer> noteIds) 
	{
		SQLiteDatabase db = getWritableDatabase();

		for(int i = 0; i < noteIds.size(); i++)
		{
			int noteId = noteIds.get(i);
			
			int result = db.delete(TextNote.NOTE_TABLE_NAME, TextNote.NOTE_ID + "=?", new String[]{Integer.toString(noteId)});
			
			if(result > 0)
			{
				db.delete(NoteItem.NOTE_ITEM_TABLE_NAME, NoteItem.NOTE_ID + "=?", new String[]{Integer.toString(noteId)});
			}
		}
	}

	public void updateNote(TextNote note)
	{
        SQLiteDatabase db = getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(TextNote.NOTE_COLOR, note.getNoteColor());
		values.put(TextNote.NOTE_TYPE, note.getType());
		values.put(TextNote.NOTE_CREATION_TIME, note.getCreationTime());
		values.put(TextNote.NOTE_MODIFIED_TIME, note.getLastEditedTime());
		
    	String where = TextNote.NOTE_ID+"=?";

		int numberOfEffectedRows = db.update(TextNote.NOTE_TABLE_NAME, values, where, new String[]{Integer.toString(note.getId())});
    	
		if(numberOfEffectedRows > 0)
		{
			List<NoteItem> items = note.getNoteItems();
			
	        db.delete(NoteItem.NOTE_ITEM_TABLE_NAME, NoteItem.NOTE_ID + "=?", new String[]{Integer.toString(note.getId())});
	        
			for(int i = 0; i < items.size(); i++)
			{
				NoteItem item = items.get(i);
				ContentValues noteItemValues = new ContentValues();
				noteItemValues.put(NoteItem.NOTE_ID, note.getId());
				noteItemValues.put(NoteItem.TEXT_COLOR, item.getTextColour());
				noteItemValues.put(NoteItem.IMAGE_NAME, item.getImageName());
				noteItemValues.put(NoteItem.DATA, item.getText());
				noteItemValues.put(NoteItem.IS_TITLE, Boolean.toString(item.isTitle()));

				db.insert(NoteItem.NOTE_ITEM_TABLE_NAME, null, noteItemValues);
			}
		}
	}

	public void setNoteColorAttribute(int noteId, int noteColor)
	{
		 SQLiteDatabase db = getWritableDatabase();
			
			ContentValues values = new ContentValues();
			values.put(TextNote.NOTE_COLOR, noteColor);
			
	    	String where = TextNote.NOTE_ID+"="+noteId;

			int numberOfEffectedRows = db.update(TextNote.NOTE_TABLE_NAME, values, where, null);
	}

	public List<Integer> getSearchNoteIds(String searchString)
	{
        List<Integer> noteIds = new ArrayList<Integer>();
		
		SQLiteDatabase db = getReadableDatabase();

    	Cursor cursor = db.rawQuery("SELECT "+NoteItem.NOTE_ID+" FROM "
                + NoteItem.NOTE_ITEM_TABLE_NAME + " where " + NoteItem.DATA + " like '%" + searchString
                + "%'", null);
    	
    	cursor.moveToFirst();
    	
    	while (cursor.isAfterLast() == false)
    	{
        	int noteId = cursor.getInt(cursor.getColumnIndexOrThrow(NoteItem.NOTE_ID));
        	
        	noteIds.add(noteId);
        	
        	cursor.moveToNext();
    	}
    	
    	return noteIds;
	}

	@Override
	public Set<Integer> getMatchingNoteIds(String query) 
	{
		Map<String, Integer> notesData = readNoteItems();
		
		Set<Integer> noteIds = null;

		if(query != null)
		{
			noteIds = new HashSet<Integer>();

			for (Entry<String, Integer> entry : notesData.entrySet())
			{
				if (entry.getKey().toUpperCase(Locale.US).contains(query.toUpperCase(Locale.US)))
				{
					noteIds.add(entry.getValue());
				}
			}
		}
		else
		{
			noteIds = new HashSet<>(notesData.values());
		}


		return noteIds;
	}
}
