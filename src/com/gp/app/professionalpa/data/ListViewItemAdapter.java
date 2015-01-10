package com.gp.app.professionalpa.data;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class ListViewItemAdapter extends ArrayAdapter<NotesListItem>
{

	private Context context = null;
	
	private List<NotesListItem> listItems = null;
	
	
	public ListViewItemAdapter(Context context, List<NotesListItem> values) {
		
		super(context, 0, values);
		
		System.out.println("ListViewItemAdapter -> constructor");

		this.context = context;
		
		if(values != null)
		{
			listItems = values;
		}
		
		System.out.println("ListViewItemAdapter <- return");

		// TODO Auto-generated constructor stub
	}
	
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    
		Log.d("getView ->", "enter");
		
	    if(convertView == null)
	        convertView = LayoutInflater.from(getContext()).inflate(R.layout.composite_control_for_list_view, parent, false);
	    
	    EditText textView = (EditText) convertView.findViewById(R.id.composite_control_text_box);
	    
	    Resources androidResources = ProfessionalPAParameters.getApplicationContext().getResources();
	    
	    LayoutParams params = textView.getLayoutParams();
	    
	    params.height = (int)androidResources.getDimension(R.dimen.composite_control_textview_height_compressed);
    
	    textView.setLayoutParams(params);
	    
	    ImageButton importanceImageButton = (ImageButton) convertView.findViewById(R.id.composite_control_importance_button);

	    LayoutParams importanceButtonParams = importanceImageButton.getLayoutParams();
        importanceButtonParams.height =  params.height;
        importanceButtonParams.width = (int)androidResources.getDimension(R.dimen.composite_control_importance_button_compressed_width);
        importanceImageButton.setLayoutParams(importanceButtonParams);
        
	    ImageButton alarmImageButton = (ImageButton)convertView.findViewById(R.id.composite_control_alarm_button);
        LayoutParams alarmButtonParams = alarmImageButton.getLayoutParams();
        alarmButtonParams.height = params.height;
        alarmButtonParams.width = (int)androidResources.getDimension(R.dimen.composite_control_alarm_button_compressed_width);
	    alarmImageButton.setLayoutParams(alarmButtonParams);
	    
	    if(listItems != null)
	    {
		    textView.setText(listItems.get(position).getTextViewData());
		    
		    System.out.println("getView -> listItems.get(position)="+listItems.get(position).getTextViewData());
	    }
	    
	    textView.setActivated(false);
	    
	    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
//	    LayoutParams layoutParamsForConvertView = convertView.getLayoutParams();
//	    
//	    layoutParamsForConvertView.height = params.height;
//	    
//	    layoutParamsForConvertView.width = (int)((params.width + importanceButtonParams.width + alarmButtonParams.width)+5);
//	    
//	    convertView.setLayoutParams(layoutParamsForConvertView);
	    
	    Log.d("getView <-", "return "+textView.getText().toString());
	    
	    return convertView;
	  }
	
}
