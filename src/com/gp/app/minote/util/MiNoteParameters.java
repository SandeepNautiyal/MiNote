package com.gp.app.minote.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.gp.app.minote.layout.manager.NotesLayoutManagerActivity;
import com.gp.app.minote.notes.fragments.FragmentCreationManager;

public class MiNoteParameters 
{
	private static Context applicationContext;
	
	private static int linearLayoutWidth = 0;
	
	private static int screenHeight = 0;

	private static int parentLayoutWidth;
	
	private static SharedPreferences professionalPASharedPrefs = null;
	
//	private static ProfessionalPANotesWriter notesWriter = null;
//	
//	private static ProfessionalPANotesParser notesParser = null;
//	
//	private static ProfessionalPANotesReader notesReader = null;

	private static FragmentCreationManager fragmentCreationManager;
	
	private static NotesLayoutManagerActivity notesLayoutManagerActivity;
	
    public static void setApplicationContext(Context context)
    {
    	applicationContext = context;
    }
    
    public static Context getApplicationContext()
    {
    	return applicationContext;
    }

	public static void setLinearLayoutWidth(int width) 
	{
		linearLayoutWidth = width;
	}
	
	public static int getLinearLayoutWidth() 
	{
		System.out.println("getLinearLayoutWidth -> linearLayoutWidth="+linearLayoutWidth);

		return linearLayoutWidth;
	}

	public static void setParentLinearLayoutWidth(int width) 
	{
		parentLayoutWidth = width;
	}
	
	public static int getParentLinearLayoutWidth()
	{
		return parentLayoutWidth;
	}
	
	public static int getId()
	{
		 return (int)Math.abs(Math.random()*10000);
	}
	
	public static SharedPreferences getSharedPreferences()
	{
		if(professionalPASharedPrefs ==  null)
		{
			professionalPASharedPrefs = applicationContext.getSharedPreferences("ProfessionalPASharedPreference", Context.MODE_PRIVATE);
		}
		
		return professionalPASharedPrefs;
	}
	
//	public static ProfessionalPANotesParser getProfessionalPANotesParser() throws ProfessionalPABaseException
//	{
//		if(notesParser == null)
//		{
//			notesParser = new ProfessionalPANotesParser();
//		}
//		
//		return notesParser;
//	}
//	
//	public static ProfessionalPANotesWriter getProfessionalPANotesWriter()
//	{
//		if(notesWriter == null)
//		{
//			notesWriter = new ProfessionalPANotesWriter();
//		}
//		
//		return notesWriter;
//	}

	public static FragmentCreationManager getFragmentCreationManager() 
	{
		if(fragmentCreationManager == null)
		{
			fragmentCreationManager = new FragmentCreationManager();
		}
		
		return fragmentCreationManager;
	}

	public static void setNotesActivity(NotesLayoutManagerActivity notesManagerActivity) 
	{
		notesLayoutManagerActivity = notesManagerActivity;
	}
	
	public static NotesLayoutManagerActivity getNotesActivity() 
	{
		return notesLayoutManagerActivity;
	}
}
