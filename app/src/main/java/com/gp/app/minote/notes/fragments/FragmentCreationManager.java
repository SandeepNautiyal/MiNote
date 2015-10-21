package com.gp.app.minote.notes.fragments;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;

import com.gp.app.minote.data.Event;
import com.gp.app.minote.data.Note;
import com.gp.app.minote.data.TextNote;
import com.gp.app.minote.util.MiNoteConstants;

public class FragmentCreationManager extends ListFragment
{
	public static Fragment createFragment(Note note)
	{
		Fragment noteFragment = null;
		
    	if(note.getType() != Note.EVENT_NOTE)
    	{
    		TextNote textNote = (TextNote)note;
    		
    		TextNoteFragment fragment = new TextNoteFragment();
    	    
    		Bundle bundle = new Bundle();

        	bundle.putParcelable(MiNoteConstants.NOTE_DATA, textNote);
    	    
    	    fragment.setArguments(bundle);
    	    
    	    noteFragment = fragment;
    	}
    	else
    	{
    		Event event = (Event)note;
    		
            EventNoteFragment fragment = new EventNoteFragment();
    	    
    		Bundle bundle = new Bundle();

        	bundle.putParcelable(MiNoteConstants.NOTE_DATA, event);
    	    
    	    fragment.setArguments(bundle);
    	    
    	    noteFragment = fragment;
    	}
    	
    	return noteFragment;
	}
}
