package com.gp.app.professionalpa.notes.fragments;

import java.util.ArrayList;

import android.app.ListFragment;
import android.os.Bundle;
import com.gp.app.professionalpa.data.ListViewItemAdapter;
import com.gp.app.professionalpa.data.NoteListItem;
import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;

public class ProfessionalPANoteFragment extends ListFragment
{
	private ArrayList<NoteListItem> values = new ArrayList<NoteListItem>();
	
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

	public ProfessionalPANoteFragment createFragmentFromFile()
	{
		return null;
	}
}
