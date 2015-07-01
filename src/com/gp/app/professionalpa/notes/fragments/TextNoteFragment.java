package com.gp.app.professionalpa.notes.fragments;


import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.data.TextNote;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;

public class TextNoteFragment extends ListFragment
{
	private TextNoteFragmentAdapter adapter;

	private ListView listView = null;
	
	private TextNote note;
	
	public TextNoteFragment()
	{
		super();
	}
	
	public TextNoteFragment(TextNote note)
	{
		super();
		
		this.note = note;
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
			TextNote note = bundle.getParcelable(ProfessionalPAConstants.NOTE_DATA);
			
		    if(note != null)
		    {
				adapter = new TextNoteFragmentAdapter(getActivity(), note);
				
				listView.setAdapter(adapter);
				
				listView.setDivider(null);
				
				listView.setDividerHeight(0);			
				
				setListViewHeightBasedOnItems();
//				setListShown(true);
				listView.setBackgroundColor(note.getNoteColor());
				
				listView.setBackgroundResource(R.drawable.list_view_border_blue);
				
				MarginLayoutParams p = (MarginLayoutParams)listView.getLayoutParams();
				
				p.setMargins(5,5,5,5);
				
				adapter.notifyDataSetChanged();
		    }
		}
	}
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
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
	        
	        //TODO to be checked for NPE, it which case NPE can occur.
	        if(params != null)
	        {
	        	params.height = totalItemsHeight + totalDividersHeight;
		        listView.setLayoutParams(params);
		        listView.requestLayout();
	        }
	    }
	}
	
	public int getFragmentLength()
	{
		int length = 0;
		
		if(note != null)
		{
			length = note.getLength();
		}
		
		return length;
	}
}
