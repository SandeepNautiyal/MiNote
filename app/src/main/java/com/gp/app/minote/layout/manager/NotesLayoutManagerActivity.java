package com.gp.app.minote.layout.manager;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.gp.app.minote.R;
import com.gp.app.minote.calendar.events.database.CalendarDBManager;
import com.gp.app.minote.calendar.interfaces.DBChangeListener;
import com.gp.app.minote.calendar.ui.EventCreationUI;
import com.gp.app.minote.calendar.ui.MiNoteCalendar;
import com.gp.app.minote.calendar.ui.MiNoteEventCalendar;
import com.gp.app.minote.colorpicker.ColourPickerChangeListener;
import com.gp.app.minote.compositecontrols.ListViewItemLayout;
import com.gp.app.minote.data.Event;
import com.gp.app.minote.data.Note;
import com.gp.app.minote.data.NoteItem;
import com.gp.app.minote.data.TextNote;
import com.gp.app.minote.util.MiNoteConstants;
import com.gp.app.minote.listeners.NotesActionModeCallback;
import com.gp.app.minote.notes.database.NotesDBManager;
import com.gp.app.minote.notes.fragments.FragmentCreationManager;
import com.gp.app.minote.notes.fragments.NotesManager;
import com.gp.app.minote.notes.fragments.TextNoteFragment;
import com.gp.app.minote.notes.images.ImageLocationPathManager;
import com.gp.app.minote.notes.operations.NotesOperationManager;
import com.gp.app.minote.start.StartMiNoteActivity;
import com.gp.app.minote.util.MiNoteParameters;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class NotesLayoutManagerActivity extends FragmentActivity implements ColourPickerChangeListener, OnQueryTextListener,
		ScrollViewScrollListener, DBChangeListener
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

	private static final String SELECTED_NOTE_IDS = "selectedNoteIds";

    private static final String IS_NOTE_TAB_SELECTED = "isNoteTabSelected";

    private int layoutIndexToBeOccupiedNext = 0;

    private int linearLayoutsCount = 0;

	private Map<Integer, FrameLayout> childFrames = new LinkedHashMap<Integer, FrameLayout>();

	private List<LinearLayout> linearLayouts = new ArrayList<LinearLayout>();

	private ImageLocationPathManager imageCaptureManager = null;
	
	private RelativeLayout parentRelativeLayout = null;
	
	private FrameLayout noteCreatorFrameLayout = null;
	
	private FrameLayout listNoteFrameLayout = null;
	
	private FrameLayout paragraphNoteFrameLayout = null;
	
	private FrameLayout imageNoteFrameLayout = null;
	
	private FrameLayout calendarFrameLayout = null;
	
	private FrameLayout eventFrameLayout = null;

	private boolean areNoteButtonCreated = false;

	private ActionMode currenActionMode = null;

    private Set<Integer> fiterNoteIds = null;

    private CustomizedScrollView scrollView = null;

    private byte currentAppliedFilter = 0;

    private boolean isNoteTabSelected = true;

    private Object lock;

	public NotesLayoutManagerActivity()
	{
		super();
		
		imageCaptureManager = ImageLocationPathManager.getInstance();

        MiNoteParameters.setNotesActivity(this);
    }

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

        lock = new Object();

		if(MiNoteParameters.getApplicationContext() == null)
		{
			MiNoteParameters.setApplicationContext(getApplicationContext());
		}

		parentRelativeLayout = (RelativeLayout) getLayoutInflater().inflate(
				R.layout.activity_notes_layout_manager, null);

		setContentView(parentRelativeLayout);

		linearLayoutsCount = getNumberOfLinearLayouts();

        fillLinearLayoutList();

        NotesManager.getInstance().deleteAllNotes();

//	    createNotes();

		ActionBar actionBar = getActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionBar.setDisplayShowTitleEnabled(false);


        ActionBar.Tab tab = actionBar.newTab()
                .setText(MiNoteConstants.NOTES_TAB)
                .setTabListener(new TabListener<TabFragment>(
                        this, MiNoteConstants.NOTES_TAB, TabFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(MiNoteConstants.EVENTS_TAB)
                .setTabListener(new TabListener<TabFragment>(
                        this, MiNoteConstants.EVENTS_TAB, TabFragment.class));
        actionBar.addTab(tab);

        actionBar.setStackedBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.minoteTabColor)));

        forceTabs();

//		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(120, 100, 255)));
//
//		actionBar.setDisplayHomeAsUpEnabled(true);

		handleIntent(getIntent());

        scrollView = (CustomizedScrollView)parentRelativeLayout.findViewById(R.id.notesLayoutManagerScrollView);

        scrollView.setScrollViewListener(this);

        CalendarDBManager.getInstance().addDataChangeListener(this);
    }

	public void forceTabs()
	{
		try {
			final ActionBar actionBar = getActionBar();
			final Method setHasEmbeddedTabsMethod = actionBar.getClass()
					.getDeclaredMethod("setHasEmbeddedTabs", boolean.class);
			setHasEmbeddedTabsMethod.setAccessible(true);
			setHasEmbeddedTabsMethod.invoke(actionBar, false);
		}
		catch(final Exception e) {
			// Handle issues as needed: log, warn user, fallback etc
			// This error is safe to ignore, standard tabs will appear.
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		outState.putByte(NUMBER_OF_LINEAR_LAYOUTS, (byte) linearLayoutsCount);

		outState.putByte(APPLIED_FILTER, currentAppliedFilter);

        outState.putBoolean(IS_NOTE_TAB_SELECTED, isNoteTabSelected);

        outState.putIntegerArrayList(SELECTED_NOTE_IDS, new ArrayList<>(NotesOperationManager.getInstance().getSelectedNoteIds()));

		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) 
	{
        linearLayoutsCount =  savedInstanceState
				.getByte(NUMBER_OF_LINEAR_LAYOUTS);

        updateNumberOfLinearLayoutsOnScreenChange(getResources()
                .getConfiguration());

		currentAppliedFilter =  savedInstanceState
				.getByte(APPLIED_FILTER);

        isNoteTabSelected = savedInstanceState.getBoolean(IS_NOTE_TAB_SELECTED);

		fillLinearLayoutList();
		
		List<Integer> selectedIds =  savedInstanceState.getIntegerArrayList(SELECTED_NOTE_IDS);
		
		for(int i = 0; i < selectedIds.size(); i++)
		{
			int selectedNoteId = selectedIds.get(i);
			
			NotesOperationManager.getInstance().selectNote(selectedNoteId);
		}

        if(isNoteTabSelected)
        {
            getActionBar().setSelectedNavigationItem(0);
        }
        else
        {
            getActionBar().setSelectedNavigationItem(1);
        }
		
//	    applyFilter(currentAppliedFilter);
	}
	
	public void copyNote(View view)
	{
        if(areNoteButtonCreated)
        {
            createNoteTypeButtons();
        }

		NotesOperationManager.getInstance().copyNote();
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
			    
//		    case R.id.defaultFilter :
//		    	applyFilter(MiNoteConstants.DEFAULT_FILTER);
//		    	return true;
//		    case R.id.note :
//		    	applyFilter(MiNoteConstants.NOTE_FILTER);
//		    	return true;
//		    case R.id.event :
//		    	applyFilter(MiNoteConstants.EVENT_FILTER);
//		    	return true;
//		    case R.id.export_notes :
//
//			    try
//			    {
//				    MiNoteNotesExporter.export();
//			    }
//			    catch (MiNoteBaseException e)
//			    {
//				// TODO Auto-generated catch block
//				    e.printStackTrace();
//			    }
//
//			    return true;
//
		    case R.id.import_notes :

//		    	View view = getLayoutInflater().inflate(R.layout.event_gui, null);
//
//				Dialog dialog = new Dialog(this);
//
//				dialog.setContentView(view);
//
//				dialog.show();;

//                Dialog dialog = new Dialog(this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                MiNoteCalendar view = new MiNoteCalendar(this);
//                dialog.setContentView(view,  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                dialog.setCancelable(true);
//                dialog.show();

		    	return true;
			case R.id.actionSearch :
				return true;

			case R.id.editEmailId:
				Intent intent = new Intent(this, StartMiNoteActivity.class);
				intent.putExtra("EDIT_EMAIL_ID", true);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
	    }
	}

//	private void applyFilter(byte filterType)
//	{
//		currentAppliedFilter = filterType;
//
//		for(Entry<Integer, FrameLayout> entry : childFrames.entrySet())
//		{
//			int noteId = entry.getKey();
//
//			FrameLayout frameLayout = entry.getValue();
//
//			Note note = NotesManager.getInstance().getNote(noteId);
//
//			if(note != null)
//			{
//				if(filterType == 0)
//				{
//					frameLayout.setVisibility(View.VISIBLE);
//				}
//
//				if(filterType == 1 )
//			    {
//					if(note.getType() == Note.LIST_NOTE || note.getType() == Note.PARAGRAPH_NOTE
//				        || note.getType() == Note.IMAGE_NOTE)
//					{
//					    frameLayout.setVisibility(View.VISIBLE);
//					}
//				    else
//				    {
//						frameLayout.setVisibility(View.GONE);
//				    }
//			    }
//
//				if(filterType == 2)
//				{
//					if(note.getType() == Note.EVENT_NOTE)
//					{
//						frameLayout.setVisibility(View.VISIBLE);
//					}
//					else
//					{
//						frameLayout.setVisibility(View.GONE);
//					}
//				}
//			}
//		}
//	}

	private void createCalendarView()
	{
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		MiNoteCalendar view = new MiNoteEventCalendar(this);
//        view.setGravity(Gravity.CENTER);
		dialog.setContentView(view);
		dialog.setCancelable(true);
		dialog.show();
	}

	private void createImageNote()
    {
        String filename = ImageLocationPathManager.getInstance().getImagePath();

        Uri imageUri = Uri.fromFile(new File(filename));

// start default camera
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                imageUri);

		startActivityForResult(cameraIntent,
                MiNoteConstants.TAKE_PHOTO_CODE);
	}

	private void createParagraphNote() {
		Intent intent = new Intent(getApplicationContext(),
				ParagraphNoteCreatorActivity.class);

		startActivityForResult(intent, LIST_ACTIVITY_RESULT_CREATED);
	}

	private void createListNote() {
		Intent intent = new Intent(getApplicationContext(),
				ListCreatorActivity.class);

		startActivityForResult(intent, LIST_ACTIVITY_RESULT_CREATED);
	}

	private void updateNumberOfLinearLayoutsOnScreenChange(Configuration newConfig)
	{
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) 
		{
			linearLayoutsCount++;
		}
		else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			linearLayoutsCount--;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		TextNote note = null;

		if (requestCode == MiNoteConstants.TAKE_PHOTO_CODE && resultCode == RESULT_OK) 
		{
//			Bitmap photo = (Bitmap) data.getExtras().get("data");

            String filePath = ImageLocationPathManager.getInstance().getMostRecentImageFilePath();

//			ImageLocationPathManager.getInstance().createAndSaveImage(photo, imageUri.);

			//TODO can be removed if quality of image not good, but it increases loading time in activity
			ImageLocationPathManager.getInstance().compressImage(filePath);

			note = createProfessionalPANoteFromImage(filePath);

			NotesDBManager.getInstance().saveNotes(Arrays.asList(note));
		} 
		else
		{
			if (data != null) 
			{
				note = data.getParcelableExtra(MiNoteConstants.NOTE_DATA);
			}
		}

		if (note != null)
		{
			createFragmentForNote(note);

            changeTabs(true);
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
		
		int fragmentLength = isTextNote ? ((TextNoteFragment)fragment).getFragmentLength() : 8;

        frameLayout.setClickable(true);
		
		int id = MiNoteParameters.getId();
			
		frameLayout.setId(id);

		String tag = fragment.getTag() != null ? fragment.getTag() : "Tag-"
				.concat(Integer.toString(MiNoteParameters.getId()));

		getFragmentManager().beginTransaction().add(id, fragment, tag).commit();

        childFrames.put(noteId, frameLayout);

        updateActivityView(frameLayout);
	}

    private void changeTabs(boolean isTextNote)
    {
         if(isTextNote)
         {
             getActionBar().setSelectedNavigationItem(0);
         }
         else
         {
             getActionBar().setSelectedNavigationItem(1);
         }
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
		}

		return numberOfLinearLayouts;
	}

	private void updateActivityView(FrameLayout frameLayout)
	{
		int availableLayoutIndex = getAvailableLayoutIndex();

        System.out.println("updateActivityView -> availableLayoutIndex=" + availableLayoutIndex);

		if(availableLayoutIndex < linearLayouts.size())
		{
			LinearLayout linearLayout = linearLayouts.get(availableLayoutIndex);

			LinearLayout parentView = (LinearLayout) frameLayout.getParent();

			if (parentView != null)
			{
				parentView.removeView(frameLayout);
			}

			linearLayout.addView(frameLayout, 0);
		}
	}

	private void fillLinearLayoutList() 
	{
		LinearLayout layout = null;

//		LinearLayoutOnClickListener clickListener = new LinearLayoutOnClickListener();

		linearLayouts.clear();

		switch (linearLayoutsCount)
		{
		    case 5:
			    layout = (LinearLayout) findViewById(R.id.linearLayout5);
//                layout.setOnClickListener(clickListener);
		    case 4:
			    layout = (LinearLayout) findViewById(R.id.linearLayout4);
//                layout.setOnClickListener(clickListener);
                linearLayouts.add(layout);
		    case 3:
			    layout = (LinearLayout) findViewById(R.id.linearLayout3);
//                layout.setOnClickListener(clickListener);
                linearLayouts.add(layout);
		    case 2:
			    layout = (LinearLayout) findViewById(R.id.linearLayout2);
//                layout.setOnClickListener(clickListener);
                linearLayouts.add(layout);
		    case 1:
			    layout = (LinearLayout) findViewById(R.id.linearLayout1);
//                layout.setOnClickListener(clickListener);
//                ViewTreeObserver vto4 = layout.getViewTreeObserver();
//                vto4.addOnGlobalLayoutListener(new LayoutCreationListener());
                linearLayouts.add(layout);
			break;
		}
    }

	@Override
	public void onScrollChanged(CustomizedScrollView scrollView, int x, int y, int oldx, int oldy)
	{
        System.out.println("onScrollChanged -> x="+x+" y="+y+" oldX="+oldx+" oldY="+oldy);

        System.out.println("onScrollChanged -> scrollView.getScrollY()="+scrollView.getScrollY()+
                " scrollView.getHeight()="+scrollView.getHeight());

//        linearLayout.getMeasuredHeight() <= scrollView.getScrollY() +
//                scrollView.getHeight()
	}

    //TODO layout listener. Do not remove. Introduced for Dynamic loading of Notes on scrolling
//	class LayoutCreationListener implements ViewTreeObserver.OnGlobalLayoutListener
//    {
//        @Override
//        public void onGlobalLayout()
//        {
//
////            createNotesForRemainingNotes();
//        }
//    }

    //TODO layout listener. Do not remove. Introduced for Dynamic loading of Notes on scrolling
//    private void createNotesForRemainingNotes()
//    {
//		Iterator<Note> noteIterator = notes.iterator();
//
//        int size = notes.size();
//
//
//		System.out.println("onCreate -> scrollView.getScrollY()=" + scrollView.getScrollY() +
//				" scrollView.getHeight()=" + scrollView.getHeight());
//
//        while(noteIterator.hasNext())
//        {
//            Note note = noteIterator.next();
//
//            createFragmentForNote(note);
//
//            noteIterator.remove();
//        }

//        synchronized (lock)
//        {
//            System.out.println("createNotesForRemainingNotes -> notifying");
//
////            lock.notifyAll();
//        }
//    }

    @Override
    public void onBackPressed()
    {
		super.onBackPressed();
	}
	
	@Override
	protected void onResume() 
	{
		super.onResume();

        createNoteCreatorOptionsFrameLayout();
	}

	private void createNoteCreatorOptionsFrameLayout()
	{
		noteCreatorFrameLayout = (FrameLayout)findViewById(R.id.notesLayoutManagerFrameLayout);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);

		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, parentRelativeLayout.getId());

		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, parentRelativeLayout.getId());

		params.setMargins(10, 0, 40, 80);

		noteCreatorFrameLayout.setLayoutParams(params);

		ImageView button = new ImageView(this);

		button.setImageDrawable(getResources().getDrawable(R.drawable.minote_plus));

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
        if(isNoteTabSelected)
        {
            createListNoteFrameLayout();

            createParagraphNoteFrameLayout();

            createImageNoteFragment();
        }
		else
        {
            createCalendarFrameLayout();

            createEventFrameLayout();
        }
		
		areNoteButtonCreated = !areNoteButtonCreated;
	}
	
	private void createListNoteFrameLayout() 
	{
		if(listNoteFrameLayout == null)
		{
			listNoteFrameLayout = (FrameLayout)findViewById(R.id.listNoteFrameLayout);
			
//			listNoteFrameLayout.setBackgroundResource(R.drawable.day_selected);
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
			
			params.addRule(RelativeLayout.ABOVE, R.id.notesLayoutManagerFrameLayout);
			
			params.addRule(RelativeLayout.ALIGN_LEFT, R.id.notesLayoutManagerFrameLayout);
			
			params.setMargins(10, 0, 40, 30);
			
			listNoteFrameLayout.setLayoutParams(params);
			
			ImageView button = new ImageView(this);
			
			button.setImageDrawable(getResources().getDrawable(R.drawable.minote_list));
			
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
			
//			eventFrameLayout.setBackgroundResource(R.drawable.day_selected);
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
			
			params.addRule(RelativeLayout.ABOVE, R.id.calendarFrameLayout);
			
			params.addRule(RelativeLayout.ALIGN_LEFT, R.id.notesLayoutManagerFrameLayout);
			
			params.setMargins(10, 0, 40, 30);
			
			eventFrameLayout.setLayoutParams(params);
			
			ImageView button = new ImageView(this);
			
			button.setImageDrawable(getResources().getDrawable(R.drawable.minote_reminder));
			
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) 
				{
					createEventDialog();
				}
			});
			
			eventFrameLayout.addView(button);
		}
		else
		{
			setFrameLayoutVisibilityState(eventFrameLayout);
		}
	}

    public void createEventDialog()
    {
        new EventCreationUI(this).createEventUI(false);
    }



    private void createParagraphNoteFrameLayout()
	{
		if(paragraphNoteFrameLayout == null)
		{
			paragraphNoteFrameLayout = (FrameLayout)findViewById(R.id.paragraphNoteFrameLayout);
			
//			paragraphNoteFrameLayout.setBackgroundResource(R.drawable.day_selected);
			
//	        paragraphNoteFrameLayout.setBackgroundColor(Color.TRANSPARENT);
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
			
			params.addRule(RelativeLayout.ABOVE, R.id.listNoteFrameLayout);
			
			params.addRule(RelativeLayout.ALIGN_LEFT, R.id.listNoteFrameLayout);
			
			params.setMargins(10, 0, 40, 30);
			
			paragraphNoteFrameLayout.setLayoutParams(params);
			
			ImageView button = new ImageView(this);
			
			button.setImageDrawable(getResources().getDrawable(R.drawable.minote_paragraph));
			
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
			
//			imageNoteFrameLayout.setBackgroundResource(R.drawable.day_selected);
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
			
			params.addRule(RelativeLayout.ABOVE, R.id.paragraphNoteFrameLayout);
			
			params.addRule(RelativeLayout.ALIGN_LEFT, R.id.paragraphNoteFrameLayout);
			
			params.setMargins(10, 0, 40, 30);
			
			imageNoteFrameLayout.setLayoutParams(params);
			
			ImageView button = new ImageView(this);
			
			button.setImageDrawable(getResources().getDrawable(R.drawable.minote_camera));
			
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
			calendarFrameLayout = (FrameLayout) findViewById(R.id.calendarFrameLayout);

//			calendarFrameLayout.setBackgroundResource(R.drawable.day_selected);
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);

            params.addRule(RelativeLayout.ABOVE, R.id.notesLayoutManagerFrameLayout);

            params.addRule(RelativeLayout.ALIGN_LEFT, R.id.notesLayoutManagerFrameLayout);

            params.setMargins(10, 0, 40, 30);

            calendarFrameLayout.setLayoutParams(params);

            ImageView button = new ImageView(this);

            button.setImageDrawable(getResources().getDrawable(R.drawable.minote_calendar));
			
			button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
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

	private void createNotes(boolean areNotesToBeCreated)
	{
        clearAllLayouts();

        List<TextNote> parsedNotes = NotesDBManager.getInstance().readNotes();

        Map<Long, Note> notesMap = new TreeMap<Long, Note>();

        if(areNotesToBeCreated)
        {
            for(int i = 0, size = parsedNotes == null ? 0 : parsedNotes.size(); i < size; i++)
            {
                TextNote note = parsedNotes.get(i);

                if(note != null)
                {
                    notesMap.put(note.getCreationTime(), note);
                }
            }
        }
        else
        {
            List<Event> events = CalendarDBManager.getInstance().readAllEvents();

            for(int i = 0, size = events == null ? 0 : events.size(); i < size; i++)
            {
                Event note = events.get(i);

                if (note != null)
                {
                    notesMap.put(note.getCreationTime(), note);
                }
            }
        }

		List<Note> notes = new LinkedList<Note>(notesMap.values());

        for(int i = 0; i < notes.size(); i++)
        {
            Note note = notes.get(i);

            if (note != null)
            {
                createFragmentForNote(note);
            }
        }
	}

    private void clearAllLayouts()
    {
        for(LinearLayout layout : linearLayouts)
        {
            if(layout != null)
            {
                layout.removeAllViews();
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

//	class LinearLayoutOnClickListener implements OnClickListener {
//		@Override
//		public void onClick(View v)
//		{
//			NotesOperationManager.getInstance().copyNote();
//
//			System.out.println("onClick -> areNoteButtonCreated ="+areNoteButtonCreated);
//
//            if(areNoteButtonCreated)
//			{
//				createNoteTypeButtons();
//			}
//		}
//
//	}

	public void openNoteInEditMode(int noteId) 
	{
		Note note = NotesManager.getInstance().getNote(noteId);
		
		if(note.getType() != Note.EVENT_NOTE)
		{
			TextNote textNote = (TextNote)note;
			
			if(textNote.isListNote())
			{
				Intent intent = new Intent(getApplicationContext(),
						ListCreatorActivity.class);

				intent.putExtra(MiNoteConstants.NOTE_ID, noteId);
				
				startActivityForResult(intent, LIST_ACTIVITY_RESULT_CREATED);
			}
			else
			{
				Intent intent = new Intent(getApplicationContext(),
						ParagraphNoteCreatorActivity.class);

				intent.putExtra(MiNoteConstants.NOTE_ID, noteId);
				
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

	private void resetNoteColor(int selectedNoteId)
	{
		FrameLayout frameLayout = childFrames.get(selectedNoteId);

		if(frameLayout != null)
		{
			View view = frameLayout.getChildAt(0);

			if(view != null)
			{
				Note note = NotesManager.getInstance().getNote(selectedNoteId);

				if(note != null && note.getType() != Note.EVENT_NOTE)
				{
                    view.setBackgroundColor(((TextNote) note).getNoteColor());

                    ListView listView = (ListView) view;

                    for (int i = 0; i < listView.getChildCount(); i++) {
                        View childView = listView.getChildAt(i);

                        if (childView instanceof RelativeLayout) {
                            RelativeLayout item = (RelativeLayout) childView;

                            final ImageView imageView = (ImageView) item.findViewById(R.id.compositeControlImageView);

                            if (imageView != null) {
                                imageView.setColorFilter(Color.argb(0, 0, 0, 0));
                            }
                        }

                    }
                }
				else
				{
					view.setBackgroundColor(Color.rgb(255, 255, 255));
				}
			}
		}
	}

	private int getAvailableLayoutIndex()
	{
        int availableIndex = -1;

		if(layoutIndexToBeOccupiedNext < linearLayoutsCount)
        {
            availableIndex = layoutIndexToBeOccupiedNext++;
        }
        else
        {
            layoutIndexToBeOccupiedNext = 0;

            availableIndex = layoutIndexToBeOccupiedNext;

            layoutIndexToBeOccupiedNext++;
        }

		return availableIndex;
	}
	
	private void filterNotes(Set<Integer> noteIds)
	{
		Set<Integer> childFrameIds = childFrames.keySet();

        fiterNoteIds = noteIds;

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
        	getActionBar().setDisplayShowHomeEnabled(false);
        	
            getActionBar().setDisplayShowTitleEnabled(false);
            
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    @Override
	public boolean onQueryTextChange(String query) 
	{
		Set<Integer> noteIds = NotesDBManager.getInstance().getMatchingNoteIds(query);
		
		Set<Integer> eventIds = CalendarDBManager.getInstance().getMatchingNoteIds(query);
		
		noteIds.addAll(eventIds);
		
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
			if(currenActionMode == null)
			{
				currenActionMode = startActionMode(new NotesActionModeCallback());
			}

			View view = frameLayout.getChildAt(0);

			if(view != null)
			{
                ListView listView = (ListView)view;

                for(int i = 0; i < listView.getChildCount(); i++)
                {
                    View childView = listView.getChildAt(i);

                    if(childView instanceof RelativeLayout)
                    {
                        RelativeLayout item = (RelativeLayout)childView;

                        final ImageView imageView = (ImageView) item.findViewById(R.id.compositeControlImageView);

                        if(imageView != null)
                        {
                            imageView.setColorFilter(Color.argb(70, 105, 105, 105));
                        }
                    }

                }

				view.setBackgroundColor(Color.argb(70, 105, 105, 105));
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

            if(currenActionMode != null)
            {
                currenActionMode = null;
            }
		}
		else if(NotesOperationManager.getInstance().getSelectedNoteIds().size() == 1
				&& !NotesOperationManager.getInstance().isSelectedNoteEvent())
		{
			Menu menu = currenActionMode.getMenu();
			
			MenuItem item = menu.findItem(R.id.itemEdit);
			
			if(item != null)
			{
				item.setVisible(true);
			}
		}
	}

	@Override
	public void recieveNotification(byte command, Event event)
    {
        if(command == DBChangeListener.DELETE_COMMAND)
		{
			FrameLayout layout =  childFrames.get(event.getId());

			if(layout != null)
			{
				for(int i = 0; i < linearLayouts.size(); i++)
				{
					LinearLayout linearLayout = linearLayouts.get(i);

					if(linearLayout != null)
					{
                        linearLayout.removeView(layout);
					}
				}
			}
		}
		else if(command == DBChangeListener.INSERT_COMMAND)
		{
            createFragmentForNote(event);

            changeTabs(false);
        }
		else if(command == DBChangeListener.UPDATE_COMMAND)
		{
            //TODO this use case to be checked.

            FrameLayout layout =  childFrames.get(event.getId());

			if(layout != null)
			{
				for(int i = 0; i < linearLayouts.size(); i++)
				{
					LinearLayout linearLayout = linearLayouts.get(i);

					if(linearLayout != null)
					{
						linearLayout.removeView(layout);
					}
				}
			}

			createFragmentForNote(event);

            changeTabs(false);
        }
	}

    class TabListener<T extends Fragment> implements ActionBar.TabListener
    {
        private Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;

        /** Constructor used each time a new tab is created.
         * @param activity  The host Activity, used to instantiate the fragment
         * @param tag  The identifier tag for the fragment
         * @param clz  The fragment's Class, used to instantiate the fragment
         */
        private TabListener(Activity activity, String tag, Class<T> clz)
        {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }

    /* The following are each of the ActionBar.TabListener callbacks */

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft)
        {
            // Check if the fragment is already initialized
            if (mFragment == null)
            {
                // If not, instantiate and add it to the activity
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                ft.add(android.R.id.content, mFragment, mTag);
            }
            else
            {
                // If it exists, simply attach it in order to show it
                ft.attach(mFragment);
            }

            if(mTag.equals(MiNoteConstants.NOTES_TAB))
            {
                createNotes(true);

                isNoteTabSelected = true;

                if(areNoteButtonCreated)
                {
                    makeNoteButtonsInvisible();
                }
            }
            else if(mTag.equals(MiNoteConstants.EVENTS_TAB))
            {
                createNotes(false);

                isNoteTabSelected = false;

                if(areNoteButtonCreated)
                {
                    makeNoteButtonsInvisible();
                }
            }
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                // Detach the fragment, because another one is being attached
                ft.detach(mFragment);
            }
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // User selected the already selected tab. Usually do nothing.
        }
    }

    private void makeNoteButtonsInvisible()
    {
        if(calendarFrameLayout != null)
        {
            calendarFrameLayout.setVisibility(View.GONE);
        }

        if(eventFrameLayout != null)
        {
            eventFrameLayout.setVisibility(View.GONE);
        }

        if(listNoteFrameLayout != null)
        {
            listNoteFrameLayout.setVisibility(View.GONE);
        }

        if(paragraphNoteFrameLayout != null)
        {
            paragraphNoteFrameLayout.setVisibility(View.GONE);
        }

        if(imageNoteFrameLayout != null)
        {
            imageNoteFrameLayout.setVisibility(View.GONE);
        }

        areNoteButtonCreated = false;
    }
}
