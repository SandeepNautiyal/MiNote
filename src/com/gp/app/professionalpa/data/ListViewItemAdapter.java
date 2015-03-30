package com.gp.app.professionalpa.data;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class ListViewItemAdapter extends ArrayAdapter<NoteListItem>
{

	private byte noteType = -1;
	
	private Context context = null;
	
	private List<NoteListItem> listItems = null;
	
	
	public ListViewItemAdapter(Context context, List<NoteListItem> values, byte noteType) {
		
		super(context, 0, values);
		
		this.context = context;
		
		this.noteType = noteType;

		if(values != null)
		{
			listItems = values;
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
	    if(convertView == null)
	        convertView = LayoutInflater.from(getContext()).inflate(R.layout.composite_control_for_list_view, parent, false);
	    
	    NoteListItem noteListItem = listItems.get(position);
	    
	    EditText textView = (EditText) convertView.findViewById(R.id.compositeControlTextBox);
	    
	    Resources androidResources = ProfessionalPAParameters.getApplicationContext().getResources();
	    
	    int compressedViewHeight = (int)androidResources.getDimension(R.dimen.composite_control_textview_height_compressed);

	    ImageButton importanceImageButton = (ImageButton) convertView.findViewById(R.id.compositeControlBulletButton);
//
//	    ImageButton alarmImageButton = (ImageButton)convertView.findViewById(R.id.composite_control_alarm_button);

	    if(noteType == ProfessionalPAConstants.LIST_NOTE || noteType == ProfessionalPAConstants.PARAGRAPH_NOTE)
	    {
	    	if(noteType == ProfessionalPAConstants.LIST_NOTE)
	    	{
	    		LayoutParams importanceButtonParams = importanceImageButton.getLayoutParams();
		        importanceButtonParams.height =  compressedViewHeight;
		        importanceButtonParams.width = (int)androidResources.getDimension(R.dimen.composite_control_importance_button_compressed_width);
		        importanceImageButton.setLayoutParams(importanceButtonParams);
	    	}
	    	else if(noteType == ProfessionalPAConstants.PARAGRAPH_NOTE)
	    	{
	    		LayoutParams importanceButtonParams = importanceImageButton.getLayoutParams();
		        importanceButtonParams.height =  0;
		        importanceButtonParams.width = 0;
		        importanceImageButton.setLayoutParams(importanceButtonParams);
	    		importanceImageButton.setVisibility(View.INVISIBLE);
	    	}
//	        
//	        LayoutParams alarmButtonParams = alarmImageButton.getLayoutParams();
//	        alarmButtonParams.height = compressedViewHeight;
//	        alarmButtonParams.width = (int)androidResources.getDimension(R.dimen.composite_control_alarm_button_compressed_width);
//		    alarmImageButton.setLayoutParams(alarmButtonParams);
		    
		    final LayoutParams params = textView.getLayoutParams();

		    params.height =  LayoutParams.WRAP_CONTENT;

		    textView.setLayoutParams(params);

		    if(listItems != null && listItems.size() > 0)
		    {
			    textView.setText(listItems.get(position).getTextViewData());
		    }
		    
		    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

		    textView.setActivated(false);

		    textView.setLayoutParams(params);
	    }
	    else
	    {
		    LayoutParams importanceButtonParams = importanceImageButton.getLayoutParams();
	        importanceButtonParams.height =  0;
	        importanceButtonParams.width = 0;
	        importanceImageButton.setLayoutParams(importanceButtonParams);
	        
	        LayoutParams params = textView.getLayoutParams();
		    params.height =  0;
		    params.width = 0;
		    textView.setLayoutParams(params);
		    
//	        LayoutParams alarmButtonParams = alarmImageButton.getLayoutParams();
//	        alarmButtonParams.height = 0;
//	        alarmButtonParams.width = 0;
//		    alarmImageButton.setLayoutParams(alarmButtonParams);
		    
	        textView.setVisibility(View.INVISIBLE);
//	        importanceImageButton.setVisibility(View.INVISIBLE);
	        textView.setVisibility(View.INVISIBLE);
//	        alarmImageButton.setVisibility(View.INVISIBLE);
	        
	        ImageView imageView = (ImageView) convertView.findViewById(R.id.compositeControlImageView);

		    LayoutParams imageViewParams = imageView.getLayoutParams();
		    imageViewParams.height =  LayoutParams.MATCH_PARENT;
		    imageViewParams.width = LayoutParams.MATCH_PARENT;
		    Bitmap image = Bitmap.createScaledBitmap(noteListItem.getImageData(), 300, 300,
	                true);
		    
		    imageView.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) 
				{
					System.out.println("long click for image view");
					
					return false;
				}
			});
		    
		    imageView.setImageBitmap(image);
		    imageView.setLayoutParams(imageViewParams);
	    }
	    

	    return convertView;
	}
}
