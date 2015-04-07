package com.gp.app.professionalpa.util;

public class ProfessionalPANotesIdGenerator 
{
    private static int noteId = 0;
    
	public static int generateNoteId() 
	{
		return noteId++;
	}

}
