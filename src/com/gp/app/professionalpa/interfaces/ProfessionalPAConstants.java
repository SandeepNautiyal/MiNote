package com.gp.app.professionalpa.interfaces;

public interface ProfessionalPAConstants 
{
    int ADD_BUTTON_ID = 100000;
    
    int SAVE_BUTTON_ID = 100001;
    
    int TAKE_PHOTO_CODE = 1888;

	String NOTE_DATA = "LIST_ITEMS";

	String PROFESSIONAL_PA_XML_FILE_NAME = "notes.xml";

	String IS_PARAGRAPH_NOTE = "IS_PARAGRAPH_NOTE";
	
	String PROFESSIONAL_PA_EXPORT_PATH = "/ProfessionalPA//Export";
	
	String PROFESSIONAL_PA_IMPORT_PATH = PROFESSIONAL_PA_EXPORT_PATH+"//"
	    +PROFESSIONAL_PA_XML_FILE_NAME;

	public static final byte IMAGE_NOTE = 2;

	public static final byte PARAGRAPH_NOTE = 1;

	public static final byte LIST_NOTE = 0;
}
