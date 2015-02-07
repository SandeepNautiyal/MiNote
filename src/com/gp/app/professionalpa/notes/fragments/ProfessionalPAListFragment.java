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
import com.gp.app.professionalpa.data.NotesListItem;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.notes.xml.ProfessionalPANotesReader;
import com.gp.app.professionalpa.notes.xml.ProfessionalPANotesWriter;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class ProfessionalPAListFragment extends ListFragment
{
	private List<NotesListItem> values = new ArrayList<NotesListItem>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) 
	{
	    super.onSaveInstanceState(outState);
	    
	    NotesListItem[] valuesInListFragment = new NotesListItem[values.size()];
	    
	    outState.putParcelableArray("LIST_ITEMS", values.toArray(valuesInListFragment));  
	 }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		Bundle bundle = this.getArguments();
		
		if(bundle != null)
		{
			Parcelable[] parceables = bundle.getParcelableArray("LIST_ITEMS");
			
			values.clear();
			
			for(int i = 0, size = parceables == null ? 0 : parceables.length; i < size; i++)
			{
				values.add((NotesListItem)parceables[i]);
			}
			
			ListViewItemAdapter adapter = new ListViewItemAdapter(getActivity(), values);
			
			setListAdapter(adapter);
			
			getListView().setDivider(null);
			
			getListView().setDividerHeight(0);			
			
			adapter.notifyDataSetChanged();
			
			try
			{
				persistListElement();
			}
			catch(ProfessionalPABaseException exception)
			{
				//TODO improve
			}
			
			try 
			{
				ProfessionalPANotesReader reader = ProfessionalPAParameters.getProfessionalPANotesReader();
				
				reader.readNotes();
			} 
			catch (ProfessionalPABaseException exception) 
			{
				//TODO improve
			}
			
		}
	}
	
	private void persistListElement() throws ProfessionalPABaseException
	{
//		dummyMethod();
		
		ProfessionalPANotesWriter fragmentWriter = ProfessionalPAParameters.getProfessionalPANotesWriter();
		
		fragmentWriter.writeNotes("NotesList", values);
		
		fragmentWriter.completeWritingProcess();
	}

	private void dummyMethod() {
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
	
	public ProfessionalPAListFragment createFragmentFromFile()
	{
		return null;
	}
}
