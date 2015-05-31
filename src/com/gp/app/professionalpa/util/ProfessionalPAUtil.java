package com.gp.app.professionalpa.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;

import android.os.Environment;

public class ProfessionalPAUtil 
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
     
    public static long parseDateAndTimeString(String time, String format) throws ParseException 
 	{
 		SimpleDateFormat formatter = new SimpleDateFormat(format);

 		Date creationDate = formatter.parse(time);
 		
 		return creationDate.getTime();
 	}
    
    public static String createStringForDate(long creationTimeAndDate, String format) {
		Date creationTime = new Date(creationTimeAndDate);

		SimpleDateFormat formatter = new SimpleDateFormat(format);

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
    
    public static long createTime(String date, String time)
    {
        long parsedTime = 0l;
		
        if(date == null || time == null)
        {
        	return parsedTime;
        }
        
		String [] timeTokens = null;
		
		String [] dateToken = null;
		
		try 
    	{
			timeTokens = time.split(":");
				
			dateToken = date.split("/");
			
    		String createdStartTime = dateToken[2]+dateToken[1]+dateToken[0]+timeTokens[0]+timeTokens[1];
    		
    		parsedTime = ProfessionalPAUtil.parseDateAndTimeString(createdStartTime,"yyyyMMddHHmm");
    	} 
    	catch (ParseException e) 
    	{
    		//TODO improve
			e.printStackTrace();
		}
		
		return parsedTime;
    }
    
    public static String pad(int c) 
	{
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}

	public static long getDayStartTime(long lastModifiedTime) 
	{
		String date = createStringForDate(lastModifiedTime, "dd/MM/yyyy");
		
		long time = createTime(date, "00:00");
		
		System.out.println("getDayStartTime -> time="+time+" date="+date);
		
		return time;
	}
}
