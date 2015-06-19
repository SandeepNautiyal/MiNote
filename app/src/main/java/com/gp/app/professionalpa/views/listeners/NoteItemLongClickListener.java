package com.gp.app.professionalpa.views.listeners;

import android.view.ActionMode;
import android.view.View;
import android.view.View.OnLongClickListener;

import com.gp.app.professionalpa.notes.operations.NotesOperationManager;

public class NoteItemLongClickListener implements OnLongClickListener
{
    private int noteId = -1;

	public NoteItemLongClickListener(int noteId)
    {
    	this.noteId  = noteId;
    }
	
	@Override
	public boolean onLongClick(View view) 
	{
		NotesOperationManager notesOperationManager = NotesOperationManager.getInstance(); 
		
		notesOperationManager.selectNote(noteId);
        
        return true;
	}

}
