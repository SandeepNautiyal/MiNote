package com.gp.app.professionalpa.notes.operations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.Window;
import android.widget.GridView;

import com.gp.app.professionalpa.calendar.events.database.CalendarDBManager;
import com.gp.app.professionalpa.colorpicker.ColourPickerAdapter;
import com.gp.app.professionalpa.colorpicker.ColourProperties;
import com.gp.app.professionalpa.data.Event;
import com.gp.app.professionalpa.data.Note;
import com.gp.app.professionalpa.data.NoteItem;
import com.gp.app.professionalpa.data.TextNote;
import com.gp.app.professionalpa.notes.database.NotesDBManager;
import com.gp.app.professionalpa.notes.fragments.NotesManager;
import com.gp.app.professionalpa.notes.images.ImageLocationPathManager;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class NotesOperationManager 
{
	private static NoteCopyManager notesCopyManager = null;
	
	private static boolean isCopyInProgress = false;
	
	private static NotesOperationManager manager = null;

	private List<Integer> selectedNoteIds = new ArrayList<Integer>();

	public void deleteSelectedNotes()
    {
		for(int i = 0; i < selectedNoteIds.size(); i++)
		{
			int selectNoteId = selectedNoteIds.get(i);

			Note note = NotesManager.getInstance().getNote(selectNoteId);
			
			if(note != null)
			{
				if(note.getType() != Note.EVENT_NOTE)
				{
					TextNote textNote = (TextNote)note;
					
					List<NoteItem> items = textNote.getNoteItems();
					
					for(int j = 0, size = items.size(); j < size; j++)
					{
						NoteItem item = textNote.getNoteItems().get(i);
						
						ImageLocationPathManager.getInstance().deleteImage(item.getImageName());
					}
					
			        NotesDBManager.getInstance().deleteNotes(Arrays.asList(selectNoteId));
				}
				else
				{
					CalendarDBManager.getInstance().deleteEvent(selectNoteId);
				}
			}
		}
		
		NotesManager.getInstance().deleteNotes(selectedNoteIds);

		ProfessionalPAParameters.getNotesActivity().deleteNotes(selectedNoteIds);
		
		selectedNoteIds.clear();
    }

	public void startCopyProcess() 
	{
		notesCopyManager = new NoteCopyManager();
		
		isCopyInProgress = true;
	}

	public void copyNote() 
	{
		if(isCopyInProgress)
		{
			notesCopyManager.copyNote(selectedNoteIds);
			
			deSelectSelectedNotes();
			
			isCopyInProgress = false;
		}
	}

	private void deSelectSelectedNotes() 
	{
		Iterator<Integer> iterator = selectedNoteIds.iterator();
		
		while(iterator.hasNext())
		{
            ProfessionalPAParameters.getNotesActivity().deSelectNote(iterator.next());
            
            iterator.remove();
		}
	}

	public void createColourPicker()
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
		
		Activity noteActivity = ProfessionalPAParameters.getNotesActivity();
		
		GridView gridView = new GridView(noteActivity); 
		
		gridView.setBackgroundColor(Color.parseColor("#7F7CD9"));
		
		gridView.setNumColumns(5);
		
		//TODO to be checked and removed.
		Dialog dialog = new Dialog(noteActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		ColourPickerAdapter adapter = new ColourPickerAdapter(noteActivity, gridArray, dialog);
		
		gridView.setAdapter(adapter);
		
		 //before     
        dialog.setContentView(gridView);
        dialog.setCancelable(true);
        dialog.show();
	}

	public static NotesOperationManager getInstance() 
	{
		if(manager == null)
		{
			manager = new NotesOperationManager();
		}
		
		return manager;
	}

	public void addSelectedNote(int noteId) 
	{
		selectedNoteIds.add(noteId);
		
		Note note = NotesManager.getInstance().getNote(noteId);
		
		ProfessionalPAParameters.getNotesActivity().setNoteSelected(noteId);
	}
	
	public boolean isNoteSelected()
	{
		return selectedNoteIds.size() > 0;
	}
	
	public List<Integer> getSelectedNoteIds()
	{
		return selectedNoteIds;
	}
	
	public void clearSelectedNotes()
	{
		 selectedNoteIds.clear();
	}

	public void editSelectedNote() 
	{
		ProfessionalPAParameters.getNotesActivity().openNoteInEditMode(selectedNoteIds.get(0));
	}

	public void deSelectNote(int noteId) 
	{   
		if(selectedNoteIds.contains(noteId))
		{
			ProfessionalPAParameters.getNotesActivity().deSelectNote(noteId);
			
			selectedNoteIds.remove((Integer)noteId);
		}
	}

	public void selectNote(int noteId) 
	{
		addSelectedNote(noteId);
	}

	public void createEventNote(Event event) 
	{
		ProfessionalPAParameters.getNotesActivity().createFragmentForNote(event);
	}

	public void shareSelectedNote()
	{
		for(int i = 0; i < selectedNoteIds.size(); i++)
		{
			Note note = NotesManager.getInstance().getNote(selectedNoteIds.get(i));
			
			if(note != null)
			{
				StringBuilder noteText = new StringBuilder();
				
				Uri imageUri = null;
				
				if(note.getType() == Note.EVENT_NOTE)
				{
					Event event = (Event)note;
					
					noteText.append("Event"+"\n");
					
					noteText.append(event.getEventName()+"\n");
					
					noteText.append(event.getLocation()+"\n");

					noteText.append(event.getStartDate()+"  "+event.getStartTime()+"\n");

					noteText.append(event.getEndDate()+"  "+event.getEndTime());
					
					System.out.println("noteText ="+noteText);
				}
				else
				{
					TextNote textNote = (TextNote)note;
					
					List<NoteItem> items = textNote.getNoteItems();
					
					for(int j = 0; j < items.size(); j++)
					{
						NoteItem item = items.get(j);
						
						String imageName = item.getImageName();
						
						if(imageName != null && !imageName.trim().equals(""))
						{
							String path = ImageLocationPathManager.getInstance().getImagePath(imageName);
							
							imageUri = Uri.parse("file://"+path);
						}
						
						String text = item.getText();
						
						if(text != null && !text.trim().equals(""))
						{
							noteText.append(text+"\n");
						}
					}
				}
			   
				Intent shareIntent = new Intent();
			   shareIntent.setAction(Intent.ACTION_SEND);
			   //Target whatsapp:
			   //Add text and then Image URI
			   shareIntent.putExtra(Intent.EXTRA_TEXT, noteText.toString());
			   if(imageUri != null)
			   {
				   shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
				   shareIntent.setType("image/jpeg");
			   }
			   else
			   {
				   shareIntent.setType("text/plain");
			   }
			   
			   shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			   shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			   try 
			   {
			       ProfessionalPAParameters.getApplicationContext().startActivity(shareIntent);
			   } catch (android.content.ActivityNotFoundException ex) {
//			       ToastHelper.MakeShortText("Whatsapp have not been installed.");
			   }
			}
		}
	}
	
	public boolean isSelectedNoteEvent() 
    {
		boolean isEventNoteSelected = false;
		
		for(int i = 0; i < selectedNoteIds.size(); i++)
		{
			Note selectedNote = NotesManager.getInstance().getNote(selectedNoteIds.get(0));
			
			if(selectedNote != null && selectedNote.getType() == Note.EVENT_NOTE)
			{
				isEventNoteSelected = true;
			}
		}
		
		return isEventNoteSelected;
	}
}
