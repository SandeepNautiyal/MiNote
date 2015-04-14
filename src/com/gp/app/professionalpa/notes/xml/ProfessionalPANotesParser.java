package com.gp.app.professionalpa.notes.xml;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.gp.app.professionalpa.data.NoteListItem;
import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.interfaces.XMLEntity;
import com.gp.app.professionalpa.util.ProfessionalPATools;

public class ProfessionalPANotesParser extends DefaultHandler
{
	private List<ProfessionalPANote> notes = new ArrayList<ProfessionalPANote>();
	
	private ProfessionalPANote currentNote = null;
	
	private NoteListItem currentNoteItem = null;
	
	private SAXParserFactory factory = null;
	
	private SAXParser saxParser = null;
	
	private boolean note = false;
	private boolean data = false;
	private boolean imageName = false;
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
			currentNote = new ProfessionalPANote();
			
			//Fetching the ID of TownCenter, we use it as a reference to fetch the child nodes.
			String typeOfNote = attributes.getValue("type");
			
            String CreationTime = attributes.getValue("creationTime");//new Date(attributes.getValue("creationTime")).getTime();
			
			String lastEditedTime = attributes.getValue("lastEditedTime"); //new Date(attributes.getValue("lastEditedTime")).getTime();

			String noteId = attributes.getValue("noteId");
			
			currentNote.setNoteId(Integer.valueOf(noteId));
			
			try 
			{
				currentNote.setCreationTime(ProfessionalPATools.parseDateAndTimeString(CreationTime));

				currentNote.setLastEditedTime(ProfessionalPATools.parseDateAndTimeString(lastEditedTime));
			} 
			catch (ParseException exception)
			{
				//TODO improve
			}
			
			currentNote.setTypeOfNote(Byte.valueOf(typeOfNote));
		}
 
		if(qName.equalsIgnoreCase("NoteItem"))
		{
			currentNoteItem = new NoteListItem();
		}
		if (qName.equalsIgnoreCase("data")) 
		{
			data = true;
		}
 
		if (qName.equalsIgnoreCase("imageName")) 
		{
			imageName = true;
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
 
		if (qName.equalsIgnoreCase("imageName")) 
		{
			imageName = false;
		}
		
		if(qName.equalsIgnoreCase("NoteItem"))
		{
			currentNote.addNoteItem(currentNoteItem);
		}
		
		if (qName.equalsIgnoreCase("note"))
		{
			System.out.println("endElement -> note added="+currentNote);
			
			notes.add(currentNote);
			
			currentNote.setState(XMLEntity.READ_STATE);
		}
 

	}
 
	public void characters(char ch[], int start, int length) throws SAXException 
	{
		if (data) 
		{
			String data = new String(ch, start, length);
			
			System.out.println("characters -> data="+data);
			
			currentNoteItem.setTextViewData(data);
		}
 
		if (imageName) 
		{
			String imageName = new String(ch, start, length);
			
			System.out.println("characters -> imageName="+imageName);

			currentNoteItem.setImageName(imageName);
		}
	}
	
	public List<ProfessionalPANote> getNotes()
	{
		return notes;
	}

	public ProfessionalPANote getNote(int noteId) 
	{
		ProfessionalPANote result = null;
		
		for(int i = 0; i < notes.size(); i++)
		{
			ProfessionalPANote note = notes.get(i);
			
			if(note.getNoteId() == noteId)
			{
				result = note;
				
				break;
			}
		}
		
		return result;
	}
}
