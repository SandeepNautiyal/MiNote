package com.gp.app.professionalpa.layout.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.activity.state.ActivityStateMonitor;
import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.export.ProfessionalPANotesExporter;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.interfaces.XMLDataChangeListener;
import com.gp.app.professionalpa.interfaces.XMLDataChangePublisher;
import com.gp.app.professionalpa.notes.fragments.FragmentCreationManager;
import com.gp.app.professionalpa.notes.xml.ProfessionalPANotesReader;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class NotesLayoutManagerActivity extends Activity implements XMLDataChangeListener
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
    
	private List<FrameLayout> childFrames = new ArrayList<FrameLayout>();
	
	private List<LinearLayout> linearLayouts = new ArrayList<LinearLayout>();
	
	private LinearLayout activityLayout = null;
	
	private Lock lock = null;
	
	public NotesLayoutManagerActivity()
	{
		super();
		
		lock = new ReentrantLock();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		activityLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.activity_notes_layout_manager, null);
		
		setContentView(activityLayout);

		numberOfLinearLayouts = getNumberOfLinearLayouts();
		
		fillLinearLayoutList();
		
        XMLDataChangePublisher publisher = ProfessionalPAParameters.getProfessionalPANotesWriter();
		
		publisher.addXMLDataChangeListener(this);
		
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notes_layout_manager, menu);
		
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
	    
	    ProfessionalPANote note = data.getParcelableExtra(ProfessionalPAConstants.NOTE_DATA);
	    
	    createFragmentForNote(note);
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
	}


	private void addFrameLayoutToActivtyView(FrameLayout frameLayout) 
	{
		childFrames.add(0, frameLayout);
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
								
				linearLayout.addView(frameLayout, index);
				
				index++;
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
	public void notifyXMLDataChange() 
	{
		lock.lock();

		Condition condition = lock.newCondition();

		try 
		{
			condition.await();
		} 
		catch (InterruptedException e)
		{
			// TODO improve
			e.printStackTrace();
		} 
		finally 
		{
			lock.unlock();
		}
		
		try 
		{
			createNotes();

		} catch (ProfessionalPABaseException e)
		{

			// TODO improve
			// e.printStackTrace();
		}
	}
	
	public class ActivityStateMonitor extends Application implements ActivityLifecycleCallbacks
	{
		boolean isInterestingActivityVisible;

	    @Override
	    public void onCreate() 
	    {
	        super.onCreate();

	        registerActivityLifecycleCallbacks(this);
	    }

	    public boolean isInterestingActivityVisible() 
	    {
	        return isInterestingActivityVisible;
	    }

	    @Override
	    public void onActivityResumed(Activity activity) 
	    {
	        if (activity instanceof NotesLayoutManagerActivity)
	        {
	             isInterestingActivityVisible = true;
	             
	             Condition condition = lock.newCondition();
	             
	             condition.signalAll();
	        }
	    }

	    @Override
	    public void onActivityStopped(Activity activity) 
	    {
	        if (activity instanceof NotesLayoutManagerActivity) 
	        {
	             isInterestingActivityVisible = false;
	        }
	    }

		@Override
		public void onActivityCreated(Activity arg0, Bundle arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivityDestroyed(Activity arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivityPaused(Activity arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivitySaveInstanceState(Activity arg0, Bundle arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivityStarted(Activity arg0) {
			// TODO Auto-generated method stub
			
		}
		
		public boolean isNotesLayoutManagerActivityResumed()
		{
			return isInterestingActivityVisible;
		}
	}
}
