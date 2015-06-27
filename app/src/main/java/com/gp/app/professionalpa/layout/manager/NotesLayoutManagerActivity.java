package com.gp.app.professionalpa.layout.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseIntArray;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.calendar.events.database.CalendarDBManager;
import com.gp.app.professionalpa.calendar.ui.ProfessionalPACalendarView;
import com.gp.app.professionalpa.colorpicker.ColourPickerChangeListener;
import com.gp.app.professionalpa.data.Event;
import com.gp.app.professionalpa.data.Note;
import com.gp.app.professionalpa.data.NoteItem;
import com.gp.app.professionalpa.data.TextNote;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.export.ProfessionalPANotesExporter;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.notes.database.NotesDBManager;
import com.gp.app.professionalpa.notes.fragments.FragmentCreationManager;
import com.gp.app.professionalpa.notes.fragments.NotesManager;
import com.gp.app.professionalpa.notes.fragments.TextNoteFragment;
import com.gp.app.professionalpa.notes.images.ImageLocationPathManager;
import com.gp.app.professionalpa.notes.operations.NotesOperationManager;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;
import com.gp.app.professionalpa.views.listeners.NotesActionModeCallback;

//TODO create notes for calendar events also
public class NotesLayoutManagerActivity extends Activity implements ColourPickerChangeListener, OnQueryTextListener 
{
	private static final String NUMBER_OF_LINEAR_LAYOUTS = "NUMBER_OF_LINEAR_LAYOUTS";

	private static final int LIST_ACTIVITY_RESULT_CREATED = 1;
	
	private static final int PARAGRAPH_ACTIVITY_RESULT_CREATED = 2;

	private static final byte NUMBER_OF_LINEAR_LAYOUT_FOR_SMALL_SCREEN_PORTRAIT = 1;

	private static final byte NUMBER_OF_LINEAR_LAYOUT_FOR_SMALL_SCREEN_LANDSCAPE = 2;

	private static final short NUMBER_OF_LINEAR_LAYOUT_FOR_NORMAL_SCREEN_PORTRAIT = 2;

	private static final short NUMBER_OF_LINEAR_LAYOUT_FOR_NORMAL_SCREEN_LANDSCAPE = 3;

	private static final short NUMBER_OF_LINEAR_LAYOUT_FOR_LARGE_SCREEN_PORTRAIT = 3;

	private static final short NUMBER_OF_LINEAR_LAYOUT_FOR_LARGE_SCREEN_LANDSCAPE = 4;

	private static final short NUMBER_OF_LINEAR_LAYOUT_FOR_EXTRA_LARGE_SCREEN_PORTRAIT = 4;

	private static final short NUMBER_OF_LINEAR_LAYOUT_FOR_EXTRA_LARGE_SCREEN_LANDSCAPE = 5;

	private static final String APPLIED_FILTER = "currentAppliedFilter";

	private SparseIntArray linearLayoutOccupancy = new SparseIntArray();
	
	private Map<Integer, FrameLayout> childFrames = new LinkedHashMap<Integer, FrameLayout>();

	private List<LinearLayout> linearLayouts = new ArrayList<LinearLayout>();

	private ImageLocationPathManager imageCaptureManager = null;
	
	private NotesDBManager.NotesSearchManager notesSearchManager = null;
	
	private RelativeLayout scrollView = null;
	
	private FrameLayout noteCreatorFrameLayout = null;
	
	private FrameLayout listNoteFrameLayout = null;
	
	private FrameLayout paragraphNoteFrameLayout = null;
	
	private FrameLayout imageNoteFrameLayout = null;
	
	private FrameLayout calendarFrameLayout = null;
	
	private FrameLayout eventFrameLayout = null;

	private boolean areNoteButtonCreated = false;

	private ActionMode currenActionMode = null;
	
	private byte EVENT_FILTER = 2;
	
	private byte NOTE_FILTER = 1;
	
	private byte DEFAULT_FILTER = 0;
	
	private byte currentAppliedFilter = 0;
	
	public NotesLayoutManagerActivity() 
	{
		super();
		
		imageCaptureManager = ImageLocationPathManager.getInstance();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		scrollView = (RelativeLayout) getLayoutInflater().inflate(
				R.layout.activity_notes_layout_manager, null);

		setContentView(scrollView);

		int numberOfLinearLayouts = getNumberOfLinearLayouts();
		
		reInitializeLinearLayoutOccupancy(numberOfLinearLayouts);

		fillLinearLayoutList();

		NotesManager.getInstance().deleteAllNotes();

	    createNotes();

		ActionBar actionBar = getActionBar();

		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#7F7CD9")));

		actionBar.setDisplayHomeAsUpEnabled(true);
		
		ProfessionalPAParameters.setNotesActivity(this);
		
		handleIntent(getIntent());
	}

	private void reInitializeLinearLayoutOccupancy(int numberOfLinearLayouts) 
	{
		for(int i = 0; i < numberOfLinearLayouts; i++)
		{
			linearLayoutOccupancy.append(i, 0);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		outState.putByte(NUMBER_OF_LINEAR_LAYOUTS, (byte)linearLayoutOccupancy.size());

		outState.putByte(APPLIED_FILTER, currentAppliedFilter);
		
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) 
	{
		linearLayoutOccupancy.clear();
		
		int numberOfLinearLayouts =  savedInstanceState
				.getByte(NUMBER_OF_LINEAR_LAYOUTS);
		
		updateNumberOfLinearLayoutsOnScreenChange(getResources()
				.getConfiguration(), numberOfLinearLayouts);

		currentAppliedFilter =  savedInstanceState
				.getByte(APPLIED_FILTER);
		
		fillLinearLayoutList();
		
	    applyFilter(currentAppliedFilter);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.notes_layout_manager_menu, menu);

		// Associate searchable configuration with the SearchView
	    SearchManager searchManager =
	           (SearchManager) getSystemService(Context.SEARCH_SERVICE);
	    
	    SearchView searchView =
	            (SearchView) menu.findItem(R.id.actionSearch).getActionView();

	    searchView.setSearchableInfo(
	            searchManager.getSearchableInfo(getComponentName()));
	    
	    searchView.setOnQueryTextListener(this);
	    
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		switch(item.getItemId())
		{
		    case R.id.action_settings :
			    return true;
			    
		    case R.id.defaultFilter :
		    	applyFilter(DEFAULT_FILTER);
		    	return true;
		    case R.id.note :
		    	applyFilter(NOTE_FILTER);
		    	return true;
		    case R.id.event :
		    	applyFilter(EVENT_FILTER);
		    	return true;
		    case R.id.export_notes :
			
			    try 
			    {
				    ProfessionalPANotesExporter.export();
			    } 
			    catch (ProfessionalPABaseException e)
			    {
				// TODO Auto-generated catch block
				    e.printStackTrace();
			    }
				
			    return true;
	
		    case R.id.import_notes : 
				
		    	List<TextNote> notes;

////				try
////				{
////					//TODO improve import functionality hampered
//////					notes = NotesDBManager.getInstance().readNotes(true);
	//////
//////					ProfessionalPAParameters.getProfessionalPANotesWriter()
//////							.writeNotes(notes);
////				} 
////				catch (ProfessionalPABaseException e) 
////				{
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
	//
		    	return true;
			case R.id.actionSearch :
				notesSearchManager =  NotesDBManager.getInstance().new  NotesSearchManager();
				return true;

			default:
				return super.onOptionsItemSelected(item);
	    }
	}

	private void applyFilter(byte filterType) 
	{
		currentAppliedFilter = filterType;
		
		for(Entry<Integer, FrameLayout> entry : childFrames.entrySet())
		{
			int noteId = entry.getKey();
			
			FrameLayout frameLayout = entry.getValue();
			
			Note note = NotesManager.getInstance().getNote(noteId);
			
			if(note != null)
			{
				if(filterType == 0)
				{
					frameLayout.setVisibility(View.VISIBLE);
				}
				
				if(filterType == 1 )
			    {
					if(note.getType() == Note.LIST_NOTE || note.getType() == Note.PARAGRAPH_NOTE
				        || note.getType() == Note.IMAGE_NOTE)
					{
					    frameLayout.setVisibility(View.VISIBLE);
					}
				    else
				    {
						frameLayout.setVisibility(View.GONE);
				    }
			    }
				
				if(filterType == 2) 
				{
					if(note.getType() == Note.EVENT_NOTE)
					{
						frameLayout.setVisibility(View.VISIBLE);
					}
					else
					{
						frameLayout.setVisibility(View.GONE);
					}
				}
			}
		}
	}

	private void createCalendarView()
	{
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(new ProfessionalPACalendarView(this));
		dialog.setCancelable(true);
		dialog.show();
	}

	private void createImageNote() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		startActivityForResult(cameraIntent,
				ProfessionalPAConstants.TAKE_PHOTO_CODE);
	}

	private void createParagraphNote() {
		Intent intent = new Intent(getApplicationContext(),
				ParagraphNoteCreatorActivity.class);

		startActivityForResult(intent, LIST_ACTIVITY_RESULT_CREATED);
	}

	private void createListNote() {
		Intent intent = new Intent(getApplicationContext(),
				ListItemCreatorActivity.class);

		startActivityForResult(intent, LIST_ACTIVITY_RESULT_CREATED);
	}

	private void updateNumberOfLinearLayoutsOnScreenChange(Configuration newConfig, int numberOfLinearLayouts) 
	{
		int noOfLinearLayout = numberOfLinearLayouts;
		
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) 
		{
			noOfLinearLayout = numberOfLinearLayouts + 1;
		}
		else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			noOfLinearLayout = numberOfLinearLayouts - 1;
		}
		
		reInitializeLinearLayoutOccupancy(noOfLinearLayout);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		TextNote note = null;

		if (requestCode == ProfessionalPAConstants.TAKE_PHOTO_CODE && resultCode == RESULT_OK) 
		{
			Bitmap photo = (Bitmap) data.getExtras().get("data");

			ImageLocationPathManager.getInstance().createAndSaveImage(photo);

			note = createProfessionalPANoteFromImage(imageCaptureManager
					.getMostRecentImageFilePath());

			NotesDBManager.getInstance().saveNotes(Arrays.asList(note));
		} 
		else
		{
			if (data != null) 
			{
				note = data.getParcelableExtra(ProfessionalPAConstants.NOTE_DATA);
			}
		}

		if (note != null)
		{
			createFragmentForNote(note);
		}
	}

	private TextNote createProfessionalPANoteFromImage(String imagePath)
	{
		ArrayList<NoteItem> items = new ArrayList<NoteItem>();

		items.add(new NoteItem(null, ImageLocationPathManager.getInstance()
				.getImageName(imagePath)));

		TextNote note = new TextNote(NotesManager.getInstance().getNextFreeNoteId(),
				Note.IMAGE_NOTE, items);

		long creationTime = Long.valueOf(imageCaptureManager
				.getImageName(imagePath));

		note.setCreationTime(creationTime);

		note.setLastEditedTime(creationTime);

		note.setTypeOfNote(Note.IMAGE_NOTE);

		note.setLastEditedTime(System.currentTimeMillis());
		return note;
	}

	public void createFragmentForNote(Note note) 
	{
		if (note != null)
		{
			Fragment fragment = FragmentCreationManager.createFragment(note);

			NotesManager.getInstance().addNote(note.getId(), note);

			if (fragment != null) 
			{
				boolean isTextNote = note.getType() != Note.EVENT_NOTE;
				
				createActivityLayout(fragment, note.getId(), isTextNote);
			}
		}
	}


	private void createActivityLayout(Fragment fragment, int noteId, boolean isTextNote) 
	{
        final FrameLayout frameLayout =  (FrameLayout)getLayoutInflater().inflate(R.layout.professional_pa_frame_layout, null, false);
		
		int fragmentLength = isTextNote ? ((TextNoteFragment)fragment).getFragmentLength() : 3;

		frameLayout.setClickable(true);
		
		int id = ProfessionalPAParameters.getId();
			
		frameLayout.setId(id);

		String tag = fragment.getTag() != null ? fragment.getTag() : "Tag-"
				.concat(Integer.toString(ProfessionalPAParameters.getId()));

		getFragmentManager().beginTransaction().add(id, fragment, tag).commit();

		childFrames.put(noteId, frameLayout);
		
		updateActivityView(frameLayout, fragmentLength);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		NotesManager.getInstance().deleteAllNotes();
		
		NotesOperationManager.getInstance().clearSelectedNotes();
	}

	public byte getNumberOfLinearLayouts()
    {
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

	private void updateActivityView(FrameLayout frameLayout, int fragmentLength)
	{
		int minimumOccupiedIndex = getMinimumOccupiedLayoutIndex();
		
		int occupancy = linearLayoutOccupancy.get(minimumOccupiedIndex);
		
		occupancy = occupancy + fragmentLength;
		
		linearLayoutOccupancy.put(minimumOccupiedIndex, occupancy);
		
		LinearLayout linearLayout = linearLayouts.get(minimumOccupiedIndex);

		LinearLayout parentView = (LinearLayout) frameLayout.getParent();

		if (parentView != null) 
		{
			parentView.removeView(frameLayout);
		}

		linearLayout.addView(frameLayout, 0);
	}

	private void fillLinearLayoutList() 
	{
		LinearLayout layout = null;

		LinearLayoutOnClickListener clickListener = new LinearLayoutOnClickListener();

		linearLayouts.clear();
		
		switch (linearLayoutOccupancy.size()) 
		{
		    case 5:
			    layout = (LinearLayout) findViewById(R.id.linearLayout5);
			    layout.setOnClickListener(clickListener);
			    linearLayouts.add(layout);
		    case 4:
			    layout = (LinearLayout) findViewById(R.id.linearLayout4);
			    layout.setOnClickListener(clickListener);
			    linearLayouts.add(layout);
		    case 3:
			    layout = (LinearLayout) findViewById(R.id.linearLayout3);
			    layout.setOnClickListener(clickListener);
			    linearLayouts.add(layout);
		    case 2:
			    layout = (LinearLayout) findViewById(R.id.linearLayout2);
			    layout.setOnClickListener(clickListener);
			    linearLayouts.add(layout);
		    case 1:
			    layout = (LinearLayout) findViewById(R.id.linearLayout1);
			    layout.setOnClickListener(clickListener);
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
		super.onResume();
		
		noteCreatorFrameLayout = (FrameLayout)findViewById(R.id.notesLayoutManagerFrameLayout);
		
		noteCreatorFrameLayout.setBackgroundResource(R.drawable.day_selected);
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
		
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, scrollView.getId());
		
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, scrollView.getId());
		
		params.setMargins(10, 0, 40, 80);
		
		noteCreatorFrameLayout.setLayoutParams(params);
		
		ImageView button = new ImageView(this);
		
		button.setImageDrawable(getResources().getDrawable(R.drawable.color_picker_icon));
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				createNoteTypeButtons();
			}
		});
		
		noteCreatorFrameLayout.addView(button);
	}

	private void createNoteTypeButtons()
	{
		createListNoteFrameLayout();
		
		createParagraphNoteFrameLayout();
		
		createImageNoteFragment();
		
		createCalendarFrameLayout();
		
		createEventFrameLayout();
		
		areNoteButtonCreated = !areNoteButtonCreated;
	}
	
	private void createListNoteFrameLayout() 
	{
		if(listNoteFrameLayout == null)
		{
			listNoteFrameLayout = (FrameLayout)findViewById(R.id.listNoteFrameLayout);
			
			listNoteFrameLayout.setBackgroundResource(R.drawable.day_selected);
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
			
			params.addRule(RelativeLayout.ABOVE, R.id.notesLayoutManagerFrameLayout);
			
			params.addRule(RelativeLayout.ALIGN_LEFT, R.id.notesLayoutManagerFrameLayout);
			
			params.setMargins(10, 0, 40, 30);
			
			listNoteFrameLayout.setLayoutParams(params);
			
			ImageView button = new ImageView(this);
			
			button.setImageDrawable(getResources().getDrawable(R.drawable.professional_pa_list_view1));
			
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) 
				{
					createListNote();
				}
			});
			
			listNoteFrameLayout.addView(button);
		}
		else
		{
			setFrameLayoutVisibilityState(listNoteFrameLayout);
		}
	}
	
	private void createEventFrameLayout() 
	{
		if(eventFrameLayout == null)
		{
			eventFrameLayout = (FrameLayout)findViewById(R.id.eventFrameLayout);
			
			eventFrameLayout.setBackgroundResource(R.drawable.day_selected);
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
			
			params.addRule(RelativeLayout.ABOVE, R.id.calendarFrameLayout);
			
			params.addRule(RelativeLayout.ALIGN_LEFT, R.id.notesLayoutManagerFrameLayout);
			
			params.setMargins(10, 0, 40, 30);
			
			eventFrameLayout.setLayoutParams(params);
			
			ImageView button = new ImageView(this);
			
			button.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_event));
			
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) 
				{
					createListNote();
				}
			});
			
			eventFrameLayout.addView(button);
		}
		else
		{
			setFrameLayoutVisibilityState(eventFrameLayout);
		}
	}

	private void createParagraphNoteFrameLayout() 
	{
		if(paragraphNoteFrameLayout == null)
		{
			paragraphNoteFrameLayout = (FrameLayout)findViewById(R.id.paragraphNoteFrameLayout);
			
			paragraphNoteFrameLayout.setBackgroundResource(R.drawable.day_selected);
			
//	        paragraphNoteFrameLayout.setBackgroundColor(Color.TRANSPARENT);
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
			
			params.addRule(RelativeLayout.ABOVE, R.id.listNoteFrameLayout);
			
			params.addRule(RelativeLayout.ALIGN_LEFT, R.id.listNoteFrameLayout);
			
			params.setMargins(10, 0, 40, 30);
			
			paragraphNoteFrameLayout.setLayoutParams(params);
			
			ImageView button = new ImageView(this);
			
			button.setImageDrawable(getResources().getDrawable(R.drawable.professional_pa_paragraph_view1));
			
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) 
				{
					createParagraphNote();
				}
			});
			
			paragraphNoteFrameLayout.addView(button);
		}
		else
		{
			setFrameLayoutVisibilityState(paragraphNoteFrameLayout);
		}
	}
	
	private void createImageNoteFragment() 
	{
		if(imageNoteFrameLayout == null)
		{
			imageNoteFrameLayout = (FrameLayout)findViewById(R.id.imageNoteFrameLayout);
			
			imageNoteFrameLayout.setBackgroundResource(R.drawable.day_selected);
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
			
			params.addRule(RelativeLayout.ABOVE, R.id.paragraphNoteFrameLayout);
			
			params.addRule(RelativeLayout.ALIGN_LEFT, R.id.paragraphNoteFrameLayout);
			
			params.setMargins(10, 0, 40, 30);
			
			imageNoteFrameLayout.setLayoutParams(params);
			
			ImageView button = new ImageView(this);
			
			button.setImageDrawable(getResources().getDrawable(R.drawable.professional_pa_camera));
			
			button.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					createImageNote();
				}
			});
			
			imageNoteFrameLayout.addView(button);
		}
		else
		{
			setFrameLayoutVisibilityState(imageNoteFrameLayout);
		}
	}
	
	private void createCalendarFrameLayout() 
	{
		if(calendarFrameLayout == null)
		{
			calendarFrameLayout = (FrameLayout)findViewById(R.id.calendarFrameLayout);
			
			calendarFrameLayout.setBackgroundResource(R.drawable.day_selected);
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
			
			params.addRule(RelativeLayout.ABOVE, R.id.imageNoteFrameLayout);
			
			params.addRule(RelativeLayout.ALIGN_LEFT, R.id.imageNoteFrameLayout);
			
			params.setMargins(10, 0, 40, 30);
			
			calendarFrameLayout.setLayoutParams(params);
			
			ImageView button = new ImageView(this);
			
			button.setImageDrawable(getResources().getDrawable(R.drawable.calendar_image));
			
			button.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					createCalendarView();
				}
			});
			
			calendarFrameLayout.addView(button);
		}
		else
		{
			setFrameLayoutVisibilityState(calendarFrameLayout);
		}
	}
	
	private void setFrameLayoutVisibilityState(FrameLayout frameLayout) 
	{
		if(areNoteButtonCreated)
		{
			frameLayout.setVisibility(View.GONE);
		}
		else
		{
			frameLayout.setVisibility(View.VISIBLE);
		}
	}

	private void createNotes()
	{
		List<TextNote> parsedNotes = NotesDBManager.getInstance().readNotes();

		for (int i = 0, size = parsedNotes == null ? 0 : parsedNotes.size(); i < size; i++)
		{
			TextNote note = parsedNotes.get(i);

			if (note != null)
			{
				createFragmentForNote(note);
			}
		}
		
		List<Event> events = CalendarDBManager.getInstance().readAllEvents();
		
		for(int i = 0, size = events.size(); i < size; i++)
		{
			Event note = events.get(i);

			if (note != null)
			{
				createFragmentForNote(note);
			}
		}
	}

	@Override
	protected void onPause() 
	{
		super.onPause();
	}

	public void deleteNotes(List<Integer> noteIds)
	{
		for(int k = 0; k < noteIds.size(); k++)
		{
			int noteId = noteIds.get(k);
			
			FrameLayout layout = childFrames.get(noteId);

			if (layout != null) 
			{
				for (int i = 0; i < linearLayouts.size(); i++)
				{
					LinearLayout linearLayout = linearLayouts.get(i);

					linearLayout.removeView(layout);
				}
			}
		}
	}

	class LinearLayoutOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) 
		{
			NotesOperationManager.getInstance().copyNote();
		}

	}

	public void openNoteInEditMode(int noteId) 
	{
		Note note = NotesManager.getInstance().getNote(noteId);
		
		if(note.getType() != Note.EVENT_NOTE)
		{
			TextNote textNote = (TextNote)note;
			
			if(textNote.isListNote())
			{
				Intent intent = new Intent(getApplicationContext(),
						ListItemCreatorActivity.class);

				intent.putExtra(ProfessionalPAConstants.NOTE_ID, noteId);
				
				startActivityForResult(intent, LIST_ACTIVITY_RESULT_CREATED);
			}
			else
			{
				Intent intent = new Intent(getApplicationContext(),
						ParagraphNoteCreatorActivity.class);

				intent.putExtra(ProfessionalPAConstants.NOTE_ID, noteId);
				
				startActivityForResult(intent, PARAGRAPH_ACTIVITY_RESULT_CREATED);
			}
		}
	}

	@Override
	public void changeColour(int noteColor) 
	{
		List<Integer> selectedNoteIds = NotesOperationManager.getInstance().getSelectedNoteIds();
		
		for(int i = 0; i < selectedNoteIds.size(); i++)
		{
			int selectedNoteId = selectedNoteIds.get(i);
			
			setNoteListViewColour(noteColor, selectedNoteId);
		}
		
		NotesOperationManager.getInstance().clearSelectedNotes();
	}

	private void setNoteListViewColour(int noteColor, final int selectedNoteId)
	{
		FrameLayout frameLayout = childFrames.get(selectedNoteId);

		if(frameLayout != null)
		{
			View view = frameLayout.getChildAt(0);
			
			if(view != null)
			{
				view.setBackgroundColor(noteColor);
				
				Note note = NotesManager.getInstance().getNote(selectedNoteId);
				
				if(note != null && note.getType() != Note.EVENT_NOTE)
				{
					((TextNote)note).setNoteColor(noteColor);

					NotesDBManager.getInstance().setNoteColorAttribute(selectedNoteId, noteColor);
				}
			}
		}
	}
	
	public void resetNoteListViewColour()
	{
		List<Integer> selectedNoteIds = NotesOperationManager.getInstance().getSelectedNoteIds();
		
		for(int i = 0, size = selectedNoteIds.size(); i < size; i++)
		{
			int selectedNoteId = selectedNoteIds.get(i);
			
			resetNoteColor(selectedNoteId);
		}
	}

	private void resetNoteColor(int selectedNoteId) {
		FrameLayout frameLayout = childFrames.get(selectedNoteId);

		if(frameLayout != null)
		{
			View view = frameLayout.getChildAt(0);
			
			if(view != null)
			{
				Note note = NotesManager.getInstance().getNote(selectedNoteId);

				if(note != null && note.getType() != Note.EVENT_NOTE)
				{
					view.setBackgroundColor(((TextNote)note).getNoteColor());
				}
			}
		}
	}
	
	private int getMinimumOccupiedLayoutIndex() 
	{
		int minimumOccupiedLayoutIndex = 0;
		
		int minimumOccupancy = linearLayoutOccupancy.get(0);

		for(int linearLayoutIndex = 1; linearLayoutIndex < linearLayoutOccupancy.size(); linearLayoutIndex++)
		{
			int layoutOccupancy = linearLayoutOccupancy.get(linearLayoutIndex);
			
			if (layoutOccupancy < minimumOccupancy)
			{
				minimumOccupancy = layoutOccupancy;
				
				minimumOccupiedLayoutIndex = linearLayoutIndex;
			}
		}
		
		System.out.println("getMinimumOccupiedLayoutIndex -> minimumOccupiedLayoutIndex="+minimumOccupiedLayoutIndex
				+"minimumOccupancy="+minimumOccupancy);
		
		return minimumOccupiedLayoutIndex;
	}
	
	public void filterNotes(Set<Integer> noteIds) 
	{
		Set<Integer> childFrameIds = childFrames.keySet();
		
		if(childFrameIds != null)
		{
			for(int id : childFrameIds)
			{
				boolean isGone = false;
				
				if(!noteIds.contains(id))
				{
					isGone = true;
				}
				
				FrameLayout frameLayout = childFrames.get(id);
				
				if(frameLayout != null)
				{
					if(isGone)
					{
						frameLayout.setVisibility(View.GONE);
					}
					else
					{
						frameLayout.setVisibility(View.VISIBLE);
					}
				}
				
			}
		}
	}
	
	@Override
    protected void onNewIntent(Intent intent) 
	{
    	System.out.println("onNewIntent ->");

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) 
    {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) 
        {
        	System.out.println("handleIntent -> intent.getAction()="+intent.getAction());
        	
        	getActionBar().setDisplayShowHomeEnabled(false);
        	
            getActionBar().setDisplayShowTitleEnabled(false);
            
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }
    
    @Override
	public boolean onQueryTextChange(String query) 
	{
		Set<Integer> noteIds = notesSearchManager.getMatchingNoteIds(query);
		
		filterNotes(noteIds);
		
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query)
	{
		return false;
	}

	public void setNoteSelected(int noteId) 
	{
		FrameLayout frameLayout = childFrames.get(noteId);
		
		if(frameLayout != null)
		{
			currenActionMode = startActionMode(new NotesActionModeCallback());

			View view = frameLayout.getChildAt(0);

			if(view != null)
			{
				view.setBackgroundResource(R.drawable.blue);
			}
		}
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event)
	{
	    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) 
	    {
	        if(NotesOperationManager.getInstance().isNoteSelected())
	        {
	        	resetNoteListViewColour();
		    		
		    	NotesOperationManager.getInstance().clearSelectedNotes();
	        }
	    }
	    
	    return super.dispatchKeyEvent(event);
	}

	public void deSelectNote(int noteId)
	{
		resetNoteColor(noteId);
		
		if(NotesOperationManager.getInstance().getSelectedNoteIds().size() == 0)
		{
			currenActionMode.finish();
		}
		else if(NotesOperationManager.getInstance().getSelectedNoteIds().size() == 1)
		{
			Menu menu = currenActionMode.getMenu();
			
			MenuItem item = menu.findItem(R.id.itemEdit);
			
			if(item != null)
			{
				item.setVisible(true);
			}
		}
	}
}
