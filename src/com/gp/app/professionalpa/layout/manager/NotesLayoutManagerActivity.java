package com.gp.app.professionalpa.layout.manager;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.layout.notes.data.ProfessionalPAListView;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class NotesLayoutManagerActivity extends FragmentActivity {

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
	
	FragmentManager fragmentManager = null;
	
	List<FrameLayout> childFrames = new ArrayList<FrameLayout>();
	
	List<LinearLayout> linearLayouts = new ArrayList<LinearLayout>();
	
	LinearLayout activityLayout = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		fragmentManager = getFragmentManager();
		
		activityLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.activity_notes_layout_manager, null);
		
		numberOfLinearLayouts = getNumberOfLinearLayouts();
		
		setContentView(activityLayout);
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.linearLayout1);
		
		System.out.println("onResume -> width="+layout.getWidth());
		
		ProfessionalPAParameters.setParentLinearLayoutWidth(layout.getWidth());
		
	    fillLinearLayoutList();

		updateActivityView();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notes_layout_manager, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		
		if (id == R.id.action_settings) {
			return true;
		}
		
		else if(id == R.id.action_create_list_view)
		{
//			Log.d("list view button clicked", "list view clicked");
			
			Intent intent = new Intent(getApplicationContext(), ListItemCreatorActivity.class);
			
//			intent.putExtra("ASSOCIATED_FRAGMENT_ID", listViewFragment.getId());
			
			startActivityForResult(intent, LIST_ACTIVITY_RESULT_CREATED);
			
//			
//			
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);

	    // Checks the orientation of the screen
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) 
	    {
	    	 numberOfLinearLayouts = (byte)(numberOfLinearLayouts+1);
	    } 
	    else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
	    {
	    	numberOfLinearLayouts = (byte)(numberOfLinearLayouts-1);
	    }
	    
	    updateActivityView();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (data == null) {return;}
	    
	    Fragment fragment = new ProfessionalPAListView();
	    
	    Bundle bundle = new Bundle();
	    
	    Parcelable[] values = data.getParcelableArrayExtra("LIST_ITEMS");
	    
	    if(values != null)
	    {
	    	bundle.putParcelableArray("LIST_ITEMS", values);
		    
		    fragment.setArguments(bundle);
		    
		    FrameLayout frameLayout = new FrameLayout(this);
		    
		    frameLayout.setId(ProfessionalPAParameters.getId());
		    
		    fragmentManager.beginTransaction().add(frameLayout.getId(), fragment).commit();
		    
		    addFrameLayoutToActivtyView(frameLayout);
		    
		    updateActivityView();
	    }
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
			
			linearLayout.removeAllViews();
			
			int index = 0;
			
			for(int j = i; j < childFrames.size(); j = j+numberOfLinearLayouts)
			{
				FrameLayout frameLayout = childFrames.get(j);
				
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
}
