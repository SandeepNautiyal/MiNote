package com.gp.app.professionalpa.layout.manager;

import java.util.HashMap;
import java.util.Map;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.data.ListViewItem;
import com.gp.app.professionalpa.layout.notes.data.ProfessionalPAListView;

public class NotesLayoutManagerActivity extends FragmentActivity {

	private static final int LIST_ACTIVITY_RESULT_CREATED = 1;
	
	FragmentManager fragmentManager = null;
	
	Map<Integer, ProfessionalPAListView> fragments = new HashMap<Integer, ProfessionalPAListView>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		fragmentManager = getFragmentManager();
		
		setContentView(R.layout.activity_notes_layout_manager);
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
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (data == null) {return;}
	    
	    Fragment fragment = new ProfessionalPAListView();
	    
	    Bundle bundle = new Bundle();
	    
	    bundle.putParcelableArray("LIST_ITEMS", data.getParcelableArrayExtra("LIST_ITEMS"));
	    
	    fragment.setArguments(bundle);
	    
	    fragmentManager.beginTransaction().add(R.id.notes_layout_activity_manager, fragment).commit();
	  }
}
