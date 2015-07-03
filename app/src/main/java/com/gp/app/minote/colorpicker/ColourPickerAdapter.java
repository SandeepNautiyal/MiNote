package com.gp.app.minote.colorpicker;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.gp.app.minote.R;

public class ColourPickerAdapter extends ArrayAdapter<ColourProperties> 
{
	private final Context noteActivityContext; 
	
	private int layoutResourceId; 
	
	private Dialog parentDialog = null;
	
	ArrayList<ColourProperties> data = new ArrayList<ColourProperties>(); 
	
	public ColourPickerAdapter(Context context, ArrayList<ColourProperties> data, Dialog parentDialog)
	{
		super(context, 0, data); 
		
		layoutResourceId = R.layout.colour_picker_adapter_view;
		
		this.noteActivityContext = context; 
		
		this.parentDialog = parentDialog;
		
		this.data = data; 
	}
	
	@Override 
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView; 
		
		RecordHolder holder = null; 
		
		if (row == null) 
		{ 
			LayoutInflater inflater = ((Activity) noteActivityContext).getLayoutInflater(); 
			
			row = inflater.inflate(layoutResourceId, parent, false); 
			
			holder = new RecordHolder(); 
			
			holder.button = (Button)row.findViewById(R.id.colourPickerImage); 
			
			row.setTag(holder); 
	    } 
		else 
	    { 
			holder = (RecordHolder) row.getTag();
		} 
		
		ColourProperties properties = data.get(position); 
		
		setColourButtonProperties(properties, holder.button);
		
		return row; 
	} 
	
	private void setColourButtonProperties(ColourProperties properties, Button colourButton) 
	{
		colourButton.setBackgroundColor(properties.getColorKey());
		
		colourButton.setId(properties.getColorKey());
		
		colourButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View view) 
			{
				System.out.println("onClick -> view="+view);
				
				((ColourPickerChangeListener)noteActivityContext).changeColour(view.getId());
				
				parentDialog.dismiss();
			}
		});
	}

	static class RecordHolder 
	{ 
		Button button; 
	}
}
