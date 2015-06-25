package com.gp.app.professionalpa.notes.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gp.app.professionalpa.R;
import com.gp.app.professionalpa.data.Note;
import com.gp.app.professionalpa.data.NoteItem;
import com.gp.app.professionalpa.data.TextNote;
import com.gp.app.professionalpa.notes.images.ImageLocationPathManager;
import com.gp.app.professionalpa.notes.operations.NotesOperationManager;

public class TextNoteFragmentAdapter extends ArrayAdapter<NoteItem>
{
	private TextNote note = null;
	
	private Context context;
    
	public TextNoteFragmentAdapter(Context context, TextNote note) 
	{
		super(context, 0, note.getNoteItems());
		
		this.context = context;
		
		this.note = note;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		//TODO improve 1) introduce convert view reusing 2) if 1st cannot be done remove viewholder.
	    final NoteItem noteListItem = note.getNoteItems().get(position);
	    
		convertView = LayoutInflater.from(getContext()).inflate(R.layout.composite_control_for_list_view, null, false);
		
		TextView editText = (TextView) convertView.findViewById(R.id.compositeControlTextBox);
		
		ImageButton bulletPointImage = (ImageButton) convertView.findViewById(R.id.compositeControlBulletButton);
		
		final ImageView imageView = (ImageView) convertView.findViewById(R.id.compositeControlImageView);
		
        final int noteId = note.getId();
        
//        editText.setOnLongClickListener(new NoteItemLongClickListener(noteId));
//        
//        imageView.setOnLongClickListener(new NoteItemLongClickListener(noteId));
        
        editText.setOnClickListener(new View.OnClickListener()
		{
            @Override
            public void onClick(View v)
            {
            	NotesOperationManager.getInstance().deSelectNote(noteId);
            }
        });
        
		imageView.setOnClickListener(new View.OnClickListener()
		{
            @Override
            public void onClick(View v)
            {
            	if(NotesOperationManager.getInstance().getSelectedNoteIds().contains((Integer)noteId))
            	{
                	NotesOperationManager.getInstance().deSelectNote(noteId);
            	}
            	else
            	{
            		Intent intent = new Intent();
            		intent.setAction(Intent.ACTION_VIEW);
            		String imagePath = ImageLocationPathManager.getInstance().getImagePath(noteListItem.getImageName());
            		intent.setDataAndType(Uri.parse("file://"+imagePath), "image/*");
            		context.startActivity(intent);
            	}
            }

			
        });
		
	    Resources androidResources = getContext().getResources();
	    
	    int compressedViewHeight = (int)androidResources.getDimension(R.dimen.composite_control_textview_height_compressed);

	    byte noteType = note.getType();
	    	
		if (noteType == Note.LIST_NOTE && (noteListItem.getImageName() == null || noteListItem.getImageName().equals("")))
		{
			if(noteListItem.isTitle())
			{
				bulletPointImage.setVisibility(View.GONE);
			}
			else
			{
				LayoutParams bulletPointImageViewParams = bulletPointImage.getLayoutParams();
				bulletPointImageViewParams.height = compressedViewHeight;
				bulletPointImageViewParams.width = (int) androidResources.getDimension(R.dimen.composite_control_importance_button_compressed_width);
				bulletPointImage.setLayoutParams(bulletPointImageViewParams);
			}
		} 
		else if (noteType == Note.PARAGRAPH_NOTE) 
		{
			LayoutParams importanceButtonParams = bulletPointImage.getLayoutParams();
			importanceButtonParams.height = 0;
			importanceButtonParams.width = 0;
			bulletPointImage.setLayoutParams(importanceButtonParams);
			bulletPointImage.setVisibility(View.GONE);
		}

		if (noteListItem.getText() != null && !noteListItem.getText().equals(""))
		{
			final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

			params.addRule(RelativeLayout.RIGHT_OF, bulletPointImage.getId());
			
			if(noteListItem.isTitle())
			{
				editText.setGravity(Gravity.CENTER);
				
				editText.setTypeface(null, Typeface.BOLD);
				
				editText.setBackgroundResource(R.drawable.note_title_border);
				
				editText.setBackgroundColor(Color.rgb(120, 100, 255));
			}
			
		    editText.setLayoutParams(params);
		    
			editText.setText(noteListItem.getText());
			
			editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);

			List<String> imageNames = new ArrayList<String>();
			
			List<NoteItem> noteItems = note.getNoteItems();
			
			for(int i = 0; i < noteItems.size(); i++)
			{
				NoteItem item = noteItems.get(i);
				
				if(item.getImageName() != null && !item.getImageName().equals(""))
				{
					imageNames.add(item.getImageName());
				}
			}
			
			editText.setFocusable(false);
			editText.setClickable(true);
			editText.setTextColor(noteListItem.getTextColour());
		}

		if (noteListItem.getImageName() != null && !noteListItem.getImageName().equals("")) 
		{
			LayoutParams imageViewParams = imageView.getLayoutParams();
			imageViewParams.height = LayoutParams.WRAP_CONTENT;
			imageViewParams.width = LayoutParams.MATCH_PARENT;

			Bitmap image = ImageLocationPathManager.getInstance().getImage(
					noteListItem.getImageName(), true);

			image = Bitmap.createScaledBitmap(image, 350, 350, false);
			imageView.setImageBitmap(image);
			imageView.setLayoutParams(imageViewParams);
			
			LayoutParams importanceButtonParams = bulletPointImage.getLayoutParams();
			importanceButtonParams.height = 0;
			importanceButtonParams.width = 0;
			bulletPointImage.setLayoutParams(importanceButtonParams);
			bulletPointImage.setVisibility(View.INVISIBLE);
			
			LayoutParams textButtonParams = editText.getLayoutParams();
			textButtonParams.height = 0;
			textButtonParams.width = 0;
			editText.setLayoutParams(textButtonParams);
			editText.setVisibility(View.INVISIBLE);
		}

	    return convertView;
	}
	
}
