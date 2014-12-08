package com.gp.app.professionalpa.layout.manager;

import java.util.HashMap;
import java.util.Map;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.layout.notes.data.ProfessionalPAListView;

public class NotesLayoutManagerActivity extends FragmentActivity {

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
			
			startActivity(intent);
			
//			
//			
		}
		
		return super.onOptionsItemSelected(item);
	}
}
