package com.gp.app.professionalpa.notes.save;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.res.AssetManager;

import com.gp.app.professionalpa.data.NotesListItem;
import com.gp.app.professionalpa.layout.notes.data.ProfessionalPAListView;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

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


