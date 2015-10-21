package com.gp.app.minote.compositecontrols;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gp.app.minote.R;

import java.io.ByteArrayOutputStream;

public class ListViewItemLayout extends RelativeLayout
{
	
	private EditText textView = null;
	
	private ImageView imageView = null;
	
	private ImageView importanceImageButton = null;
	
//	private ImageButton alarmImageButton = null;
	
	private int stateToSave;

	private String imageName;
	
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
		
		importanceImageButton = (ImageView) findViewById(R.id.compositeControlBulletButton);
		
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
	  
	public EditText getEditText()
	{
		return textView;
	}
	
	public String getText() 
	{
		return textView.getText().toString();
	}
	
	public void setText(String text)
	{
		textView.setText(text.toCharArray(), 0 , text.toCharArray().length);
	}
	
	public ImageView getImportanceImageButton() {
		return importanceImageButton;
	}
	
	public void setImage(String imageName, Bitmap image, boolean changeSize)
	{
		if(changeSize)
		{
			ViewGroup.LayoutParams imageViewParams = imageView.getLayoutParams();
		    imageViewParams.height =  350;
		    imageViewParams.width = 350;
		    image = Bitmap.createScaledBitmap(image, 300, 300,true);
		    imageView.setLayoutParams(imageViewParams);
		}
		else
		{
			ViewGroup.LayoutParams imageViewParams = imageView.getLayoutParams();
		    imageViewParams.height =  LayoutParams.MATCH_PARENT;
		    imageViewParams.width = LayoutParams.MATCH_PARENT;
		    imageView.setLayoutParams(imageViewParams);
		}
	    
	    imageView.setImageBitmap(image);
	    
	    textView.setVisibility(View.GONE);
	    
	    this.imageName = imageName;
	}
	
	public String getImageName()
	{
		return imageName == null ? "" : imageName;
	}

	public void setTextColour(int textColour) 
	{
		textView.setTextColor(textColour);
	}
}
