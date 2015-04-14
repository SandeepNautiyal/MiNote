package com.gp.app.professionalpa.data;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;
import com.gp.app.professionalpa.layout.manager.ImageLocationPathManager;
import com.gp.app.professionalpa.util.ProfessionalPAParameters;
import com.gp.app.professionalpa.views.listeners.NoteItemLongClickListener;
import com.gp.app.professionalpa.views.listeners.NotesActionMode;

public class ListViewItemAdapter extends ArrayAdapter<NoteListItem>
{
	private ProfessionalPANote note = null;
	
	private Context context = null;
	
	private List<NoteListItem> listItems = null;
	
	public ListViewItemAdapter(Context context, ProfessionalPANote note) {
		
		super(context, 0, note.getNoteItems());
		
		this.context = context;
		
		this.note = note;

		if(note.getNoteItems() != null)
		{
			listItems = note.getNoteItems();
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
	    if(convertView == null)
	        convertView = LayoutInflater.from(getContext()).inflate(R.layout.data_adapter_view, parent, false);
	    
		System.out.println("getView -> position="+position);

	    NoteListItem noteListItem = listItems.get(position);
	    
	    TextView textView = (TextView) convertView.findViewById(R.id.compositeControlTextBox);
	    
	    Resources androidResources = ProfessionalPAParameters.getApplicationContext().getResources();
	    
	    int compressedViewHeight = (int)androidResources.getDimension(R.dimen.composite_control_textview_height_compressed);

	    ImageButton bulletPointImage = (ImageButton) convertView.findViewById(R.id.compositeControlBulletButton);
//
//	    ImageButton alarmImageButton = (ImageButton)convertView.findViewById(R.id.composite_control_alarm_button);

	    byte noteType = note.getNoteType();
	    		
		if (noteType == ProfessionalPAConstants.LIST_NOTE && (noteListItem.getImageName() == null || noteListItem.getImageName().equals("")))
		{
			LayoutParams importanceButtonParams = bulletPointImage.getLayoutParams();
			importanceButtonParams.height = compressedViewHeight;
			importanceButtonParams.width = (int) androidResources.getDimension(R.dimen.composite_control_importance_button_compressed_width);
			bulletPointImage.setLayoutParams(importanceButtonParams);
		} 
		else if (noteType == ProfessionalPAConstants.PARAGRAPH_NOTE) 
		{
			LayoutParams importanceButtonParams = bulletPointImage.getLayoutParams();
			importanceButtonParams.height = 0;
			importanceButtonParams.width = 0;
			bulletPointImage.setLayoutParams(importanceButtonParams);
			bulletPointImage.setVisibility(View.INVISIBLE);
		}

		if (noteListItem.getTextViewData() != null && !noteListItem.getTextViewData().equals(""))
		{
			final LayoutParams params = textView.getLayoutParams();

			params.height = LayoutParams.WRAP_CONTENT;

			textView.setLayoutParams(params);

			textView.setText(noteListItem.getTextViewData());

			System.out.println("getView -> text data="+noteListItem.getTextViewData());
			
			textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

			textView.setOnLongClickListener(new NoteItemLongClickListener(
					new NotesActionMode(note.getNoteId(), note.getNoteType(),
							note.getImageNames())));

			textView.setLayoutParams(params);
		}

		if (noteListItem.getImageName() != null && !noteListItem.getImageName().equals("")) 
		{
			ImageView imageView = (ImageView) convertView.findViewById(R.id.compositeControlImageView);

			System.out.println("getView -> imageView");

			LayoutParams imageViewParams = imageView.getLayoutParams();
			imageViewParams.height = LayoutParams.MATCH_PARENT;
			imageViewParams.width = LayoutParams.MATCH_PARENT;

			Bitmap image = ImageLocationPathManager.getInstance().getImage(
					noteListItem.getImageName());

			image = Bitmap.createScaledBitmap(image, 300, 300, true);
			imageView.setImageBitmap(image);
			imageView.setLayoutParams(imageViewParams);
		}
	    
		System.out.println("getView -> returning");

	    return convertView;
	}
}
