package com.gp.app.minote.listeners;

import android.graphics.Color;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.gp.app.minote.R;
import com.gp.app.minote.notes.operations.NotesOperationManager;

public class NotesActionModeCallback implements ActionMode.Callback 
{
	public NotesActionModeCallback()
	{
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
    	if(NotesOperationManager.getInstance().getSelectedNoteIds().size() > 1)
    	{
    		MenuItem item = menu.findItem(R.id.itemEdit);
    		
    		item.setVisible(false);
    	}
    	
        return true; 
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) 
    {
        switch (item.getItemId())
        {
            case R.id.itemDelete:
            	NotesOperationManager.getInstance().deleteSelectedNotes();
            	mode.finish();
            	break;
            case R.id.itemEdit:
            	NotesOperationManager.getInstance().editSelectedNote();
            	break;
            case R.id.pickColor:
            	NotesOperationManager.getInstance().createColourPicker();
            	break;
            case R.id.shareNote:
            	NotesOperationManager.getInstance().shareSelectedNote(false);
            	break;
            case R.id.noteCopy:
            	NotesOperationManager.getInstance().startCopyProcess();
            	break;
            case R.id.connectFriends:
                NotesOperationManager.getInstance().shareSelectedNote(true);
                break;
            default:
        }
        
        mode.finish();
        
        return true;
    }

	private void copyNote() 
    {
		
	}
	
	@Override
    public void onDestroyActionMode(ActionMode mode) 
    {
		
    }
}