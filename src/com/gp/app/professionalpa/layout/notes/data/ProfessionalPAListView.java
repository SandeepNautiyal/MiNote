package com.gp.app.professionalpa.layout.notes.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.app.ListFragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Parcelable;

import com.gp.app.professionalpa.data.ListViewItemAdapter;
import com.gp.app.professionalpa.data.NotesListItem;
import com.gp.app.professionalpa.notes.save.Writable;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class ProfessionalPAListView extends ListFragment implements Serializable, Writable
{
	private ArrayList<NotesListItem> values = new ArrayList<NotesListItem>();
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
//        setRetainInstance(true);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) 
	{
		System.out.println("Fragment -> onSaveInstanceState -> ");

	    super.onSaveInstanceState(outState);
	    
	    NotesListItem[] valuesInListFragment = new NotesListItem[values.size()];
	    
	    outState.putParcelableArray("LIST_ITEMS", values.toArray(valuesInListFragment));  
	    
		System.out.println("Fragment -> onSaveInstanceState <- return="+values);
	 }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		Bundle bundle = this.getArguments();
		
		if(bundle != null)
		{
			Parcelable[] parceables = bundle.getParcelableArray("LIST_ITEMS");
			
			System.out.println("onActivityCreated -> values="+values);

			values.clear();
			
			for(int i = 0, size = parceables == null ? 0 : parceables.length; i < size; i++)
			{
				values.add((NotesListItem)parceables[i]);
			}
			
			persistListElement();
			
			System.out.println("onActivityCreated -> values1="+values);

			System.out.println("onActivityCreated -> values ="+values);

			ListViewItemAdapter mAdapter = new ListViewItemAdapter(getActivity(), values);
			
			setListAdapter(mAdapter);
			
			getListView().setDivider(null);
			
			getListView().setDividerHeight(0);			
			
			mAdapter.notifyDataSetChanged();
		}
		
		System.out.println("onActivityCreated <- return");
	}
	
	private void persistListElement() 
	{
		Set<String> fragmentState = new HashSet<String>();
		
		for(NotesListItem listItem : values)
		{
			String listItemState = listItem.convertToString();
			
			fragmentState.add(listItemState);
		}
		
		String tag = this.getTag();

		SharedPreferences sharedPrefernces = ProfessionalPAParameters.getSharedPreferences();
		
		Editor editor = sharedPrefernces.edit();
		
        Set<String> tags = sharedPrefernces.getStringSet("TAGS", null);
		
		if(tags == null)
		{
			tags = new HashSet<String>();
		}
		
		tags.add(tag);
		
		editor.putStringSet(tag, fragmentState);
		
		editor.putStringSet("TAGS", tags);
		
		editor.commit();
	}

	public void saveNote()
	{
		
	}
	
	public ProfessionalPAListView createFragmentFromFile()
	{
		return null;
	}
}
