package com.gp.app.professionalpa.views.listeners;

import java.util.List;

import android.content.Intent;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.layout.manager.ImageLocationPathManager;
import com.gp.app.professionalpa.layout.manager.NotesLayoutManagerActivity;
import com.gp.app.professionalpa.notes.operations.NotesOperationManager;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class NotesActionMode implements ActionMode.Callback 
{
	int noteId = -1;
	
	public NotesActionMode(int noteId)
	{
		this.noteId = noteId;
	}
    // Called when the action mode is created; startActionMode() was called
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) 
    {
        MenuInflater inflater = mode.getMenuInflater();
    
        inflater.inflate(R.menu.contextual_menu, menu);
        
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu)
    {
        return true; 
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) 
    {
        switch (item.getItemId())
        {
            case R.id.item_delete:
            	NotesOperationManager.getInstance().deleteSelectedNotes();
            	mode.finish();
                return true;
            case R.id.action_discard_notes:
            	NotesOperationManager.getInstance().startCopyProcess();
            case R.id.pickColor:
            	NotesOperationManager.getInstance().createColourPicker();
            default:
                
        }
        
        mode.finish();
        
        return false;
    }

	private void copyNote() 
    {
		
	}
	
	@Override
    public void onDestroyActionMode(ActionMode mode) 
    {
		
    }
	
	public int getNoteId()
	{
		return noteId;
	}
}