package com.gp.app.minote.notes.xml;

import android.util.Log;

import com.gp.app.minote.data.TextNote;
import com.gp.app.minote.exceptions.MiNoteBaseException;
import com.gp.app.minote.util.MiNoteUtil;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class NotesReader 
{
	private NotesReader() 
	{
		
	}
	
	public static List<TextNote> readNotes(boolean isImportedFile) throws MiNoteBaseException
	{
        SAXParserFactory factory = SAXParserFactory.newInstance();
		
		SAXParser saxParser = null;
		
		try 
		{
			saxParser = factory.newSAXParser();
		} 
		catch (Exception exception) 
		{
			throw  new MiNoteBaseException("NOTES_XML_FILES_PARSING_FAILED", exception);
		} 
		
		NotesParser parser = new NotesParser();
		
		List<TextNote> notes = new ArrayList<TextNote>();
		
		String filePath =  isImportedFile ? MiNoteUtil.createExportedFilePath() :
			MiNoteUtil.createInternalXMLFilePath();
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
			throw  new MiNoteBaseException("NOTES_XML_FILES_PARSING_FAILED", exception);
		}
		
		return notes;
	}

}
