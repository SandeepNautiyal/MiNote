package com.gp.app.professionalpa.notes.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import android.os.Environment;
import android.util.Log;

import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;

public class ProfessionalPANotesReader 
{
	SAXParserFactory factory = null;
	
	SAXParser saxParser = null;
	
	public ProfessionalPANotesReader() throws ProfessionalPABaseException
	{
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
	
	public List<ProfessionalPANote> readNotes() throws ProfessionalPABaseException
	{
		ProfessionalPANotesParser parser = new ProfessionalPANotesParser();
		
		List<ProfessionalPANote> notes = new ArrayList<ProfessionalPANote>();
		
		try 
		{
			File directory = Environment.getExternalStorageDirectory();
			  // assumes that a file article.rss is available on the SD card
			File file = new File(directory + "/notes.xml");
			
			saxParser.parse(file, parser);
			
			notes = parser.getNotes();
		}
		catch(IOException exception)
		{
			Log.i("FILE_NOT_CREATED_YET", "FILE_NOT_CREATED_YET");
		}
		catch (SAXException exception) 
		{
			throw  new ProfessionalPABaseException("NOTES_XML_FILES_PARSING_FAILED", exception);
		}
		
		return notes;
	}

}
