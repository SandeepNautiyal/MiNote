package com.gp.app.professionalpa.views.listeners;

import android.view.View;
import android.view.View.OnLongClickListener;

public class NoteItemLongClickListener implements OnLongClickListener
{
	NotesActionMode actionModelCallback = null;
	
	int noteId  = -1;
	
    public NoteItemLongClickListener(int noteId)
    {
    	this.noteId = noteId;
    	
    	actionModelCallback = new NotesActionMode();
    }
	
	@Override
	public boolean onLongClick(View view) 
	{
        view.startActionMode(actionModelCallback);
        
        view.setSelected(true);
        
        return false;
	}

}
