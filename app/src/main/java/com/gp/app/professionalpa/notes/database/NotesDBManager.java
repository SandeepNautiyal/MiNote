package com.gp.app.professionalpa.notes.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gp.app.professionalpa.data.NoteItem;
import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class NotesDBManager extends SQLiteOpenHelper
{
    private static NotesDBManager instance;
	
	private NotesDBManager() 
    {
        super(ProfessionalPAParameters.getApplicationContext(), ProfessionalPADBConstants.DATABASE_NAME, null, ProfessionalPADBConstants.DATABASE_VERSION);
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
        db.execSQL("DROP TABLE IF EXISTS "+ProfessionalPANote.NOTE_TABLE_NAME);
        
        db.execSQL("DROP TABLE IF EXISTS "+NoteItem.NOTE_ITEM_TABLE_NAME);

        onCreate(db);
    }
    
	private void createTables(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE " + ProfessionalPANote.NOTE_TABLE_NAME + "(" + ProfessionalPANote.NOTE_ID +  " INTEGER, " +
				ProfessionalPANote.NOTE_TYPE + " INTEGER, " + ProfessionalPANote.NOTE_COLOR + " INTEGER, " + ProfessionalPANote.NOTE_CREATION_TIME + " INTEGER, "
				+ ProfessionalPANote.NOTE_MODIFIED_TIME + " INTEGER);");
		
		db.execSQL("CREATE TABLE " + NoteItem.NOTE_ITEM_TABLE_NAME + "(" + NoteItem.NOTE_ID +  " INTEGER, " +
				NoteItem.DATA + " TEXT, " + NoteItem.IMAGE_NAME + " TEXT, " + NoteItem.TEXT_COLOR + " INTEGER, "+
				" FOREIGN KEY ("+NoteItem.NOTE_ID+") REFERENCES "+ProfessionalPANote.NOTE_TABLE_NAME+" ("+ProfessionalPANote.NOTE_ID+"));");
	}

	public void saveNotes(List<ProfessionalPANote> notes) 
	{
	    for(int i = 0; i < notes.size(); i++)
	    {
	    	ProfessionalPANote note = notes.get(i);
	    	
	    	saveNote(note);
	    }
	}
	
	private void saveNote(ProfessionalPANote note) 
	{
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues noteValues = new ContentValues();
		noteValues.put(ProfessionalPANote.NOTE_ID, note.getNoteId());
		noteValues.put(ProfessionalPANote.NOTE_TYPE, note.getNoteType());
		noteValues.put(ProfessionalPANote.NOTE_COLOR, note.getNoteColor());
		noteValues.put(ProfessionalPANote.NOTE_CREATION_TIME, note.getCreationTime());
		noteValues.put(ProfessionalPANote.NOTE_MODIFIED_TIME, note.getLastEditedTime());
		
		db.insert(
				 ProfessionalPANote.NOTE_TABLE_NAME, null,
				 noteValues);
		
		List<NoteItem> noteItems = note.getNoteItems();
		
		for(int i = 0; i < noteItems.size(); i++)
		{
			NoteItem item = noteItems.get(i);
			ContentValues noteItemValues = new ContentValues();
			noteItemValues.put(NoteItem.NOTE_ID, note.getNoteId());
			noteItemValues.put(NoteItem.TEXT_COLOR, item.getTextColour());
			noteItemValues.put(NoteItem.IMAGE_NAME, item.getImageName());
			noteItemValues.put(NoteItem.DATA, item.getText());
			
			db.insert(
					 NoteItem.NOTE_ITEM_TABLE_NAME,null,
					 noteItemValues);
			
			ContentValues virtualValues = new ContentValues();
			noteItemValues.put(NoteItem.NOTE_ID, note.getNoteId());
			noteItemValues.put(NoteItem.DATA, item.getText());
			
			db.insert(
					 NoteItem.NOTE_ITEM_VIRTUAL_TABLE,null,
					 virtualValues);
		}
	}
	
	public List<ProfessionalPANote> readNotes()
	{
		List<ProfessionalPANote> notes = readNotes(null);
		
		return notes;
	}
	
	public ProfessionalPANote readNote(int noteId)
	{
		List<ProfessionalPANote> notes = readNotes(Integer.toString(noteId));
		
		return notes.get(0);
	}
	
	private List<ProfessionalPANote> readNotes(String noteId) 
	{
		SQLiteDatabase db = getReadableDatabase();

		List<ProfessionalPANote> notes = new ArrayList<ProfessionalPANote>();

    	String sortOrder =
    			ProfessionalPANote.NOTE_ID + " DESC";

    	Cursor cursor = null;
    	
    	if(noteId == null)
    	{
    		cursor = db.rawQuery("select * from "+ProfessionalPANote.NOTE_TABLE_NAME, null);
    	}
    	else
    	{
    		String where = NoteItem.NOTE_ID+"=?";

        	String [] whereArguments = new String []{noteId};
        	
        	final String[] PROJECTION_FOR_NOTE = 
        		{
                ProfessionalPANote.NOTE_ID,
                ProfessionalPANote.NOTE_TYPE,
                ProfessionalPANote.NOTE_COLOR,
                ProfessionalPANote.NOTE_CREATION_TIME,
                ProfessionalPANote.NOTE_MODIFIED_TIME,
            };
        	
        	cursor = db.query(
        		ProfessionalPANote.NOTE_TABLE_NAME,  // The table to query
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
    		int readNoteId = (int)cursor.getInt(cursor.getColumnIndexOrThrow(ProfessionalPANote.NOTE_ID));
    		byte noteType = (byte)cursor.getInt(cursor.getColumnIndexOrThrow(ProfessionalPANote.NOTE_TYPE));
        	int noteColor = (int)cursor.getInt(cursor.getColumnIndexOrThrow(ProfessionalPANote.NOTE_COLOR));
        	long creationTime = cursor.getLong(cursor.getColumnIndexOrThrow(ProfessionalPANote.NOTE_CREATION_TIME));
        	long lastEditedTime = cursor.getLong(cursor.getColumnIndexOrThrow(ProfessionalPANote.NOTE_MODIFIED_TIME));
        	ProfessionalPANote note = new ProfessionalPANote(readNoteId, noteType, noteColor, creationTime, lastEditedTime);
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
        	item = new NoteItem(itemData, imageName);
        	item.setTextColour(noteColor);
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
			
			int result = db.delete(ProfessionalPANote.NOTE_TABLE_NAME, ProfessionalPANote.NOTE_ID + "=?", new String[]{Integer.toString(noteId)});
			
			if(result > 0)
			{
				db.delete(NoteItem.NOTE_ITEM_TABLE_NAME, NoteItem.NOTE_ID + "=?", new String[]{Integer.toString(noteId)});
			}
		}
	}

	public void updateNote(ProfessionalPANote note)
	{
        SQLiteDatabase db = getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(ProfessionalPANote.NOTE_COLOR, note.getNoteColor());
		values.put(ProfessionalPANote.NOTE_TYPE, note.getNoteType());
		values.put(ProfessionalPANote.NOTE_CREATION_TIME, note.getCreationTime());
		values.put(ProfessionalPANote.NOTE_MODIFIED_TIME, note.getLastEditedTime());
		
    	String where = ProfessionalPANote.NOTE_ID+"=?";

		int numberOfEffectedRows = db.update(ProfessionalPANote.NOTE_TABLE_NAME, values, where, new String[]{Integer.toString(note.getNoteId())});
    	
		if(numberOfEffectedRows > 0)
		{
			List<NoteItem> items = note.getNoteItems();
			
	        db.delete(NoteItem.NOTE_ITEM_TABLE_NAME, NoteItem.NOTE_ID + "=?", new String[]{Integer.toString(note.getNoteId())});
	        
			for(int i = 0; i < items.size(); i++)
			{
				NoteItem item = items.get(i);
				ContentValues noteItemValues = new ContentValues();
				noteItemValues.put(NoteItem.NOTE_ID, note.getNoteId());
				noteItemValues.put(NoteItem.TEXT_COLOR, item.getTextColour());
				noteItemValues.put(NoteItem.IMAGE_NAME, item.getImageName());
				noteItemValues.put(NoteItem.DATA, item.getText());
				
				db.insert(
						 NoteItem.NOTE_ITEM_TABLE_NAME, null, noteItemValues);
			}
		}
	}

	public void setNoteColorAttribute(int noteId, int noteColor)
	{
		 SQLiteDatabase db = getWritableDatabase();
			
			ContentValues values = new ContentValues();
			values.put(ProfessionalPANote.NOTE_COLOR, noteColor);
			
	    	String where = ProfessionalPANote.NOTE_ID+"="+noteId;

			int numberOfEffectedRows = db.update(ProfessionalPANote.NOTE_TABLE_NAME, values, where, null);
	}

	public void deleteAllNotes() 
	{
        SQLiteDatabase db = getWritableDatabase();
        
//        db.execSQL("DELETE '*' from "+NoteListItem.NOTE_ITEM_TABLE_NAME);
//
//        db.execSQL("DELETE '*' from "+ProfessionalPANote.NOTE_TABLE_NAME);
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
	
	public class NotesSearchManager
	{
		Map<String, Integer> notesData = null;
		
		public NotesSearchManager()
		{
			notesData = readNoteItems();
		}
		
		public Set<Integer> getMatchingNoteIds(String query)
		{
			Set<Integer> noteIds = new HashSet<Integer>();

			System.out.println("getMatchingNoteIds -> notesData="+notesData);
			
			for (Entry<String, Integer> entry : notesData.entrySet())
			{
				//TODO support localization
				if (entry.getKey().toUpperCase(Locale.US).contains(query.toUpperCase(Locale.US))) 
				{
					noteIds.add(entry.getValue());
				}
			}

			System.out.println("getMatchingNoteIds <- noteIds=" + noteIds);

			return noteIds;
		}
	}
}
