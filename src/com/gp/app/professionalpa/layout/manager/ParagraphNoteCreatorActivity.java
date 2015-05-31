package com.gp.app.professionalpa.layout.manager;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.data.NoteListItem;
import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.notes.database.NotesDBManager;
import com.gp.app.professionalpa.notes.fragments.NotesManager;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class ParagraphNoteCreatorActivity extends Activity
{
	private NoteListItem listItem = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		listItem = new NoteListItem();
		
		RelativeLayout activityLayout = (RelativeLayout)getLayoutInflater().inflate(R.layout.paragraph_note_creator_activtiy, null);
		
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
			
			saveParagraph();
		}
		
		if (id == R.id.action_click_photo) 
		{
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
            
            startActivityForResult(cameraIntent, ProfessionalPAConstants.TAKE_PHOTO_CODE);
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
		ProfessionalPANote note = new ProfessionalPANote(NotesManager.getInstance().getNextFreeNoteId(), ProfessionalPAConstants.PARAGRAPH_NOTE, Arrays.asList(listItem));
			
        long creationTime = System.currentTimeMillis();
		
		note.setCreationTime(creationTime);
		
		note.setLastEditedTime(creationTime);
		
        List<ProfessionalPANote> notes = new ArrayList<ProfessionalPANote>();
		
		notes.add(note);
		
		Intent returnIntent = new Intent();
		
		returnIntent.putExtra(ProfessionalPAConstants.NOTE_DATA, note);
		
		setResult(RESULT_OK,returnIntent);
		
		try
		{
			persistListElement(notes);
		} 
		catch (ProfessionalPABaseException exception) 
		{
			// TODO improve
		}
		
		finish();
	}
	
	
	private void persistListElement(List<ProfessionalPANote> notes) throws ProfessionalPABaseException
	{
		NotesDBManager.getInstance().saveNotes(notes);
	}
}
