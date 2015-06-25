package com.gp.app.professionalpa.notes.backup;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Environment;

import com.gp.app.professionalpa.data.TextNote;
import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.notes.database.NotesDBManager;
import com.gp.app.professionalpa.notes.xml.ProfessionalPANotesWriter;
import com.gp.app.professionalpa.notification.service.AlarmRequestCreator;
import com.gp.app.professionalpa.util.ProfessionalPAUtil;

public class NotesBackupManager 
{
	public static void setInitialBackupTime()
	{
		String nextBackupDate = getNextBackupDate();
		
		AlarmRequestCreator.createAlarmRequest(nextBackupDate, "00:00");
	}

	private static String getNextBackupDate() 
	{
		Date date = new Date(System.currentTimeMillis());
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		c.add(Calendar.DATE, 1);
		date = c.getTime();	
		
		String todaysDate = ProfessionalPAUtil.createStringForDate(date.getTime(), "dd/MM/yyyy");
		
		System.out.println("start -> todaysDate="+todaysDate);
		return todaysDate;
	}

	public static void startBackupProcess()
	{
		File directory = Environment.getExternalStorageDirectory();
//		  // assumes that a file article.rss is available on the SD card
		File notesXmlBackupFile = new File(directory + "/notes.xml");
		
		boolean isNewFileCreated = false;
		
		if(!notesXmlBackupFile.exists())
		{
			try
			{
				isNewFileCreated = notesXmlBackupFile.createNewFile();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		long lastModifiedTime = isNewFileCreated ? 0 : notesXmlBackupFile.lastModified();
		
		if(lastModifiedTime < ProfessionalPAUtil.getDayStartTime(System.currentTimeMillis()))
		{
			updateBackupXmlFile();
		}
	}

	private static void updateBackupXmlFile()
	{
		List<TextNote> notes = NotesDBManager.getInstance().readNotes();
		
		try 
		{
			ProfessionalPANotesWriter.getInstance().writeNotes(notes);
		} 
		catch (ProfessionalPABaseException e) 
		{
			//TODO to be improved.
			e.printStackTrace();
		}
		
        String nextBackupDate = getNextBackupDate();
		
		AlarmRequestCreator.createAlarmRequest(nextBackupDate, "00:00");
	}


}
