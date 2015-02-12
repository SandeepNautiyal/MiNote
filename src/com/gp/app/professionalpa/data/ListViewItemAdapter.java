package com.gp.app.professionalpa.data;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
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

public class ListViewItemAdapter extends ArrayAdapter<NoteListItem>
{

	private Context context = null;
	
	private List<NoteListItem> listItems = null;
	
	
	public ListViewItemAdapter(Context context, List<NoteListItem> values) {
		
		super(context, 0, values);
		
		this.context = context;
		
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
	    
	    final EditText textView = (EditText) convertView.findViewById(R.id.composite_control_text_box);
	    
	    Resources androidResources = ProfessionalPAParameters.getApplicationContext().getResources();
	    
	    int compressedViewHeight = (int)androidResources.getDimension(R.dimen.composite_control_textview_height_compressed);

	    ImageButton importanceImageButton = (ImageButton) convertView.findViewById(R.id.composite_control_importance_button);

	    LayoutParams importanceButtonParams = importanceImageButton.getLayoutParams();
        importanceButtonParams.height =  compressedViewHeight;
        importanceButtonParams.width = (int)androidResources.getDimension(R.dimen.composite_control_importance_button_compressed_width);
        importanceImageButton.setLayoutParams(importanceButtonParams);
        
	    ImageButton alarmImageButton = (ImageButton)convertView.findViewById(R.id.composite_control_alarm_button);
        LayoutParams alarmButtonParams = alarmImageButton.getLayoutParams();
        alarmButtonParams.height = compressedViewHeight;
        alarmButtonParams.width = (int)androidResources.getDimension(R.dimen.composite_control_alarm_button_compressed_width);
	    alarmImageButton.setLayoutParams(alarmButtonParams);
	    
	    final LayoutParams params = textView.getLayoutParams();

	    params.height =  LayoutParams.WRAP_CONTENT;

	    textView.setLayoutParams(params);

	    if(listItems != null)
	    {
		    textView.setText(listItems.get(position).getTextViewData());
	    }
	    
	    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

	    textView.setActivated(false);

	    textView.setLayoutParams(params);

	    return convertView;
	}
	
	class EditTextSizeCalculator extends Thread
	{
		private EditText editText = null;
		
		private int editTextHeight = 0;
		
		public EditTextSizeCalculator(EditText editText)
		{
			this.editText = editText;
		}
		
		public int getEditTextHeight() 
		{
			return editTextHeight;
		}

		@Override
		public void run() 
		{
			editTextHeight = editText.getLineCount();
		}
		
	}
	
	
	
}
