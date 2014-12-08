package com.gp.app.professionalpa.layout.notes.data;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.gp.app.professionalpa.R;

public class ListDataFragment extends Fragment 
{
	private EditText editText = null;
	
	private ImageButton importanceButton = null;
	
	private ImageButton alarmButton = null;
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.composite_control_for_list_view, container);
        
		editText = (EditText)view.findViewById(R.id.composite_control_text_box);
		
		importanceButton = (ImageButton)view.findViewById(R.id.composite_control_importance_button);
		
		alarmButton = (ImageButton)view.findViewById(R.id.composite_control_alarm_button);
		
        return view;
        // Inflate the layout for this fragment
    }
}
