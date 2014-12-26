package com.gp.app.professionalpa.data;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;

public class ListViewItemAdapter extends ArrayAdapter<ListViewItem>
{

	private Context context = null;
	
	private List<ListViewItem> listItems = null;
	
	
	public ListViewItemAdapter(Context context, List<ListViewItem> values) {
		
		super(context, R.layout.composite_control_for_list_view, values);
		
		this.context = context;
		
		if(values != null)
		{
			listItems = values;
		}
		// TODO Auto-generated constructor stub
	}
	
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    
	    if(convertView == null)
	        convertView = inflater.inflate(R.layout.composite_control_for_list_view, parent, false);
	    
	    EditText textView = (EditText) convertView.findViewById(R.id.composite_control_text_box);
	    
	    Resources androidResources = ProfessionalPAParameters.getApplicationContext().getResources();
	    
	    LayoutParams params = textView.getLayoutParams();
	    
	    params.height = (int)androidResources.getDimension(R.dimen.composite_control_textview_height_compressed);
	    
	    params.width = ProfessionalPAParameters.getParentLinearLayoutWidth();//(int)androidResources.getDimension(R.dimen.composite_control_textview_compressed_width);

	    System.out.println("text view height="+params.height+" width="+params.width);
	    
	    textView.setLayoutParams(params);
	    
	    ImageButton importanceImageButton = (ImageButton) convertView.findViewById(R.id.composite_control_importance_button);
	    
        LayoutParams importanceButtonParams = importanceImageButton.getLayoutParams();
	    
        importanceImageButton.setVisibility(View.INVISIBLE);
        
        importanceButtonParams.height =  params.height;
        
        importanceButtonParams.width = (int)androidResources.getDimension(R.dimen.composite_control_importance_button_compressed_width);

	    System.out.println("importanceButton view height="+importanceButtonParams.height+" width="+importanceButtonParams.width);

        //(int)androidResources.getDimension(R.dimen.composite_control_image_button_height_compressed);

        importanceImageButton.setLayoutParams(importanceButtonParams);
        
	    ImageButton alarmImageButton = (ImageButton)convertView.findViewById(R.id.composite_control_alarm_button);
	    
	    alarmImageButton.setVisibility(View.INVISIBLE);

        LayoutParams alarmButtonParams = alarmImageButton.getLayoutParams();

        alarmButtonParams.height = params.height;
        
        alarmButtonParams.width = (int)androidResources.getDimension(R.dimen.composite_control_alarm_button_compressed_width);
        
	    System.out.println("alarm button view height="+alarmButtonParams.height+" width="+alarmButtonParams.width);

        //(int)androidResources.getDimension(R.dimen.composite_control_image_button_height_compressed);

	    alarmImageButton.setLayoutParams(alarmButtonParams);
	    
	    if(listItems != null)
	    {
		    textView.setText(listItems.get(position).getTextViewData());
	    }
	    
	    textView.setEnabled(false);
	    
	    LayoutParams layoutParamsForConvertView = convertView.getLayoutParams();
	    
	    layoutParamsForConvertView.height = params.height;
	    
	    layoutParamsForConvertView.width = (int)((params.width + importanceButtonParams.width + alarmButtonParams.width)+5);
	    
	    System.out.println("layoutParamsForConvertView width ="+layoutParamsForConvertView.width+" height="+layoutParamsForConvertView.height);
	    
	    convertView.setLayoutParams(layoutParamsForConvertView);
	    
	    return convertView;
	  }
	
}
