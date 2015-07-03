package com.gp.app.minote.colorpicker;


public class ColorPickerSelectionManager 
{
	public ColourProperties getSelectedColor(int colorKey)
	{
		ColourProperties property = null;
		
		if(colorKey == ColourProperties.RED.getColorKey())
		{
			property = ColourProperties.RED;
		}
		else if(colorKey == ColourProperties.PINK.getColorKey())
		{
			property = ColourProperties.PINK;
		}
		else if(colorKey == ColourProperties.BLUE.getColorKey())
		{
			property = ColourProperties.BLUE;
		}
		else if(colorKey == ColourProperties.CYAN.getColorKey())
		{
			property = ColourProperties.CYAN;
		}
		else if(colorKey == ColourProperties.DARK_GRAY.getColorKey())
		{
			property = ColourProperties.DARK_GRAY;
		}
		else if(colorKey == ColourProperties.GRAY.getColorKey())
		{
			property = ColourProperties.GRAY;
		}
		else if(colorKey == ColourProperties.GREEN.getColorKey())
		{
			property = ColourProperties.GREEN;
		}
		else if(colorKey == ColourProperties.MAGENTA.getColorKey())
		{
			property = ColourProperties.MAGENTA;
		}
		else if(colorKey == ColourProperties.ORANGE.getColorKey())
		{
			property = ColourProperties.ORANGE;
		}
		else if(colorKey == ColourProperties.WHITE.getColorKey())
		{
			property = ColourProperties.WHITE;
		}
		else if(colorKey == ColourProperties.YELLOW.getColorKey())
		{
			property = ColourProperties.YELLOW;
		}
		
		return property;
	}
}
