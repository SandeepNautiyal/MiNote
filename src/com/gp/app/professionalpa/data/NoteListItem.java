package com.gp.app.professionalpa.data;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

public class NoteListItem implements Parcelable 
{
	private static final byte HIGH_IMPORTANCE_INDEX = 0;
	
	private static final byte ALARM_ACTIVE_INDEX = 1;
	
	private String itemText = null;
	private Bitmap image = null;

	private boolean isImportanceHigh = false;

	private boolean isAlarmActive = false;

	public NoteListItem(String textViewData)
	{
		this.itemText = textViewData;
	}
	
	public NoteListItem(Bitmap image) 
	{
		this.image = image;
	}
	
	public NoteListItem(String textViewData, Bitmap image) 
	{
		this.itemText = textViewData;
		
		this.image = image;
	}

	public NoteListItem(String textViewData, boolean isImportanceHigh) {

		this.itemText = textViewData;
		
		this.isImportanceHigh = isImportanceHigh;
	}

	public NoteListItem(String textViewData, Bitmap image, boolean isImportanceHigh,
			boolean isAlarmActive) {

		this(textViewData, isImportanceHigh);

		this.image = image;
		
		this.isAlarmActive = isAlarmActive;
	}

	public NoteListItem() 
	{
	}

	public String getTextViewData() {
		return itemText;
	}

	public void setTextViewData(String textViewData) {
		this.itemText = textViewData;
	}
	
	public Bitmap getImageData() {
		return image;
	}

	public boolean isImageAndText()
	{
		return image != null && itemText != null && !itemText.equals("");
	}
	
	public boolean isOnlyImageItem()
	{
		return image != null && (itemText == null || itemText.equals(""));
	}
	
	public boolean isOnlyTextItem()
	{
		return image == null && (itemText != null && !itemText.equals(""));
	}
	
	public void setTextViewData(Bitmap image) {
		this.image = image;
	}

	public boolean isImportanceHigh() {
		return isImportanceHigh;
	}

	public void setImportanceHigh(boolean isImportanceHigh) {
		this.isImportanceHigh = isImportanceHigh;
	}

	public boolean isAlarmActive() {
		return isAlarmActive;
	}

	public void setAlarmActive(boolean isAlarmActive) {
		this.isAlarmActive = isAlarmActive;
	}

	@Override
	public int describeContents() 
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) 
	{
		dest.writeString(itemText);
		
		if(image != null)
		{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			
			image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			
			byte[] imageArray = stream.toByteArray();

			dest.writeByteArray(imageArray);
			
			System.out.println("writeToParcel -> array length="+imageArray.length);
			
			dest.writeInt(imageArray.length);
		}
		else
		{
			dest.writeInt(0);
		}
		
		dest.writeBooleanArray(new boolean[] { isImportanceHigh, isAlarmActive });
	}

	public static final Parcelable.Creator<NoteListItem> CREATOR = new Parcelable.Creator<NoteListItem>() 
	{
		@Override
		public NoteListItem createFromParcel(Parcel source) {
			
			String textViewData = source.readString();
			
			int imageArrayLength = source.readInt();
			
			Bitmap image = null;
			
			if(imageArrayLength != 0)
			{
				byte [] imageArray = new byte[imageArrayLength];
				
				source.readByteArray(imageArray);
				
				image = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
				
				System.out.println("Creator -> image="+image + "lenth="+imageArrayLength);
			}
			
			boolean [] attributes = new boolean[2];
			
			source.readBooleanArray(attributes);
			
			boolean isImportanceHigh = attributes[HIGH_IMPORTANCE_INDEX];
			
			boolean isAlarmActive = attributes[ALARM_ACTIVE_INDEX];
			
			return new NoteListItem(textViewData, image, isImportanceHigh, isAlarmActive);
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
		
		builder.append("isImportant="+isImportanceHigh+"\n");

		builder.append("isAlarm="+isAlarmActive+"\n");

		return builder.toString();
	}
	
	public String convertToString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("ITEM_TEXT="+itemText);
		
		String importanceText = isImportanceHigh ? "true":"false";
		
		sb.append("\n ITEM_IMPORTANCE="+importanceText);
		
		String alarmActiveText = isAlarmActive ? "true":"false";
		
		sb.append("\n ACTIVE_ALARM="+alarmActiveText);
		
		return sb.toString();
	}
}
