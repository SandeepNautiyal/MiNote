package com.gp.app.professionalpa.data;

import android.os.Parcel;
import android.os.Parcelable;

public class NoteListItem implements Parcelable 
{
	private String itemText = null;
	
	private String imageName = null;

	private int textColour = 0;
	
	public NoteListItem(String textViewData)
	{
		this.itemText = textViewData;
	}
	
	public NoteListItem(String textViewData, String imageName) 
	{
		this(textViewData);
		
		this.imageName = imageName;
	}
	
	public NoteListItem() 
	{
	}

	public String getTextViewData() 
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
		
//		if(imagePath != null)
//		{
//			ByteArrayOutputStream stream = new ByteArrayOutputStream();
//			
//			imagePath.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//			
//			byte[] imageArray = stream.toByteArray();
//
//			dest.writeByteArray(imageArray);
//			
//			dest.writeInt(imageArray.length);
//		}
//		else
//		{
//			dest.writeInt(0);
//		}
	}

	public static final Parcelable.Creator<NoteListItem> CREATOR = new Parcelable.Creator<NoteListItem>() 
	{
		@Override
		public NoteListItem createFromParcel(Parcel source) {
			
			String [] itemProperties = new String[2];
			
			source.readStringArray(itemProperties);
			
			int colour = source.readInt();
			
			NoteListItem noteListItem = new NoteListItem(itemProperties[0], itemProperties[1]);
			
			noteListItem.setTextColour(colour);
			
			return noteListItem;
		}

		@Override
		public NoteListItem[] newArray(int size) 
		{
			return new NoteListItem[size];
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
	
	public String convertToString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("ITEM_TEXT="+itemText);
		
		sb.append("IMAGE_NAME="+imageName);
		
		return sb.toString();
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
}
