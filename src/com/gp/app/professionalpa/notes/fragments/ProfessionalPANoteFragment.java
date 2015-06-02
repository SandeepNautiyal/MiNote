package com.gp.app.professionalpa.notes.fragments;


import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.notes.fragments.NoteFragmentAdapter;
import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;

public class ProfessionalPANoteFragment extends ListFragment
{
	private int noteId = -1;
	
	private NoteFragmentAdapter adapter;

	private ListView listView = null;
	
	private int noteColor;
	
	public ProfessionalPANoteFragment(int noteColor)
	{
		super();
		
		this.noteColor = noteColor;
	}
	
	/**
	 * 
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) 
	{
		listView = (ListView)inflater.inflate(R.layout.listview_for_list_fragment, null);   
		
	    return listView;       
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
				adapter = new NoteFragmentAdapter(getActivity(), note);
				
				listView.setAdapter(adapter);
				
				listView.setDivider(null);
				
				listView.setDividerHeight(0);			
				
				setListViewHeightBasedOnItems();
//				setListShown(true);
				listView.setBackgroundColor(noteColor);
				
				adapter.notifyDataSetChanged();
		    }
		}
	}
	
	public void setNoteFragmentId(int noteId)
	{
		this.noteId = noteId;
	}
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
	}
	
	public int getFragmentNoteId()
	{
		return noteId;
	}
	
	public int getNumberOfItems()
	{
		return NotesManager.getInstance().getNote(noteId).getNoteItems().size();
	}
	
	/**
	 * Sets ListView height dynamically based on the height of the items.   
	 *
	 * @param listView to be resized
	 * @return true if the listView is successfully resized, false otherwise
	 */
	public void setListViewHeightBasedOnItems() 
	{
	    ListAdapter listAdapter = listView.getAdapter();
	    
	    if (listAdapter != null) {

	        int numberOfItems = listAdapter.getCount();

	        int totalItemsHeight = 0;
	        
	        for (int itemPos = 0; itemPos < numberOfItems; itemPos++) 
	        {
	            View item = listAdapter.getView(itemPos, null, listView);
	            item.measure(0, 0);
	            totalItemsHeight += item.getMeasuredHeight();
	        }

	        // Get total height of all item dividers.
	        int totalDividersHeight = listView.getDividerHeight() * (numberOfItems - 1);

	        // Set list height.
	        ViewGroup.LayoutParams params = listView.getLayoutParams();
	        params.height = totalItemsHeight + totalDividersHeight;
	        listView.setLayoutParams(params);
	        listView.requestLayout();
	    }
	}
	
	public int getFragmentLength()
	{
		return NotesManager.getInstance().getNote(noteId).getLength();
	}
}
