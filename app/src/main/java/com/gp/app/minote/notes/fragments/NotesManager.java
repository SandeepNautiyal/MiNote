package com.gp.app.minote.notes.fragments;

import com.gp.app.minote.data.Note;
import com.gp.app.minote.data.TextNote;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class NotesManager 
{
	private static NotesManager notesManager = null;
	
	Map<Integer, Note> notes = new TreeMap<Integer, Note>();
	
	private NotesManager()
	{
		
	}

	public static NotesManager getInstance()
	{
		if(notesManager == null)
		{
			notesManager = new NotesManager();
		}
		
		return notesManager;
	}

	public Note getNote(int noteId)
	{
		return notes.get(noteId);
	}
	
	public void addNote(int noteId, Note note)
	{
		notes.put(noteId, note);
	}

	public void deleteAllNotes()
	{
		notes.clear();
	}

	public void deleteNotes(List<Integer> noteIds)
	{
		for(int i = 0; i < noteIds.size(); i++)
		{
			notes.remove(noteIds.get(i));
		}
	}
	
	public int getNextFreeNoteId() 
	{
		int lastOccupiedKey = 0;
		
		if(notes.size() > 0)
		{
			lastOccupiedKey = ((TreeMap<Integer, Note>)notes).lastKey();
		}
		 
		return ++lastOccupiedKey;
	}

	public TextNote createCopyOfNote(TextNote note) 
	{
		int noteId = NotesManager.getInstance().getNextFreeNoteId();
		
		TextNote copiedNote = new TextNote(noteId, note.getType(), note.getNoteItems());
		
		copiedNote.setCreationTime(System.currentTimeMillis());
		
		copiedNote.setLastEditedTime(System.currentTimeMillis());
		
		return copiedNote;
	}
}
