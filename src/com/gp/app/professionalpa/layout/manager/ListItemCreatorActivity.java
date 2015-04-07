package com.gp.app.professionalpa.layout.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.compositecontrols.ListViewItemLayout;
import com.gp.app.professionalpa.data.NoteListItem;
import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.interfaces.XMLEntity;
import com.gp.app.professionalpa.notes.xml.ProfessionalPANotesWriter;
import com.gp.app.professionalpa.util.ProfessionalPANotesIdGenerator;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class ListItemCreatorActivity extends Activity
{
	private List<ListViewItemLayout> listItems = new ArrayList<ListViewItemLayout>();
			
	private ListViewItemLayout lastAddedListItem = null;
	
	private ScrollView scrollView = null;
	
	private RelativeLayout activityLayout = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(lastAddedListItem == null)
		{
			LayoutInflater inflater = getLayoutInflater();

			scrollView = (ScrollView)inflater.inflate(R.layout.list_item_creator_activity, null);
			
			activityLayout = (RelativeLayout)scrollView.findViewById(R.id.list_item_creator_activity_layout);
			
			lastAddedListItem = (ListViewItemLayout)inflater.inflate(R.layout.compound_control_layout, null);
			
			listItems.add(lastAddedListItem);
			
			activityLayout.addView(lastAddedListItem);
			
			addSaveAndAddItemButton();
			
			setContentView(scrollView);
		}
		
        ActionBar actionBar = getActionBar();
		
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7F7CD9")));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
        getMenuInflater().inflate(R.menu.list_creator_activity_menu_items, menu);
        
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
	    ProfessionalPANote note = null;
	    
	    if (requestCode == ProfessionalPAConstants.TAKE_PHOTO_CODE && resultCode == RESULT_OK) 
	    {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
	    	
	    	ImageLocationPathManager.getInstance().createSaveImage(photo);
	    	
	    	Bitmap image = createProfessionalPANoteFromImage(ImageLocationPathManager.getInstance().getMostRecentImageFilePath());
	        
	        addNewListItem();
	        
	        lastAddedListItem.setImage(image);
	    }
	}
	
	//TODO Duplicate
	private Bitmap createProfessionalPANoteFromImage(String imagePath) 
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		
		options.inSampleSize = 8;
		
		Bitmap image = BitmapFactory.decodeFile(imagePath, options);
		
		return image;
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
		else if(id == R.id.action_click_photo)
		{
			//TODO duplicate
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
            
            startActivityForResult(cameraIntent, ProfessionalPAConstants.TAKE_PHOTO_CODE);
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void addNewListItem() 
	{
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
		
		setContentView(scrollView);
	}

	private void addSaveAndAddItemButton() 
	{
		Button saveButton = (Button)findViewById(ProfessionalPAConstants.SAVE_BUTTON_ID);
		
		Button addNewListItem = (Button)findViewById(ProfessionalPAConstants.ADD_BUTTON_ID);
		
		activityLayout.removeView(saveButton);
		
		activityLayout.removeView(addNewListItem);
		
		saveButton = new Button(this);
		
		saveButton.setText("Save");
		
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				saveListOfItems();
			}
		});
		
		saveButton.setId(ProfessionalPAConstants.SAVE_BUTTON_ID);

		addNewListItem = new Button(this);
		
		addNewListItem.setText("Add");
		
		addNewListItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view)
			{
				addNewListItem();
			}
		});
		
		saveButton.setId(ProfessionalPAConstants.ADD_BUTTON_ID);
		
		RelativeLayout.LayoutParams layoutParamsForSaveButton = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		layoutParamsForSaveButton.addRule(RelativeLayout.BELOW, lastAddedListItem.getId());
		
		saveButton.setLayoutParams(layoutParamsForSaveButton);
		
		RelativeLayout.LayoutParams layoutParamsForAddListItem = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		
//		layoutParamsForAddListItem.addRule(RelativeLayout.BELOW, lastAddedListItem.getId());
		
		layoutParamsForAddListItem.addRule(RelativeLayout.RIGHT_OF, saveButton.getId());
		
//		layoutParamsForAddListItem.addRule(RelativeLayout.ALIGN_BASELINE, saveButton.getId());
		
		layoutParamsForAddListItem.addRule(RelativeLayout.ALIGN_BOTTOM, saveButton.getId());
		
		addNewListItem.setLayoutParams(layoutParamsForAddListItem);
		
		activityLayout.addView(saveButton, listItems.size());
		
		activityLayout.addView(addNewListItem);
	}

	private void saveListOfItems()
	{
		List<NoteListItem> item = new ArrayList<NoteListItem>();
		
		for(int i = 0, size = listItems.size(); i < size; i++)
		{
			ListViewItemLayout compoundControl = listItems.get(i);
			
            NoteListItem listItem = new NoteListItem(compoundControl.getText());
			
            item.add(listItem);
		}

		ProfessionalPANote note = new ProfessionalPANote(ProfessionalPANotesIdGenerator.generateNoteId(), ProfessionalPAConstants.LIST_NOTE, item);
		
		long creationTime = System.currentTimeMillis();
		
		note.setCreationTime(creationTime);
		
		note.setLastEditedTime(creationTime);
		
		List<ProfessionalPANote> notes = new ArrayList<ProfessionalPANote>();
		
		notes.add(note);
		
		try
		{
			persistListElement(notes);
		} 
		catch (ProfessionalPABaseException exception) 
		{
			// TODO improve
		}
		
		Intent returnIntent = new Intent();

		returnIntent.putExtra(ProfessionalPAConstants.NOTE_DATA, note);

		setResult(RESULT_OK,returnIntent);
		
		finish();
	}
	
	@Override
	public void onBackPressed() 
	{
	    super.onBackPressed();
	}
	
	private void persistListElement(List<ProfessionalPANote> notes) throws ProfessionalPABaseException
	{
		ProfessionalPANotesWriter fragmentWriter = ProfessionalPAParameters.getProfessionalPANotesWriter();
		
		fragmentWriter.writeNotes(notes);
	}
}
