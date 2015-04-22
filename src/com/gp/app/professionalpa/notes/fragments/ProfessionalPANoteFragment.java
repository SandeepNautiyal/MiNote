package com.gp.app.professionalpa.notes.fragments;


import android.app.ListFragment;
import android.os.Bundle;
import com.gp.app.professionalpa.data.ListViewItemAdapter;
import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;

public class ProfessionalPANoteFragment extends ListFragment
{
	private int noteId = -1;
	
	public ProfessionalPANoteFragment()
	{
		super();
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
				ListViewItemAdapter adapter = new ListViewItemAdapter(getActivity(), note);
				
				setListAdapter(adapter);
				
				getListView().setDivider(null);
				
				getListView().setDividerHeight(0);			
				
				adapter.notifyDataSetChanged();
		    }
		}
	}
	
	public void setNoteFragmentId(int noteId)
	{
		this.noteId = noteId;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	public int getFragmentNoteId()
	{
		return noteId;
	}
}
