package com.gp.app.professionalpa.notes.fragments;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.ListFragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Parcelable;

import com.gp.app.professionalpa.data.ListViewItemAdapter;
import com.gp.app.professionalpa.data.NoteListItem;
import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionPARuntimeException;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.interfaces.XMLEntity;
import com.gp.app.professionalpa.notes.xml.ProfessionalPANotesReader;
import com.gp.app.professionalpa.notes.xml.ProfessionalPANotesWriter;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class ProfessionalPAListFragment extends ListFragment
{
	private ArrayList<NoteListItem> values = new ArrayList<NoteListItem>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) 
	{
	    super.onSaveInstanceState(outState);
	    
//	    outState.putParcelableArrayList(ProfessionalPAConstants.NOTE_DATA, values);  
	 }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		Bundle bundle = this.getArguments();
		
		if(bundle != null)
		{
			ProfessionalPANote note = bundle.getParcelable(ProfessionalPAConstants.NOTE_DATA);
			
		    if(note != null)
		    {
		    	values.clear();
		    	
		    	values.addAll(note.getNoteItems());
		    	
				ListViewItemAdapter adapter = new ListViewItemAdapter(getActivity(), note.getNoteItems());
				
				setListAdapter(adapter);
				
				getListView().setDivider(null);
				
				getListView().setDividerHeight(0);			
				
				adapter.notifyDataSetChanged();
		    }
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public ProfessionalPAListFragment createFragmentFromFile()
	{
		return null;
	}
}
