package com.gp.app.professionalpa.util;

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
}
