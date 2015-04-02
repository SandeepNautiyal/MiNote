package com.gp.app.professionalpa.data;

import android.os.Parcel;
import android.os.Parcelable;

public class NoteListItem implements Parcelable 
{
	private String itemText = null;
	
	private String imageName = null;

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
		return itemText;
	}

	public void setTextViewData(String textViewData) 
	{
		this.itemText = textViewData;
	}
	
	public String getImageName() 
	{
		return imageName;
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
			
			return new NoteListItem(itemProperties[0], itemProperties[1]);
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
		
		return builder.toString();
	}
	
	public String convertToString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("ITEM_TEXT="+itemText);
		
		return sb.toString();
	}
}
