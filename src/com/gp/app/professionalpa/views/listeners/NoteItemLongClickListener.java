package com.gp.app.professionalpa.views.listeners;

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
        view.startActionMode(actionModelCallback);
        
        NotesOperationManager.getInstance().setSelectedNote(actionModelCallback.getNoteId());
        
        view.setSelected(true);
        
        return true;
	}

}
