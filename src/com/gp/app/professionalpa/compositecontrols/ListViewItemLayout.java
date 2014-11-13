package com.gp.app.professionalpa.compositecontrols;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gp.app.professionalpa.R;

public class ListViewItemLayout extends RelativeLayout
{
	
	private TextView notesTextView = null;
	
	private ImageButton importanceImageButton = null;
	
	private ImageButton alarmImageButton = null;
	
	private ImageButton addNewNoteButton = null;
	
	public ListViewItemLayout(Context context) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		addView(inflater.inflate(R.layout.composite_control_for_list_view, null));

		notesTextView = (TextView) findViewById(R.id.composite_control_text_box);
		
		importanceImageButton = (ImageButton) findViewById(R.id.composite_control_importance_button);
		
		alarmImageButton = (ImageButton) findViewById(R.id.composite_control_alarm_button);
		
		addNewNoteButton = (ImageButton) findViewById(R.id.composite_control_add_new_listitem);
		// TODO Auto-generated constructor stub
	}

	public TextView getNotesTextView() {
		return notesTextView;
	}

	public ImageButton getImportanceImageButton() {
		return importanceImageButton;
	}

	public ImageButton getAlarmImageButton() {
		return alarmImageButton;
	}

	public ImageButton getAddNewNoteButton() {
		return addNewNoteButton;
	}
	
	

}
