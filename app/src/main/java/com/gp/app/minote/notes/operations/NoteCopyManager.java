package com.gp.app.minote.notes.operations;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;

import com.gp.app.minote.data.Note;
import com.gp.app.minote.data.TextNote;
import com.gp.app.minote.layout.manager.NotesLayoutManagerActivity;
import com.gp.app.minote.notes.database.NotesDBManager;
import com.gp.app.minote.notes.fragments.NotesManager;
import com.gp.app.minote.util.MiNoteParameters;

public class NoteCopyManager 
{
    public NoteCopyManager()
    {
    }

	public void copyNote(List<Integer> selectedNoteIds) 
	{
		for(int i = 0; i < selectedNoteIds.size(); i++)
		{
			Note note = NotesManager.getInstance().getNote(selectedNoteIds.get(i));

			if(note != null && note.getType() != Note.EVENT_NOTE)
			{
	            TextNote copiedNote = NotesManager.getInstance().createCopyOfNote((TextNote)note);
	            
				NotesDBManager.getInstance().saveNotes(Arrays.asList(copiedNote));

				MiNoteParameters.getNotesActivity().createFragmentForNote(copiedNote);
			}
		}
	}
}
