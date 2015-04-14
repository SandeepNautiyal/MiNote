package com.gp.app.professionalpa.views.listeners;

import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.layout.manager.NotesLayoutManagerActivity;
import com.gp.app.professionalpa.notes.xml.ProfessionalPANotesWriter;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

import android.content.Intent;
import android.view.View;
import android.view.View.OnLongClickListener;

public class NoteItemLongClickListener implements OnLongClickListener
{
	NotesActionMode actionModelCallback = null;
	
	int noteId  = -1;
	
    public NoteItemLongClickListener(NotesActionMode actionMode)
    {
    	actionModelCallback = actionMode;
    }
	
	@Override
	public boolean onLongClick(View view) 
	{
        view.startActionMode(actionModelCallback);
        
        view.setSelected(true);
        
        return true;
	}

}
