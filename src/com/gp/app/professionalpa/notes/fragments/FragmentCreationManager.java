package com.gp.app.professionalpa.notes.fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
		System.out.println("createFragment -> fragments size="+fragments.size()+" fragments="+fragments);
		
		for(Fragment fragment : fragments)
		{
			Bundle bundle = fragment.getArguments();
			
			ProfessionalPANote fragmentNote = bundle.getParcelable(ProfessionalPAConstants.NOTE_DATA);
			
			if(fragmentNote != null)
			{
				System.out.println("createFragment -> fragmentNote");

				int result = comparator.compare(note, fragmentNote);
				
				System.out.println("createFragment -> result="+result);

				if(result == 0)
				{
					return null;
				}
			}
		}
		
		Fragment fragment = new ProfessionalPANoteFragment();
	    
		Bundle bundle = new Bundle();

    	bundle.putParcelable(ProfessionalPAConstants.NOTE_DATA, note);
	    
	    fragment.setArguments(bundle);
	    
	    System.out.println("fragment added="+fragment);
	    
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

	public void removeAllFragments() 
	{
		fragments.clear();
	}
	
	public Fragment deleteFragment(ProfessionalPANote note)
	{
		Fragment resultFragment = null;
		
		Iterator<Fragment> iterator = fragments.iterator();
		
		while(iterator.hasNext())
		{
			Fragment fragment = iterator.next();
			
			Bundle bundle = fragment.getArguments();
			
			ProfessionalPANote fragmentNote = bundle.getParcelable(ProfessionalPAConstants.NOTE_DATA);
			
			if(fragmentNote != null)
			{
				int result = comparator.compare(note, fragmentNote);
				
				System.out.println("createFragment -> result="+result);

				if(result == 0)
				{
					resultFragment = fragment;
					
					iterator.remove();
					
					break;
				}
			}
		}
		
		System.out.println("deleteFragment -> resultFragment="+resultFragment);
		
		return resultFragment;
	}
}
