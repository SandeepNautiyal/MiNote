package com.gp.app.professionalpa.layout.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.util.ProfessionalPATools;

public class ImageLocationPathManager
{
	private String imageDirectoryPath = null;
	
	private String mostRecentImageFilePath = null;
	
	public static ImageLocationPathManager imagePathManager = null;
	
	public static ImageLocationPathManager getInstance()
	{
		if(imagePathManager == null)
		{
			imagePathManager = new ImageLocationPathManager();
		}
		
		return imagePathManager;
	}
	
	private ImageLocationPathManager()
	{
		createImageDirectory();
	}
	
	public void deleteImage(String imageName) 
	{
		File file = new File(imageDirectoryPath+imageName+".jpeg");
		
		if(file.exists())
		{
			file.delete();
		}
	}

	public String getCreatedImagePath() 
	{
		return imageDirectoryPath+ProfessionalPATools.createImageNameFromTime()+".jpeg";
	}

	private void createImageDirectory()
	{
		final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/ProfessionalPA/"; 
        
		File newdir = new File(dir); 
        
        if(!newdir.exists())
        {
	        newdir.mkdirs();
        }
        
        imageDirectoryPath = dir;
	}
	
//	TODO improve in case image is added by user with different name. will throw number format exception.
	public List<String> getImagesFilePath()
	{
		List<String> imageFilePaths = new ArrayList<String>();
		
		File file = new File(imageDirectoryPath);
		
		if(file.isDirectory())
		{
			File [] files = file.listFiles();
			
			for(int i = 0, size = files != null ? files.length : 0; i < size; i++)
			{
				File imageFile = files[i];
				
//				int endIndex = path.indexOf(".jpeg");
//				
//				String imageName = path.substring(0, endIndex);
				
				imageFilePaths.add(imageFile.getAbsolutePath());
			}
		}
		
		return imageFilePaths;
	}
	
	
	public String getImageName(String imagePath)
	{
		int index = imagePath.indexOf(imageDirectoryPath);
		
		int endIndex = imagePath.indexOf(".jpeg");
		
		String imageFileName = imagePath.substring(index + imageDirectoryPath.length(), endIndex);

		return imageFileName;
	}
	
	public File createNextFile()
	{
        String filePath = imageDirectoryPath + ProfessionalPATools.createImageNameFromTime()+".jpeg";
        
        File newfile = new File(filePath);
        
        try 
        {
            if(newfile.createNewFile())
            {
            	mostRecentImageFilePath = filePath;
            }
        } 
        catch (IOException e) 
        {
        	//TODO improve
        } 
        
        return newfile;
	}
	
	public String getMostRecentImageFilePath()
	{
		return mostRecentImageFilePath;
	}

	public void createSaveImage(Bitmap imageNote)
	{
        String filePath = imageDirectoryPath + ProfessionalPATools.createImageNameFromTime()+".jpeg";

		OutputStream imagefile = null;
		
		//TODO improve
		try 
		{
			imagefile = new FileOutputStream(filePath);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		// Write 'bitmap' to file using JPEG and 80% quality hint for JPEG:
		imageNote.compress(CompressFormat.JPEG, 100, imagefile);
		
		mostRecentImageFilePath = filePath;
	}

	public Bitmap getImage(String imageName)
	{
        BitmapFactory.Options options = new BitmapFactory.Options();
		
		options.inSampleSize = 8;
		
		Bitmap image = BitmapFactory.decodeFile(imageDirectoryPath+imageName+".jpeg", options);
;
        return image;
	}
}
