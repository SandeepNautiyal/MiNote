package com.gp.app.professionalpa.notes.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gp.app.professionalpa.data.Note;
import com.gp.app.professionalpa.data.TextNote;
import com.gp.app.professionalpa.notes.database.NotesDBManager;
import com.gp.app.professionalpa.notes.fragments.NotesManager;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class NoteCopyManager 
{
	private List<Integer> originalId = new ArrayList<Integer>();
	
	private int copiedNoteId = -1;
	
	private boolean isCopyInProgress = false;
	
    public NoteCopyManager(List<Integer> noteId)
    {
    	this.originalId = noteId;
    	
    	isCopyInProgress = true;
    }

	public void copyNote() 
	{
		if(isCopyInProgress)
		{
			Note note = NotesManager.getInstance().getNote(originalId.get(0));
			
			if(note != null && note.getType() != Note.EVENT_NOTE)
			{
				TextNote textNote = (TextNote)note;
				
				textNote.setCreationTime(System.currentTimeMillis());
				
				textNote.setLastEditedTime(System.currentTimeMillis());
				
				textNote.setNoteId(NotesManager.getInstance().getNextFreeNoteId());
				
				copiedNoteId = note.getId();
				
				NotesDBManager.getInstance().saveNotes(Arrays.asList(textNote));
					
				ProfessionalPAParameters.getNotesActivity().createFragmentForNote(note);
			}
		}
	}
}
