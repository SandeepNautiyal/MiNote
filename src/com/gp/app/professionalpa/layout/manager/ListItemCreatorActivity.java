package com.gp.app.professionalpa.layout.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.compositecontrols.ListViewItemLayout;
import com.gp.app.professionalpa.data.ListViewItem;
import com.gp.app.professionalpa.layout.notes.data.ProfessionalPAListView;

public class ListItemCreatorActivity extends Activity {
	
	private FragmentManager fragmentManager = null;
	
	private List<View> listViewControls = new ArrayList<View>();

	private List<ListViewItemLayout> listItems = new ArrayList<ListViewItemLayout>();
			
	private ListViewItemLayout lastAddedListItem = null;
	
	private RelativeLayout activityLayout = null;
	
	Map<Integer, ProfessionalPAListView> fragments = new HashMap<Integer, ProfessionalPAListView>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        LayoutInflater inflater = getLayoutInflater();

        activityLayout = (RelativeLayout)inflater.inflate(R.layout.list_item_creator_activity, null);

		lastAddedListItem = (ListViewItemLayout)inflater.inflate(R.layout.compound_control_layout, null);
		
		listItems.add(lastAddedListItem);
		
		activityLayout.addView(lastAddedListItem);
		
		addSaveAndAddItemButton();
		
		setContentView(activityLayout);

//		RelativeLayout listDataCreator = (RelativeLayout)getApplicationContext().findViewById(R.id.list_item_creator_activity);
//		
//		ListViewItemLayout listViewControl = (ListViewItemLayout)findViewById(R.id.composite_control_layout);
//		
//		listDataCreator.addView(listViewControl);
//		
//		listViewControls.add(listViewControl);
//		
//		this.setContentView(listDataCreator);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.list_item_creator, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void addNewListItem(View view)
	{
//		LayoutInflater infltor = this.getLayoutInflater();
//		
//		ListViewItemLayout itemLayout = (ListViewItemLayout)infltor.inflate(R.layout.compound_control_layout, null);
//		
//		RelativeLayout layout = (RelativeLayout)findViewById(R.id.list_item_creator_activity);
		
		ListViewItemLayout currentAddedListItem = new ListViewItemLayout(this);
		
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		layoutParams.addRule(RelativeLayout.BELOW, lastAddedListItem.getId());
		
		currentAddedListItem.setLayoutParams(layoutParams);
		
		Log.e("index", "currentAddedListItem ="+currentAddedListItem+" id :"+currentAddedListItem.getId()+" lastAddedListItem="+lastAddedListItem
				+" id :"+lastAddedListItem.getId());
		
		activityLayout.addView(currentAddedListItem, listItems.size());
		
		lastAddedListItem = currentAddedListItem;
		
		listItems.add(currentAddedListItem);
		
		addSaveAndAddItemButton();
		
		setContentView(activityLayout);
		
//		fragmentManager.beginTransaction().add(R.id.notes_layout_activity_manager, listViewFragment).commit();
//		
//		fragments.put(listViewFragment.getId(), listViewFragment);

	}

	private void addSaveAndAddItemButton() 
	{
		View saveButton = findViewById(R.id.save_list);
		
		View addNewListItem = findViewById(R.id.add_more_items);
		
        activityLayout.removeView(saveButton);
		
		activityLayout.removeView(addNewListItem);
		
		RelativeLayout.LayoutParams layoutParamsForSaveButton = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		Log.e("BELOW", "item added below:"+lastAddedListItem+" id:"+lastAddedListItem.getId());
		
		layoutParamsForSaveButton.addRule(RelativeLayout.BELOW, lastAddedListItem.getId());
		
		saveButton.setLayoutParams(layoutParamsForSaveButton);
		
		RelativeLayout.LayoutParams layoutParamsForAddListItem = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		layoutParamsForAddListItem.addRule(RelativeLayout.RIGHT_OF, saveButton.getId());
		
		layoutParamsForAddListItem.addRule(RelativeLayout.ALIGN_BASELINE, saveButton.getId());
		
		layoutParamsForAddListItem.setMargins(30, 0, 0, 0);
		
		layoutParamsForAddListItem.addRule(RelativeLayout.RIGHT_OF, saveButton.getId());
		
		addNewListItem.setLayoutParams(layoutParamsForAddListItem);
		
		activityLayout.addView(saveButton, listItems.size());
		
		activityLayout.addView(addNewListItem);

		
		
		
	}
}
