package com.gp.app.professionalpa.notes.fragments;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;

import com.gp.app.professionalpa.data.Event;
import com.gp.app.professionalpa.data.Note;
import com.gp.app.professionalpa.data.TextNote;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;

public class FragmentCreationManager extends ListFragment
{
	public static Fragment createFragment(Note note)
	{
		Fragment noteFragment = null;
		
    	if(note.getType() != Note.EVENT_NOTE)
    	{
    		TextNote textNote = (TextNote)note;
    		
    		TextNoteFragment fragment = new TextNoteFragment(textNote);
    	    
    		Bundle bundle = new Bundle();

        	bundle.putParcelable(ProfessionalPAConstants.NOTE_DATA, textNote);
    	    
    	    fragment.setArguments(bundle);
    	    
    	    noteFragment = fragment;
    	}
    	else
    	{
    		Event event = (Event)note;
    		
            EventNoteFragment fragment = new EventNoteFragment(event);
    	    
    		Bundle bundle = new Bundle();

        	bundle.putParcelable(ProfessionalPAConstants.NOTE_DATA, event);
    	    
    	    fragment.setArguments(bundle);
    	    
    	    noteFragment = fragment;
    	}
    	
    	return noteFragment;
	}
}
