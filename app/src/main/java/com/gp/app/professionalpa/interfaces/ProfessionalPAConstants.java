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

	int OPEN_NOTE_IN_EDIT_MODE = 10;

	public static final String IS_NOTIFICATION = "IS_NOTIFICATION";

	public static final String EVENT_NAME = "EVENT_NAME";

	public static final String EVENT_MESSAGE = "EVENT_MESSAGE";

	public static final String NOTIFICATION_ID = "NOTIFICATION_ID";

	public static final String NOTE_ID = "NOTE_ID";

	public static final byte MIXED_CONTENT_NOTE = 3;
}
