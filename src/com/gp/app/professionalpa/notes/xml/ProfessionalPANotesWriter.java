package com.gp.app.professionalpa.notes.xml;
 
 
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.Environment;

import com.gp.app.professionalpa.data.NoteItem;
import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionPARuntimeException;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.util.ProfessionalPAUtil;
 
 
public class ProfessionalPANotesWriter
{
    private static ProfessionalPANotesWriter notesWriter;

	private Document xmlDocument = null;
    
    private Element rootElement = null;
    
    public static ProfessionalPANotesWriter getInstance()
    {
    	if(notesWriter == null)
    	{
    		notesWriter = new ProfessionalPANotesWriter();
    	}
    	
    	return notesWriter;
    }
    //TODO exception handling in this method can be improved.
	private ProfessionalPANotesWriter()
	{
		SAXParserFactory.newInstance();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder dBuilder;

		try 
		{
			dBuilder = dbFactory.newDocumentBuilder();
		}
		catch (ParserConfigurationException exception) 
		{
			throw new ProfessionPARuntimeException("DOCUMENT_BUILDER_NOT_INITIALIZED", exception);
		}

		xmlDocument = dBuilder.newDocument();

		rootElement = xmlDocument.createElementNS("rootElement", "Notes");

		xmlDocument.appendChild(rootElement);
		
		try
		{
			List<ProfessionalPANote> notes = ProfessionalPANotesReader.readNotes(false);
			
			for(int i = 0, size = notes == null ? 0 : notes.size(); i < size; i++)
			{
				writeNote(notes.get(i));
			}
		}
		catch(ProfessionalPABaseException exception)
		{
			//TODO
		}
		
	}
    
	public void writeNotes(List<ProfessionalPANote> notes) throws ProfessionalPABaseException
	{
		for(int i = 0, size = notes == null ? 0 : notes.size(); i < size; i++)
		{
			writeNote(notes.get(i));
		}
	}
	
	private void writeNote(ProfessionalPANote note) throws ProfessionalPABaseException
    {
		if(note == null)
		{
			return;
		}
		
		Element noteElement = xmlDocument.createElement("Note");

		noteElement.setAttribute("type", Byte.toString(note.getNoteType()));

		noteElement.setAttribute("noteId", Integer.toString(note.getNoteId()));

		String creationDate = ProfessionalPAUtil.createStringForDate(note.getCreationTime(), "E yyyy.MM.dd 'at' hh:mm:ss:SSS a zzz");

		String lastEditedTime = ProfessionalPAUtil.createStringForDate(note.getLastEditedTime(), "E yyyy.MM.dd 'at' hh:mm:ss:SSS a zzz");
		
		noteElement.setAttribute("creationTime", creationDate);

		noteElement.setAttribute("lastEditedTime", lastEditedTime);

		noteElement.setAttribute("noteColor", Integer.toString(note.getNoteColor()));

		rootElement.appendChild(noteElement);

		for (int i = 0, size = note.getNoteItems().size(); i < size; i++) 
		{
			createNoteItem(noteElement, note.getNoteItems().get(i));
		}
		
		completeWritingProcess();
    }

    private void createNoteItem(Element note, NoteItem noteListItem) 
    {
        Element noteItem = xmlDocument.createElement("NoteItem");
        
        note.appendChild(noteItem);
        
        createNoteListItem(noteItem, noteListItem);
    }
    
    private void createNoteListItem(Element noteItem, NoteItem noteListItem) 
    {
        Element data = xmlDocument.createElement("data");
    	
        data.appendChild(xmlDocument.createTextNode(noteListItem.getText()));
        
        noteItem.appendChild(data);
    	
        Element imageName = xmlDocument.createElement("imageName");
        
        imageName.appendChild(xmlDocument.createTextNode(noteListItem.getImageName()));

        noteItem.appendChild(imageName);
        
        Element textColor = xmlDocument.createElement("textColor");
        
        textColor.appendChild(xmlDocument.createTextNode(String.valueOf(noteListItem.getTextColour())));

        noteItem.appendChild(textColor);
	}

	private void completeWritingProcess() throws ProfessionalPABaseException
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

	public void deleteXmlElement(long creationTime) throws ProfessionalPABaseException 
	{
		Element notesElement = xmlDocument.getDocumentElement();
		
		System.out.println("deleteXmlElement -> noteElement="+notesElement);
		
		NodeList nodeList = notesElement.getChildNodes();
		
		for(int i = 0; i < nodeList.getLength(); i++)
		{
			Node noteElement = nodeList.item(i);
			
			if (noteElement.hasAttributes())
			{
                Attr attr = (Attr) noteElement.getAttributes().getNamedItem("creationTime");
                if (attr != null) {
                    String attribute= attr.getValue();                      
                    System.out.println("attribute: " + attribute); 
                    
                    try 
                    {
						long readCreationTime = ProfessionalPAUtil.parseDateAndTimeString(attribute, "E yyyy.MM.dd 'at' hh:mm:ss:SSS a zzz");
						
						if(readCreationTime == creationTime)
						{
							notesElement.removeChild(noteElement);
							
							System.out.println("creation time found");
							
							completeWritingProcess();

							break;
						}
					}
                    catch (ParseException e) 
                    {
						e.printStackTrace();
					}
                }
            }
		}
    }
		
    public void deleteXmlElement(int noteId) throws ProfessionalPABaseException 
	{
		Element notesElement = xmlDocument.getDocumentElement();
			
		NodeList nodeList = notesElement.getChildNodes();
			
		for(int i = 0; i < nodeList.getLength(); i++)
		{
			Node noteElement = nodeList.item(i);
				
			if (noteElement.hasAttributes()) 
			{
	            Attr attr = (Attr) noteElement.getAttributes().getNamedItem("noteId");
	            if (attr != null) 
	            {
	                String attribute= attr.getValue();                      
	                
	                int readNoteId = Integer.valueOf(attribute);
							
					if(noteId == readNoteId)
					{
						notesElement.removeChild(noteElement);
					
						completeWritingProcess();

						break;
					}
						
	            }
	        }
		}
	}
    
    public void setColorAttribute(int noteId, int color) throws ProfessionalPABaseException 
	{
		Element notesElement = xmlDocument.getDocumentElement();
			
		NodeList nodeList = notesElement.getChildNodes();
			
		for(int i = 0; i < nodeList.getLength(); i++)
		{
			Node noteElement = nodeList.item(i);
				
			if (noteElement.hasAttributes()) 
			{
	            Attr attr = (Attr) noteElement.getAttributes().getNamedItem("noteId");
	            
	            if (attr != null) 
	            {
	                String attribute= attr.getValue();                      
	                
	                int readNoteId = Integer.valueOf(attribute);
							
					if(noteId == readNoteId)
					{
						Attr colorAttribute = (Attr) noteElement.getAttributes().getNamedItem("noteColor");
						
						colorAttribute.setValue(String.valueOf(color));
					
						completeWritingProcess();

						break;
					}
						
	            }
	        }
		}
	}
}