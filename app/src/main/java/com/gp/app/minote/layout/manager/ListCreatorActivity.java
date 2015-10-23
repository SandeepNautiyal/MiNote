package com.gp.app.minote.layout.manager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.gp.app.minote.R;
import com.gp.app.minote.colorpicker.ColorPickerCreator;
import com.gp.app.minote.colorpicker.ColourPickerChangeListener;
import com.gp.app.minote.compositecontrols.ListViewItemLayout;
import com.gp.app.minote.data.Note;
import com.gp.app.minote.data.NoteItem;
import com.gp.app.minote.data.TextNote;
import com.gp.app.minote.util.MiNoteConstants;
import com.gp.app.minote.notes.database.NotesDBManager;
import com.gp.app.minote.notes.fragments.NotesManager;
import com.gp.app.minote.notes.images.ImageLocationPathManager;
import com.gp.app.minote.util.MiNoteParameters;
import com.gp.app.minote.util.MiNoteUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ListCreatorActivity extends Activity implements ColourPickerChangeListener
{
	private List<ListViewItemLayout> listItems = new LinkedList<ListViewItemLayout>();
			
	private ListViewItemLayout lastAddedListItem = null;
	
	private ScrollView scrollView = null;
	
	private RelativeLayout activityLayout = null;

	private boolean isAddButtonToBeCreated = true;

	private boolean isLastItemToBeAddedInLayout = true;
	
	private int modifiedNoteId = -1;
	
	private List<View> selectedViews = new ArrayList<View>(1);

	private EditText titleEditText;

//    private RelativeLayout parentLayout = null;

    private FrameLayout listMenuOptionsFrameLayout = null;

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
			
			if(isAddButtonToBeCreated)
			{
				addAddItemButton();
			}
			
			isLastItemToBeAddedInLayout = true;

			isAddButtonToBeCreated = true;
			
			setContentView(scrollView);
		}
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
					    		
					    		isAddButtonToBeCreated = false;
				    		}
				    		else
				    		{
				    			List<NoteItem> listItems = professionalPANote.getNoteItems();
				    			
				    			for(int i = 0; i < listItems.size(); i++)
				    			{
				    				NoteItem item = listItems.get(i);

                                    createListItem(item);
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

    private void createListItem(NoteItem item) {
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

    @Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
        getMenuInflater().inflate(R.menu.list_creator_activity_menu_items, menu);
        
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		//TODO image deletion to be checked
	    if (requestCode == MiNoteConstants.TAKE_PHOTO_CODE && resultCode == RESULT_OK)
	    {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");

//	    	ImageLocationPathManager.getInstance().createAndSaveImage(photo);

	    	String imagePath = ImageLocationPathManager.getInstance().getMostRecentImageFilePath();

			ImageLocationPathManager.getInstance().compressImage(imagePath);

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
			String filename = ImageLocationPathManager.getInstance().getImagePath();

			Uri imageUri = Uri.fromFile(new File(filename));

			Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

			cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					imageUri);
            
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

        ImageView deleteButton = currentAddedListItem.getDeleteImageView();
		
		if(deleteButton != null)
		{
			deleteButton.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
                    if(listItems.size() > 1)
                    {
                        int indexOfView = activityLayout.indexOfChild(currentAddedListItem);

                        if(indexOfView != -1)
                        {
                            currentAddedListItem.setVisibility(View.GONE);
                        }

                        listItems.remove(currentAddedListItem);
                    }
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
		ImageView addNewListItem = (ImageView)findViewById(R.id.list_item_add_button);
		
		activityLayout.removeView(addNewListItem);
		
		addNewListItem = new ImageView(this);
		
		addNewListItem.setImageDrawable(getResources().getDrawable(R.drawable.minote_list_addition));

		addNewListItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewListItem();

                updateActivityLayout();
            }
        });
		
		addNewListItem.setId(R.id.list_item_add_button);
		
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

		setResult(RESULT_OK, returnIntent);
		
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
}
