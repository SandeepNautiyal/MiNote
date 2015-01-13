package com.gp.app.professionalpa.data;

import java.io.Serializable;

import com.gp.app.professionalpa.interfaces.ProfessionalPAConstants;

import android.os.Parcel;
import android.os.Parcelable;

public class NotesListItem implements Parcelable 
{
	private static final byte HIGH_IMPORTANCE_INDEX = 0;
	
	private static final byte ALARM_ACTIVE_INDEX = 1;
	
	private String itemText = null;

	private boolean isImportanceHigh = false;

	private boolean isAlarmActive = false;

	public NotesListItem(String textViewData) {
		this.itemText = textViewData;
	}

	public NotesListItem(String textViewData, boolean isImportanceHigh) {

		this(textViewData);

		this.isImportanceHigh = isImportanceHigh;
	}

	public NotesListItem(String textViewData, boolean isImportanceHigh,
			boolean isAlarmActive) {

		this(textViewData, isImportanceHigh);

		this.isAlarmActive = isAlarmActive;
	}

	public String getTextViewData() {
		return itemText;
	}

	public void setTextViewData(String textViewData) {
		this.itemText = textViewData;
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
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) 
	{
		dest.writeString(itemText);

		dest.writeBooleanArray(new boolean[] { isImportanceHigh, isAlarmActive });
	}

	public static final Parcelable.Creator<NotesListItem> CREATOR = new Parcelable.Creator<NotesListItem>() {

		@Override
		public NotesListItem createFromParcel(Parcel source) {
			
			String textViewData = source.readString();
			
			boolean [] attributes = new boolean[2];
			
			source.readBooleanArray(attributes);
			
			boolean isImportanceHigh = attributes[HIGH_IMPORTANCE_INDEX];
			
			boolean isAlarmActive = attributes[ALARM_ACTIVE_INDEX];
			
			System.out.println("createFromParcel -> textViewData="+textViewData);

			return new NotesListItem(textViewData, isImportanceHigh, isAlarmActive);
		}

		@Override
		public NotesListItem[] newArray(int size) {
			return new NotesListItem[size];
		}
	};
	
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		builder.append("textViewData="+itemText);
		
		return builder.toString();
	}
	
	public String convertToString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(ProfessionalPAConstants.LIST_ITEM_STRING_START_DELIMITER);
		
		sb.append(ProfessionalPAConstants.LIST_ITEM_VALUE_START_DELIMITER);
		
		sb.append("ITEM_TEXT="+itemText);
		
		sb.append(ProfessionalPAConstants.LIST_ITEM_VALUE_END_DELIMITER);

		String importanceText = isImportanceHigh ? "true":"false";
		
		sb.append(ProfessionalPAConstants.LIST_ITEM_VALUE_START_DELIMITER);

		sb.append("ITEM_IMPORTANCE="+importanceText);
		
		sb.append(ProfessionalPAConstants.LIST_ITEM_VALUE_END_DELIMITER);

		String alarmActiveText = isAlarmActive ? "true":"false";
		
		sb.append(ProfessionalPAConstants.LIST_ITEM_VALUE_START_DELIMITER);

		sb.append("ACTIVE_ALARM="+alarmActiveText);
		
		sb.append(ProfessionalPAConstants.LIST_ITEM_VALUE_END_DELIMITER);
		
		sb.append(ProfessionalPAConstants.LIST_ITEM_STRING_END_DELIMITER);
		
		return sb.toString();
	}
}
