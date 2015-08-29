package com.gp.app.minote.export;

import android.os.Environment;

import com.gp.app.minote.exceptions.MiNoteBaseException;
import com.gp.app.minote.util.MiNoteUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MiNoteNotesExporter {

	public static void export() throws MiNoteBaseException
	{
		File exportDirectory = new File(MiNoteUtil.createExportedDirectoryPath());
		
		if(!exportDirectory.exists())
		{
			exportDirectory.mkdirs();
		}
		
		File fileToBeCopied = new File(Environment.getExternalStorageDirectory()+"/notes.xml");
		
		File destinationFile = new File(MiNoteUtil.createExportedFilePath());
		
		try 
		{
			copyFile(fileToBeCopied, destinationFile);
		} 
		catch (IOException exception) 
		{
			throw new MiNoteBaseException("FILE_EXPORT_FAILED", exception);
		}
	}
	
	public static void copyFile(File oldLocation, File newLocation) throws IOException 
	{
		if (oldLocation.exists()) 
		{
			BufferedInputStream reader = new BufferedInputStream(new FileInputStream(oldLocation));

			BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(newLocation, false));

			try 
			{
				byte[] buff = new byte[8192];

				int numChars;

				while ((numChars = reader.read(buff, 0, buff.length)) != -1) 
				{
					writer.write(buff, 0, numChars);
				}
			} 
			catch (IOException ex) 
			{
				throw new IOException("IOException when transferring "
						+ oldLocation.getPath() + " to "
						+ newLocation.getPath());
			} 
			finally
			{
				try 
				{
					if (reader != null) 
					{
						writer.close();

						reader.close();
					}
				} 
				catch (IOException ex) 
				{
					// TODO
				}
			}
		} 
		else 
		{
			throw new IOException(
					"Old location does not exist when transferring "
							+ oldLocation.getPath() + " to "
							+ newLocation.getPath());
		}
	}
}
