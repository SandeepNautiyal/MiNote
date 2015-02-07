package com.gp.app.professionalpa.notes.xml;

import java.io.File;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.os.Environment;

import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;

public class ProfessionalPANotesReader 
{
	ProfessionalPANotesParser parser = null;
	
	SAXParserFactory factory = null;
	
	SAXParser saxParser = null;
	
	public ProfessionalPANotesReader() throws ProfessionalPABaseException
	{
		parser = new ProfessionalPANotesParser();
		
		factory = SAXParserFactory.newInstance();
		
		try 
		{
			saxParser = factory.newSAXParser();
		} 
		catch (Exception exception) 
		{
			throw  new ProfessionalPABaseException("NOTES_XML_FILES_PARSING_FAILED", exception);
		} 
	}
	
	public List<ParsedNote> readNotes() throws ProfessionalPABaseException
	{
		List<ParsedNote> notes = null;
		
		try 
		{
			File directory = Environment.getExternalStorageDirectory();
			  // assumes that a file article.rss is available on the SD card
			File file = new File(directory + "/notes.xml");
			
			saxParser.parse(file, parser);
			
			notes = parser.getNotes();
		}
		catch (Exception exception) 
		{
			throw  new ProfessionalPABaseException("NOTES_XML_FILES_PARSING_FAILED", exception);
		}
		
		return notes;
	}

}
