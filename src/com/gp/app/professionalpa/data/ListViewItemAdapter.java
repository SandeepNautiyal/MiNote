package com.gp.app.professionalpa.data;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gp.app.professionalpa.R;

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
	    
	    TextView textView = (TextView) convertView.findViewById(R.id.composite_control_text_box);
	    
	    ImageButton importanceImageButton = (ImageButton) convertView.findViewById(R.id.composite_control_importance_button);
	    
	    ImageButton alarmImageButton = (ImageButton)convertView.findViewById(R.id.composite_control_alarm_button);
	    
	    if(listItems != null)
	    {
		    textView.setText(listItems.get(position).getTextViewData());
	    }
	    
	    return convertView;
	  }
	
}
