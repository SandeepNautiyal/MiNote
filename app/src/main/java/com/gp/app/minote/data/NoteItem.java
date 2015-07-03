package com.gp.app.minote.data;

import android.os.Parcel;
import android.os.Parcelable;

public class NoteItem implements Parcelable 
{
	public static final String NOTE_ID = "note_id";
	public static final String DATA = "data";
    public static final String	IMAGE_NAME = "image_name";
    public static final String	TEXT_COLOR ="text_color";
    public static final String NOTE_ITEM_TABLE_NAME = "note_item";
	public static final String IS_TITLE = "is_title";

	private String itemText = null;
	
	private String imageName = null;

	private int textColour = 0;
	
	private boolean isNoteTitle = false;
	
	public NoteItem(String textViewData)
	{
		this.itemText = textViewData;
	}
	
	public NoteItem(String textViewData, String imageName) 
	{
		this(textViewData);
		
		this.imageName = imageName;
	}
	
	public NoteItem() 
	{
	}

	public String getText() 
	{
		return itemText == null ? "" : itemText;
	}

	public void setTextViewData(String textViewData) 
	{
		this.itemText = textViewData;
	}
	
	public String getImageName() 
	{
		String imageName1 = imageName == null ? "" : imageName;
		
		return imageName1;
	}

	public void setTextColour(int colour)
	{
		textColour = colour;
	}
	
	public int getTextColour()
	{
		return textColour;
	}
	
	public void setImageName(String imagePath) 
	{
		this.imageName = imagePath;
	}

	@Override
	public int describeContents() 
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) 
	{
		dest.writeStringArray(new String[]{itemText, imageName});
		
		dest.writeInt(textColour);
		
		byte isTitle = (byte)(isTitle() ? 1 : 0);
		
		dest.writeByte(isTitle);
	}

	public static final Parcelable.Creator<NoteItem> CREATOR = new Parcelable.Creator<NoteItem>() 
	{
		@Override
		public NoteItem createFromParcel(Parcel source) {
			
			String [] itemProperties = new String[2];
			
			source.readStringArray(itemProperties);
			
			int colour = source.readInt();
			
			boolean isTitle = source.readByte() == 0 ? false : true;
			
			NoteItem noteListItem = new NoteItem(itemProperties[0], itemProperties[1]);
			
			noteListItem.setTextColour(colour);
			
			noteListItem.setIsTitle(isTitle);
			
			return noteListItem;
		}

		@Override
		public NoteItem[] newArray(int size) 
		{
			return new NoteItem[size];
		}
	};
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("textViewData="+itemText+"\n");
		
		builder.append("imageName="+imageName+"\n");
		
		return builder.toString();
	}
	
	public int getLength()
	{
		int textFactor = 0;
		
		if(itemText != null)
		{
			//TODO constant 22 has to be changed according to device size. Ver important- has to be changed.
			textFactor = itemText.length() > 22 ? Math.abs((int)Math.ceil(itemText.length()/22.0)) : 1;
		}
		
		int length =  textFactor + (imageName == null || imageName.trim().length() == 0  ? 0 : 10);

		return length;
	}

	public void setIsTitle(boolean isTitle) 
	{
		this.isNoteTitle = isTitle;
	}

	public boolean isTitle() 
	{
		return isNoteTitle;
	}
}
