package com.gp.app.professionalpa.compositecontrols;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gp.app.professionalpa.R;

public class ListViewItemLayout extends RelativeLayout
{
	
	private EditText textView = null;
	
	private ImageView imageView = null;
	
	private ImageButton importanceImageButton = null;
	
//	private ImageButton alarmImageButton = null;
	
	private int stateToSave;
	
	public ListViewItemLayout(Context context) 
	{
		super(context);
		
		initControls(context);
	}
	
	public ListViewItemLayout(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);

		initControls(context);
		// TODO Auto-generated constructor stub
	}
	
	private void initControls(Context context) {
		
		super.setId((int)Math.abs(Math.random()*100000));
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		addView(inflater.inflate(R.layout.composite_control_for_list_view, null));

		textView = (EditText) findViewById(R.id.compositeControlTextBox);
		
		imageView  = (ImageView)findViewById(R.id.compositeControlImageView);
		
		importanceImageButton = (ImageButton) findViewById(R.id.compositeControlBulletButton);
		
//		alarmImageButton = (ImageButton) findViewById(R.id.composite_control_alarm_button);
	}
	
	  // ... variables

	  @Override
	  public Parcelable onSaveInstanceState() 
	  {

	    Bundle bundle = new Bundle();
	    
	    bundle.putParcelable("instanceState", super.onSaveInstanceState());
	    
	    bundle.putInt("stateToSave", this.stateToSave);
	    
	    bundle.putString("TEXT_VALUE", textView.getText().toString());
	    
	    if(imageView.getDrawable() != null)
	    {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();

			Bitmap image = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
			
			image.compress(Bitmap.CompressFormat.JPEG, 100, stream);

			byte[] imageArray = stream.toByteArray();

			bundle.putByteArray("BITMAP_IMAGE", imageArray);

			System.out.println("writeToParcel -> array length="
					+ imageArray.length);
	    }
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
	      
	      byte [] imageBytes = bundle.getByteArray("BITMAP_IMAGE");
	      
		  Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
			
		  this.imageView.setImageBitmap(image);
		  
	      this.textView.setText(bundle.getString("TEXT_VALUE"));
	      
	      // ... load everything
	      state = bundle.getParcelable("instanceState");
	    }
	    
	    super.onRestoreInstanceState(state);
	  }
	  
	public String getText() {
		return textView.getText().toString();
	}
	
	public ImageButton getImportanceImageButton() {
		return importanceImageButton;
	}
//
//	public ImageButton getAlarmImageButton() {
//		return alarmImageButton;
//	}
}
