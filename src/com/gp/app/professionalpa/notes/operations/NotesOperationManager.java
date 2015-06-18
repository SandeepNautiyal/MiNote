package com.gp.app.professionalpa.notes.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.view.Window;
import android.widget.GridView;

import com.gp.app.professionalpa.colorpicker.ColourPickerAdapter;
import com.gp.app.professionalpa.colorpicker.ColourProperties;
import com.gp.app.professionalpa.data.NoteItem;
import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.layout.manager.ImageLocationPathManager;
import com.gp.app.professionalpa.layout.manager.NotesLayoutManagerActivity;
import com.gp.app.professionalpa.notes.database.NotesDBManager;
import com.gp.app.professionalpa.notes.fragments.NotesManager;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class NotesOperationManager 
{
	private static NoteCopyManager notesCopyManager = null;
	
	private static boolean isCopyInProgress = false;
	
	private static NotesOperationManager manager = null;

	private List<Integer> selectedNoteIds = new ArrayList<Integer>();
	
    public void deleteSelectedNotes()
    {
    	NotesManager.getInstance().deleteNotes(selectedNoteIds);

        NotesDBManager.getInstance().deleteNotes(selectedNoteIds);
	        
		ProfessionalPAParameters.getNotesActivity().deleteNotes(selectedNoteIds);

		for(int i = 0; i < selectedNoteIds.size(); i++)
		{
			ProfessionalPANote note = NotesManager.getInstance().getNote(selectedNoteIds.get(i));
			
			if(note != null)
			{
				List<NoteItem> items = note.getNoteItems();
				
				for(int j = 0, size = items.size(); j < size; j++)
				{
					NoteItem item = note.getNoteItems().get(i);
					
					ImageLocationPathManager.getInstance().deleteImage(item.getImageName());
				}
			}
		}
		
		selectedNoteIds.clear();
    }

	public void startCopyProcess() 
	{
		notesCopyManager = new NoteCopyManager(selectedNoteIds);
		
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

	public void addSelectedNote(int noteId) 
	{
		selectedNoteIds.add(noteId);
		
		ProfessionalPAParameters.getNotesActivity().setNoteSelected(noteId);
	}
	
	public boolean isNoteSelected()
	{
		return selectedNoteIds.size() > 0;
	}
	
	public List<Integer> getSelectedNoteIds()
	{
		return selectedNoteIds;
	}
	
	public void clearSelectedNotes()
	{
		 selectedNoteIds.clear();
	}
}
