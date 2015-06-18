package com.gp.app.professionalpa.views.listeners;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnLongClickListener;

import com.gp.app.professionalpa.notes.operations.NotesOperationManager;

public class NoteItemLongClickListener implements OnLongClickListener
{
	NotesActionMode actionModelCallback = null;
	
    public NoteItemLongClickListener(NotesActionMode actionMode)
    {
    	actionModelCallback = actionMode;
    }
	
	@Override
	public boolean onLongClick(View view) 
	{
		NotesOperationManager notesOperationManager = NotesOperationManager.getInstance(); 
		
		if(!notesOperationManager.isNoteSelected())
		{
	        view.startActionMode(actionModelCallback);
		}
        
		notesOperationManager.addSelectedNote(actionModelCallback.getNoteId());
        
        view.setSelected(true);
        
        return true;
	}

}
