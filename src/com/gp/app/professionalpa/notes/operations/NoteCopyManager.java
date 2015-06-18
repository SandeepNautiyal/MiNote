package com.gp.app.professionalpa.notes.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gp.app.professionalpa.data.ProfessionalPANote;
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
			ProfessionalPANote note = NotesManager.getInstance().getNote(originalId.get(0));
			
			note.setCreationTime(System.currentTimeMillis());
			
			note.setLastEditedTime(System.currentTimeMillis());
			
			note.setNoteId(NotesManager.getInstance().getNextFreeNoteId());
			
			copiedNoteId = note.getNoteId();
			
			NotesDBManager.getInstance().saveNotes(Arrays.asList(note));
				
			NotesManager.getInstance().addNote(copiedNoteId, note);
			
			ProfessionalPAParameters.getNotesActivity().addNote(copiedNoteId);
		}
	}
}
