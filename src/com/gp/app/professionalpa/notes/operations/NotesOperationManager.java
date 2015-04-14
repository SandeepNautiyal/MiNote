package com.gp.app.professionalpa.notes.operations;

import java.util.List;

import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.layout.manager.ImageLocationPathManager;
import com.gp.app.professionalpa.notes.fragments.NotesManager;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class NotesOperationManager 
{
	private static NoteCopyManager notesCopyManager = null;
	
	private static boolean isCopyInProgress = false;
	
    public static void deleteNote(int noteId, List<String> imagesName, byte noteType)
    {
    	try 
        {
	        NotesManager.getInstance().deleteNote(noteId);

        	ProfessionalPAParameters.getProfessionalPANotesWriter().deleteXmlElement(noteId);
	        
			ProfessionalPAParameters.getNotesActivity().deleteNote(noteId);

	        if(noteType == ProfessionalPAConstants.IMAGE_NOTE || noteType == ProfessionalPAConstants.IMAGE_NOTE)
	        {
	        	for(int i = 0; i < imagesName.size(); i++)
	        	{
		        	ImageLocationPathManager.getInstance().deleteImage(imagesName.get(i));
	        	}
	        }
		} 
        catch (ProfessionalPABaseException e) 
        {
        	//TODO improve
		}
    }

	public static void startCopyProcess(int noteId, List<String> imageNames, byte noteType) 
	{
		notesCopyManager = new NoteCopyManager(noteId);
		
		isCopyInProgress = true;
	}

	public static void copyNote() 
	{
		if(isCopyInProgress)
		{
			notesCopyManager.copyNote();
		}
	}
    
}
