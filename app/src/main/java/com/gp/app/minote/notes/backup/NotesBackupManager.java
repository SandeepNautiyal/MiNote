package com.gp.app.minote.notes.backup;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Environment;

import com.gp.app.minote.data.TextNote;
import com.gp.app.minote.exceptions.MiNoteBaseException;
import com.gp.app.minote.notes.database.NotesDBManager;
import com.gp.app.minote.notes.xml.NotesWriter;
import com.gp.app.minote.notification.service.AlarmRequestCreator;
import com.gp.app.minote.util.MiNoteUtil;

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
		
		String todaysDate = MiNoteUtil.createStringForDate(date.getTime(), "dd/MM/yyyy");
		
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
		
		if(lastModifiedTime < MiNoteUtil.getDayStartTime(System.currentTimeMillis()))
		{
			updateBackupXmlFile();
		}
	}

	private static void updateBackupXmlFile()
	{
		List<TextNote> notes = NotesDBManager.getInstance().readNotes();
		
		try 
		{
			NotesWriter.getInstance().writeNotes(notes);
		} 
		catch (MiNoteBaseException e) 
		{
			//TODO to be improved.
			e.printStackTrace();
		}
		
        String nextBackupDate = getNextBackupDate();
		
		AlarmRequestCreator.createAlarmRequest(nextBackupDate, "00:00");
	}


}
