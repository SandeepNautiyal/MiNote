package com.gp.app.professionalpa.layout.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.data.NoteListItem;
import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.export.ProfessionalPANotesExporter;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.notes.fragments.FragmentCreationManager;
import com.gp.app.professionalpa.notes.xml.ProfessionalPANotesReader;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class NotesLayoutManagerActivity extends Activity
{
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
	
    private byte numberOfLinearLayouts = -1;
    
	private Map<Integer, FrameLayout> childFrames = new LinkedHashMap<Integer, FrameLayout>();
	
	private List<LinearLayout> linearLayouts = new ArrayList<LinearLayout>();
	
	private LinearLayout activityLayout = null;
	
	private FrameLayoutTouchListener touchListener = null;
	
	private List<Integer> selectedViewIds = new ArrayList<Integer>();
	
	private ImageCaptureManager imageCaptureManager = null;
	
	public NotesLayoutManagerActivity()
	{
		super();
		
		touchListener = new FrameLayoutTouchListener();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		activityLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.activity_notes_layout_manager, null);
		
		setContentView(activityLayout);

		numberOfLinearLayouts = getNumberOfLinearLayouts();
		
		fillLinearLayoutList();
		
		try 
		{
			createNotes();
		} 
		catch (ProfessionalPABaseException e) 
		{
			//TODO improve
			e.printStackTrace();
		}
	}

	
	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
				
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


	private void createActivityLayoutInCaseOfConfigurationChange() 
	{
		updateNumberOfLinearLayoutsOnScreenChange(getResources().getConfiguration());
		
		fillLinearLayoutList();
		
		List<Fragment> fragments  = ProfessionalPAParameters.getFragmentCreationManager().getFragments();
		
		for(int i = 0; i < fragments.size(); i++)
		{
			Fragment fragment = fragments.get(i);
			
			createActivityLayout(fragment);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
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
		else if(id == R.id.export_notes)
		{
			try
			{
				ProfessionalPANotesExporter.export();
			}
			catch(ProfessionalPABaseException exception)
			{
				//TODO
			}
		}
		else if(id == R.id.import_notes)
		{
			List<ProfessionalPANote> notes;
			
			try 
			{
				notes = ProfessionalPANotesReader.readNotes(true);
				
				ProfessionalPAParameters.getProfessionalPANotesWriter().writeNotes(notes);
			} 
			catch (ProfessionalPABaseException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else if(id == R.id.action_click_photo)
		{
			if(imageCaptureManager == null)
			{
				imageCaptureManager = new ImageCaptureManager();
			}
			
			File imageFile = imageCaptureManager.createNextFile();
			
			Uri outputFileUri = Uri.fromFile(imageFile);
			
			System.out.println("photo click imageFile="+imageFile.getAbsolutePath());
			
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
            
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

            startActivityForResult(cameraIntent, ProfessionalPAConstants.TAKE_PHOTO_CODE);
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
	    ProfessionalPANote note = null;
	    
	    if (requestCode == ProfessionalPAConstants.TAKE_PHOTO_CODE && resultCode == RESULT_OK) 
	    {
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        
	        options.inSampleSize = 8;
	        
	        final Bitmap image = BitmapFactory.decodeFile(imageCaptureManager.getLastCreatedImagePath(), options);
	        
	        ArrayList<NoteListItem> items = new ArrayList<NoteListItem>();
	        
	        items.add(new NoteListItem(image));
	        
	        note = new ProfessionalPANote(ProfessionalPAConstants.IMAGE_NOTE, items);
	    }
	    else
	    {
	    	if (data != null)
	    	{
			    note = data.getParcelableExtra(ProfessionalPAConstants.NOTE_DATA);
	    	}
	    }
	    
	    if(note != null)
	    {
		    createFragmentForNote(note);
	    }
	}


	private void createFragmentForNote(ProfessionalPANote note) 
	{
	    if(note != null)
	    {
	    	FragmentCreationManager fragmentManager = ProfessionalPAParameters.getFragmentCreationManager();
	    	
	    	Fragment fragment = fragmentManager.createFragment(note);
	    	
	    	if(fragment != null)
	    	{
			    createActivityLayout(fragment);
			    
			    System.out.println("createFragmentForNote -> fragment="+fragment);
	    	}
	    }
	}


	private void createActivityLayout(Fragment fragment) 
	{
		FrameLayout frameLayout =  (FrameLayout)getLayoutInflater().inflate(R.layout.professional_pa_frame_layout, null, false);
		
		registerForContextMenu(frameLayout);
		
//		frameLayout.setOnTouchListener(touchListener);
		
		int id = ProfessionalPAParameters.getId();
			
		frameLayout.setId(id);

		System.out.println("createActivityLayout -> id="+id);
		
		String tag = fragment.getTag() != null ? fragment.getTag() : "Tag-"+ProfessionalPAParameters.getId();
				
		getFragmentManager().beginTransaction().add(id, fragment, tag).commit();
		
		addFrameLayoutToActivtyView(frameLayout);
		
		updateActivityView();
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
	}


	private void addFrameLayoutToActivtyView(FrameLayout frameLayout) 
	{
		childFrames.put(frameLayout.getId(), frameLayout);
	}

	
	public byte getNumberOfLinearLayouts() {
		
		byte numberOfLinearLayouts = -1;
		
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) 
		{
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) 
			{
				numberOfLinearLayouts = NUMBER_OF_LINEAR_LAYOUT_FOR_EXTRA_LARGE_SCREEN_PORTRAIT;
			} 
			else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
			{
				numberOfLinearLayouts = NUMBER_OF_LINEAR_LAYOUT_FOR_EXTRA_LARGE_SCREEN_LANDSCAPE;
			}

		}
		if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) 
		{
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) 
			{
				numberOfLinearLayouts = NUMBER_OF_LINEAR_LAYOUT_FOR_LARGE_SCREEN_PORTRAIT;
			} 
			else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) 
			{
				numberOfLinearLayouts = NUMBER_OF_LINEAR_LAYOUT_FOR_LARGE_SCREEN_LANDSCAPE;
			}
		} 
		else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL)
		{
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) 
			{
				numberOfLinearLayouts = NUMBER_OF_LINEAR_LAYOUT_FOR_NORMAL_SCREEN_PORTRAIT;
			} 
			else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) 
			{
				numberOfLinearLayouts = NUMBER_OF_LINEAR_LAYOUT_FOR_NORMAL_SCREEN_LANDSCAPE;
			}
		} 
		else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) 
		{
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) 
			{
				numberOfLinearLayouts = NUMBER_OF_LINEAR_LAYOUT_FOR_SMALL_SCREEN_PORTRAIT;
			} 
			else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) 
			{
				numberOfLinearLayouts = NUMBER_OF_LINEAR_LAYOUT_FOR_SMALL_SCREEN_LANDSCAPE;
			}
		} 
		else 
		{
		}

		return numberOfLinearLayouts;
	}
	
	private void updateActivityView() 
	{		
		int index = 0;
		
		int linearLayoutIndex = 0;
		
		for(int i = 0; i < linearLayouts.size(); i++)
		{
			LinearLayout layout = linearLayouts.get(i);
			
			layout.removeAllViews();
		}
		
		for (Entry<Integer, FrameLayout> entry : childFrames.entrySet()) 
		{
			LinearLayout linearLayout = linearLayouts.get(index);

			FrameLayout frameLayout = entry.getValue();

			LinearLayout parentView = (LinearLayout) frameLayout.getParent();

			if (parentView != null) 
			{
				parentView.removeView(frameLayout);
			}

			linearLayout.addView(frameLayout, linearLayoutIndex);

			index++;
			
			if (index % linearLayouts.size() == 0) 
			{
				index = 0;
				
				linearLayoutIndex++;
			}
		}
			
		
	}


	private void fillLinearLayoutList() 
	{
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
		super.onResume();
		
		ProfessionalPAParameters.setLinearLayoutWidth(linearLayouts.get(0).getWidth());
		
		System.out.println("onResume -> linearLayouts.get(0).getWidth()="+linearLayouts.get(0).getWidth());
	}

	private void createNotes() throws ProfessionalPABaseException
	{
		List<ProfessionalPANote> parsedNotes = ProfessionalPANotesReader.readNotes(false);

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
	protected void onPause() 
	{
		super.onPause();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) 
	{
	    super.onCreateContextMenu(menu, v, menuInfo);
	    
	    System.out.println("onCreateContextMenu -> v="+v.getId());
	    
	    selectedViewIds.add(v.getId());
	    
	    MenuInflater inflater = getMenuInflater();
	    
	    inflater.inflate(R.menu.notes_selection_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    
	    System.out.println("item id:"+item.getItemId()+" info="+info);
	    
	    switch (item.getItemId()) 
	    {
	        case R.id.action_discard_notes:
	        	clearSelectedNotes();
//	            System.out.println("context menu id :"+info.id);
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	/**
	 * 
	 */
	private void clearSelectedNotes() 
	{
		for(int i = 0; i < selectedViewIds.size(); i++)
		{
			int selectedViewId = selectedViewIds.get(i);
			
			Fragment noteFragment = getFragmentManager().findFragmentById(selectedViewId);
			
			Bundle bundle = noteFragment.getArguments();
			
			ProfessionalPANote fragmentNote = bundle.getParcelable(ProfessionalPAConstants.NOTE_DATA);

			System.out.println("clearSelectedNotes -> fragmentNote.getCreationTime()="+fragmentNote.getCreationTime());
			
			try 
			{
				ProfessionalPAParameters.getProfessionalPANotesWriter().deleteXmlElement(fragmentNote.getCreationTime());
				
				System.out.println("childFrames size1="+childFrames.size()+" frames ="+childFrames);
				
	        	childFrames.remove(selectedViewId);
	        	
	        	ProfessionalPAParameters.getFragmentCreationManager().removeFragment(noteFragment);
	        	
				System.out.println("childFrames size2="+childFrames.size()+" selectedViewId="+selectedViewId);

	    		updateActivityView();
			}
			catch (ProfessionalPABaseException e)
			{
				// TODO improve
			}

//			for(int j = 0; j < childFrames.size(); j++)
//			{
//				FrameLayout layout = childFrames.get(j);
//				
//				if(layout != null)
//				{
//					if(layout.getId() == selectedViewId)
//					{
//						
//						childFrames.remove(layout);
//					}
//				}
//			}
		}
	}

	class FrameLayoutTouchListener implements OnTouchListener
	{
		@Override
		public boolean onTouch(View v, MotionEvent event) 
		{
			System.out.println("onTouch -> id="+v.getId());
			selectedViewIds.add(v.getId());
			
			return true;
		}
		
	}
}

class ImageCaptureManager
{
	private static final String IMAGE_NAME_START_STRING = "image";

	private String lastCreatedImageName = null;
	
	private String imageDirectoryPath = null;
	
	private int imageNameCounter = 1;
	
	ImageCaptureManager()
	{
		createImageDirectory();
	}
	
	private void createImageDirectory()
	{
		final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/ProfessionalPA/"; 
        
		File newdir = new File(dir); 
        
        if(!newdir.exists())
        {
	        newdir.mkdirs();
        }
        
        imageDirectoryPath = dir;
	}
	
	public File createNextFile()
	{
        String file = imageDirectoryPath+createNextFileName();
        
        File newfile = new File(file);
        
        try 
        {
            newfile.createNewFile();
        } 
        catch (IOException e) 
        {
        	//TODO improve
        } 
        
        return newfile;
	}
	
	private String createNextFileName()
	{
		lastCreatedImageName = IMAGE_NAME_START_STRING+imageNameCounter+".jpeg";
		
		System.out.println("createNextFileName -> lastCreatedImageName="+lastCreatedImageName);
		
		imageNameCounter++;
		
		return lastCreatedImageName;
	}
	
	private String getLastCreatedImageName()
	{
		return lastCreatedImageName;
	}
	
	public String getLastCreatedImagePath()
	{
		String imagePath = imageDirectoryPath + getLastCreatedImageName();
		
		System.out.println("getLastCreatedImagePath -> lastCreatedImageName="+imagePath);

		return imagePath;
	}
}
