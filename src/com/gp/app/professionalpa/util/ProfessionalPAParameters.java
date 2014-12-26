package com.gp.app.professionalpa.util;

import android.content.Context;

public class ProfessionalPAParameters 
{
	private static Context applicationContext;
	
	private static int screenWidth = 0;
	
	private static int screenHeight = 0;

	private static int parentLayoutWidth;
	
    public static void setApplicationContext(Context context)
    {
    	applicationContext = context;
    }
    
    public static Context getApplicationContext()
    {
    	return applicationContext;
    }

	public static void setScreenWidth(int width) {
		screenWidth = width;
	}

	public static void setScreenHeight(int height) {
		
		screenHeight = height;
		
	}
	
	public static int getScreenWidth() {
		return screenWidth;
	}

	public static int getScreenHeight() {
		
		return screenHeight;
		
	}

	public static void setParentLinearLayoutWidth(int width) {
		// TODO Auto-generated method stub
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
}
