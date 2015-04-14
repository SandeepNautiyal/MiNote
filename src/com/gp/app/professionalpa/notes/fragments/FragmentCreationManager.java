package com.gp.app.professionalpa.notes.fragments;

import android.app.Fragment;
import android.os.Bundle;

import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;

public class FragmentCreationManager 
{
	public static Fragment createFragment(ProfessionalPANote note)
	{
		ProfessionalPANoteFragment fragment = new ProfessionalPANoteFragment();
	    
		fragment.setNoteFragmentId(note.getNoteId());
		
		Bundle bundle = new Bundle();

    	bundle.putParcelable(ProfessionalPAConstants.NOTE_DATA, note);
	    
	    fragment.setArguments(bundle);
	    
		return fragment;
	}
}
