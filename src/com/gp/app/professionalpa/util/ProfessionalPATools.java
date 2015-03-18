package com.gp.app.professionalpa.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;

import android.os.Environment;

public class ProfessionalPATools 
{
     public static String createInternalXMLFilePath()
     {
    	 return Environment.getExternalStorageDirectory()+"/"+ProfessionalPAConstants.PROFESSIONAL_PA_XML_FILE_NAME;
     }
     
     public static String createExportedFilePath()
     {
    	 return Environment.getExternalStorageDirectory() + ProfessionalPAConstants.PROFESSIONAL_PA_EXPORT_PATH
    			 +"//"+ProfessionalPAConstants.PROFESSIONAL_PA_XML_FILE_NAME;
     }
     
     public static String createExportedDirectoryPath()
     {
    	 return Environment.getExternalStorageDirectory() + ProfessionalPAConstants.PROFESSIONAL_PA_EXPORT_PATH;
     }
     
    public static long parseDateAndTimeString(String time) throws ParseException 
 	{
 		SimpleDateFormat formatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss:SSS a zzz");

 		Date creationDate = formatter.parse(time);
 		
 		return creationDate.getTime();
 	}
    
    public static String createStringForDate(long creationTimeAndDate) {
		Date creationTime = new Date(creationTimeAndDate);

		SimpleDateFormat formatter = new SimpleDateFormat(
				"E yyyy.MM.dd 'at' hh:mm:ss:SSS a zzz");

		String creationDate = formatter.format(creationTime);
		return creationDate;
	}
    
    public static String createImageNameFromTime()
    {
		Date creationTime = new Date(System.currentTimeMillis());

		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyMMddHHmmss");

		String creationDate = formatter.format(creationTime);
		return creationDate;
	}
    
    public static long parseDateAndTimeImageNameString(String time) throws ParseException 
 	{
 		SimpleDateFormat formatter = new SimpleDateFormat("yyMMddHHmmss");

 		Date creationDate = formatter.parse(time);
 		
 		return creationDate.getTime();
 	}
}
