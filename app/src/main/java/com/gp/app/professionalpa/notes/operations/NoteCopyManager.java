package com.gp.app.professionalpa.notes.operations;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;

import com.gp.app.professionalpa.data.Note;
import com.gp.app.professionalpa.data.TextNote;
import com.gp.app.professionalpa.layout.manager.NotesLayoutManagerActivity;
import com.gp.app.professionalpa.notes.database.NotesDBManager;
import com.gp.app.professionalpa.notes.fragments.NotesManager;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

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

				ProfessionalPAParameters.getNotesActivity().createFragmentForNote(copiedNote);
			}
		}
	}
}
