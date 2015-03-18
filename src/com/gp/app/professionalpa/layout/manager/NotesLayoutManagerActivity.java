package com.gp.app.professionalpa.layout.manager;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.compositecontrols.ListViewItemLayout;
import com.gp.app.professionalpa.data.NoteListItem;
import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.export.ProfessionalPANotesExporter;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.notes.fragments.FragmentCreationManager;
import com.gp.app.professionalpa.notes.xml.ProfessionalPANotesReader;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;
import com.gp.app.professionalpa.util.ProfessionalPATools;

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
	
	private ImagePathInformationManager imageCaptureManager = null;
	
	public NotesLayoutManagerActivity()
	{
		super();
		
		touchListener = new FrameLayoutTouchListener();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		imageCaptureManager = new ImagePathInformationManager();
		
		ScrollView scrollView  = (ScrollView)getLayoutInflater().inflate(R.layout.activity_notes_layout_manager, null);
		
		activityLayout = (LinearLayout)scrollView.findViewById(R.id.notes_layout_activity_manager);
		
		setContentView(scrollView);

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
		
//		List<Fragment> fragments  = ProfessionalPAParameters.getFragmentCreationManager().getFragments();
//		
//		System.out.println("createActivityLayoutInCaseOfConfigurationChange -> fragments size ="+fragments.size()+" fragments="+fragments);
//		
//		for(int i = 0; i < fragments.size(); i++)
//		{
//			Fragment fragment = fragments.get(i);
//			
//			getFragmentManager().beginTransaction().remove(fragment).commit();
//			
//			getFragmentManager().executePendingTransactions();
//			
//			createActivityLayout(fragment);
//		}
		
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
			File imageFile = imageCaptureManager.createNextFile();
			
			Uri outputFileUri = Uri.fromFile(imageFile);
			
			System.out.println("photo click imageFile="+imageFile.getAbsolutePath());
			
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
            
            cameraIntent.putExtra("IMAGE_PATH", imageFile.getAbsolutePath());
            
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
//	    	String imagePath = imageCaptureManager.getCreatedImagePath();
	    	
	    	System.out.println("onActivityResult -> imagePath="+imageCaptureManager.getMostRecentImageFilePath());
	    	
	        note = createProfessionalPANoteFromImage(imageCaptureManager.getMostRecentImageFilePath());
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

	private ProfessionalPANote createProfessionalPANoteFromImage(String imagePath) 
	{
		ProfessionalPANote note;
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		
		options.inSampleSize = 8;
		
		final Bitmap image = BitmapFactory.decodeFile(imagePath, options);
		
		ArrayList<NoteListItem> items = new ArrayList<NoteListItem>();
		
		items.add(new NoteListItem(image));
		
		note = new ProfessionalPANote(ProfessionalPAConstants.IMAGE_NOTE, items);
		
		//TODO improve exception handling
		try 
		{
			long creationTime = imageCaptureManager.imageCreationTime(imagePath);
			
			note.setCreationTime(creationTime);
			
			note.setLastEditedTime(creationTime);

		} 
		catch (ProfessionalPABaseException e)
		{
			e.printStackTrace();
		}
		
		note.setLastEditedTime(System.currentTimeMillis());
		return note;
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
	    	}
	    }
	}


	private void createActivityLayout(Fragment fragment) 
	{
		FrameLayout frameLayout =  (FrameLayout)getLayoutInflater().inflate(R.layout.professional_pa_frame_layout, null, false);
		
		System.out.println("Registered for context menu");
		
		registerForContextMenu(frameLayout);
		
//		frameLayout.setOnTouchListener(touchListener);
		
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
		
		ProfessionalPAParameters.getFragmentCreationManager().removeAllFragments();
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
		int index = linearLayouts.size()-1;
		
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

			index--;
			
			if (index == -1) 
			{
				index = linearLayouts.size()-1;
				
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
	}

	private void createNotes() throws ProfessionalPABaseException
	{
		System.out.println("createNotes ->");
		
		List<ProfessionalPANote> parsedNotes = ProfessionalPANotesReader.readNotes(false);

		List<String> createdImagesPaths = imageCaptureManager.getImagesFilePath();
		
		for(int i = 0; i < createdImagesPaths.size(); i++)
		{
			String filePath = createdImagesPaths.get(i);
			
			ProfessionalPANote imageNote = createProfessionalPANoteFromImage(createdImagesPaths.get(i));
			
			long creationTime = imageCaptureManager.imageCreationTime(filePath);

			imageNote.setCreationTime(creationTime);
			
			imageNote.setLastEditedTime(creationTime);
			
			parsedNotes.add(imageNote);
		}
		
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
	    
	    System.out.println("onCreateContextMenu -> v="+v);
	    
	    selectedViewIds.add(v.getId());
	    
	    MenuInflater inflater = getMenuInflater();
	    
	    inflater.inflate(R.menu.notes_selection_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    
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
		Iterator<Integer> iterator = selectedViewIds.iterator();
		
		while(iterator.hasNext())
		{
			int selectedViewId = iterator.next();
			
			Fragment noteFragment = getFragmentManager().findFragmentById(selectedViewId);
			
			Bundle bundle = noteFragment.getArguments();
			
			ProfessionalPANote fragmentNote = bundle.getParcelable(ProfessionalPAConstants.NOTE_DATA);

			if(!fragmentNote.isImageNote())
			{
				try 
				{
					ProfessionalPAParameters.getProfessionalPANotesWriter().deleteXmlElement(fragmentNote.getCreationTime());
				}
				catch (ProfessionalPABaseException e)
				{
					// TODO improve
				}
			}
			else
			{
				removeSelectedImage(String.valueOf(fragmentNote.getCreationTime()));
			}
			
			childFrames.remove(selectedViewId);
        	
        	ProfessionalPAParameters.getFragmentCreationManager().removeFragment(noteFragment);

    		updateActivityView();
			
			
			iterator.remove();
		}
	}

	private void removeSelectedImage(String imageName) 
	{
		imageCaptureManager.deleteImage(imageName);
	}

	class FrameLayoutTouchListener implements OnTouchListener
	{
		@Override
		public boolean onTouch(View v, MotionEvent event) 
		{
			selectedViewIds.add(v.getId());
			
			return true;
		}
		
	}
}

class ImagePathInformationManager
{
	private String imageDirectoryPath = null;
	
	private String mostRecentImageFilePath = null;
	
	ImagePathInformationManager()
	{
		createImageDirectory();
	}
	
	public void deleteImage(String imageName) 
	{
		File file = new File(imageDirectoryPath+imageName+".jpeg");
		
		if(file.exists())
		{
			file.delete();
		}
	}

	public String getCreatedImagePath() 
	{
		return imageDirectoryPath+ProfessionalPATools.createImageNameFromTime()+".jpeg";
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
	
//	TODO improve in case image is added by user with different name. will throw number format exception.
	public List<String> getImagesFilePath()
	{
		List<String> imageFilePaths = new ArrayList<String>();
		
		File file = new File(imageDirectoryPath);
		
		if(file.isDirectory())
		{
			File [] files = file.listFiles();
			
			for(int i = 0, size = files != null ? files.length : 0; i < size; i++)
			{
				File imageFile = files[i];
				
//				int endIndex = path.indexOf(".jpeg");
//				
//				String imageName = path.substring(0, endIndex);
				
				imageFilePaths.add(imageFile.getAbsolutePath());
			}
		}
		
		return imageFilePaths;
	}
	
	
	public long imageCreationTime(String imagePath) throws ProfessionalPABaseException 
	{
		int index = imagePath.indexOf(imageDirectoryPath);
		
		int endIndex = imagePath.indexOf(".jpeg");
		
		String imageFileName = imagePath.substring(index + imageDirectoryPath.length(), endIndex);

		return Long.valueOf(imageFileName);
	}
	
	public File createNextFile()
	{
        String filePath = imageDirectoryPath + ProfessionalPATools.createImageNameFromTime()+".jpeg";
        
        File newfile = new File(filePath);
        
        try 
        {
            if(newfile.createNewFile())
            {
            	mostRecentImageFilePath = filePath;
            }
        } 
        catch (IOException e) 
        {
        	//TODO improve
        } 
        
        return newfile;
	}
	
	public String getMostRecentImageFilePath()
	{
		return mostRecentImageFilePath;
	}
}
