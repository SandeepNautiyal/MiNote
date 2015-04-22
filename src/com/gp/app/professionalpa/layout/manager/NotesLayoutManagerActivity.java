package com.gp.app.professionalpa.layout.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.data.NoteListItem;
import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.export.ProfessionalPANotesExporter;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.notes.fragments.FragmentCreationManager;
import com.gp.app.professionalpa.notes.fragments.NotesManager;
import com.gp.app.professionalpa.notes.fragments.ProfessionalPANoteFragment;
import com.gp.app.professionalpa.notes.operations.NotesOperationManager;
import com.gp.app.professionalpa.notes.xml.ProfessionalPANotesReader;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class NotesLayoutManagerActivity extends Activity
{
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

	private byte numberOfLinearLayouts = -1;

	private Map<Integer, FrameLayout> childFrames = new LinkedHashMap<Integer, FrameLayout>();

	private List<LinearLayout> linearLayouts = new ArrayList<LinearLayout>();

	private LinearLayoutAndIndexSelector linearLayoutAndIndexSelector = null;
	private List<Integer> selectedViewIds = new ArrayList<Integer>();

	private ImageLocationPathManager imageCaptureManager = null;

	public NotesLayoutManagerActivity() 
	{
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		imageCaptureManager = ImageLocationPathManager.getInstance();

		ScrollView scrollView = (ScrollView) getLayoutInflater().inflate(
				R.layout.activity_notes_layout_manager, null);

		setContentView(scrollView);

		numberOfLinearLayouts = getNumberOfLinearLayouts();

		linearLayoutAndIndexSelector = new LinearLayoutAndIndexSelector(
				numberOfLinearLayouts);

		fillLinearLayoutList();

		NotesManager.getInstance().deleteAllNotes();

		try 
		{
			createNotes();
		} 
		catch (ProfessionalPABaseException e) 
		{
			//TODO improve
		}

		ActionBar actionBar = getActionBar();

		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#7F7CD9")));

		ProfessionalPAParameters.setNotesActivity(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{

		// outState.putStringArrayList(FRAGMENT_TAGS,
		// (ArrayList<String>)fragmentTags);

		outState.putByte(NUMBER_OF_LINEAR_LAYOUTS, numberOfLinearLayouts);

		super.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) 
	{
		// super.onRestoreInstanceState(savedInstanceState);
		//
		numberOfLinearLayouts = (byte) savedInstanceState
				.getByte(NUMBER_OF_LINEAR_LAYOUTS);
		//
		// fragmentTags.addAll(savedInstanceState.getStringArrayList(FRAGMENT_TAGS));
		//
		// if(fragmentTags.size() > 0)
		// {
		createActivityLayoutInCaseOfConfigurationChange();
		// }
	}

	private void createActivityLayoutInCaseOfConfigurationChange()
	{
		updateNumberOfLinearLayoutsOnScreenChange(getResources()
				.getConfiguration());

		fillLinearLayoutList();

		//TODO to be removed if everything works fine in case of screen orientation change.
//		try 
//		{
//			System.out.println("createActivityLayoutInCaseOfConfigurationChange ->");
//			
//			createNotes();
//		} 
//		catch (ProfessionalPABaseException e) 
//		{
//			// TODO improve
//			e.printStackTrace();
//		}
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

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		int id = item.getItemId();

		if (id == R.id.action_settings)
		{
			return true;
		} 
		else if (id == R.id.action_create_list_view) 
		{
			Intent intent = new Intent(getApplicationContext(),
					ListItemCreatorActivity.class);

			startActivityForResult(intent, LIST_ACTIVITY_RESULT_CREATED);
		} 
		else if (id == R.id.action_create_paragraph_view)
		{
			Intent intent = new Intent(getApplicationContext(),
					ParagraphNoteCreatorActivity.class);

			startActivityForResult(intent, LIST_ACTIVITY_RESULT_CREATED);
		}
		else if (id == R.id.export_notes) 
		{
			try 
			{
				ProfessionalPANotesExporter.export();
			}
			catch (ProfessionalPABaseException exception) 
			{
				// TODO
			}
		} 
		else if (id == R.id.import_notes) 
		{
			List<ProfessionalPANote> notes;

			try
			{
				notes = ProfessionalPANotesReader.readNotes(true);

				ProfessionalPAParameters.getProfessionalPANotesWriter()
						.writeNotes(notes);
			} 
			catch (ProfessionalPABaseException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} 
		else if (id == R.id.action_click_photo) 
		{
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			startActivityForResult(cameraIntent,
					ProfessionalPAConstants.TAKE_PHOTO_CODE);
		}

		return super.onOptionsItemSelected(item);
	}

	public void updateNumberOfLinearLayoutsOnScreenChange(Configuration newConfig) 
	{
		// Checks the orientation of the screen
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) 
		{
			numberOfLinearLayouts = (byte) (numberOfLinearLayouts + 1);
		}
		else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			numberOfLinearLayouts = (byte) (numberOfLinearLayouts - 1);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		ProfessionalPANote note = null;

		System.out.println("onActivityResult -> requestCode="+requestCode+" result code="+resultCode+" data="+data);
		
		if (requestCode == ProfessionalPAConstants.TAKE_PHOTO_CODE && resultCode == RESULT_OK) 
		{
			Bitmap photo = (Bitmap) data.getExtras().get("data");

			ImageLocationPathManager.getInstance().createAndSaveImage(photo);

			note = createProfessionalPANoteFromImage(imageCaptureManager
					.getMostRecentImageFilePath());

			try 
			{
				ProfessionalPAParameters.getProfessionalPANotesWriter()
						.writeNotes(Arrays.asList(note));
			} 
			catch (ProfessionalPABaseException e) 
			{
				// e.printStackTrace();
			}
		} 
		else
		{
			System.out.println("onActivityResult -> data1="+data);

			if (data != null) 
			{
				System.out.println("onActivityResult -> data="+data);
				
				note = data.getParcelableExtra(ProfessionalPAConstants.NOTE_DATA);
			}
		}

		if (note != null)
		{
			createFragmentForNote(note);
		}
	}

	private ProfessionalPANote createProfessionalPANoteFromImage(String imagePath)
	{
		ProfessionalPANote note;

		ArrayList<NoteListItem> items = new ArrayList<NoteListItem>();

		items.add(new NoteListItem(null, ImageLocationPathManager.getInstance()
				.getImageName(imagePath)));

		note = new ProfessionalPANote(NotesManager.getInstance().getNextFreeNoteId(),
				ProfessionalPAConstants.IMAGE_NOTE, items);

		long creationTime = Long.valueOf(imageCaptureManager
				.getImageName(imagePath));

		note.setCreationTime(creationTime);

		note.setLastEditedTime(creationTime);

		note.setTypeOfNote(ProfessionalPAConstants.IMAGE_NOTE);

		note.setLastEditedTime(System.currentTimeMillis());
		return note;
	}

	private void createFragmentForNote(ProfessionalPANote note) 
	{
		if (note != null)
		{
			Fragment fragment = FragmentCreationManager.createFragment(note);

			NotesManager.getInstance().addNote(note.getNoteId(), note);

			if (fragment != null) 
			{
				createActivityLayout(fragment);
			}
		}
	}

	private void createActivityLayout(Fragment fragment) 
	{
        FrameLayout frameLayout =  (FrameLayout)getLayoutInflater().inflate(R.layout.professional_pa_frame_layout, null, false);
		
		frameLayout.setClickable(true);
		
//		frameLayout.setOnTouchListener(touchListener);
		
		int id = ProfessionalPAParameters.getId();
			
		frameLayout.setId(id);

		String tag = fragment.getTag() != null ? fragment.getTag() : "Tag-"
				.concat(Integer.toString(ProfessionalPAParameters.getId()));

		getFragmentManager().beginTransaction().add(id, fragment, tag).commit();

		int noteId = ((ProfessionalPANoteFragment)fragment).getFragmentNoteId();
		
		childFrames.put(noteId, frameLayout);
		
		updateActivityView(frameLayout);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		NotesManager.getInstance().deleteAllNotes();
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

	private void updateActivityView(FrameLayout frameLayout)
	{
		int[] indexes = linearLayoutAndIndexSelector.getNextAvailableIndex();

		int linearLayoutIndex = indexes[0];

		int availableIndex = indexes[1];

		LinearLayout linearLayout = linearLayouts.get(linearLayoutIndex);

		LinearLayout parentView = (LinearLayout) frameLayout.getParent();

		if (parentView != null) 
		{
			parentView.removeView(frameLayout);
		}

		linearLayout.addView(frameLayout, availableIndex);

		linearLayoutAndIndexSelector.fillLayout(linearLayoutIndex, ++availableIndex);
	}

	private void fillLinearLayoutList() 
	{
		LinearLayout layout = null;

		LinearLayoutOnClickListener clickListener = new LinearLayoutOnClickListener();

		switch (numberOfLinearLayouts) {
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
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();

		ProfessionalPAParameters.setLinearLayoutWidth(linearLayouts.get(0)
				.getWidth());
	}

	private void createNotes() throws ProfessionalPABaseException
	{
		List<ProfessionalPANote> parsedNotes = ProfessionalPANotesReader
				.readNotes(false);

		for (int i = 0, size = parsedNotes == null ? 0 : parsedNotes.size(); i < size; i++)
		{
			ProfessionalPANote note = parsedNotes.get(i);

			if (note != null)
			{
				createFragmentForNote(note);
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	public void deleteNote(int noteId)
	{
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

	public void addNote(int noteId) {
		ProfessionalPANote note = NotesManager.getInstance().getNote(noteId);

		if (note != null) {
			createFragmentForNote(note);
		}
	}

	class LinearLayoutOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			NotesOperationManager.copyNote();
		}

	}

	public class LinearLayoutAndIndexSelector 
	{
		private int numberOfLinearLayout = -1;

		private int[] nextAvailableIndex = null;

		public LinearLayoutAndIndexSelector(int numberOfLinearLayout) 
		{
			this.numberOfLinearLayout = numberOfLinearLayout;

			nextAvailableIndex = new int[numberOfLinearLayout];

			for (int i = 0; i < numberOfLinearLayout; i++) 
			{
				nextAvailableIndex[i] = 0;
			}
		}

		public void fillLayout(int filledLayoutIndex, int indexesOccupiedInLayout) 
		{
			if (filledLayoutIndex < numberOfLinearLayout)
			{
				nextAvailableIndex[filledLayoutIndex] = indexesOccupiedInLayout;
			}
		}

		public int[] getNextAvailableIndex() 
		{
			int availableIndex = nextAvailableIndex[0];

			int availableLinearLayout = 0;

			for (int i = 1; i < numberOfLinearLayout; i++)
			{
				if (availableIndex > nextAvailableIndex[i]) 
				{
					availableLinearLayout = i;

					availableIndex = nextAvailableIndex[i];
				}
			}

			return new int[] { availableLinearLayout, availableIndex };
		}
	}

	public void openNoteInEditMode(int noteId) 
	{
		Intent intent = new Intent(getApplicationContext(),
				ListItemCreatorActivity.class);

		intent.putExtra(ProfessionalPAConstants.NOTE_ID, noteId);
		
		startActivityForResult(intent, LIST_ACTIVITY_RESULT_CREATED);
//		startActivity(intent);
	}
}
