package com.gp.app.professionalpa.notes.xml;

import java.util.LinkedList;
import java.util.List;

import com.gp.app.professionalpa.data.NotesListItem;

public class ParsedNote 
{
	boolean isParagraphNote = false;
	
	LinkedList<NotesListItem> notes = null;

	ParsedNote()
	{
		notes = new LinkedList<NotesListItem>();
	}
	
	public boolean isParagraphNote() 
	{
		return isParagraphNote;
	}

	public void setTypeOfNote(boolean isParagraphNote) 
	{
		this.isParagraphNote = isParagraphNote;
	}

	public void addNoteItem(NotesListItem item)
	{
		notes.add(item);
	}
	
	public List<NotesListItem> getNoteItems()
	{
		return notes;
	}
	
	public NotesListItem getMostRecentAddedNoteItem()
	{
		return notes.getLast();
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("isParagraph note:"+isParagraphNote);
		
		sb.append("Note item :"+notes);
		
		return sb.toString();
	}
}
