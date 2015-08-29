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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.gp.app.minote.R;
import com.gp.app.minote.backend.entities.eventEntityApi.EventEntityApi;
import com.gp.app.minote.backend.entities.eventEntityApi.model.EventEntity;
import com.gp.app.minote.backend.messaging.Messaging;
import com.gp.app.minote.colorpicker.ColorPickerCreator;
import com.gp.app.minote.colorpicker.ColourPickerChangeListener;
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

public class ParagraphNoteCreatorActivity extends Activity implements ColourPickerChangeListener
{
	private NoteItem listItem = null;
	
	private int modifiedNoteId = -1;

	private EditText titleEditText = null;
	
	private EditText paragraphEditText = null;
	
	private List<View> selectedViews = new ArrayList<View>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		RelativeLayout activityLayout = (RelativeLayout)getLayoutInflater().inflate(R.layout.paragraph_note_creator_activtiy, null);

		setContentView(activityLayout);

		titleEditText = (EditText)activityLayout.findViewById(R.id.paragraphTitle);

		paragraphEditText = (EditText)activityLayout.findViewById(R.id.paragraphNote);

		paragraphEditText.setOnFocusChangeListener(new OnFocusChangeListener() 
		{
			@Override
			public void onFocusChange(View view, boolean hasFocus)
			{
				resetSelectedView(view);
			}
		});
		
		titleEditText.setOnFocusChangeListener(new OnFocusChangeListener() 
		{
			@Override
			public void onFocusChange(View view, boolean hasFocus)
			{
				resetSelectedView(view);
			}
		});
		
		listItem = new NoteItem();

        Intent intent = getIntent();
		
		if(intent != null)
		{
			Bundle extras = intent.getExtras();
			
			if(extras != null)
			{
				int noteId = extras.getInt(MiNoteConstants.NOTE_ID);
				
				modifiedNoteId = noteId;
				
				if(noteId != 0)
				{
			    	Note note = NotesManager.getInstance().getNote(noteId);
			    	
			    	if(note != null && note.getType() != Note.EVENT_NOTE)
			    	{
			    		TextNote textNote = (TextNote)note;
			    		
			    		List<NoteItem> items = textNote.getNoteItems();
				    	
				    	for(int i = 0; i < items.size(); i++)
				    	{
				    		NoteItem item = items.get(i);
				    		
				    		if(item.isTitle())
				    		{
				    			titleEditText.setText(item.getText());
						    	
						    	titleEditText.setTextColor(item.getTextColour());
				    		}
				    		else
				    		{
						    	paragraphEditText.setText(item.getText());
						    	
						    	paragraphEditText.setTextColor(item.getTextColour());

			                    ImageView imageView = (ImageView)activityLayout.findViewById(R.id.paragraphNoteImportanceView);
						    	
			                    imageView.setImageBitmap(ImageLocationPathManager.getInstance().getImage(item.getImageName(), true));
			                    
			                    listItem.setImageName(item.getImageName());
				    		}
				    	} 
			    	}
				}
			}
		}

        ActionBar actionBar = getActionBar();
		
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.rgb(120, 100, 255)));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.paragraph_notes_activity_menu_items, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		int id = item.getItemId();
		
		if (id == R.id.action_save_paragraph_note) 
		{
			String paragraphData = paragraphEditText.getText().toString();
			 
			listItem.setTextViewData(paragraphData);
			
            listItem.setTextColour(paragraphEditText.getCurrentTextColor());
			
			saveParagraph();
		}
		
		if (id == R.id.action_click_photo) 
		{
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
            
            startActivityForResult(cameraIntent, MiNoteConstants.TAKE_PHOTO_CODE);
		}
		else if(id == R.id.pickColor)
		{
			new ColorPickerCreator(this, this).createColourPicker();
		}
		
		return super.onOptionsItemSelected(item);
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
	    
	        ImageView imageView = (ImageView)findViewById(R.id.paragraphNoteImportanceView);
	        
	        imageView.setImageBitmap(image);
	        
	    	listItem.setImageName(imageName);
	    }
	}
	
	private void saveParagraph()
	{
		if(modifiedNoteId != -1)
		{
			NotesManager.getInstance().deleteNotes(Arrays.asList(modifiedNoteId));
			
			MiNoteParameters.getNotesActivity().deleteNotes(Arrays.asList(modifiedNoteId));
			
			NotesDBManager.getInstance().deleteNotes(Arrays.asList(modifiedNoteId));
		}

		String title = titleEditText.getText().toString();
		
		NoteItem titleItem = null;
		
		if(title != null && !title.trim().equals(""))
		{
			titleItem = new NoteItem(title);
			
			titleItem.setIsTitle(true);
			
			titleItem.setTextColour(titleEditText.getCurrentTextColor());
		}
		
		String imageName = listItem.getImageName();
		
		boolean isValidImageName =  imageName != null && imageName.length() > 0 && !imageName.trim().equals("");

		String listItemData = listItem.getText();
		
		boolean isValidListText =  listItemData != null && listItemData.length() > 0 && !listItemData.trim().equals("");

		TextNote note = null;

		if(isValidImageName || isValidListText)
		{
			int noteId = modifiedNoteId != -1 ? modifiedNoteId : NotesManager.getInstance().getNextFreeNoteId();
			
			 List<NoteItem> items = titleItem != null ? Arrays.asList(titleItem, listItem) : Arrays.asList(listItem);
		    
			note = new TextNote(noteId, Note.PARAGRAPH_NOTE, items);

	        long creationTime = System.currentTimeMillis();
			
			note.setCreationTime(creationTime);
			
			note.setLastEditedTime(creationTime);
			
			persistListElement(Arrays.asList(note));
		}

		Intent returnIntent = new Intent();
		
		returnIntent.putExtra(MiNoteConstants.NOTE_DATA, note);
		
		setResult(RESULT_OK,returnIntent);
		
		finish();
	}
	
	
	private void persistListElement(List<TextNote> notes)
	{
		NotesDBManager.getInstance().saveNotes(notes);
	}

	@Override
	public void changeColour(int colourCode) 
	{
        EditText selectedView1 = (EditText)selectedViews.get(0);
		
		if(selectedViews != null)
		{
			MiNoteUtil.setCursorColor(selectedView1, colourCode);
				
			selectedView1.setTextColor(colourCode);
		}
	}
	
	private void resetSelectedView(View view) 
	{
		selectedViews.clear();
		
		selectedViews.add(view);
	}
}
