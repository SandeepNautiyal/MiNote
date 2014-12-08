package com.gp.app.professionalpa.compositecontrols;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gp.app.professionalpa.R;

public class ListViewItemLayout extends RelativeLayout
{
	
	private EditText textView = null;
	
	private ImageButton importanceImageButton = null;
	
	private ImageButton alarmImageButton = null;
	
	private int id;
	
	public ListViewItemLayout(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);

		initControls(context);
		// TODO Auto-generated constructor stub
	}
	private void initControls(Context context) {
		
		id = (int)Math.abs(Math.random()*100000);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		addView(inflater.inflate(R.layout.composite_control_for_list_view, null));

		textView = (EditText) findViewById(R.id.composite_control_text_box);
		
		importanceImageButton = (ImageButton) findViewById(R.id.composite_control_importance_button);
		
		alarmImageButton = (ImageButton) findViewById(R.id.composite_control_alarm_button);
	}
	public ListViewItemLayout(Context context) {
		super(context);
		
		initControls(context);
	}

	public EditText getListItemEditView() {
		return textView;
	}
	
	@Override
	public int getId()
	{
		return id;
	}

	public ImageButton getImportanceImageButton() {
		return importanceImageButton;
	}

	public ImageButton getAlarmImageButton() {
		return alarmImageButton;
	}
}
