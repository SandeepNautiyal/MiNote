package com.gp.app.professionalpa.notes.xml;
 
 
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import android.content.Context;
import android.os.Environment;

import com.gp.app.professionalpa.data.NotesListItem;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;
 
 
public class ProfessionalPANotesWriter 
{
    private Document xmlDocument = null;
    
    private Element rootElement = null;
    
	public ProfessionalPANotesWriter() throws ProfessionalPABaseException 
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder dBuilder;

		try 
		{
			dBuilder = dbFactory.newDocumentBuilder();
		}
		catch (ParserConfigurationException exception) 
		{
			throw new ProfessionalPABaseException("DOCUMENT_BUILDER_NOT_INITIALIZED", exception);
		}

		xmlDocument = dBuilder.newDocument();

		rootElement = xmlDocument.createElementNS("rootElement", "Notes");

		xmlDocument.appendChild(rootElement);
	}
    
    public Node writeNotes(String type, List<NotesListItem> noteListItem) 
    {
        Element note = xmlDocument.createElement("Note");
 
        note.setAttribute("type", type);
        
        rootElement.appendChild(note);
        
//        Element noteType = xmlDocument.createElement("type");
//    	
//        noteType.appendChild(xmlDocument.createTextNode(type));
        
//    	note.appendChild(noteType);
    	
    	for(int i = 0; i < noteListItem.size(); i++)
    	{
    		createNoteItem(note, noteListItem.get(i));
    	}
 
        return note;
    }
 
 
    private void createNoteItem(Element note, NotesListItem noteListItem) 
    {
        Element noteItem = xmlDocument.createElement("NoteItem");
        
        note.appendChild(noteItem);
        
        createNoteListItem(noteItem, noteListItem);
    }
    
    private void createNoteListItem(Element noteItem, NotesListItem noteListItem) 
    {
        Element data = xmlDocument.createElement("data");
    	
        data.appendChild(xmlDocument.createTextNode(noteListItem.getTextViewData()));
        
        noteItem.appendChild(data);
    	
        Element isAlarm = xmlDocument.createElement("isAlarm");
        
        isAlarm.appendChild(xmlDocument.createTextNode(noteListItem.isAlarmActive()+""));

        noteItem.appendChild(isAlarm);
        
        Element isImportant = xmlDocument.createElement("isImportant");
        
        isImportant.appendChild(xmlDocument.createTextNode(noteListItem.isImportanceHigh()+""));

        noteItem.appendChild(isImportant);
	}

	public void completeWritingProcess() throws ProfessionalPABaseException
    {
    	try 
		{
    		TransformerFactory transformerFactory = TransformerFactory.newInstance();

			Transformer transformer = transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "notes.dtd");

			DOMSource source = new DOMSource(xmlDocument);

			File directory = Environment.getExternalStorageDirectory();
			  // assumes that a file article.rss is available on the SD card
			File file = new File(directory + "/notes.xml");
			  
//			FileOutputStream output = ProfessionalPAParameters.getApplicationContext().openFileOutput("notes.xml", Context.MODE_PRIVATE);
			
//			output.write(1234567);
			
			StreamResult result = new StreamResult(new FileOutputStream(file));
			
			transformer.transform(source, result);
		}
		catch (Exception exception) 
		{
			throw new ProfessionalPABaseException("PROBLEM_WRITING_XML", exception);
		}
    }
 
}