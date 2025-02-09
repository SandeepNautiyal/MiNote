package com.gp.app.minote.notes.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.gp.app.minote.R;
import com.gp.app.minote.data.Event;
import com.gp.app.minote.util.MiNoteConstants;
import com.gp.app.minote.notes.operations.NotesOperationManager;

public class EventNoteFragment extends Fragment
{
    private Event event = null;
	
    private EventNoteFragmentAdapter adapter;

	private ListView listView = null;

	public EventNoteFragment()
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
			Event event = bundle.getParcelable(MiNoteConstants.NOTE_DATA);
			
		    if(event != null)
		    {
                this.event = event;

				adapter = new EventNoteFragmentAdapter(getActivity(), event);
				
				listView.setAdapter(adapter);
				
				listView.setOnItemLongClickListener(new OnItemLongClickListener() 
				{
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) 
					{
						selectNote();
						
						return true;
					}
		        });
				
				listView.setOnItemClickListener(new OnItemClickListener() 
				{
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) 
					{
						deSelectNote();
					}
		        });
				
				listView.setBackgroundColor(0);
				
				listView.setBackgroundResource(R.drawable.list_view_border_red);
				
                MarginLayoutParams p = (MarginLayoutParams)listView.getLayoutParams();
				
                if(p != null)
				{
					p.setMargins(5,5,5,5);
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
	
	private void deSelectNote() 
	{
		if(NotesOperationManager.getInstance().getSelectedNoteIds().contains(event.getId()))
		{
			NotesOperationManager.getInstance().deSelectNote(event.getId());
		}
	}
	
	private void selectNote() 
	{
		NotesOperationManager.getInstance().selectNote(event.getId());
	}

}
