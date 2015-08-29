package com.gp.app.minote.layout.manager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.gp.app.minote.R;
import com.gp.app.minote.backend.entities.eventEntityApi.EventEntityApi;
import com.gp.app.minote.backend.entities.eventEntityApi.model.EventEntity;
import com.gp.app.minote.colorpicker.ColorPickerCreator;
import com.gp.app.minote.colorpicker.ColourPickerChangeListener;
import com.gp.app.minote.compositecontrols.ListViewItemLayout;
import com.gp.app.minote.data.Note;
import com.gp.app.minote.data.NoteItem;
import com.gp.app.minote.data.TextNote;
import com.gp.app.minote.interfaces.MiNoteConstants;
import com.gp.app.minote.notes.database.NotesDBManager;
import com.gp.app.minote.notes.fragments.NotesManager;
import com.gp.app.minote.notes.images.ImageLocationPathManager;
import com.gp.app.minote.util.MiNoteParameters;
import com.gp.app.minote.util.MiNoteUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListItemCreatorActivity extends Activity implements ColourPickerChangeListener
{
	private List<ListViewItemLayout> listItems = new ArrayList<ListViewItemLayout>();
			
	private ListViewItemLayout lastAddedListItem = null;
	
	private ScrollView scrollView = null;
	
	private RelativeLayout activityLayout = null;

	private boolean isSaveButtonToBeCreated = true;

	private boolean isLastItemToBeAddedInLayout = true;
	
	private int modifiedNoteId = -1;
	
	private List<View> selectedViews = new ArrayList<View>(1);

	private EditText titleEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		if(lastAddedListItem == null)
		{
			LayoutInflater inflater = getLayoutInflater();

			scrollView = (ScrollView)inflater.inflate(R.layout.list_item_creator_activity, null);
			
			activityLayout = (RelativeLayout)scrollView.findViewById(R.id.list_item_creator_activity_layout);
			
			titleEditText = (EditText)activityLayout.findViewById(R.id.noteTitle);
			
			titleEditText.setOnFocusChangeListener(new OnFocusChangeListener() 
			{
				@Override
				public void onFocusChange(View view, boolean hasFocus)
				{
					selectedViews.clear();
					
					selectedViews.add(view);
				}
			});
			
			editNotes();
			
			if(isLastItemToBeAddedInLayout)
			{
				addNewListItem();
			}
			
			if(isSaveButtonToBeCreated)
			{
				addAddItemButton();
			}
			
			isLastItemToBeAddedInLayout = true;

			isSaveButtonToBeCreated = true;
			
			setContentView(scrollView);
		}
		
        ActionBar actionBar = getActionBar();
		
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(120, 100, 255)));
	}

	private void editNotes()
	{
		Intent intent = getIntent();
		
		if(intent != null)
		{
			Bundle extras = intent.getExtras();
			
			if(extras != null)
			{
				int noteId = extras.getInt(MiNoteConstants.NOTE_ID);
				
				if(noteId != 0)
				{
			    	Note note = NotesManager.getInstance().getNote(noteId);

			    	if(note.getType() != Note.EVENT_NOTE)
			    	{
			    		TextNote professionalPANote = (TextNote)note;
			    		
			    		if(professionalPANote != null)
				    	{
				    		if(professionalPANote.getType() == Note.IMAGE_NOTE)
				    		{
				    			createActivityLayoutForNote(professionalPANote);
					    		
					    		isSaveButtonToBeCreated  = false;
				    		}
				    		else
				    		{
				    			List<NoteItem> listItems = professionalPANote.getNoteItems();
				    			
				    			for(int i = 0; i < listItems.size(); i++)
				    			{
				    				NoteItem item = listItems.get(i);
				    				
				    				if(item != null)
				    				{
				    					if(item.isTitle())
				    					{
				    						titleEditText.setText(item.getText());
				    					}
				    					else
				    					{
				    						String textViewData = item.getText();
					    					
					    					String imageName = item.getImageName();
					    					
					    					if(textViewData != null && !textViewData.equals(""))
					    					{
					    						addNewListItem();
					    						
					    						lastAddedListItem.setText(textViewData);
					    						
					    						lastAddedListItem.setTextColour(item.getTextColour());
					    					}
					    					
					    					if(imageName != null && !imageName.equals(""))
					    					{
			                                    addNewListItem();
					    						
					    						lastAddedListItem.setImage(imageName, ImageLocationPathManager.getInstance().getImage(imageName, true), true);
					    					}
				    					}
				    				}
				    			}
				    			
				    			updateActivityLayout();
				    			
				    			isLastItemToBeAddedInLayout = false;

				    			modifiedNoteId = noteId;
				    		}
				    	}
			    	}
				}
			}
		}
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
	    if (requestCode == MiNoteConstants.TAKE_PHOTO_CODE && resultCode == RESULT_OK) 
	    {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
	    	
	    	ImageLocationPathManager.getInstance().createAndSaveImage(photo);
	    	
	    	String imagePath = ImageLocationPathManager.getInstance().getMostRecentImageFilePath();
	    	
	    	Bitmap image = ImageLocationPathManager.getInstance().getImage(imagePath, false);
	        
	    	String imageName = ImageLocationPathManager.getInstance().getImageName(imagePath);
	    	
	    	boolean isTextPresentInEditText = lastAddedListItem.getText() != null && !lastAddedListItem.getText().equals("");
	    	
	    	boolean isImagePresentInImageView = lastAddedListItem.getImageName() != null && !lastAddedListItem.getImageName().equals("");
	    	
	    	if(isTextPresentInEditText ||  isImagePresentInImageView)
	    	{
	    		addNewListItem();
	    		
	    		updateActivityLayout();
	    	}
	    	
	        lastAddedListItem.setImage(imageName, image, true);
	        
//	        addNewListItem();
	        
//			updateActivityLayout();
	    }
	}
	
	private void createActivityLayoutForNote(TextNote professionalPANote) 
	{
		if(professionalPANote.getType() == Note.IMAGE_NOTE)
		{
			NoteItem item = professionalPANote.getNoteItems().get(0);

			Bitmap bitMap = ImageLocationPathManager.getInstance().getImage(item.getImageName(), true);
			
			if(bitMap != null)
			{
				lastAddedListItem.setImage(item.getImageName(), bitMap, false);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		int id = item.getItemId();
		
		if (id == R.id.action_settings) 
		{
			return true;
		}
		else if(id == R.id.clickPhoto)
		{
			//TODO duplicate
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
            
            startActivityForResult(cameraIntent, MiNoteConstants.TAKE_PHOTO_CODE);
		}
		else if(id == R.id.saveListNote)
		{
			saveListOfItems();
		}
		else if(id == R.id.pickColor)
		{
			new ColorPickerCreator(this, this).createColourPicker();
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void addNewListItem() 
	{
		final ListViewItemLayout currentAddedListItem = new ListViewItemLayout(this);
		
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		if(lastAddedListItem != null)
		{
			layoutParams.addRule(RelativeLayout.BELOW, lastAddedListItem.getId());
		}
		else
		{
			layoutParams.addRule(RelativeLayout.BELOW, R.id.noteTitle);
		}
		
		currentAddedListItem.setLayoutParams(layoutParams);
		
		ImageButton deleteButton = currentAddedListItem.getImportanceImageButton();
		
		if(deleteButton != null)
		{
			deleteButton.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					activityLayout.removeView(currentAddedListItem);
					
					listItems.remove(currentAddedListItem);
				}
			});
		}
		
		currentAddedListItem.getEditText().setOnFocusChangeListener(new OnFocusChangeListener() 
		{
			@Override
			public void onFocusChange(View view, boolean hasFocus)
			{
				selectedViews.clear();
				
				selectedViews.add(view);
			}
		});
		
		activityLayout.addView(currentAddedListItem, listItems.size());
		
		listItems.add(currentAddedListItem);
		
		lastAddedListItem = currentAddedListItem;
	}

	private void updateActivityLayout() 
	{
		addAddItemButton();
		
		setContentView(scrollView);
	}

	private void addAddItemButton() 
	{
		Button addNewListItem = (Button)findViewById(MiNoteConstants.ADD_BUTTON_ID);
		
		activityLayout.removeView(addNewListItem);
		
		addNewListItem = new Button(this);
		
		addNewListItem.setText("+");
		
		addNewListItem.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View view)
			{
				addNewListItem();
				
				updateActivityLayout();
			}
		});
		
		addNewListItem.setId(MiNoteConstants.ADD_BUTTON_ID);
		
		RelativeLayout.LayoutParams layoutParamsForSaveButton = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		layoutParamsForSaveButton.addRule(RelativeLayout.BELOW, lastAddedListItem.getId());
		
		addNewListItem.setLayoutParams(layoutParamsForSaveButton);
		
		activityLayout.addView(addNewListItem, listItems.size());
	}

	private void saveListOfItems()
	{
		List<NoteItem> noteItems = new ArrayList<NoteItem>();
		
		String title = titleEditText.getText().toString();
		
		if(title != null && !title.equals(""))
		{
			NoteItem item = new NoteItem(title);
			
			item.setTextColour(titleEditText.getCurrentTextColor());

			noteItems.add(item);
			
			item.setIsTitle(true);
		}
		
		for(int i = 0, size = listItems.size(); i < size; i++)
		{
			ListViewItemLayout compoundControl = listItems.get(i);
			
			String imageName = compoundControl.getImageName();
			
			boolean isValidImageName =  imageName != null && imageName.length() > 0 && !imageName.equals("");

			String listItemData = compoundControl.getText();
			
			boolean isValidListText =  listItemData != null && listItemData.length() > 0 && !listItemData.equals("");

			if(isValidImageName || isValidListText)
			{
	            NoteItem listItem = new NoteItem(listItemData, imageName);

	            listItem.setTextColour(compoundControl.getEditText().getCurrentTextColor());
	            
	            noteItems.add(listItem);
			}
		}

		TextNote note = null;
		
		if(noteItems.size() > 0)
		{
			int noteId = modifiedNoteId == -1 ? NotesManager.getInstance().getNextFreeNoteId() : modifiedNoteId;
			
			note = new TextNote(noteId, Note.LIST_NOTE, noteItems);
			
			long creationTime = System.currentTimeMillis();
			
			note.setCreationTime(creationTime);
			
			long lastEditedTime = modifiedNoteId == -1 ? creationTime : System.currentTimeMillis();
			
			note.setLastEditedTime(lastEditedTime);
			
				if(modifiedNoteId != -1)
				{
					NotesManager.getInstance().deleteNotes(Arrays.asList(modifiedNoteId));
					
					MiNoteParameters.getNotesActivity().deleteNotes(Arrays.asList(modifiedNoteId));
					
					NotesDBManager.getInstance().deleteNotes(Arrays.asList(modifiedNoteId));
				}
				
				persistListElement(Arrays.asList(note));
			
			modifiedNoteId = -1;
		}


//        new EventStoreAsyncTask(this).execute();

		Intent returnIntent = new Intent();

		returnIntent.putExtra(MiNoteConstants.NOTE_DATA, note);

		setResult(RESULT_OK,returnIntent);
		
		finish();
	}
	
	@Override
	public void onBackPressed() 
	{
	    saveListOfItems();

	    super.onBackPressed();
	}
	
	private void persistListElement(List<TextNote> notes)
	{
		NotesDBManager.getInstance().saveNotes(notes);
	}
	
	@Override
	public void changeColour(int colourCode) 
	{
        EditText selectedView = (EditText)selectedViews.get(0);
		
		if(selectedView != null)
		{
			MiNoteUtil.setCursorColor(selectedView, colourCode);
				
			selectedView.setTextColor(colourCode);
		}
	}

//    //TODO to be removed.
//    class EventStoreAsyncTask extends AsyncTask<Void, Void, String> {
//
//        private GoogleCloudMessaging gcm;
//        private Context context;
//
//        // TODO: change to your own sender ID to Google Developers Console project number, as per instructions above
//        private static final String SENDER_ID = "700276642861";
//
//        public EventStoreAsyncTask(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        protected String doInBackground(Void... params)
//        {
//
//                EventEntityApi.Builder builder = new EventEntityApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
//                        .setRootUrl("https://minote-997.appspot.com/_ah/api/");
//                // end of optional local run code
//
//                EventEntity eventEntity = new EventEntity();
//
//			eventEntity.setEventName("First");
//
//			eventEntity.setLocation("First");
//
//			eventEntity.setStartDate("18/7/2015");
//
//			eventEntity.setStartTime("6:00");
//
//			eventEntity.setEndDate("18/7/2015");
//
//			eventEntity.setEndTime("7:00");
//
//			EventEntityApi eventApi = builder.build();
//
//                try
//                {
//                    System.out.println("inserting events");
//
//                    EventEntityApi.Insert insertEventEntity = eventApi.insert(eventEntity);
//
//                    insertEventEntity.execute();
//
//                    System.out.println("inserting events1");
//                }
//                catch(IOException exception)
//                {
//                    System.out.println("IoException ="+exception.getStackTrace());
//                }
//
//
//
//            return "Success";
//        }
//
//        @Override
//        protected void onPostExecute(String msg) {
//            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
//            Logger.getLogger("REGISTRATION").log(Level.INFO, msg);
//        }
//    }
}
