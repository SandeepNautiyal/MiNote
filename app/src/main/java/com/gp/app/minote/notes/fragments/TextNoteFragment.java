package com.gp.app.minote.notes.fragments;


import android.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.gp.app.minote.R;
import com.gp.app.minote.data.TextNote;
import com.gp.app.minote.util.MiNoteConstants;

public class TextNoteFragment extends ListFragment
{
	private TextNoteFragmentAdapter adapter;

	private ListView listView = null;
	
	private TextNote note;
	
	public TextNoteFragment()
	{
		super();
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
			TextNote note = bundle.getParcelable(MiNoteConstants.NOTE_DATA);
			
		    if(note != null)
		    {
                this.note = note;

				adapter = new TextNoteFragmentAdapter(getActivity(), note);
				
				listView.setAdapter(adapter);
				
				listView.setDivider(null);
				
				listView.setDividerHeight(0);

//                listView.setElevation(5.0f);

				setListViewHeightBasedOnItems();
//				setListShown(true);
				listView.setBackgroundColor(note.getNoteColor());
				
//				listView.setBackgroundResource(R.drawable.list_view_border_blue);

//                listView.setBackgroundColor(Color.rgb(240,240,240));

                listView.getBackground().setAlpha(255);

				MarginLayoutParams p = (MarginLayoutParams)listView.getLayoutParams();

				if(p != null)
				{
					p.setMargins(1,2,1,2);
				}

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
