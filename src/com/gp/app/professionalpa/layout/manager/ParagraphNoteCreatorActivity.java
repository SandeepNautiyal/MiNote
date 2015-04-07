package com.gp.app.professionalpa.layout.manager;


import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.data.NoteListItem;
import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.notes.xml.ProfessionalPANotesWriter;
import com.gp.app.professionalpa.util.ProfessionalPANotesIdGenerator;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class ParagraphNoteCreatorActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
        RelativeLayout activityLayout = (RelativeLayout)getLayoutInflater().inflate(R.layout.paragraph_note_creator_activtiy, null);
		
		setContentView(activityLayout);
		
        ActionBar actionBar = getActionBar();
		
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7F7CD9")));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.paragraph_notes_creator_activity_menu_items, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		int id = item.getItemId();
		
		if (id == R.id.action_save_paragraph_note) 
		{
			EditText paragraphEditText = (EditText)findViewById(R.id.paragraph_note);
			
			String paragraphData = paragraphEditText.getText().toString();
			
			saveParagraph(paragraphData);
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void saveParagraph(String paragraphData)
	{
		List<NoteListItem> item = new ArrayList<NoteListItem>();
		
		item.add(new NoteListItem(paragraphData));
		
		ProfessionalPANote note = new ProfessionalPANote(ProfessionalPANotesIdGenerator.generateNoteId(), ProfessionalPAConstants.PARAGRAPH_NOTE, item);
			
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
//		dummyMethod();
		
		ProfessionalPANotesWriter fragmentWriter = ProfessionalPAParameters.getProfessionalPANotesWriter();
		
		fragmentWriter.writeNotes(notes);
	}
}
