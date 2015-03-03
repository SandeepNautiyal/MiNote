package com.gp.app.professionalpa.notes.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import android.app.Fragment;
import android.os.Bundle;

import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;

public class FragmentCreationManager 
{
	private ProfessionalPANoteComparator comparator = null;
	
	private List<Fragment> fragments = new ArrayList<Fragment>();

	public FragmentCreationManager()
	{
		comparator = new ProfessionalPANoteComparator();
	}
	
	public Fragment createFragment(ProfessionalPANote note)
	{
		for(Fragment fragment : fragments)
		{
			Bundle bundle = fragment.getArguments();
			
			ProfessionalPANote fragmentNote = bundle.getParcelable(ProfessionalPAConstants.NOTE_DATA);
			
			if(fragmentNote != null)
			{
				int result = comparator.compare(note, fragmentNote);
				
				if(result == 0)
				{
					return null;
				}
			}
		}
		
		Fragment fragment = note.isParagraphNote() ? new ProfessionalPAParagraphFragment() : new ProfessionalPAListFragment();
	    
		Bundle bundle = new Bundle();

    	bundle.putParcelable(ProfessionalPAConstants.NOTE_DATA, note);
	    
	    fragment.setArguments(bundle);
	    
	    fragments.add(fragment);
	    
		return fragment;
	}
	
	public void removeFragment(Fragment fragment)
	{
		System.out.println("removeFragment1 ="+fragments.size());
		
		fragments.remove(fragment);
		
		System.out.println("removeFragment2 ="+fragments.size());

	}
	
	public List<Fragment> getFragments()
	{
		return fragments;
	}
}
