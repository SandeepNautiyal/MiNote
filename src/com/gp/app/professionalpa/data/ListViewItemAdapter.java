package com.gp.app.professionalpa.data;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
		//TODO improve 1) introduce convert view reusing 2) if 1st cannot be done remove viewholder.
	    final NoteListItem noteListItem = listItems.get(position);
	    
		convertView = LayoutInflater.from(getContext()).inflate(R.layout.professional_pa_note_view, parent, false);
		
		EditText text = (EditText) convertView.findViewById(R.id.compositeControlTextBox);
		
		ImageButton bulletPointImage = (ImageButton) convertView.findViewById(R.id.compositeControlBulletButton);
		
		ImageView imageView = (ImageView) convertView.findViewById(R.id.compositeControlImageView);
		
        final int noteId = note.getNoteId();
        
        text.setOnClickListener(new View.OnClickListener()
		{
            @Override
            public void onClick(View v)
            {
            	ProfessionalPAParameters.getNotesActivity().openNoteInEditMode(noteId);
            }
        });
        
		imageView.setOnClickListener(new View.OnClickListener()
		{
            @Override
            public void onClick(View v)
            {
            	ProfessionalPAParameters.getNotesActivity().openNoteInEditMode(noteId);
            }
        });
		
	    Resources androidResources = getContext().getResources();
	    
	    int compressedViewHeight = (int)androidResources.getDimension(R.dimen.composite_control_textview_height_compressed);

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
			final LayoutParams params = text.getLayoutParams();

//			if(listItems.size() > 1)
//			{
				params.height = LayoutParams.WRAP_CONTENT;
//			}
//			else
//			{
//				params.height = 100;
//			}

		    text.setLayoutParams(params);

			text.setText(noteListItem.getTextViewData());

			text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

			text.setOnLongClickListener(new NoteItemLongClickListener(
					new NotesActionMode(note.getNoteId(), note.getNoteType(),
							note.getImageNames())));

			text.setFocusable(false);
			text.setClickable(true);
			text.setTextColor(noteListItem.getTextColour());
			text.setLayoutParams(params);
		}

		if (noteListItem.getImageName() != null && !noteListItem.getImageName().equals("")) 
		{
			LayoutParams imageViewParams = imageView.getLayoutParams();
			imageViewParams.height = LayoutParams.MATCH_PARENT;
			imageViewParams.width = LayoutParams.MATCH_PARENT;

			Bitmap image = ImageLocationPathManager.getInstance().getImage(
					noteListItem.getImageName(), true);

			image = Bitmap.createScaledBitmap(image, 300, 300, true);
			imageView.setImageBitmap(image);
			imageView.setLayoutParams(imageViewParams);
			bulletPointImage.setVisibility(View.INVISIBLE);
			text.setVisibility(View.INVISIBLE);
		}

	    return convertView;
	}
}
