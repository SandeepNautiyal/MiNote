package com.gp.app.professionalpa.layout.manager;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.layout.notes.data.ProfessionalPAListView;

public class NotesLayoutManagerActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		
		FragmentManager fragmentManager = getFragmentManager();
		
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		else if(id == R.id.action_create_list_view)
		{
//			Log.d("list view button clicked", "list view clicked");
			
			ProfessionalPAListView listViewFragment = new ProfessionalPAListView();
			
			fragmentManager.beginTransaction().add(R.id.notes_layout_activity_manager, listViewFragment).commit();
		}
		
		return super.onOptionsItemSelected(item);
	}
}
