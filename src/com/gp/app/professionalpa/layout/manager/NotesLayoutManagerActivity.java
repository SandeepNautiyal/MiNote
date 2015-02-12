package com.gp.app.professionalpa.layout.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.data.NoteListItem;
import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.notes.fragments.ProfessionalPAListFragment;
import com.gp.app.professionalpa.notes.fragments.ProfessionalPAParagraphFragment;
import com.gp.app.professionalpa.notes.xml.ProfessionalPANotesReader;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class NotesLayoutManagerActivity extends Activity {

	private static final String FRAGMENT_TAGS = "FRAGMENT_TAGS";

	private static final String NUMBER_OF_LINEAR_LAYOUTS = "NUMBER_OF_LINEAR_LAYOUTS";

	private static final int LIST_ACTIVITY_RESULT_CREATED = 1;
	
	private static final byte NUMBER_OF_LINEAR_LAYOUT_FOR_SMALL_SCREEN_PORTRAIT = 1;
	
	private static final byte NUMBER_OF_LINEAR_LAYOUT_FOR_SMALL_SCREEN_LANDSCAPE = 2;
	
    private static final short NUMBER_OF_LINEAR_LAYOUT_FOR_NORMAL_SCREEN_PORTRAIT = 2;
	
	private static final short NUMBER_OF_LINEAR_LAYOUT_FOR_NORMAL_SCREEN_LANDSCAPE = 3;
	
    private static final short NUMBER_OF_LINEAR_LAYOUT_FOR_LARGE_SCREEN_PORTRAIT = 3;
	
	private static final short NUMBER_OF_LINEAR_LAYOUT_FOR_LARGE_SCREEN_LANDSCAPE = 4;
	
    private static final short NUMBER_OF_LINEAR_LAYOUT_FOR_EXTRA_LARGE_SCREEN_PORTRAIT = 4;
	
	private static final short NUMBER_OF_LINEAR_LAYOUT_FOR_EXTRA_LARGE_SCREEN_LANDSCAPE = 5;
	
    byte numberOfLinearLayouts = -1;
	
	private List<FrameLayout> childFrames = new ArrayList<FrameLayout>();
	
	private List<LinearLayout> linearLayouts = new ArrayList<LinearLayout>();
	
	private LinearLayout activityLayout = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		activityLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.activity_notes_layout_manager, null);
		
		setContentView(activityLayout);

		numberOfLinearLayouts = getNumberOfLinearLayouts();
		
		fillLinearLayoutList();
	}

	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
				
//		outState.putStringArrayList(FRAGMENT_TAGS, (ArrayList<String>)fragmentTags);
		
		outState.putByte(NUMBER_OF_LINEAR_LAYOUTS, numberOfLinearLayouts);
		
	    super.onSaveInstanceState(outState);
	    
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) 
	{
//		super.onRestoreInstanceState(savedInstanceState);
//		
		numberOfLinearLayouts = (byte)savedInstanceState.getByte(NUMBER_OF_LINEAR_LAYOUTS);
//
//		fragmentTags.addAll(savedInstanceState.getStringArrayList(FRAGMENT_TAGS));
//
//		if(fragmentTags.size() > 0)
//		{
			createActivityLayoutInCaseOfConfigurationChange();
//		}
	}


	private void createActivityLayoutInCaseOfConfigurationChange() {
		updateNumberOfLinearLayoutsOnScreenChange(getResources().getConfiguration());
		
		fillLinearLayoutList();
		
//		ListIterator<String> iterator = fragmentTags.listIterator();
//		
//		try 
//		{
//			ProfessionalPANotesReader parser = ProfessionalPAParameters.getProfessionalPANotesReader();
//			
//			List<ParsedNote> parsedNotes = parser.readNotes();
//			
//			for(int i = 0, size = parsedNotes == null ? 0 : parsedNotes.size(); i < size; i++)
//			{
//				ParsedNote note = parsedNotes.get(i);
//				
//				Intent intent = new Intent();
//				
//				List<NotesListItem> listItems = note.getNoteItems();
//				
//				if(listItems != null && listItems.size() > 0)
//				{
//					NotesListItem [] items = new NotesListItem[1];
//					
//					items = listItems.toArray(items);
//					
//					intent.putExtra("LIST_ITEMS", items);
//					
//					intent.putExtra(ProfessionalPAConstants.IS_PARAGRAPH_NOTE, note.isParagraphNote());
//					
//					onActivityResult(0, 0, intent);
//				}
//			}
//			
//		} catch (ProfessionalPABaseException e) 
//		{
////			e.printStackTrace();
//		}
		
//		while(iterator.hasNext())
//		{
//			String tag = iterator.next();
//			
//			Fragment fragment = getFragmentManager().findFragmentByTag(tag);
//			
//			if(fragment != null)
//			{
//				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//				
//				fragmentTransaction.remove(fragment);
//				
//				fragmentTransaction.commit();
//				
//				getFragmentManager().executePendingTransactions();
//				
//				fragmentTransaction = getFragmentManager().beginTransaction();
//				
//				fragmentTransaction.commit();
//				
//				createActivityLayout(fragment);
//				
//				iterator.add(fragment.getTag());
//			}
//			else
//			{
//				iterator.remove();
//			}
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notes_layout_manager, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		int id = item.getItemId();
		
		if (id == R.id.action_settings) {
			return true;
		}
		
		else if(id == R.id.action_create_list_view)
		{
			Intent intent = new Intent(getApplicationContext(), ListItemCreatorActivity.class);
			
			startActivityForResult(intent, LIST_ACTIVITY_RESULT_CREATED);
		}
		else if(id == R.id.action_create_paragraph_view)
		{
            Intent intent = new Intent(getApplicationContext(), ParagraphNoteCreatorActivity.class);
			
			startActivityForResult(intent, LIST_ACTIVITY_RESULT_CREATED);
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void updateNumberOfLinearLayoutsOnScreenChange(Configuration newConfig)
	{
	    // Checks the orientation of the screen
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) 
	    {
	    	 numberOfLinearLayouts = (byte)(numberOfLinearLayouts+1);
	    } 
	    else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
	    {
	    	numberOfLinearLayouts = (byte)(numberOfLinearLayouts-1);
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
	    if (data == null) {return;}
	    
	    Bundle bundle = new Bundle();
	    
	    ProfessionalPANote note = data.getParcelableExtra(ProfessionalPAConstants.NOTE_DATA);
	    
	    System.out.println("onActivityResult -> note="+note.getNoteItems().size());
	    
	    Fragment fragment = note.isParagraphNote() ? new ProfessionalPAParagraphFragment() : new ProfessionalPAListFragment();
	    	    
	    if(note != null)
	    {
	    	bundle.putParcelable(ProfessionalPAConstants.NOTE_DATA, note);
		    
		    fragment.setArguments(bundle);
		    
		    createActivityLayout(fragment);
		    
//			fragmentTags.add(fragment.getTag());
	    }
	}


	private void createActivityLayout(Fragment fragment) {
		
		FrameLayout frameLayout =  (FrameLayout)getLayoutInflater().inflate(R.layout.professional_pa_frame_layout, null, false);
		
		int id = ProfessionalPAParameters.getId();
				
		frameLayout.setId(id);

		String tag = fragment.getTag() != null ? fragment.getTag() : "Tag-"+ProfessionalPAParameters.getId();
				
		getFragmentManager().beginTransaction().add(id, fragment, tag).commit();
		
		addFrameLayoutToActivtyView(frameLayout);
		
		updateActivityView();
		
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		
		System.out.println("on destroy for activity called");
	}


	private void addFrameLayoutToActivtyView(FrameLayout frameLayout) 
	{
		childFrames.add(0, frameLayout);
	}

	
	public byte getNumberOfLinearLayouts() {
		
		byte numberOfLinearLayouts = -1;
		
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {

			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				numberOfLinearLayouts = NUMBER_OF_LINEAR_LAYOUT_FOR_EXTRA_LARGE_SCREEN_PORTRAIT;
			} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				numberOfLinearLayouts = NUMBER_OF_LINEAR_LAYOUT_FOR_EXTRA_LARGE_SCREEN_LANDSCAPE;
			}

		}
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {

			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				numberOfLinearLayouts = NUMBER_OF_LINEAR_LAYOUT_FOR_LARGE_SCREEN_PORTRAIT;
			} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				numberOfLinearLayouts = NUMBER_OF_LINEAR_LAYOUT_FOR_LARGE_SCREEN_LANDSCAPE;
			}
		} else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				numberOfLinearLayouts = NUMBER_OF_LINEAR_LAYOUT_FOR_NORMAL_SCREEN_PORTRAIT;
			} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				numberOfLinearLayouts = NUMBER_OF_LINEAR_LAYOUT_FOR_NORMAL_SCREEN_LANDSCAPE;
			}
		} else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {

			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				numberOfLinearLayouts = NUMBER_OF_LINEAR_LAYOUT_FOR_SMALL_SCREEN_PORTRAIT;
			} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				numberOfLinearLayouts = NUMBER_OF_LINEAR_LAYOUT_FOR_SMALL_SCREEN_LANDSCAPE;
			}
		} else {
		}

		return numberOfLinearLayouts;
	}
	
	private void updateActivityView() 
	{		
		for(int i = 0; i < numberOfLinearLayouts; i++)
		{
			LinearLayout linearLayout = linearLayouts.get(i);
			
			int index = 0;
			
			for(int j = i; j < childFrames.size(); j = j+numberOfLinearLayouts)
			{
				FrameLayout frameLayout = childFrames.get(j);
				
				LinearLayout parentView = (LinearLayout)frameLayout.getParent();
				
				if(parentView != null)
				{
					parentView.removeView(frameLayout);
				}
								
//				 FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(linearLayout.getWidth(), 60);
//
//				 frameLayout.setLayoutParams(frameLayoutParams);

				linearLayout.addView(frameLayout, index);
				
				index++;
			}
		}
	}


	private void fillLinearLayoutList() {
		LinearLayout layout = null;

		switch(numberOfLinearLayouts)
		{
		    case 5:
		    	layout = (LinearLayout)findViewById(R.id.linearLayout5);
		    	linearLayouts.add(layout);
		    case 4:
		    	layout = (LinearLayout)findViewById(R.id.linearLayout4);
		    	linearLayouts.add(layout);
		    case 3:
		    	layout = (LinearLayout)findViewById(R.id.linearLayout3);
		    	linearLayouts.add(layout);
		    case 2:
		    	layout = (LinearLayout)findViewById(R.id.linearLayout2);
		    	linearLayouts.add(layout);
		    case 1:
		    	layout = (LinearLayout)findViewById(R.id.linearLayout1);
		    	linearLayouts.add(layout);
		    	break;
		}
	}
	
	@Override
	public void onBackPressed() 
	{
	    super.onBackPressed();
	}
	
	@Override
	protected void onResume() 
	{
		try 
		{
			ProfessionalPANotesReader parser = ProfessionalPAParameters.getProfessionalPANotesReader();
			
			List<ProfessionalPANote> parsedNotes = parser.readNotes();
			
			System.out.println("onResume -> parsedNotes="+parsedNotes);
			
			for(int i = 0, size = parsedNotes == null ? 0 : parsedNotes.size(); i < size; i++)
			{
				ProfessionalPANote note = parsedNotes.get(i);
				
				if(note != null)
				{
					Intent intent = new Intent();
					
					intent.putExtra(ProfessionalPAConstants.NOTE_DATA, note);
					
					onActivityResult(0, 0, intent);
				}
			}
			
		} catch (ProfessionalPABaseException e) 
		{
//			e.printStackTrace();
		}
		
		super.onResume();
	}


	@Override
	protected void onPause() 
	{
		super.onPause();
	}
}
