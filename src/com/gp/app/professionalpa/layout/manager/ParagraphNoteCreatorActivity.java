package com.gp.app.professionalpa.layout.manager;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.colorpicker.ColorPickerCreator;
import com.gp.app.professionalpa.colorpicker.ColourPickerAdapter;
import com.gp.app.professionalpa.colorpicker.ColourPickerChangeListener;
import com.gp.app.professionalpa.colorpicker.ColourProperties;
import com.gp.app.professionalpa.data.NoteListItem;
import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.notes.database.NotesDBManager;
import com.gp.app.professionalpa.notes.fragments.NotesManager;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;
import com.gp.app.professionalpa.util.ProfessionalPAUtil;

public class ParagraphNoteCreatorActivity extends Activity implements ColourPickerChangeListener
{
	private NoteListItem listItem = null;
	
	private int modifiedNoteId = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		RelativeLayout activityLayout = (RelativeLayout)getLayoutInflater().inflate(R.layout.paragraph_note_creator_activtiy, null);

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
			    	ProfessionalPANote professionalPANote = NotesManager.getInstance().getNote(noteId);
			    	
			    	EditText editText = (EditText)activityLayout.findViewById(R.id.paragraphNote);
			    	
			    	editText.setText(professionalPANote.getNoteItems().get(0).getTextViewData());
			    	
                    ImageView imageView = (ImageView)activityLayout.findViewById(R.id.paragraphNoteImportanceView);
			    	
                    imageView.setImageBitmap(ImageLocationPathManager.getInstance().getImage(professionalPANote.getNoteItems().get(0).getImageName(), true));
				}
			}
		}

		listItem = new NoteListItem();
		
		setContentView(activityLayout);
		
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
			EditText paragraphEditText = (EditText)findViewById(R.id.paragraphNote);
			
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
		ProfessionalPANote note = null;
		
		if(modifiedNoteId != -1)
		{
			NotesManager.getInstance().deleteNote(modifiedNoteId);
			
			ProfessionalPAParameters.getNotesActivity().deleteNote(modifiedNoteId);
			
			NotesDBManager.getInstance().deleteNote(modifiedNoteId);
			
			note = new ProfessionalPANote(modifiedNoteId, ProfessionalPAConstants.PARAGRAPH_NOTE, Arrays.asList(listItem));
		}
		else
		{
			note = new ProfessionalPANote(NotesManager.getInstance().getNextFreeNoteId(), ProfessionalPAConstants.PARAGRAPH_NOTE, Arrays.asList(listItem));
		}
			
        long creationTime = System.currentTimeMillis();
		
		note.setCreationTime(creationTime);
		
		note.setLastEditedTime(creationTime);
		
        List<ProfessionalPANote> notes = new ArrayList<ProfessionalPANote>();
		
		notes.add(note);
		
		Intent returnIntent = new Intent();
		
		returnIntent.putExtra(ProfessionalPAConstants.NOTE_DATA, note);
		
		setResult(RESULT_OK,returnIntent);
		
		persistListElement(notes);
		
		finish();
	}
	
	
	private void persistListElement(List<ProfessionalPANote> notes)
	{
		NotesDBManager.getInstance().saveNotes(notes);
	}

	@Override
	public void changeColour(int colourCode) 
	{
		EditText selectedView = (EditText)findViewById(R.id.paragraphNote);

		ProfessionalPAUtil.setCursorColor(selectedView, colourCode);
		
		selectedView.setTextColor(colourCode);
	}
}
