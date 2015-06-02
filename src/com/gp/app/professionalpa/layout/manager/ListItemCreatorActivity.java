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
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.colorpicker.ColourPickerAdapter;
import com.gp.app.professionalpa.colorpicker.ColourPickerChangeListener;
import com.gp.app.professionalpa.colorpicker.ColourProperties;
import com.gp.app.professionalpa.compositecontrols.ListViewItemLayout;
import com.gp.app.professionalpa.data.NoteListItem;
import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.notes.database.NotesDBManager;
import com.gp.app.professionalpa.notes.fragments.NotesManager;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class ListItemCreatorActivity extends Activity implements ColourPickerChangeListener
{
	private List<ListViewItemLayout> listItems = new ArrayList<ListViewItemLayout>();
			
	private ListViewItemLayout lastAddedListItem = null;
	
	private ScrollView scrollView = null;
	
	private RelativeLayout activityLayout = null;

	private boolean isSaveButtonToBeCreated = true;

	private boolean isLastItemToBeAddedInLayout = true;
	
	private int modifiedNoteId = -1;
	
	private List<View> selectedViewId = new ArrayList<View>(1);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		if(lastAddedListItem == null)
		{
			LayoutInflater inflater = getLayoutInflater();

			scrollView = (ScrollView)inflater.inflate(R.layout.list_item_creator_activity, null);
			
			activityLayout = (RelativeLayout)scrollView.findViewById(R.id.list_item_creator_activity_layout);
			
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
		
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7F7CD9")));
	}

	private void editNotes()
	{
		Intent intent = getIntent();
		
		if(intent != null)
		{
			Bundle extras = intent.getExtras();
			
			if(extras != null)
			{
				int noteId = extras.getInt(ProfessionalPAConstants.NOTE_ID);
				
				if(noteId != 0)
				{
			    	ProfessionalPANote professionalPANote = NotesManager.getInstance().getNote(noteId);

			    	if(professionalPANote != null)
			    	{
			    		if(professionalPANote.getNoteType() == ProfessionalPAConstants.IMAGE_NOTE)
			    		{
			    			createActivityLayoutForNote(professionalPANote);
				    		
				    		isSaveButtonToBeCreated  = false;
			    		}
			    		else
			    		{
			    			List<NoteListItem> listItems = professionalPANote.getNoteItems();
			    			
			    			for(int i = 0; i < listItems.size(); i++)
			    			{
			    				NoteListItem item = listItems.get(i);
			    				
			    				if(item != null)
			    				{
			    					String textViewData = item.getTextViewData();
			    					
			    					String imageName = item.getImageName();
			    					
			    					if(textViewData != null && !textViewData.equals(""))
			    					{
			    						addNewListItem();
			    						
			    						lastAddedListItem.setText(textViewData);
			    					}
			    					
			    					if(imageName != null && !imageName.equals(""))
			    					{
	                                    addNewListItem();
			    						
			    						lastAddedListItem.setImage(imageName, ImageLocationPathManager.getInstance().getImage(imageName, true), true);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
        getMenuInflater().inflate(R.menu.list_creator_activity_menu_items, menu);
        
		return true;
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
	
	private void createActivityLayoutForNote(ProfessionalPANote professionalPANote) 
	{
		if(professionalPANote.getNoteType() == ProfessionalPAConstants.IMAGE_NOTE)
		{
			NoteListItem item = professionalPANote.getNoteItems().get(0);

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
            
            startActivityForResult(cameraIntent, ProfessionalPAConstants.TAKE_PHOTO_CODE);
		}
		else if(id == R.id.saveListNote)
		{
			saveListOfItems();
		}
		else if(id == R.id.pickColor)
		{
			createColourPicker();
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
				selectedViewId.clear();
				
				selectedViewId.add(view);
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
		Button addNewListItem = (Button)findViewById(ProfessionalPAConstants.ADD_BUTTON_ID);
		
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
		
		addNewListItem.setId(ProfessionalPAConstants.ADD_BUTTON_ID);
		
		RelativeLayout.LayoutParams layoutParamsForSaveButton = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		layoutParamsForSaveButton.addRule(RelativeLayout.BELOW, lastAddedListItem.getId());
		
		addNewListItem.setLayoutParams(layoutParamsForSaveButton);
		
		activityLayout.addView(addNewListItem, listItems.size());
	}

	private void saveListOfItems()
	{
		List<NoteListItem> noteItems = new ArrayList<NoteListItem>();
		
		for(int i = 0, size = listItems.size(); i < size; i++)
		{
			ListViewItemLayout compoundControl = listItems.get(i);
			
			String imageName = compoundControl.getImageName();
			
			boolean isValidImageName =  imageName != null && imageName.length() > 0 && !imageName.equals("");

			String listItemData = compoundControl.getText();
			
			boolean isValidListText =  listItemData != null && listItemData.length() > 0 && !listItemData.equals("");

			if(isValidImageName || isValidListText)
			{
	            NoteListItem listItem = new NoteListItem(compoundControl.getText(), compoundControl.getImageName());

	            listItem.setTextColour(compoundControl.getEditText().getCurrentTextColor());
	            
	            noteItems.add(listItem);
			}
		}

		int noteId = modifiedNoteId == -1 ? NotesManager.getInstance().getNextFreeNoteId() : modifiedNoteId;
		
		ProfessionalPANote note = new ProfessionalPANote(noteId, ProfessionalPAConstants.LIST_NOTE, noteItems);
		
		long creationTime = System.currentTimeMillis();
		
		note.setCreationTime(creationTime);
		
		long lastEditedTime = modifiedNoteId == -1 ? creationTime : System.currentTimeMillis();
		
		note.setLastEditedTime(lastEditedTime);
		
		try
		{
			if(modifiedNoteId != -1)
			{
				NotesManager.getInstance().deleteNote(modifiedNoteId);
				
				ProfessionalPAParameters.getNotesActivity().deleteNote(modifiedNoteId);
			}
			
			persistListElement(Arrays.asList(note));
		} 
		catch (ProfessionalPABaseException exception) 
		{
			// TODO improve
		}
		
		modifiedNoteId = -1;
		
		Intent returnIntent = new Intent();

		returnIntent.putExtra(ProfessionalPAConstants.NOTE_DATA, note);

		setResult(RESULT_OK,returnIntent);
		
		finish();
	}
	
	@Override
	public void onBackPressed() 
	{
	    saveListOfItems();

	    super.onBackPressed();
	}
	
	private void persistListElement(List<ProfessionalPANote> notes) throws ProfessionalPABaseException
	{
		NotesDBManager.getInstance().saveNotes(notes);
	}
	
	private void createColourPicker()
	{
		ArrayList<ColourProperties> gridArray = new ArrayList<ColourProperties>();
		
		gridArray.add(ColourProperties.RED);
		
		gridArray.add(ColourProperties.GREEN);
	    
		gridArray.add(ColourProperties.BLUE);
	    
		gridArray.add(ColourProperties.YELLOW);
	    
		gridArray.add(ColourProperties.GRAY);
	    
		gridArray.add(ColourProperties.WHITE);
	    
		gridArray.add(ColourProperties.CYAN);
	    
		gridArray.add(ColourProperties.MAGENTA);
	    
		gridArray.add(ColourProperties.DARK_GRAY);
	    
		gridArray.add(ColourProperties.PINK);
		
		GridView gridView = new GridView(this); 
		
		gridView.setBackgroundColor(Color.parseColor("#7F7CD9"));
		
		gridView.setNumColumns(5);
		
		Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		gridView.setAdapter(new ColourPickerAdapter(this, gridArray, dialog));
		
        dialog.setContentView(gridView);
        dialog.setCancelable(true);
        dialog.show();

	}

	private void setCursorDrawableColor(EditText editText, int color) 
	{
	    try {
	    	
	    	//TODO to be checked and improved.
//	    	TODO Below commented 3 lines will also work but the colour of cursor will not be changed.
//	    	Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
//	        f.setAccessible(true);
//	        f.set(yourEditText, R.drawable.cursor);
	        
	        final Field fCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
	        fCursorDrawableRes.setAccessible(true);
	        final int mCursorDrawableRes = fCursorDrawableRes.getInt(editText);
	        final Field fEditor = TextView.class.getDeclaredField("mEditor");
	        fEditor.setAccessible(true);
	        final Object editor = fEditor.get(editText);
	        final Class<?> clazz = editor.getClass();
	        final Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
	        fCursorDrawable.setAccessible(true);
	        final Drawable[] drawables = new Drawable[2];
	        drawables[0] = editText.getContext().getResources().getDrawable(mCursorDrawableRes);
	        drawables[1] = editText.getContext().getResources().getDrawable(mCursorDrawableRes);
	        drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
	        drawables[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
	        fCursorDrawable.set(editor, drawables);
	    } 
	    catch (final Throwable ignored) 
	    {
	    	//TODO improve
	    	ignored.printStackTrace();
	    }
	}

	@Override
	public void changeColour(int colourCode) 
	{
        EditText selectedView = (EditText)selectedViewId.get(0);
		
		if(selectedView != null)
		{
//			TODO not needed right now as selection of subtext not allowed for edittext 

//			int startIndex = selectedView.getSelectionStart();
//			
//			int endIndex = selectedView.getSelectionEnd();
//			
//			String selectedViewText = selectedView.getText().toString();
//			
//			String selectedText = selectedViewText.substring(startIndex, endIndex);
//			
//			if(selectedText == null || selectedText.equals(""))
//			{
				setCursorDrawableColor(selectedView, colourCode);
				
				selectedView.setTextColor(colourCode);

//			}
			
//			TODO not needed right now as selection of subtext not allowed for edittext 
//			else
//			{
//				SpannableStringBuilder sb = new SpannableStringBuilder(selectedViewText);
//
//				// Span to set text color to some RGB value
//				ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(255, 0, 0)); 
//
//				// Set the text color for first 4 characters
//				sb.setSpan(fcs, startIndex, endIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE); 
//
//				selectedView.setText(sb);
//			}
		}
	}
}
