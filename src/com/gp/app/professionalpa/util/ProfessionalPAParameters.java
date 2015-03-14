package com.gp.app.professionalpa.util;

import com.gp.app.professionalpa.exceptions.ProfessionalPABaseException;
import com.gp.app.professionalpa.notes.fragments.FragmentCreationManager;
import com.gp.app.professionalpa.notes.xml.ProfessionalPANotesParser;
import com.gp.app.professionalpa.notes.xml.ProfessionalPANotesReader;
import com.gp.app.professionalpa.notes.xml.ProfessionalPANotesWriter;

import android.content.Context;
import android.content.SharedPreferences;

public class ProfessionalPAParameters 
{
	private static Context applicationContext;
	
	private static int linearLayoutWidth = 0;
	
	private static int screenHeight = 0;

	private static int parentLayoutWidth;
	
	private static SharedPreferences professionalPASharedPrefs = null;
	
	private static ProfessionalPANotesWriter notesWriter = null;
	
	private static ProfessionalPANotesParser notesParser = null;
	
	private static ProfessionalPANotesReader notesReader = null;

	private static FragmentCreationManager fragmentCreationManager;
	
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
		System.out.println("setLinearLayoutWidth -> width="+width);
		
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
		 return (int)Math.abs(Math.random()*1000000);
	}
	
	public static SharedPreferences getSharedPreferences()
	{
		if(professionalPASharedPrefs ==  null)
		{
			professionalPASharedPrefs = applicationContext.getSharedPreferences("ProfessionalPASharedPreference", Context.MODE_PRIVATE);
		}
		
		return professionalPASharedPrefs;
	}
	
	public static ProfessionalPANotesParser getProfessionalPANotesParser() throws ProfessionalPABaseException
	{
		if(notesParser == null)
		{
			notesParser = new ProfessionalPANotesParser();
		}
		
		return notesParser;
	}
	
	public static ProfessionalPANotesWriter getProfessionalPANotesWriter()
	{
		if(notesWriter == null)
		{
			notesWriter = new ProfessionalPANotesWriter();
		}
		
		return notesWriter;
	}

	public static FragmentCreationManager getFragmentCreationManager() 
	{
		if(fragmentCreationManager == null)
		{
			fragmentCreationManager = new FragmentCreationManager();
		}
		
		return fragmentCreationManager;
	}
}
