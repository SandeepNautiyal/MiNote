package com.gp.app.professionalpa.layout.manager;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.compositecontrols.ListViewItemLayout;
import com.gp.app.professionalpa.data.NotesListItem;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;

public class ParagraphNoteCreatorActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        RelativeLayout activityLayout = (RelativeLayout)getLayoutInflater().inflate(R.layout.paragraph_note_creator_activtiy, null);
		
		setContentView(activityLayout);
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
			
			System.out.println("onOptionsItemSelected -> paragraphData="+paragraphData);
			saveParagraph(paragraphData);
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void saveParagraph(String paragraphData)
	{
		NotesListItem [] listViewItems = new NotesListItem[1];

		NotesListItem listItem = new NotesListItem(paragraphData);
			
		System.out.println("saveParagraph -> paragraphData="+paragraphData);
		
		listViewItems[0] = listItem;
		
		Intent returnIntent = new Intent();
		
		returnIntent.putExtra("LIST_ITEMS", listViewItems);
		
		returnIntent.putExtra(ProfessionalPAConstants.IS_PARAGRAPH_NOTE, true);
		
		setResult(RESULT_OK,returnIntent);
		
		finish();
	}
}
