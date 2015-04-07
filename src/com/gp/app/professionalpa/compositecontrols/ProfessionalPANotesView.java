package com.gp.app.professionalpa.compositecontrols;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.gp.app.professionalpa.R;

public class ProfessionalPANotesView extends LinearLayout
{

	List<ListViewItemLayout>  items = new ArrayList<ListViewItemLayout>();
	
	public ProfessionalPANotesView(Context context) 
	{
		super(context);
		
		initControls(context);

	}
	
	public ProfessionalPANotesView(Context context, AttributeSet attributeSet)
	{
		super(context, attributeSet);
		
		initControls(context);
	}

    private void initControls(Context context) 
    {
		super.setId((int)Math.abs(Math.random()*100000));
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		addView(inflater.inflate(R.layout.professional_pa_note_view, null));
	}
	
	  // ... variables

	  @Override
	  public Parcelable onSaveInstanceState() 
	  {
		for(int i = 0; i < this.getChildCount(); i++)
		{
			View child = this.getChildAt(i);
		}
	    Bundle bundle = new Bundle();
	    
	    bundle.putParcelable("instanceState", super.onSaveInstanceState());
	    
	    return bundle;
	  }

	  @Override
	  public void onRestoreInstanceState(Parcelable state) 
	  {

	    if (state instanceof Bundle) 
	    {
	      Bundle bundle = (Bundle) state;
	      
	      state = bundle.getParcelable("instanceState");
	    }
	    
	    super.onRestoreInstanceState(state);
	  }

	public ListViewItemLayout getListItemLayout() {
		// TODO Auto-generated method stub
		return null;
	}
}
