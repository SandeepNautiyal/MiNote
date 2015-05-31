package com.gp.app.professionalpa.notes.operations;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.view.Window;
import android.widget.GridView;

import com.gp.app.professionalpa.colorpicker.ColourPickerAdapter;
import com.gp.app.professionalpa.colorpicker.ColourProperties;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.layout.manager.ImageLocationPathManager;
import com.gp.app.professionalpa.notes.database.NotesDBManager;
import com.gp.app.professionalpa.notes.fragments.NotesManager;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class NotesOperationManager 
{
	private static NoteCopyManager notesCopyManager = null;
	
	private static boolean isCopyInProgress = false;
	
	private static NotesOperationManager manager = null;

	private int selectedNoteId;
	
    public void deleteNote(List<String> imagesName, byte noteType)
    {
    	NotesManager.getInstance().deleteNote(selectedNoteId);

        NotesDBManager.getInstance().deleteNote(selectedNoteId);
	        
		ProfessionalPAParameters.getNotesActivity().deleteNote(selectedNoteId);

	    if(noteType == ProfessionalPAConstants.IMAGE_NOTE || noteType == ProfessionalPAConstants.IMAGE_NOTE)
	    {
	     	for(int i = 0; i < imagesName.size(); i++)
	       	{
		       	ImageLocationPathManager.getInstance().deleteImage(imagesName.get(i));
	       	}
	    }
    }

	public void startCopyProcess(List<String> imageNames, byte noteType) 
	{
		notesCopyManager = new NoteCopyManager(selectedNoteId);
		
		isCopyInProgress = true;
	}

	public void copyNote() 
	{
		if(isCopyInProgress)
		{
			notesCopyManager.copyNote();
		}
	}

	public void createColourPicker()
	{
        ArrayList<ColourProperties> gridArray = new ArrayList<ColourProperties>();
		
		gridArray.add(ColourProperties.RED);
		
		gridArray.add(ColourProperties.GREEN);
	    
		gridArray.add(ColourProperties.BLUE);
	    
		gridArray.add(ColourProperties.YELLOW);
	    
		gridArray.add(ColourProperties.GRAY);
	    
		gridArray.add(ColourProperties.WHITE);
	    
		gridArray.add(ColourProperties.CYAN);
	    
		gridArray.add(ColourProperties.MAGENTA);
	    
		gridArray.add(ColourProperties.DARK_GRAY);
	    
		gridArray.add(ColourProperties.PINK);
		
		Activity noteActivity = ProfessionalPAParameters.getNotesActivity();
		
		GridView gridView = new GridView(noteActivity); 
		
		gridView.setBackgroundColor(Color.parseColor("#7F7CD9"));
		
		gridView.setNumColumns(5);
		
		Dialog dialog = new Dialog(noteActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		ColourPickerAdapter adapter = new ColourPickerAdapter(noteActivity, gridArray, dialog);
		
		gridView.setAdapter(adapter);
		
		 //before     
        dialog.setContentView(gridView);
        dialog.setCancelable(true);
        dialog.show();
	}

	public static NotesOperationManager getInstance() 
	{
		if(manager == null)
		{
			manager = new NotesOperationManager();
		}
		
		return manager;
	}

	public void setSelectedNote(int noteId) 
	{
		System.out.println("setSelectedNote -> noteId="+noteId);
		
		selectedNoteId = noteId;
	}
	
	public int getSelectedNoteId()
	{
		return selectedNoteId;
	}
    
}
