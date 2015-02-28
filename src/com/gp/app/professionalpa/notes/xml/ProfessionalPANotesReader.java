package com.gp.app.professionalpa.notes.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import android.util.Log;

import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.util.ProfessionalPATools;

public class ProfessionalPANotesReader 
{
	private ProfessionalPANotesReader() 
	{
		
	}
	
	public static List<ProfessionalPANote> readNotes(boolean isImportedFile) throws ProfessionalPABaseException
	{
        SAXParserFactory factory = SAXParserFactory.newInstance();
		
		SAXParser saxParser = null;
		
		try 
		{
			saxParser = factory.newSAXParser();
		} 
		catch (Exception exception) 
		{
			throw  new ProfessionalPABaseException("NOTES_XML_FILES_PARSING_FAILED", exception);
		} 
		
		ProfessionalPANotesParser parser = new ProfessionalPANotesParser();
		
		List<ProfessionalPANote> notes = new ArrayList<ProfessionalPANote>();
		
		String filePath =  isImportedFile ? ProfessionalPATools.createExportedFilePath() :
			ProfessionalPATools.createInternalXMLFilePath();
		try 
		{
			File file = new File(filePath);
			
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
