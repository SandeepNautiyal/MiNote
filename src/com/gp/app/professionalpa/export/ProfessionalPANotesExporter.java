package com.gp.app.professionalpa.export;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.util.ProfessionalPATools;

import android.os.Environment;
import android.util.Log;

public class ProfessionalPANotesExporter {

	public static void export() throws ProfessionalPABaseException
	{
		File exportDirectory = new File(ProfessionalPATools.createExportedDirectoryPath());
		
		if(!exportDirectory.exists())
		{
			exportDirectory.mkdirs();
		}
		
		File fileToBeCopied = new File(Environment.getExternalStorageDirectory()+"/notes.xml");
		
		File destinationFile = new File(ProfessionalPATools.createExportedFilePath());
		
		try 
		{
			copyFile(fileToBeCopied, destinationFile);
		} 
		catch (IOException exception) 
		{
			throw new ProfessionalPABaseException("FILE_EXPORT_FAILED", exception);
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
