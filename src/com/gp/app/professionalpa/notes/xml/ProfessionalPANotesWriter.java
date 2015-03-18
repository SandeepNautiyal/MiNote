package com.gp.app.professionalpa.notes.xml;
 
 
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.Environment;

import com.gp.app.professionalpa.data.NoteListItem;
import com.gp.app.professionalpa.data.ProfessionalPANote;
import com.gp.app.professionalpa.exceptions.ProfessionPARuntimeException;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.util.ProfessionalPATools;
 
 
public class ProfessionalPANotesWriter
{
    private Document xmlDocument = null;
    
    private Element rootElement = null;
    
    //TODO exception handling in this method can be improved.
	public ProfessionalPANotesWriter()
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

		long creationTimeAndDate = note.getCreationTime();
		
		String creationDate = ProfessionalPATools.createStringForDate(creationTimeAndDate);

		noteElement.setAttribute("creationTime", creationDate);

		noteElement.setAttribute("lastEditedTime", creationDate);

		rootElement.appendChild(noteElement);

		for (int i = 0, size = note.getNoteItems().size(); i < size; i++) 
		{
			createNoteItem(noteElement, note.getNoteItems().get(i));
		}
		
		completeWritingProcess();
    }

    private void createNoteItem(Element note, NoteListItem noteListItem) 
    {
        Element noteItem = xmlDocument.createElement("NoteItem");
        
        note.appendChild(noteItem);
        
        createNoteListItem(noteItem, noteListItem);
    }
    
    private void createNoteListItem(Element noteItem, NoteListItem noteListItem) 
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
			
			if (noteElement.hasAttributes()) {
                Attr attr = (Attr) noteElement.getAttributes().getNamedItem("creationTime");
                if (attr != null) {
                    String attribute= attr.getValue();                      
                    System.out.println("attribute: " + attribute); 
                    
                    try 
                    {
						long readCreationTime = ProfessionalPATools.parseDateAndTimeString(attribute);
						
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
			
			NamedNodeMap noteAttributes = noteElement.getAttributes();
			
			System.out.println("creation time :"+noteAttributes.getNamedItem(Long.toString(creationTime)));
			
			System.out.println("\nattributes :"+noteAttributes+" string content:"+noteElement.getTextContent()+
					"name:"+noteElement.getNodeName()+"type="+noteElement.getNodeType()+"value="+noteElement.getNodeValue());
			
			
		}

		
	}
}