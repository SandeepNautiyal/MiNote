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

import com.gp.app.professionalpa.data.NoteItem;
import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.interfaces.XMLEntity;
import com.gp.app.professionalpa.util.ProfessionalPAUtil;

public class ProfessionalPANotesParser extends DefaultHandler
{
	private List<ProfessionalPANote> notes = new ArrayList<ProfessionalPANote>();
	
	private ProfessionalPANote currentNote = null;
	
	private NoteItem currentNoteItem = null;
	
	private SAXParserFactory factory = null;
	
	private SAXParser saxParser = null;
	
	private boolean note = false;
	private boolean data = false;
	private boolean imageName = false;
	private boolean isImportant;

	private boolean textColor;
	
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
			
			String noteColor = attributes.getValue("noteColor");

			currentNote.setNoteColor(Integer.valueOf(noteColor));

			try 
			{
				currentNote.setCreationTime(ProfessionalPAUtil.parseDateAndTimeString(CreationTime, "E yyyy.MM.dd 'at' hh:mm:ss:SSS a zzz"));

				currentNote.setLastEditedTime(ProfessionalPAUtil.parseDateAndTimeString(lastEditedTime, "E yyyy.MM.dd 'at' hh:mm:ss:SSS a zzz"));
			} 
			catch (ParseException exception)
			{
				//TODO improve
			}
			
			currentNote.setTypeOfNote(Byte.valueOf(typeOfNote));
		}
 
		if(qName.equalsIgnoreCase("NoteItem"))
		{
			currentNoteItem = new NoteItem();
		}
		if (qName.equalsIgnoreCase("data")) 
		{
			data = true;
		}
 
		if (qName.equalsIgnoreCase("imageName")) 
		{
			imageName = true;
		}
		
		if (qName.equalsIgnoreCase("textColor")) 
		{
			textColor = true;
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
		
		if (qName.equalsIgnoreCase("textColor")) 
		{
			textColor = false;
		}
		
		if(qName.equalsIgnoreCase("NoteItem"))
		{
			currentNote.addNoteItem(currentNoteItem);
		}
		
		if (qName.equalsIgnoreCase("note"))
		{
			notes.add(currentNote);
			
			currentNote.setState(XMLEntity.READ_STATE);
		}
 

	}
 
	public void characters(char ch[], int start, int length) throws SAXException 
	{
		if (data) 
		{
			String data = new String(ch, start, length);
			
			currentNoteItem.setTextViewData(data);
		}
 
		if (imageName) 
		{
			String imageName = new String(ch, start, length);
			
			currentNoteItem.setImageName(imageName);
		}
		
		if(textColor)
		{
            String textColor = new String(ch, start, length);
			
			currentNoteItem.setTextColour(Integer.valueOf(textColor));
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
