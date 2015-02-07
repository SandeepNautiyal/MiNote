package com.gp.app.professionalpa.notes.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.gp.app.professionalpa.data.NotesListItem;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;

public class ProfessionalPANotesParser extends DefaultHandler
{
	private List<ParsedNote> notes = new ArrayList<ParsedNote>();
	
	private ParsedNote currentNote = null;
	
	private NotesListItem currentNoteItem = null;
	
	private SAXParserFactory factory = null;
	
	private SAXParser saxParser = null;
	
	private boolean note = false;
	private boolean data = false;
	private boolean isAlarm = false;
	private String typeOfList = null;

	private boolean isImportant;
	
	public ProfessionalPANotesParser() throws ProfessionalPABaseException
	{
		factory = SAXParserFactory.newInstance();
		
		try
		{
			saxParser = factory.newSAXParser();
		} 
		catch (ParserConfigurationException exception)
		{
		    throw new ProfessionalPABaseException("UNABLE_TO_READ_PROFESSIOANLPA_NOTES", exception);
		}
		catch (SAXException exception) 
		{
		    throw new ProfessionalPABaseException("UNABLE_TO_READ_PROFESSIOANLPA_NOTES", exception);
		}
	}
 
	public void startElement(String uri, String localName,String qName, 
                Attributes attributes) throws SAXException
    {
		if (qName.equalsIgnoreCase("note"))
		{
			currentNote = new ParsedNote();
			
			//Fetching the ID of TownCenter, we use it as a reference to fetch the child nodes.
			typeOfList = attributes.getValue("type");
			
			if(typeOfList != null)
			{
				if(typeOfList.equalsIgnoreCase("NotesList"))
				{
					currentNote.setTypeOfNote(false);
				}
				else
				{
					currentNote.setTypeOfNote(true);
				}
			}
		}
 
		if(qName.equalsIgnoreCase("NoteItem"))
		{
			currentNoteItem = new NotesListItem();
		}
		if (qName.equalsIgnoreCase("data")) 
		{
			data = true;
		}
 
		if (qName.equalsIgnoreCase("isAlarm")) 
		{
			isAlarm = true;
		}
		
		if (qName.equalsIgnoreCase("isImportant")) 
		{
			isImportant = true;
		}
	}
 
	public void endElement(String uri, String localName, String qName) throws SAXException 
    {
		if (qName.equalsIgnoreCase("note"))
		{
			note = false;
		}
 
		if (qName.equalsIgnoreCase("data")) 
		{
			data = false;
		}
 
		if (qName.equalsIgnoreCase("isAlarm")) 
		{
			isAlarm = false;
		}
		
		if (qName.equalsIgnoreCase("isImportant")) 
		{
			isImportant = false;
		}
		
		if (qName.equalsIgnoreCase("isImportant")) 
		{
			isImportant = false;
		}
		
		if(qName.equalsIgnoreCase("NoteItem"))
		{
			currentNote.addNoteItem(currentNoteItem);
		}
		
		if (qName.equalsIgnoreCase("note"))
		{
			notes.add(currentNote);
		}
 

	}
 
	public void characters(char ch[], int start, int length) throws SAXException 
	{
		String dataList = null;
		
		boolean isAlarmPresent = false;
		
		boolean isImportanct = false;
		
		if (data) 
		{
			dataList = new String(ch, start, length);
			
			currentNoteItem.setTextViewData(dataList);
		}
 
		if (isAlarm) 
		{
			isAlarmPresent = Boolean.valueOf(new String(ch, start, length));
			
			currentNoteItem.setAlarmActive(isAlarmPresent);
		}
		
		if (isImportant) 
		{
			isImportanct = Boolean.valueOf(new String(ch, start, length));
			
			currentNoteItem.setImportanceHigh(isImportanct);
		}
	}
	
	public List<ParsedNote> getNotes()
	{
		return notes;
	}
}
