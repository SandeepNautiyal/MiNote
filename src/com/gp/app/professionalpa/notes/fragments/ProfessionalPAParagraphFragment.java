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
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.interfaces.XMLEntity;
import com.gp.app.professionalpa.notes.xml.ProfessionalPANotesWriter;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class ProfessionalPAParagraphFragment extends ListFragment
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
			
			values.clear();
			
			if(note != null)
			{
				values.addAll(note.getNoteItems());
				
				ListViewItemAdapter adapter = new ListViewItemAdapter(getActivity(), values);
				
				setListAdapter(adapter);
				
				getListView().setDivider(null);
				
				getListView().setDividerHeight(0);			
				
				adapter.notifyDataSetChanged();
			}
		}
	}
	
//	private void persistListElement() 
//	{
//		Set<String> fragmentState = new HashSet<String>();
//		
//		for(NotesListItem listItem : values)
//		{
//			String listItemState = listItem.convertToString();
//			
//			fragmentState.add(listItemState);
//		}
//		
//		String tag = this.getTag();
//
//		SharedPreferences sharedPrefernces = ProfessionalPAParameters.getSharedPreferences();
//		
//		Editor editor = sharedPrefernces.edit();
//		
//        Set<String> tags = sharedPrefernces.getStringSet("TAGS", null);
//		
//		if(tags == null)
//		{
//			tags = new HashSet<String>();
//		}
//		
//		tags.add(tag);
//		
//		editor.putStringSet(tag, fragmentState);
//		
//		editor.putStringSet("TAGS", tags);
//		
//		editor.commit();
//	}
	
	public ProfessionalPAListFragment createFragmentFromFile()
	{
		return null;
	}


}
