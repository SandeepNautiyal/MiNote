package com.gp.app.professionalpa.compositecontrols;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.gp.app.professionalpa.R;

public class ListViewItemLayout extends RelativeLayout
{
	
	private EditText textView = null;
	
	private ImageButton importanceImageButton = null;
	
	private ImageButton alarmImageButton = null;
	
	private int id;
	
	private int stateToSave;
	
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
	
	  // ... variables

	  @Override
	  public Parcelable onSaveInstanceState() 
	  {

	    Bundle bundle = new Bundle();
	    
	    bundle.putParcelable("instanceState", super.onSaveInstanceState());
	    
	    bundle.putInt("stateToSave", this.stateToSave);
	    // ... save everything
	    return bundle;
	  }

	  @Override
	  public void onRestoreInstanceState(Parcelable state) 
	  {

	    if (state instanceof Bundle) 
	    {
	      Bundle bundle = (Bundle) state;
	      
	      this.stateToSave = bundle.getInt("stateToSave");
	      // ... load everything
	      state = bundle.getParcelable("instanceState");
	    }
	    
	    super.onRestoreInstanceState(state);
	  }
	  
	public ListViewItemLayout(Context context) 
	{
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
