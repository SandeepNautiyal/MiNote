package com.gp.app.professionalpa.notes.fragments;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.gp.app.professionalpa.data.ProfessionalPANote;

public class NotesManager 
{
	private static NotesManager notesManager = null;
	
	Map<Integer, ProfessionalPANote> notes = new TreeMap<Integer, ProfessionalPANote>();
	
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

	public ProfessionalPANote getNote(int noteId)
	{
		return notes.get(noteId);
	}
	
	public void addNote(int noteId, ProfessionalPANote note)
	{
		notes.put(noteId, note);
	}

	public Map<Integer, ProfessionalPANote> getNotes()
	{
		return notes;
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
			lastOccupiedKey = ((TreeMap<Integer, ProfessionalPANote>)notes).lastKey();
		}
		 
		return ++lastOccupiedKey;
	}
}
