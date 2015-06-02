package com.gp.app.professionalpa.colorpicker;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Window;
import android.widget.GridView;

public class ColorPickerCreator 
{
    private Object listener = null;
    
	private Context context = null;

	public ColorPickerCreator(Context context, ColourPickerChangeListener listener)
    {
		this.context = context;
		
    	this.listener  = listener;
    }
	
	public void createColourPicker()
	{
		ArrayList<ColourProperties> gridArray = new ArrayList<ColourProperties>();
		
		gridArray.add(ColourProperties.RED);
		
		gridArray.add(ColourProperties.GREEN);
	    
		gridArray.add(ColourProperties.BLUE);
	    
		gridArray.add(ColourProperties.YELLOW);
	    
		gridArray.add(ColourProperties.GRAY);
	    
		gridArray.add(ColourProperties.WHITE);
	    
		gridArray.add(ColourProperties.CYAN);
	    
		gridArray.add(ColourProperties.MAGENTA);
	    
		gridArray.add(ColourProperties.DARK_GRAY);
	    
		gridArray.add(ColourProperties.PINK);
		
		GridView gridView = new GridView(context); 
		
		gridView.setBackgroundColor(Color.parseColor("#7F7CD9"));
		
		gridView.setNumColumns(5);
		
		Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		gridView.setAdapter(new ColourPickerAdapter(context, gridArray, dialog));
		
        dialog.setContentView(gridView);
        dialog.setCancelable(true);
        dialog.show();
	}
}
