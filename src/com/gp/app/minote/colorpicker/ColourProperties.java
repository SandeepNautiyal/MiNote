package com.gp.app.minote.colorpicker;

import android.graphics.Color;

public enum ColourProperties 
{
    RED(Color.RED, "Red"),
    
    GREEN(Color.GREEN, "Green"),
    
    BLUE(Color.BLUE, "Blue"),
    
    YELLOW(Color.YELLOW, "Yellow"),
    
    GRAY(Color.GRAY, "Gray"),
    
    WHITE(Color.WHITE, "White"),
    
    CYAN(Color.CYAN, "Cyan"),
    
    MAGENTA(Color.MAGENTA, "Magenta"),
    
    DARK_GRAY(Color.DKGRAY, "DarkGray"),
    
    PINK(Color.rgb(255,138,166), "Pink"),
    
    ORANGE(Color.rgb(255, 153, 0), "Orange");
    
	private final int colourKey;
	
	private final String colourName;

    private ColourProperties(int colourKey, String colourName)
    {
    	this.colourKey = colourKey;
    	
    	this.colourName = colourName;
    }
    
    public int getColorKey()
    {
		return colourKey;
	}

    public String getColourName()
    {
    	return colourName;
    }
}
