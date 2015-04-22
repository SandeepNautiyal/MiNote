package com.gp.app.professionalpa.notes.operations;

import java.util.Arrays;

import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.notes.fragments.NotesManager;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class NoteCopyManager 
{
	private int originalId = -1;
	
	private int copiedNoteId = -1;
	
	private boolean isCopyInProgress = false;
	
    public NoteCopyManager(int noteId)
    {
    	this.originalId = noteId;
    	
    	isCopyInProgress = true;
    }

	public void copyNote() 
	{
		if(isCopyInProgress)
		{
			ProfessionalPANote note = NotesManager.getInstance().getNote(originalId);
			
			note.setCreationTime(System.currentTimeMillis());
			
			note.setLastEditedTime(System.currentTimeMillis());
			
			note.setNoteId(NotesManager.getInstance().getNextFreeNoteId());
			
			copiedNoteId = note.getNoteId();
			
			try
			{
				ProfessionalPAParameters.getProfessionalPANotesWriter().writeNotes(Arrays.asList(note));
				
				NotesManager.getInstance().addNote(copiedNoteId, note);
			}
			catch (ProfessionalPABaseException e) 
			{
				//TODO improve
			}
			
			ProfessionalPAParameters.getNotesActivity().addNote(copiedNoteId);
		}
	}
}
