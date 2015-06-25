package com.gp.app.professionalpa.layout.manager;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.colorpicker.ColorPickerCreator;
import com.gp.app.professionalpa.colorpicker.ColourPickerChangeListener;
import com.gp.app.professionalpa.data.Note;
import com.gp.app.professionalpa.data.NoteItem;
import com.gp.app.professionalpa.data.TextNote;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.notes.database.NotesDBManager;
import com.gp.app.professionalpa.notes.fragments.NotesManager;
import com.gp.app.professionalpa.notes.images.ImageLocationPathManager;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;
import com.gp.app.professionalpa.util.ProfessionalPAUtil;

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
		
        Intent intent = getIntent();
		
		if(intent != null)
		{
			Bundle extras = intent.getExtras();
			
			if(extras != null)
			{
				int noteId = extras.getInt(ProfessionalPAConstants.NOTE_ID);
				
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
				    			titleEditText.setText(items.get(0).getText());
						    	
						    	titleEditText.setText(items.get(0).getTextColour());
				    		}
				    		else
				    		{
				    			EditText editText = (EditText)activityLayout.findViewById(R.id.paragraphNote);
						    	
						    	editText.setText(textNote.getNoteItems().get(0).getText());
						    	
			                    ImageView imageView = (ImageView)activityLayout.findViewById(R.id.paragraphNoteImportanceView);
						    	
			                    imageView.setImageBitmap(ImageLocationPathManager.getInstance().getImage(textNote.getNoteItems().get(0).getImageName(), true));
				    		}
				    	} 
			    	}
				}
			}
		}

		listItem = new NoteItem();
		
        ActionBar actionBar = getActionBar();
		
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7F7CD9")));
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
            
            startActivityForResult(cameraIntent, ProfessionalPAConstants.TAKE_PHOTO_CODE);
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
	    if (requestCode == ProfessionalPAConstants.TAKE_PHOTO_CODE && resultCode == RESULT_OK) 
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
			
			ProfessionalPAParameters.getNotesActivity().deleteNotes(Arrays.asList(modifiedNoteId));
			
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
		
		returnIntent.putExtra(ProfessionalPAConstants.NOTE_DATA, note);
		
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
			ProfessionalPAUtil.setCursorColor(selectedView1, colourCode);
				
			selectedView1.setTextColor(colourCode);
		}
	}
	
	private void resetSelectedView(View view) 
	{
		selectedViews.clear();
		
		selectedViews.add(view);
	}
}
