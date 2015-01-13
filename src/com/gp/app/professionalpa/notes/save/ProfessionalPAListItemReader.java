package com.gp.app.professionalpa.notes.save;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gp.app.professionalpa.data.NotesListItem;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;

public class ProfessionalPAListItemReader 
{
	private ByteArrayOutputStream byteArrayOutputStream = null;
	
	public ProfessionalPAListItemReader()
	{
		byteArrayOutputStream = new ByteArrayOutputStream();
	}
	
	public static void writeNotes(List<NotesListItem> writeable)
	{
//		try
//		{
//			AssetManager assetManager = ProfessionalPAParameters.getApplicationContext().getAssets();
//			
//			InputStream input = assetManager.open("notes.txt");
//			
//			FileOutputStream fileStream = new FileOutputStream("notes.txt");
//			
//			ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
//			
//			objectStream.writeObject(writeable);
//			
//			objectStream.close();
//		}
//		catch(IOException exception)
//		{
//			
//		}
	}
	
	public static List<NotesListItem> readNotes(String NotesListItemString)
	{
		List<NotesListItem> notes = null;
		

		Pattern pattern = Pattern.compile(ProfessionalPAConstants.LIST_ITEM_STRING_START_DELIMITER+".*.+"+ProfessionalPAConstants.LIST_ITEM_STRING_END_DELIMITER);
		
		Matcher matcher = pattern.matcher(NotesListItemString);

		        boolean found = false;
		        while (matcher.find()) {
		            System.out.println("I found the text: " + matcher.group().toString());
		            found = true;
		        }
		        if (!found) {
		            System.out.println("I didn't found the text");
		        }
		        
//		ProfessionalPAListView result = null;
//				
//		try 
//		{
//			AssetManager assetManager = ProfessionalPAParameters.getApplicationContext().getAssets();
//
//			InputStream input = assetManager.open("notes.txt");
//			
//			ObjectInputStream objectInputStream = new ObjectInputStream(input);
//			
//			result = (ProfessionalPAListView)objectInputStream.readObject();
//		} 
//		catch(ClassNotFoundException exception)
//		{
//			
//		}
//		catch (IOException exception)
//		{
//
//		}
//		
//		List<Writable> writable = new ArrayList<Writable>();
		
		return notes;
	}
}


